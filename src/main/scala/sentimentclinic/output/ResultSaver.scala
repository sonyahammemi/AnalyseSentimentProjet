package sentimentclinic.output

import org.apache.spark.sql.DataFrame
import java.io.{File, PrintWriter}
import org.knowm.xchart.{PieChart, PieChartBuilder, BitmapEncoder}
import org.knowm.xchart.BitmapEncoder.BitmapFormat

object ResultSaver {

  /** 🔸 Sauvegarder au format CSV (avec colonnes filtrées) */
  def saveAsCsv(df: DataFrame, outputPath: String): Unit = {
    val selectedDF = df.select("id", "nom_clinique", "avis", "sentiment")

    selectedDF.coalesce(1)
      .write
      .option("header", "true")
      .mode("overwrite")
      .csv(outputPath + "/resultats_csv")

    println(s"✅ CSV sauvegardé à : $outputPath/resultats_csv")
  }

  /** 🔸 Sauvegarder au format JSON (tableau JSON valide pour navigateur) */
  def saveAsJson(df: DataFrame, outputPath: String): Unit = {
    import java.nio.file.{Paths, Files}
    import java.nio.charset.StandardCharsets

    val selectedDF = df.select("id", "nom_clinique", "avis", "sentiment")
    val rows = selectedDF.toJSON.collect() // chaque ligne est une string JSON

    val jsonArray = "[\n" + rows.mkString(",\n") + "\n]"

    val outputFilePath = Paths.get(outputPath, "resultats.json")
    Files.write(outputFilePath, jsonArray.getBytes(StandardCharsets.UTF_8))

    println(s"✅ JSON (tableau) sauvegardé à : $outputFilePath")
  }

  /** 🔸 Sauvegarder au format texte (avec colonnes filtrées) */
  def saveAsText(df: DataFrame, outputPath: String): Unit = {
    val selectedDF = df.select("id", "nom_clinique", "avis", "sentiment")
    val textData = selectedDF.collect().map(_.mkString(",")).mkString("\n")

    val pw = new PrintWriter(new File(outputPath + "/result.txt"))
    pw.write(textData)
    pw.close()

    println(s"✅ TXT sauvegardé à : $outputPath/result.txt")
  }

  /** 🔸 Générer un diagramme circulaire (pie chart) basé sur la colonne `sentiment` */
  def saveAsPieChart(df: DataFrame, outputPath: String): Unit = {
    // Agréger par sentiment
    val summary = df.groupBy("sentiment").count().collect()

    val categories = summary.map(_.getString(0)) // sentiment
    val values = summary.map(_.getLong(1))       // count

    val chart = new PieChartBuilder()
      .width(800)
      .height(600)
      .title("Répartition des sentiments")
      .build()

    categories.zip(values).foreach { case (category, value) =>
      chart.addSeries(category, value)
    }

    BitmapEncoder.saveBitmap(chart, outputPath + "/resultat", BitmapFormat.PNG)
    println(s"✅ PieChart sauvegardé à : $outputPath/resultat.png")
  }
}




