import { useEffect, useState } from "react";
import useNewcastle from "../components/newcastle/useNewcastle";
import useAuth from "../components/auth/useAuth";
import SortableTable from "../components/newcastle/tables/SortableTable";
import Loading from "../components/Loading";
import MonthlyAverageUsageLineChart from "../components/newcastle/charts/MonthlyAverageUsageLineChart.jsx";
import Checkbox from "../components/newcastle/Checkbox";
/**
 * @author Louis Figes
 */
const NewcastlePage = () => {

    const { accessToken } = useAuth();
    const { getAggregations } = useNewcastle();
    const [newcastleData, setNewcastleData] = useState(null);
    const [usage, setUsage] = useState(false);

    const columns = [
        { key: 'providerName', label: 'Provider Name', isNumber: false },
        { key: 'totalReadings', label: 'Total Readings', isNumber: true },
        { key: 'meterSubjectsCount', label: 'Meter Subjects', isNumber: true },
        { key: 'citizenSubjectsCount', label: 'Citizen Subjects', isNumber: true },
        { key: 'totalKwh', label: 'Total kWh', isNumber: true, format: (val) => val.toFixed(2) },
        { key: 'averageKwh', label: 'Average kWh', isNumber: true, format: (val) => val.toFixed(2) },
        { key: 'month', label: 'Month', isNumber: false },
    ];

    useEffect(() => {
        const fetchData = async () => {
            const data = await getAggregations(accessToken);
            setNewcastleData(data);
        };
        fetchData();
    }, [accessToken]);

    if (!newcastleData) {
        return <Loading />
    }

    return (
        <div>
            <h2>Newcastle Data Aggregation</h2>
            {newcastleData && (
                <div className="flex flex-col items-center gap-1 pt-4 px-4">
                    <SortableTable data={newcastleData} columns={columns} />
                    <div>
                        <Checkbox label="Graph by Usage" checked={usage} onChange={(e) => setUsage(e.target.checked)} />
                    </div>
                    <MonthlyAverageUsageLineChart usage={usage} aggregate={newcastleData} />
                </div>
            )}
        </div>
    );
};

export default NewcastlePage;
