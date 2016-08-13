package etherchainml.core

import org.apache.log4j.{Level, Logger}
import org.apache.spark
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{StreamingContext, Minutes, Seconds}
import org.apache.spark.streaming.kafka._
import kafka.serializer.StringDecoder
import org.apache.spark.streaming.{Seconds, StreamingContext, Time}
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.sql.{SparkSession, Row, SQLContext}
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
  ssc.checkpoint("checkpoint100")

  val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc,
    QueueConfig.kafkaParams,
    QueueConfig.topicsSet)

  messages.map(_._2).foreachRDD {
    rdd =>
    if (rdd.toLocalIterator.nonEmpty) {
       // Brings in default date formats etc.
      import sparkSession.implicits._

      val rddJsonWrappers = rdd.map(
         jsonWrapper => parse(jsonWrapper).extract[JSONWrapper]).persist()

      //val groupedTxs
      val dfJson = sparkSession.read.json(rddJsonWrappers.map(tx => tx.payload))
      dfJson.printSchema()

      //TODO: Partition by YYYY/MM/DD/HH/MM?
      //TODO: Extract schema name to be able to discriminate entities.
      //val entity = rddJsonWrappers
      //dfJson.write.parquet("$entit.parquet")


      rddJsonWrappers.unpersist()
    }
  }

  ssc.start()
  ssc.awaitTermination()
}
