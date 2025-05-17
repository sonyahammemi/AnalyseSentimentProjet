package sentimentclinic.utils


import org.apache.spark.sql.{Dataset, SparkSession}
import sentimentclinic.model.Avis

object DataLoader {
  def loadAvis(spark: SparkSession, path: String): Dataset[Avis] = {
    import spark.implicits._
    spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(path)
      .as[Avis]
  }
}
