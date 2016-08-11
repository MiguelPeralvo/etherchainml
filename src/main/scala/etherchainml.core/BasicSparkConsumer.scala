package etherchainml.core

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{StreamingContext, Minutes, Seconds}
import org.apache.spark.streaming.kafka._
import kafka.serializer.StringDecoder


object BasicSparkConsumer extends App{
  // set logging to error
  Logger.getLogger("org").setLevel(Level.ERROR)
  Logger.getLogger("akka").setLevel(Level.ERROR)

  val sparkConf = new SparkConf(false).setMaster("local[4]").setAppName("BasicSparkConsumer")
  val ssc = new StreamingContext(sparkConf, Seconds(10))
  ssc.checkpoint("checkpoint")

  val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc,
    QueueConfig.kafkaParams,
    QueueConfig.topicsSet)

  val lines = messages.map(_._2)
  val words = lines.flatMap(_.split(" "))
  val wordCounts = words.map(x => (x, 1L)).reduceByKey(_ + _)
  wordCounts.print()

  ssc.start()
  ssc.awaitTermination()
}
