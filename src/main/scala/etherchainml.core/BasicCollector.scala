package etherchainml.core

import java.io.{File, FilenameFilter}
import etherchainml.core.util.{HttpGetEtherChain, HttpGetPoloniex}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.streaming.{Time, Seconds, StreamingContext}
import org.json4s
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.DefaultFormats._
import org.json4s.native.Serialization.{ read, write, writePretty }
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scala.collection.JavaConversions._


object QueueConfig {
  //val kafkaHost = "192.168.99.100"
  val kafkaHost = "localhost"
  val kafkaServerPort = 9092

  val jsonProducerProps = Map(
    "metadata.broker.list" -> s"$kafkaHost:$kafkaServerPort",
    "serializer.class" -> s"kafka.serializer.DefaultEncoder",
    "key.serializer" -> s"org.apache.kafka.common.serialization.StringSerializer",
    "value.serializer" -> s"org.apache.kafka.common.serialization.StringSerializer",
    "bootstrap.servers" -> s"$kafkaHost:$kafkaServerPort")


  val zkConnect = s"$kafkaHost:2181"
  val groupId = "group"
  val topicJSON = "topicUnifiedLogEventSourcing"
  val kafkaServerURL = s"$kafkaHost"
  val kafkaProducerBufferSize = 64 * 1024
  val connectionTimeOut = 100000
  val reconnectInterval = 10000
//  val clientId = "CollectorPipeline"
//
//  val consumerProps = {
//    val props = new Properties()
//    props.put("zookeeper.connect", zkConnect)
//    props.put("group.id", groupId)
//    props.put("zookeeper.session.timeout.ms", "400")
//    props.put("zookeeper.sync.time.ms", "200")
//    props.put("auto.commit.interval.ms", "1000")
//    props
//  }
}

object BasicCollector extends App{

  def writeTxsToKafka(txsToWrite: List[Event]) = txsToWrite match {
    case txs: List[JSONWrapper] => {
      val jsonProducer = new KafkaProducer[String, String](QueueConfig.jsonProducerProps)
      //new KafkaProducer[String, String](QueueConfig.jsonProducerProps)

      txs.foreach { tx =>
        println(write(tx))
        println(s"JSONWrapper, before sending to kafka")

        val key = tx.hashCode().toString
        val value = write(tx)
        val rec = new ProducerRecord[String, String](QueueConfig.topicJSON, key, value)
        // println(rec)
        val f = jsonProducer.send(rec)
        // wait for message ack
        //f.get()
      }
    }

    case _ => println("No match")
  }

  //TODO: Generalise to a higher level of abstraction
  //TODO: In future iterations, we can move from JSON to AVRO to improve efficiency.
  implicit val formats = DefaultFormats
  val httpGetEtherChain = HttpGetEtherChain(parseResponse = HttpGetEtherChain.parseResponseJsonWrapper)
  val etherChainTxs = httpGetEtherChain.requestBatch(start = 0, end = 10)

  etherChainTxs.onComplete {
    case Success(value) => writeTxsToKafka(value)
    case Failure(e) => e.printStackTrace
  }

  val httpGetPoloniexJson = HttpGetPoloniex(parseResponse = HttpGetPoloniex.parseResponseJsonWrapper)
  val poloniexTxs = httpGetPoloniexJson.requestBatch(start = 1440000000, end = 1440001000)

  poloniexTxs.onComplete {
    case Success(value) => writeTxsToKafka(value)
    case Failure(e) => e.printStackTrace
  }

  Common.sleep(5000)

  //Now we write to Kafka




}
