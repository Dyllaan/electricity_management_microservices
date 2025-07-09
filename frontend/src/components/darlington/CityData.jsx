import CompanyTable from './CompanyTable'
import { tempData } from './mockdata';
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

function CityData(props) {
    //We'll be passing in the company data in here at some point.
    //For Now, just note that tempData is meant to be more specific.
    console.log(props.dataSet)
    /* const[ca,setCa] = useState(props.dataSet.filter(dat => dat.providerName == "Provider A"))
    const[cb,setCb] = useState(props.dataSet.filter(dat => dat.providerName == "Provider B"))
    const[cc,setCc] = useState(props.dataSet.filter(dat => dat.providerName == "Provider C")) */
    
    const[ca,setCa] = useState(true)
    const[cb,setCb] = useState(true)
    const[cc,setCc] = useState(true)
    const[query,changeQuery] = useState(props.dataSet)
    
    const newQuery = () =>{
        var qlist = []

        if(ca == true)
        {
          qlist.push("Provider A")
        }

        if(cb == true)
        {
            qlist.push("Provider B")
        }
        
        if(cc == true)
        {
            qlist.push("Provider C") 
        }

        const res = props.dataSet.filter(o => qlist.includes(o.providerName))
        changeQuery(res)
    }
  

    return (
        <>
            <h1>City Companies</h1>
            <div>
                <label>
                    <input type="checkbox" checked = {ca} onChange={() => setCa(!ca)}/>
                    Company A
                </label>

                <label>
                    <input type="checkbox" checked = {cb} onChange={() => setCb(!cb)}/>
                    Company B
                </label>

                <label>
                    <input type="checkbox" checked = {cc} onChange={() => setCc(!cc)}/>
                    Company C
                </label>
            </div>
            <button onClick={() => newQuery()}> Update </button>

            <div>
                <CompanyTable Name = "Darlington Tables" data = {query}/>
            </div>
        </>
    )
}

export default CityData