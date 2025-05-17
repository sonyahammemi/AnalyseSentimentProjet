
import org.apache.spark.sql.SparkSession
import sentimentclinic.utils.DataLoader
import sentimentclinic.preprocessing.{FeatureExtractor, TextCleaner}
import sentimentclinic.classification.SentimentClassifier
import sentimentclinic.output.ResultSaver

object Main {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "C:\\Users\\PC\\hadoop")

    val spark = SparkSession.builder
      .appName("Analyse Sentiment Project")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    // 1️⃣ Charger les avis
    val pathAvis = "data/avis_clinique.csv"
    val avisDS = DataLoader.loadAvis(spark, pathAvis)

    // 2️⃣ Nettoyer texte
    val cleanedDF = avisDS.map(avis =>
      (avis.id, avis.nom_clinique, avis.avis, TextCleaner.clean(avis.avis))
    ).toDF("id", "nom_clinique", "avis", "cleaned_avis")

    // 3️⃣ Feature extraction
    val featuresDF = FeatureExtractor.transform(cleanedDF)

    // 4️⃣ Classification
    val predictionsDF = SentimentClassifier.trainAndPredict(featuresDF)

    // 5️⃣ Sauvegarder les résultats dans dossier unique output/
    val outputDir = "output"
    ResultSaver.saveAsJson(predictionsDF, outputDir)
    ResultSaver.saveAsCsv(predictionsDF, outputDir)
    ResultSaver.saveAsText(predictionsDF, outputDir)
    ResultSaver.saveAsPieChart(predictionsDF, outputDir)

    println("✅ Analyse terminée. Résultats disponibles dans le dossier 'output/'.")
    spark.stop()
  }
}

