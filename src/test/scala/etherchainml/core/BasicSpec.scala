package etherchainml.core

import org.scalatest._

class BasicSpec extends FlatSpec with Matchers
{
//  it should "get the correct env var value for localtest" in {
//    assert("local-test" === System.getenv("ENVIRONMENT"))
//    assert("local-test.json" === System.getenv("CONFIG_FILE"))
//  }

  it should "get the correct env var value for travistest" in {
    assert("travis-test" === System.getenv("ENVIRONMENT"))
    assert("travis-test.json" === System.getenv("CONFIG_FILE"))
  }
}