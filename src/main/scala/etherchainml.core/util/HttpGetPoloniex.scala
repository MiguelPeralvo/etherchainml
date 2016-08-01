package etherchainml.core.util

import etherchainml.core.{JSONWrapper, Event, PoloniexTrade}
import org.json4s
import org.json4s._
import org.json4s.native.JsonMethods._
import scala.concurrent.Future
import scalaj.http.Http
import scala.concurrent.ExecutionContext.Implicits.global


class HttpGetPoloniex(currencyPair: String, parseResponse: (json4s.JValue) => List[Event]) extends EventPull {

  def requestBatch(start: Long, end: Long):Future[List[Event]]={
    val url = s"https://poloniex.com/public?command=returnTradeHistory&currencyPair=${currencyPair}&start=${start}&end=${end}"

    val baseRequest = Http(url)
    val response = Future {
      baseRequest.asString.body
    }
    val parsedResponse = response.map { r => parse(r) }

    parsedResponse.map{
      parseResponse
    }
  }



}

object HttpGetPoloniex {
  implicit val formats = DefaultFormats

  def apply(currencyPair: String = "BTC_ETH", parseResponse: (json4s.JValue) => List[Event] = parseResponseJsonWrapper): HttpGetPoloniex =
    new HttpGetPoloniex(currencyPair, parseResponse)

  def parseResponsePoloniexTrade: (json4s.JValue) => List[PoloniexTrade] = {
    jsonResponse =>
      val data: JValue = jsonResponse
      val trades: List[PoloniexTrade] = data.extract[List[PoloniexTrade]]
      trades
  }

  def parseResponseJsonWrapper: (json4s.JValue) => List[JSONWrapper] = {
    jsonResponse =>
      val data: JValue = jsonResponse
      val trades: List[JSONWrapper] = data.extract[List[JSONWrapper]]
      trades
  }
}
