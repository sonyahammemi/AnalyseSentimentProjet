const express = require('express');
const path = require('path');
const fs = require('fs');

const app = express();
const PORT = 3000;

// Servir les fichiers statiques depuis le dossier 'public'
app.use(express.static(path.join(__dirname, 'public')));

// Route API pour fournir les données JSON
app.get('/data', (req, res) => {
  fs.readFile(path.join(__dirname, '../output/resultats.json'), 'utf8', (err, data) => {
    if (err) {
      console.error('Erreur lors de la lecture du fichier JSON :', err);
      return res.status(500).json({ error: 'Erreur serveur' });
    }
    try {
      const jsonData = JSON.parse(data);
      res.json(jsonData);
    } catch (parseErr) {
      console.error('Erreur lors de l\'analyse du JSON :', parseErr);
      res.status(500).json({ error: 'Erreur de format JSON' });
    }
  });
});

app.listen(PORT, () => {
  console.log(`Serveur démarré sur http://localhost:${PORT}`);
});
