import { useEffect, useState } from "react";
import useAuth from "../components/auth/useAuth"; 
import Loading from "../components/Loading"; 
import MonthlyAverage from "../components/durham/charts/MonthlyAverage"; 
import DataTable from "../components/durham/tables/DurhamDataTable"; 
import axios from 'axios';

const DurhamPage = () => {
    const { accessToken } = useAuth(); 
    const [durhamData, setDurhamData] = useState(null); 

    useEffect(() => {
        const fetchData = async () => {
            if (accessToken) { 
                try {
                    const response = await axios.get('http://localhost:8000/durham/smart-city/reading/aggregate', {
                        headers: {
                            Authorization: `Bearer ${accessToken}`
                        }
                    });
                    setDurhamData(response.data); 
                } catch (error) {
                    console.error('Error fetching aggregated data:', error);
                }
            }
        };
        fetchData(); 
    }, [accessToken]);

    if (!durhamData) {
        return <Loading />; 
    }

    return (
        <div>
            <h2>Durham Data Aggregation</h2>
            {durhamData && (
                <div className="flex flex-col items-center">
                    <MonthlyAverage accessToken={accessToken} />
                    <DataTable accessToken={accessToken} />
                </div>
            )}
        </div>
    );
};

export default DurhamPage;