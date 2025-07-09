import { useEffect, useState } from "react";
import useNewcastle from "../components/newcastle/useNewcastle";
import useAuth from "../components/auth/useAuth";
import SortableTable from "../components/newcastle/tables/SortableTable";
import Loading from "../components/Loading";
import CrossCityMonthlyAverageUsageLineChart from "../components/newcastle/charts/CrossCityMonthlyAverageUsageLineChart.jsx";
import useDarlington from "../components/darlington/useDarlington";
import useDurham from "../components/durham/useDurham";
import Checkbox from "../components/newcastle/Checkbox";
/**
 * @author Louis Figes
 * this could definitely be done better but i dont have time
 */
const GroupPage = () => {

    const { accessToken } = useAuth();
    const { getAggregations: getNewcAgg } = useNewcastle();
    const { getAggregations: getDarlAgg } = useDarlington();
    const { getAggregations: getDurhAgg } = useDurham();
    const [newcastleData, setNewcastleData] = useState(null);
    const [darlingtonData, setDarlingtonData] = useState(null);
    const [durhamData, setDurhamData] = useState(null);
    const [crossCityData, setCrossCityData] = useState(null);
    const [usage, setUsage] = useState(false);
    const [combineProviders, setCombineProviders] = useState(false);


    const baseColumns = [
        { key: 'cityName', label: 'City Name', isNumber: false },
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
            setNewcastleData(await getNewcAgg(accessToken));
            setDarlingtonData(await getDarlAgg(accessToken));
            setDurhamData(await getDurhAgg(accessToken));
        };
        fetchData();
    }, [accessToken]);


    useEffect(() => {
        if (!newcastleData || !darlingtonData || !durhamData) {
            return;
        }

        const combinedData = [
            ...newcastleData.map((item) => ({ ...item, cityName: 'Newcastle' })),
            ...darlingtonData.map((item) => ({ ...item, cityName: 'Darlington' })),
            ...durhamData.map((item) => ({ ...item, cityName: 'Durham' })),
        ];

        setCrossCityData(combinedData);
    }, [newcastleData, darlingtonData, durhamData]);


    if (!crossCityData) {
        return <Loading />
    }

    return (
        <div>
            <h2>Group Data Aggregation</h2>
            {crossCityData && 
                (
                    <div>
                        <SortableTable data={crossCityData} columns={baseColumns}/>
                        <div className="flex flex-col items-center gap-1 pt-4 px-4">
                            <div className="flex flex-row gap-4">
                                <Checkbox label="Graph by Usage" checked={usage}
                                          onChange={(e) => setUsage(e.target.checked)}/>
                                <Checkbox label="Combine Providers" checked={combineProviders}
                                          onChange={(e) => setCombineProviders(e.target.checked)}/>
                            </div>
                            <CrossCityMonthlyAverageUsageLineChart aggregate={crossCityData} usage={usage}
                                                                   combineProviders={combineProviders}/>
                        </div>
                    </div>
                )
            }
        </div>
    );
};

export default GroupPage;
