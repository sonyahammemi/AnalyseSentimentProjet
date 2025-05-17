document.addEventListener('DOMContentLoaded', () => {
  fetch('/data')
    .then(response => response.json())
    .then(data => {
      // Création du tableau interactif
      const tableBody = document.getElementById('avisTableBody');
      data.forEach(item => {
        const row = document.createElement('tr');
        row.innerHTML = `
          <td>${item.id}</td>
          <td>${item.nom_clinique}</td>
          <td>${item.avis}</td>
          <td>${item.sentiment}</td>
        `;
        tableBody.appendChild(row);
      });

      // Préparation des données pour le diagramme circulaire
      const sentimentCounts = data.reduce((acc, curr) => {
        acc[curr.sentiment] = (acc[curr.sentiment] || 0) + 1;
        return acc;
      }, {});

      const labels = Object.keys(sentimentCounts);
      const counts = Object.values(sentimentCounts);

      const ctx = document.getElementById('pieChart').getContext('2d');
      new Chart(ctx, {
        type: 'pie',
        data: {
          labels: labels,
          datasets: [{
            label: 'Répartition des sentiments',
            data: counts,
            backgroundColor: [
              'rgba(75, 192, 192, 0.6)',   // Vert
              'rgba(255, 99, 132, 0.6)',   // Rouge
              'rgba(255, 206, 86, 0.6)'    // Jaune
            ]
          }]
        },
        options: {
          responsive: true
        }
      });
    })
    .catch(error => console.error('Erreur de chargement des données :', error));
});
