
name := "AnalyseSentimentProjet"

version := "0.1"

scalaVersion := "2.12.20"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.5.5",
  "org.apache.spark" %% "spark-sql" % "3.5.5",
  "org.apache.spark" %% "spark-mllib" % "3.5.5",
  "org.knowm.xchart" % "xchart" % "3.8.6"
)
