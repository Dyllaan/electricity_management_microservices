import CompanyTable from "../components/CompanyTable";
import { tempData } from "../assets/mockdata"
import { useState,useEffect } from "react";


/**
 * Data Tables for Cities.
 * @author w17024579 - Jake Cooper
 */

/**
 * The Plan:
 * 1) Take Return from API Call.
 * 2) Pass Each JSON result to the company tables. 
 */

function CityData() {
    //We'll be passing in the company data in here at some point.
    //For Now, just note that tempData is meant to be more specific.
    const[data,setData] = useState(tempData);

    useEffect(() => {
        fetch("http://localhost:8083/smart-city/reading",{"mode":"no-cors"})
        .then(response => response.json())
        .then(json => setData(json))
        .catch(error => console.error(error))
    },[])

    return (
        <>
            <h1>City Companies</h1>
                <CompanyTable Name = "Company A" data = {data}/>
                <CompanyTable Name = "Company B" data = {data}/>
                <CompanyTable Name = "Company C" data = {data}/>
            
        </>
    )
}

export default CityData