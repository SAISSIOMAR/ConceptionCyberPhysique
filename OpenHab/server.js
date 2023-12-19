const express = require('express');
const axios = require('axios');

const app = express();
const port = 3000;

const OPENHAB_IP = 'http://openhab.ubiquarium.fr';
const BASE_URL = 'rest/items';

app.get('/t', async (req, res) => {
    try {
        // Fetch all windows with their links
        const response = await axios.get(`${OPENHAB_IP}/${BASE_URL}`);
        const windows = filterWindowItems(response.data);

        // Fetch states for each window
        const windowsWithStates = await Promise.all(windows.map(async (window) => {
            try {
                const windowResponse = await axios.get(window.link);
                return { ...window, state: windowResponse.data.members[0].state };
            } catch (error) {
                console.error(`Error fetching state for ${window.label}:`, error);
                return { ...window, state: 'Error' }; // Handle error state
            }
        }));
        
        
        res.json(windowsWithStates);
    } catch (error) {
        console.error('Error fetching data from OpenHAB:', error);
        res.status(500).send('Error fetching data from OpenHAB');
    }
});


app.get('/msg', async (req, res) => {
    try {
        // Fetch all windows with their links
        const response = await axios.get(`${OPENHAB_IP}/${BASE_URL}`);
        const windows = filterWindowItems(response.data);

        // Fetch states for each window
        const windowsWithStates = await Promise.all(windows.map(async (window) => {
            try {
                const windowResponse = await axios.get(window.link);
                return { ...window, state: windowResponse.data.members[0].state };
            } catch (error) {
                console.error(`Error fetching state for ${window.label}:`, error);
                return { ...window, state: 'Error' }; // Handle error state
            }
        }));
        
        console.log('all windows', windowsWithStates)

        // Filter windows that are CLOSED or NULL
        const windowsToOpen = windowsWithStates.filter(window => window.state === 'CLOSED' || window.state === 'NULL');

        // Construct the message
        let message = 'All windows are in the correct state.';
        if (windowsToOpen.length > 0) {
            const windowLabels = windowsToOpen.map(window => window.label).join(', ');
            message = `Please open the windows: ${windowLabels}.`;
        }

        //res.json({ message: message });
        res.send(message)
    } catch (error) {
        console.error('Error fetching data from OpenHAB:', error);
        res.status(500).send('Error fetching data from OpenHAB');
    }
});

function filterWindowItems(data) {
    return data
        .filter(item => item.label && item.label.toLowerCase().includes('window'))
        .map(item => ({
            link: item.link,
            label: item.label,
        }));
}


//////

async function filterWindowItems(data, axios) {
    const windowItems = data.filter(item => item.label && item.label.toLowerCase().includes('window'));

    const windowStates = await Promise.all(windowItems.map(async (item) => {
        try {
            const response = await axios.get(item.link);
            item.state = response.data.state; // Update state with the fetched state
            return item;
        } catch (error) {
            console.error(`Error fetching state from ${item.link}:`, error);
            item.state = 'Error'; // Handle error state
            return item;
        }
    }));

    return windowStates.map(item => {
        let message = item.state === 'OPEN' ? `Please open the ${item.label}.` : `Please let the ${item.label} closed.`;
        return { link: item.link, label: item.label, msg: message };
    });
}

app.get('/data/aw', async (req, res) => {
    try {
        const response = await axios.get(`${OPENHAB_IP}/${BASE_URL}`);
        console.log('response is:', response.data);
        const filteredData = await filterWindowItems(response.data, axios); // Note the use of 'await' here
        console.log('filtered data:', filteredData);
        res.json(filteredData);
    } catch (error) {
        console.error('Error fetching data from OpenHAB:', error);
        res.status(500).send('Error fetching data from OpenHAB');
    }
});



//////


app.get('/data/allwindows', async (req, res) => {
    try {
        const response = await axios.get(`${OPENHAB_IP}/${BASE_URL}`);
        console.log('reponse is :',response.data)
        const filteredData = filterWindowItems(response.data);
        console.log('filtered data: ',filteredData);
        res.json(filteredData);
    } catch (error) {
        console.error('Error fetching data from OpenHAB:', error);
        res.status(500).send('Error fetching data from OpenHAB');
    }
});

function filterWindowItems(data) {
    return data
        .filter(item => item.label && item.label.toLowerCase().includes('window'))
        .map(item => {
            return {
                link: item.link,
                label: item.label,
            };
        });
    }

app.get('/data', async (req, res) => {
    try {
        const response = await axios.get(`${OPENHAB_IP}/${BASE_URL}`);
        console.log('reponse is :',response.data)
        const filteredData = generateSingleWindowMessage(response.data);
        console.log('filtered data: ',filteredData);
        res.json(filteredData);
    } catch (error) {
        console.error('Error fetching data from OpenHAB:', error);
        res.status(500).send('Error fetching data from OpenHAB');
    }
});
function generateSingleWindowMessage(data) {
    const windowsToOpen = data
        .filter(item => item.label && item.label.toLowerCase().includes('window'))
        .filter(item => item.state === 'NULL' || item.state === 'CLOSED')
        .map(item => item.label);

    if (windowsToOpen.length > 0) {
        return `Please open the windows: ${windowsToOpen.join(', ')}.`;
    } else {
        return `All windows are in the correct state.`;
    }
}

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
