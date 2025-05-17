
package sentimentclinic.classification

import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame}
import org.apache.spark.ml.feature.{Tokenizer, HashingTF, IDF}
import org.apache.spark.ml.classification.RandomForestClassifier
import org.apache.spark.sql.types.IntegerType

object SentimentClassifier {

  def trainAndPredict(data: DataFrame): DataFrame = {
    import data.sparkSession.implicits._

    // 1️⃣ Ajouter une colonne 'label' (0 = négatif, 1 = neutre, 2 = positif)
    val dataWithLabel = data.withColumn("label",
      when($"avis".rlike("(?i)(déçu|mauvaise|horrible|problème|attente|désagréable|catastrophique|bâclée|déconseille|inacceptable|scandaleux)"), 0)
        .when($"avis".rlike("(?i)(satisfait|excellent|parfait|impeccable|recommande|accueillant|professionnel|merci)"), 2)
        .otherwise(1)
    ).withColumn("label", col("label").cast(IntegerType))


    // 2️⃣ Tokenizer → créer 'tokenizedWords'
    val tokenizer = new Tokenizer()
      .setInputCol("avis")
      .setOutputCol("tokenizedWords")

    val tokenizedData = tokenizer.transform(dataWithLabel)

    // 3️⃣ HashingTF → créer 'hashedFeatures'
    val hashingTF = new HashingTF()
      .setInputCol("tokenizedWords")
      .setOutputCol("hashedFeatures")
      .setNumFeatures(1000)

    val featurizedData = hashingTF.transform(tokenizedData)

    // 4️⃣ IDF → pondérer les features, créer 'featuresFinal'
    val idf = new IDF()
      .setInputCol("hashedFeatures")
      .setOutputCol("featuresFinal")

    val idfModel = idf.fit(featurizedData)
    val rescaledData = idfModel.transform(featurizedData)

    // 5️⃣ Classification avec RandomForest
    val rf = new RandomForestClassifier()
      .setLabelCol("label")
      .setFeaturesCol("featuresFinal")
      .setNumTrees(20)

    val model = rf.fit(rescaledData)

    // 6️⃣ Prédictions
    val predictions = model.transform(rescaledData)

    // Vérification de la présence de la colonne 'prediction'
    predictions.show(5)

    // 7️⃣ Ajouter une colonne 'sentiment' (0 = Négatif, 1 = Neutre, 2 = Positif)
    val predictionsWithSentiment = predictions
      .withColumn("sentiment",
        when(col("prediction") === 0.0, "Négatif")
          .when(col("prediction") === 1.0, "Neutre")
          .when(col("prediction") === 2.0, "Positif")
          .otherwise("Inconnu")
      )

    // Sélectionner uniquement les colonnes nécessaires : id, nom_clinique, avis, sentiment
    predictionsWithSentiment.select("id", "nom_clinique", "avis", "sentiment")
  }

}

