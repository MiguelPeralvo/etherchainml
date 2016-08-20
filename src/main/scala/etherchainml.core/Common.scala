package etherchainml.core

object Common {
  val PoloniexTradeJsonSchema = "PoloniexTrade_20160807.1"
  val EtherChainTradeJsonSchema = "EtherChainTrade_20160820.1"

  def sleep(duration: Long) { Thread.sleep(duration) }
}
