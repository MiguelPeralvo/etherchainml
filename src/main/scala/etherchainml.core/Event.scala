package etherchainml.core
import org.json4s._

//TODO: Introduce Command Wrappers & Matching


/** Base marker trait. */
@SerialVersionUID(1L)
sealed trait Event extends Serializable


case class JSONWrapper(
  schema: String,
  hashcode: String,
  payload: JValue
) extends Event

//object JSONWrapper
//{
//  def hashcode(str: String) = {str.hashCode().toString}
//}

/* More Information
 * val url = "https://poloniex.com/public?command=returnTradeHistory&currencyPair=BTC_ETH&start=1454140000&end=1454150049"
  * */
// TODO: Refine parsing to get dates/numbers types.
/**
  *
  * @param globalTradeID
  * @param tradeID
  * @param date
  * @param `type`
  * @param rate
  * @param amount
  * @param total
  */
case class PoloniexTrade(
  globalTradeID: BigDecimal,
  tradeID: BigDecimal,
  date: String,
  `type`:String,
  rate: String,
  amount: String,
  total: String) extends Event





/* More Information at https://etherchain.org/documentation/api/#api-Transactions-GetTxsOffsetCount
* */
// TODO: Refine parsing to get dates/numbers types.
/**
  *
  * @param hash
  * @param sender
  * @param recipient
  * @param accountNonce
  * @param price
  * @param gasLimit
  * @param amount
  * @param block_id
  * @param time
  * @param newContract
  * @param isContractTx
  * @param blockHash
  * @param parentHash
  * @param txIndex
  */
case class EtherChainTrade(
  hash: String,
  sender: String,
  recipient:String,
  accountNonce: Option[String],
  price: Double,
  gasLimit: Double,
  amount: Double,
  block_id: Int,
  time: String,
  newContract: Option[Int],
  isContractTx: Option[Int],
  blockHash:String,
  parentHash: String,
  txIndex:Option[Int]) extends Event

