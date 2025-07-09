/**
 * Graphing Page
 */

import CompanyGraph from './CompanyGraph'
import { useState, useEffect } from 'react'

function CityGraphs(props)
{
    const[ca,setCa] = useState(props.dataSet.filter(dat => dat.providerName == "Provider A"))
    const[cb,setCb] = useState(props.dataSet.filter(dat => dat.providerName == "Provider B"))
    const[cc,setCc] = useState(props.dataSet.filter(dat => dat.providerName == "Provider C"))
    console.log("dataset is " + props.dataSet)
    return(
        <>
            <h1>Graphs</h1>
            <div>
                {props.dataSet && <CompanyGraph Name='Company A' data={ca}/>}
                {props.dataSet && <CompanyGraph Name='Company B' data = {cb}/>}
                {props.dataSet && <CompanyGraph Name='Company C' data = {cc}/>}
            </div>

        </>
    )
}

export default CityGraphs