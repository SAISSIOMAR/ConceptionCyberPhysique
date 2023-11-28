const express = require('express');
const axios = require('axios');

const app = express();
const port = 3000;

const OPENHAB_IP = 'http://openhab.ubiquarium.fr';

app.get('/data', async (req, res) => {
    try {
        const response = await axios.get(`${OPENHAB_IP}/rest/items`);
        console.log('reponse is :',response.data)
        res.json(response.data);
    } catch (error) {
        console.error('Error fetching data from OpenHAB:', error);
        res.status(500).send('Error fetching data from OpenHAB');
    }
});

app.get('/status-door4', async (req, res) => {
    try {
        const response = await axios.get(`${OPENHAB_IP}/rest/items/MultiSensorDoor04_Sensor_Door`);
        console.log('reponse is :',response)
        res.json(response);
    } catch (error) {
        console.error('Error fetching data from OpenHAB:', error);
        res.status(500).send('Error fetching data from OpenHAB');
    }
});



app.listen(port, () => {
    console.log(`Server running on http://localhost:${port}`);
});
