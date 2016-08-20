package etherchainml.core

import org.apache.log4j.{Level, Logger}
import org.apache.spark
import org.apache.spark.SparkConf
import org.apache.spark.sql.types.StructType

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
  import org.apache.spark.sql.functions._

  def flattenSchema(schema: StructType, prefix: String = null) : Array[Column] = {
    schema.fields.flatMap(f => {
      val colName = if (prefix == null) f.name else (prefix + "." + f.name)

      f.dataType match {
        case st: StructType => flattenSchema(st, colName)
        case _ => Array(col(colName))
      }
    })
  }


  // set logging to error
  Logger.getLogger("org").setLevel(Level.ERROR)
  Logger.getLogger("akka").setLevel(Level.ERROR)
  implicit val formats = DefaultFormats

  val sparkConf = new SparkConf(false).setMaster("local[4]").setAppName("BasicSparkConsumer")
  val sparkSession = SparkSession.builder.config(sparkConf).getOrCreate()
  val ssc = new StreamingContext(sparkSession.sparkContext, Seconds(10))
  ssc.checkpoint("checkpoint400")

  val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc,
    QueueConfig.kafkaParams,
    QueueConfig.topicsSet)

  messages.map(_._2).foreachRDD {
    rdd =>
    if (rdd.toLocalIterator.nonEmpty) {
       // Brings in default date formats etc.
      import sparkSession.implicits._


      val dfJson = sparkSession.read.json(rdd)

      dfJson.printSchema()

      dfJson.groupBy("schema").count().show()
      dfJson.select("schema").distinct.show()

      val flattenedDf = dfJson.select(flattenSchema(dfJson.schema):_*)
      flattenedDf.write.partitionBy("schema").mode(SaveMode.Overwrite).format("json").
        save("txs-json")
      //dfJson.write.partitionBy("schema").text("txs-csv")


      //TODO: Partition by YYYY/MM/DD/HH/MM?
      //TODO: Other outputs: csv, avro, parquet, orc.
    }
  }

  ssc.start()
  ssc.awaitTermination()
}
