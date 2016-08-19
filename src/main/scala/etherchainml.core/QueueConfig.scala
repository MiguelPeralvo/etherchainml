package etherchainml.core
import java.util.Properties

/**
  * Created by Miguel on 11/08/2016.
  */
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
  val topicJSON = "topicTxs10000_18237"
  val kafkaServerURL = s"$kafkaHost"
  val kafkaProducerBufferSize = 64 * 1024
  val connectionTimeOut = 100000
  val reconnectInterval = 10000
  val clientId = "CollectorPipeline"

  val topicsSet = Seq(topicJSON).toSet
  val kafkaParams = Map[String, String]("metadata.broker.list" -> s"$kafkaHost:$kafkaServerPort",
  "auto.offset.reset" -> "smallest", "group.id" -> groupId)

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
