import { useEffect, useState } from "react";
import useDarlington from "../components/darlington/useDarlington.jsx";
import useAuth from "../components/auth/useAuth.jsx";
import CityData from "../components/darlington/CityData.jsx";
import CityGraphs from "../components/darlington/CityGraphs.jsx";
/**
 * @author JakeCooper
 */
const DarlingtonPage = () => {

    const { accessToken } = useAuth();
    const { getAggregations } = useDarlington();
    const [darlingtonData, setDarlingtonData] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            const data = await getAggregations(accessToken);
            setDarlingtonData(data);
        };
        fetchData();
        console.log(darlingtonData);
    }, [accessToken]);

    return (
        <div>
            <h1>Data Visualization</h1>

            {darlingtonData && <CityData dataSet = {darlingtonData}/>}
            {darlingtonData && <CityGraphs dataSet = {darlingtonData}/>}
        </div>
    );
};

export default DarlingtonPage;