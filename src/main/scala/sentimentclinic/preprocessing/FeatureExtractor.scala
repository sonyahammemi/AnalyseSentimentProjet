package sentimentclinic.preprocessing


import org.apache.spark.ml.Pipeline
import org.apache.spark.sql.DataFrame
//import org.apache.spark.ml.feature.{HashingTF, IDF, Pipeline, RegexTokenizer, StopWordsRemover}
import org.apache.spark.ml.feature.{RegexTokenizer, StopWordsRemover, HashingTF, IDF}



object FeatureExtractor {

  //Accepte un DataFrame, pas Dataset[Avis]
  def transform(df: DataFrame): DataFrame = {
    val tokenizer = new RegexTokenizer()
      .setInputCol("cleaned_avis")
      .setOutputCol("words")

    val remover = new StopWordsRemover()
      .setInputCol("words")
      .setOutputCol("filtered_words")

    val hashingTF = new HashingTF()
      .setInputCol("filtered_words")
      .setOutputCol("rawFeatures")
      .setNumFeatures(1000)

    val idf = new IDF()
      .setInputCol("rawFeatures")
      .setOutputCol("features")

    val pipeline = new Pipeline().setStages(Array(tokenizer, remover, hashingTF, idf))

    val model = pipeline.fit(df)
    val featuresDF = model.transform(df)

    featuresDF
  }
}
