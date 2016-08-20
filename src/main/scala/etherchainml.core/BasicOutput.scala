package etherchainml.core

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{SaveMode, SparkSession, Column}
import org.apache.spark.sql.types.StructType


class BasicOutput {

}


object BasicOutput {
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

  /**
    * It will save to different formats
    * @param rdd
    * @param sparkSession
    */
  def saveToFormats(rdd: RDD[String], sparkSession: SparkSession): Unit = ???
//  {
//    // Brings in default date formats etc.
//    import sparkSession.implicits._
//
//
//    val dfJson = sparkSession.read.json(rdd)
//
//    dfJson.printSchema()
//
//    dfJson.groupBy("schema").count().show()
//    dfJson.select("schema").distinct.show()
//
//    val flattenedDf = dfJson.select(flattenSchema(dfJson.schema):_*)
//    flattenedDf.write.partitionBy("schema").mode(SaveMode.Overwrite).format("json").
//      save("txs-json")
//    //dfJson.write.partitionBy("schema").text("txs-csv")
//
//
//    //TODO: Partition by YYYY/MM/DD/HH/MM?
//    //TODO: Other outputs: csv, avro, parquet, orc.
//
//
//
//  }


}
