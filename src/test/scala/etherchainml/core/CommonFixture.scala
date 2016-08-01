package etherchainml.core

import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{BeforeAndAfterAll, Matchers, FunSpec}


trait CommonFixture extends FunSpec with Matchers with BeforeAndAfterAll with ScalaFutures with IntegrationPatience {
  implicit val defaultPatience = PatienceConfig(timeout = Span(15, Seconds), interval = Span(150, Millis))

  override def beforeAll(): Unit = {
    super.beforeAll()
  }

  override def afterAll(): Unit = {
    super.afterAll()
  }

}
