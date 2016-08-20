package etherchainml.core.util

import etherchainml.core.{Common, JSONWrapper, CommonFixture}
import org.json4s.JsonAST.JValue
import org.json4s._
import org.scalatest._
import org.scalatest.concurrent.{ScalaFutures}
import org.scalatest.time.{Seconds, Millis, Span}



class HttpGetPoloniexSpec extends FunSpec with Matchers with BeforeAndAfterAll with ScalaFutures with GivenWhenThen
   with CommonFixture {


  describe("HttpGetPoloniex") {

    it("Gets the expected number of PoloniexTrade trades for a specific time period") {

      Given("there is an instance of HttpGetPoloniex for currencyPair = \"BTC_ETH\" and parser parseResponsePoloniexTrade")
        val httpGetPoloniex = HttpGetPoloniex(currencyPair = "BTC_ETH",
        parseResponse = HttpGetPoloniex.parseResponsePoloniexTrade)

      When("requestBatch() is invoked with start = 1454140000, end = 1454150049")
        val trades = httpGetPoloniex.requestBatch(start = 1454140000, end = 1454150049)

      Then("it should retrieve 1944 trades")
        trades.futureValue.size should equal(1944)

    }

    it("Gets the expected number of Json trades for a specific time period") {
      Given("there is an instance of HttpGetPoloniex for currencyPair = \"BTC_ETH\" and parser parseResponseJsonWrapper")
        val httpGetPoloniexJson = HttpGetPoloniex(currencyPair = "BTC_ETH",
        parseResponse = HttpGetPoloniex.parseResponseJsonWrapper)

      When("requestBatch() is invoked with start = 1454140000, end = 1454150049")
        val tradesJson = httpGetPoloniexJson.requestBatch(start = 1454140000, end = 1454150049)

      Then("it should retrieve 1944 trades")
        tradesJson.futureValue.size should equal(1944)
    }

    it("Gets a Json tx in the expected Json format") {

      Given("there is an instance of HttpGetPoloniex for parseResponseJsonWrapper")
        val httpGetPoloniexJson = HttpGetPoloniex(parseResponse = HttpGetPoloniex.parseResponseJsonWrapper)

      When("requestBatch() is invoked with start = 1454140000, end = 1454140001")
        val txs = httpGetPoloniexJson.requestBatch(start = 1454140000, end = 1454140010)

      Then("it should retrieve the tx in the expected format")
      //implicit val defaultPatience = PatienceConfig(timeout = Span(5, Seconds), interval = Span(200, Millis))

        whenReady(txs) { txs =>
          txs.size should equal(1)
          val tx = txs(0)
          tx match {
            case trade: JSONWrapper =>
            {
              trade.payload match {
                case value: JValue => value should not equal(JNothing)
                case _ => throw new Exception("Unexpected type for payload")
              }

              trade.schema match {
                case value: String => value should equal(Common.PoloniexTradeJsonSchema)
                case _ => throw new Exception("Unexpected type for schema")
              }
            }

            case _ => throw new Exception("Unexpected type for tx")
          }
        }
    }
  }
}

