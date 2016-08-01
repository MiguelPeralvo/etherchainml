package etherchainml.core.util
import etherchainml.core.{Event, PoloniexTrade}
import org.json4s._
import scala.concurrent.{Await, Future}


trait EventPull {
  def requestBatch(start: Long, end: Long):Future[List[Event]]
}