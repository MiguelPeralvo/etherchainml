package etherchainml.core

import org.apache.log4j.{Level, Logger}
import org.apache.spark
import org.apache.spark.SparkConf
import org.apache.spark.sql.types.StructType
import org.json4s.native.Serialization.{ read, write, writePretty }

//import org.apache.spark.sql.types.StructType
import org.apache.spark.streaming.{StreamingContext, Minutes, Seconds}
import org.apache.spark.streaming.kafka._
import kafka.serializer.StringDecoder
import org.apache.spark.streaming.{Seconds, StreamingContext, Time}
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.sql._
import org.json4s._
import org.json4s.jackson.JsonMethods._



object BasicSparkConsumer extends App{

  // set logging to error
  Logger.getLogger("org").setLevel(Level.ERROR)
  Logger.getLogger("akka").setLevel(Level.ERROR)
  implicit val formats = DefaultFormats

  val sparkConf = new SparkConf(false).setMaster("local[4]").setAppName("BasicSparkConsumer")
  val sparkSession = SparkSession.builder.config(sparkConf).getOrCreate()
  val ssc = new StreamingContext(sparkSession.sparkContext, Seconds(10))
  ssc.checkpoint("checkpoint600")

  val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc,
    QueueConfig.kafkaParams,
    QueueConfig.topicsSet)

  messages.map(_._2).foreachRDD {
    rdd =>
    if (!rdd.isEmpty()) {
       // Brings in default date formats etc.
      import sparkSession.implicits._

      //We flatten the JSON nested structure.
      val rddJsonWrappers = rdd.map(
         jsonWrapper => parse(jsonWrapper).extract[JSONWrapper]).repartition(1)
        //.map(json =>
//        (json.hashcode, json.schema, parse(json.payload).extract[Map[String, Any]])
//      ).map((hashcode: String, schema: String, payload:Map[String, Any]) => payload)
      .persist()


      print(rddJsonWrappers.count())

      // Filter Poloniex items '%PoloniexTrade%'
      // TODO: Embed the schema in the fields and do a partitionBy?
      //println(rddJsonWrappers.filter(trade => trade.schema.contains("PoloniexTrade")).count())

      rddJsonWrappers.filter(trade => trade.schema.contains("PoloniexTrade")).map(
        json => write(json.payload)).saveAsTextFile("output/poloniextrade_json")

      // Filter Etherchain items '%EtherChainTrade%'
      //println(rddJsonWrappers.filter(trade => trade.schema.contains("EtherChainTrade")).count())

      rddJsonWrappers.filter(trade => trade.schema.contains("EtherChainTrade")).map(
        json => write(json.payload)).saveAsTextFile("output/etherchaintrade_json")


      rddJsonWrappers.unpersist()
    }
  }

  ssc.start()
  ssc.awaitTermination()
}
