function drawGauge(score) {
    var data = google.visualization.arrayToDataTable([
        ['Label', 'Value'],
        ['Score', score]
    ]);

    var options = {
        width: 400, height: 120,
        redFrom: 0, redTo: 50,
        yellowFrom:50, yellowTo: 80,
        greenFrom:80, greenTo: 100,
        minorTicks: 5
    };

    var chart = new google.visualization.Gauge(document.getElementById('gauge_chart_div'));

    chart.draw(data, options);
}

function updateAirQualityData() {
    // Fetch data from an API or sensor here
    const airQualityData = {
        score: Math.floor(Math.random() * 101), // Simulated new score
        updatedTime: new Date().toLocaleTimeString(),
        pm1: '0.10',
        pm25: '0.13',
        pm10: '0.20',
        vocs: '0.15'
    };

    // Update the gauge with the new score
    drawGauge(airQualityData.score);

    // Update other DOM elements with the new data
    document.getElementById('score').textContent = airQualityData.score;
    document.querySelector('.updated-time').textContent = `Updated ${airQualityData.updatedTime}`;
    document.querySelectorAll('.pollutant .value').forEach((element, index) => {
        const values = [airQualityData.pm1, airQualityData.pm25, airQualityData.pm10, airQualityData.vocs];
        element.textContent = values[index];
    });
}

// Simulate data update every 10 seconds
setInterval(updateAirQualityData, 2000);

// Initial update on page load
google.charts.load('current', {'packages':['gauge']});
google.charts.setOnLoadCallback(updateAirQualityData);

function redirectToProfile(){
    window.location.href = "/profile";
}
