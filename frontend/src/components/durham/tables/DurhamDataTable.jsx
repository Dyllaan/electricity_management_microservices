import { useEffect, useState } from "react";
import axios from 'axios';
import './DataTable.css'; // Import the CSS for styling

/**
* DataTable Component
* Fetches and displays monthly average energy consumption data by provider.
* @author w21023110 - Callum Dobson
*/
const DataTable = ({ accessToken }) => {
    const [providerAverages, setProviderAverages] = useState([]); // Provider average data

    const providerOrder = ['Provider A', 'Provider B', 'Provider C'];

    useEffect(() => { 
        const fetchData = async () => {
            if (accessToken) { 
                try {
                    const response = await axios.get('http://localhost:8000/durham/smart-city/reading/aggregate', {
                        headers: {
                            Authorization: `Bearer ${accessToken}`
                        }
                    });
                    const data = response.data; 
                    const providerData = {}; 

                    data.forEach(item => {
                        const { providerName, averageKwh } = item; 
                        if (!providerData[providerName]) { 
                            providerData[providerName] = []; 
                        }
                        providerData[providerName].push(averageKwh); // Aggregate average kWh
                    });
                    const averagesArray = providerOrder.map((providerName) => {
                        const averages = providerData[providerName] || []; 
                        return {
                            providerName,
                            averageKwh: averages.length > 0 ? averages.reduce((acc, val) => acc + val, 0) / averages.length : 0 
                        };
                    });

                    setProviderAverages(averagesArray); 
                } catch (error) {
                    console.error('Error fetching provider average data:', error); // Error catching to avoid confusion
                }
            }
        };
        fetchData();
    }, [accessToken]); 

    return (
        <div>
            <h2>Monthly Average Energy Consumption by Provider</h2>
            <table>
                <thead>
                    <tr>
                        <th>Provider Name</th>
                        <th>Average kWh</th>
                    </tr>
                </thead>
                <tbody>
                    {providerAverages.map((row, index) => (
                        <tr key={index}>
                            <td>{row.providerName}</td>
                            <td>{row.averageKwh.toFixed(2)}</td> {/* average kWh to 2 decimal places */}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default DataTable; 