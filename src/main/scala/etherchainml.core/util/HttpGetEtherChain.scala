package etherchainml.core.util


import etherchainml.core.{JSONWrapper, Event, EtherChainTrade, PoloniexTrade}
import org.json4s
import org.json4s._
import org.json4s.native.JsonMethods._

import scala.concurrent.Future
import scalaj.http.Http
import scala.concurrent.ExecutionContext.Implicits.global


//TODO: Generalize functionality to EventPull
class HttpGetEtherChain(parseResponse: (json4s.JValue) => List[Event]) extends EventPull {

  def requestBatch(start: Long, end: Long):Future[List[Event]]={
    //s"https://etherchain.org/api/txs/0/100"
    val url = s"https://etherchain.org/api/txs/${start}/${end}"

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


object HttpGetEtherChain {
  implicit val formats = DefaultFormats

  def apply(parseResponse: (json4s.JValue) => List[Event] = parseResponseJsonWrapper): HttpGetEtherChain =
    new HttpGetEtherChain(parseResponse)

  def parseResponseEtherChainTrade: (JValue) => List[EtherChainTrade] = {
    jsonResponse =>
      val data: JValue = jsonResponse \ "data"
      val txs: List[EtherChainTrade] = data.extract[List[EtherChainTrade]]
      txs
  }

  def parseResponseJsonWrapper: (json4s.JValue) => List[JSONWrapper] = {
    jsonResponse =>
      val data: JValue = jsonResponse \ "data"
      val txs: List[JSONWrapper] = data.extract[List[JSONWrapper]]
      txs
  }
}
