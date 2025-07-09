/**
 * Graphing Page
 */

import CompanyGraph from "../components/CompanyGraph"

function CityGraphs()
{
    return(
        <>
            <h1>Graphs</h1>
            <CompanyGraph Name='Company A'/>
            <CompanyGraph Name='Company B'/>
            <CompanyGraph Name='Company C'/>

        </>
    )
}

export default CityGraphs