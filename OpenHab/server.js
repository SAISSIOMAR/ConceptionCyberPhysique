const express = require('express');
const axios = require('axios');

const path = require('path');


const app = express();
const port = 3000;

const OPENHAB_IP = 'http://openhab.ubiquarium.fr';
const BASE_URL = 'rest/items';


app.get('/data', async (req, res) => {
    try {
        const response = await axios.get(`${OPENHAB_IP}/${BASE_URL}`);
        console.log('reponse is :',response.data)
        res.json(response.data);
    } catch (error) {
        console.error('Error fetching data from OpenHAB:', error);
        res.status(500).send('Error fetching data from OpenHAB');
    }
});
app.use(express.static(path.join(__dirname, 'public')));

app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'public/sign.html'));
});
app.get('/profile', (req, res) => {
    res.sendFile(path.join(__dirname, 'public/profile.html'));
});

app.get('/home', (req, res) => {
    res.sendFile(path.join(__dirname, 'public/home.html'));
});


app.get('/statusd4', async (req, res) => {
    try {
        const response = await axios.get(`${OPENHAB_IP}/${BASE_URL}/MultiSensorDoor04_Sensor_Door`);
        console.log('reponse is :',response.data)
        res.json(response.data);
    } catch (error) {
        console.error('Error fetching data from OpenHAB:', error);
        res.status(500).send('Error fetching data from OpenHAB');
    }
});

app.listen(port, () => {
    console.log(`Server running on http://localhost:${port}`);
});
