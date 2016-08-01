package etherchainml.core.util

import etherchainml.core.CommonFixture
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures


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
  }
}

