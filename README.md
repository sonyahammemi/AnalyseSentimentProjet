# 🏥 Analyse de Sentiments sur les Avis de Cliniques Esthétiques

Ce projet utilise Apache Spark pour analyser les avis laissés par des patients sur des cliniques esthétiques, afin de déterminer automatiquement le **sentiment** (positif, neutre ou négatif) exprimé dans chaque commentaire.

---

## 🎯 Objectif du Projet

- Automatiser l'analyse des avis textuels issus de cliniques.
- Catégoriser les avis en **positif**, **neutre** ou **négatif**.
- Générer des fichiers de résultats et des visualisations (camembert).
- Offrir une interface web simple pour explorer les résultats.

---

## 🛠️ Technologies utilisées

- **Scala 2.12**
- **Apache Spark 3.5.5** (Spark SQL & MLlib)
- **XChart 3.8.6** (pour le graphique circulaire)
- **HTML / JavaScript / Chart.js** (pour le dashboard web)
- **SBT** (outil de build)

---

## 📁 Structure du projet

```
sentimentclinic/
├── Main.scala                       # Point d'entrée principal
├── model/Avis.scala                # Classe de données (id, nom_clinique, avis)
├── utils/DataLoader.scala         # Chargement CSV → Dataset[Avis]
├── preprocessing/
│   ├── TextCleaner.scala          # Nettoyage de texte
│   └── FeatureExtractor.scala     # Pipeline TF-IDF
├── classification/
│   └── SentimentClassifier.scala  # Classification RandomForest
├── output/
│   └── ResultSaver.scala          # Export CSV, JSON, texte, PieChart
├── data/avis_clinique.csv         # Données d'entrée
├── output/                        # Résultats générés
└── dashboard/                     # Interface Web
```

---

## ⚙️ Méthodologie de traitement

1. **Chargement des données** depuis un fichier CSV (`avis_clinique.csv`)
2. **Nettoyage de texte** avec suppression de ponctuation, conversion en minuscule
3. **Extraction de caractéristiques** (Tokenization → StopWords → TF-IDF)
4. **Classification** avec un modèle Random Forest :
   - 0 : Négatif
   - 1 : Neutre
   - 2 : Positif
5. **Export des résultats** : JSON, CSV, TXT, graphique en camembert
6. **Affichage web** avec tableau et graphique dynamique

---

## 🖥️ Interface Web

Une interface web HTML/JS est disponible dans le dossier `dashboard/` :
- `index.html` : affiche les avis et le graphique
- `resultats.json` : utilisé pour alimenter dynamiquement les données

> ⚠️ Ouvrir `dashboard/index.html` dans un navigateur pour consulter les résultats.

---

## 🚀 Lancer le projet

### 1. Prérequis

- Java 8 ou supérieur
- SBT installé
- Apache Spark 3.5.5 (inclus via dépendances)

### 2. Exécution locale

```bash
sbt run
```
🌐 Démarrage du dashboard web (Node.js)
bash
Copier
Modifier
cd dashboard
npm install
nodemon server.js
Le dashboard sera disponible à l’adresse :
👉 http://localhost:3000
Le programme :
- lit `data/avis_clinique.csv`
- effectue le traitement
- génère les résultats dans le dossier `output/`

---

## 📦 Résultats générés

- `output/resultats.json` : format lisible par navigateur ou API
- `output/resultats_csv/` : fichier CSV
- `output/result.txt` : export brut en texte
- `output/resultat.png` : graphique en camembert

---
