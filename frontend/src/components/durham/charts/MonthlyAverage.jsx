import { useEffect, useState } from "react";
import { BarChart } from '@mui/x-charts/BarChart'; 
import axios from 'axios';

/**
* MonthlyAverage Component
* Fetches and displays monthly average energy consumption data by provider in a graph
* @author w21023110 - Callum Dobson
*/

const MonthlyAverage = ({ accessToken }) => {
    const [categories, setCategories] = useState([]); 
    const [values, setValues] = useState([]); 
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

                        providerData[providerName].push(averageKwh); 
                    });

                    const providerOrder = ["Provider A", "Provider B", "Provider C"];
                    setCategories(providerOrder.filter(provider => providerData[provider])); 
                    setValues(providerOrder.map(provider => {
                        const averages = providerData[provider] || [];
                        return averages.length > 0 ? averages.reduce((acc, val) => acc + val, 0) / averages.length : 0;
                    }));

                } catch (error) {
                    console.error('Error fetching monthly average data:', error);
                }
            }
        };
        fetchData(); 
    }, [accessToken]);

    return (
        <BarChart
            xAxis={[{ scaleType: 'band', data: categories, label: 'Providers' }]} 
            yAxis={[{ scaleType: 'linear', label: 'Average kWh' }]}
            series={[{ data: values, label: 'Average kWh per Provider' }]}
            width={500}
            height={300}
            title="Monthly Average Energy Consumption by Provider" 
        />
    );
};

export default MonthlyAverage;