package etherchainml.core.util

import etherchainml.core.{JSONWrapper, CommonFixture}
import org.json4s._
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span, Millis}


class HttpGetEtherChainSpec extends FunSpec with Matchers with BeforeAndAfterAll with ScalaFutures with GivenWhenThen
   with CommonFixture {


  describe("HttpGetEtherChain") {

    it("Gets the expected number of EtherChainTrade txs for a specific time period") {

      Given("there is an instance of HttpGetEtherChain for parseResponseEtherChainTrade")
        val httpGetEtherChain = HttpGetEtherChain(parseResponse = HttpGetEtherChain.parseResponseEtherChainTrade)

      When("requestBatch() is invoked with start = 0, end = 100")
        val txs = httpGetEtherChain.requestBatch(start = 0, end = 100)

      Then("it should retrieve 100 txs")
        txs.futureValue.size should equal(100)

    }

    it("Gets the expected number of Json txs for a specific time period") {

      Given("there is an instance of HttpGetEtherChain for parseResponseEtherChainTrade")
        val httpGetEtherChain = HttpGetEtherChain(parseResponse = HttpGetEtherChain.parseResponseJsonWrapper)

      When("requestBatch() is invoked with start = 0, end = 100")
        val txs = httpGetEtherChain.requestBatch(start = 0, end = 100)

      Then("it should retrieve 100 txs")
        txs.futureValue.size should equal(100)

    }

    it("Gets a Json tx in the expected Json format") {

      Given("there is an instance of HttpGetEtherChain for parseResponseEtherChainTrade")
        val httpGetEtherChain = HttpGetEtherChain(parseResponse = HttpGetEtherChain.parseResponseJsonWrapper)

      When("requestBatch() is invoked with start = 0, end = 100")
        val txs = httpGetEtherChain.requestBatch(start = 0, end = 1)

      Then("it should retrieve the tx in the expected format")
        //implicit val defaultPatience = PatienceConfig(timeout = Span(5, Seconds), interval = Span(200, Millis))

        whenReady(txs) { txs =>
          txs.size should equal(1)
          val tx = txs(0)
          tx match {
            case expected: JSONWrapper =>
            {
              expected.body match {
                case body: JValue => body should not equal(JNothing)
                case _ => throw new Exception("Unexpected type for body")
              }
            }

            case _ => throw new Exception("Unexpected type for tx")
          }
        }

    }
  }
}

