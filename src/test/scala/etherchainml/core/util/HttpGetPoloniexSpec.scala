package etherchainml.core.util

import etherchainml.core.CommonFixture
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
  }
}

