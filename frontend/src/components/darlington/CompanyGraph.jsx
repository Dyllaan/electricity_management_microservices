import { tempData } from './mockdata'
import { useState } from 'react'
import LineChart from './linechart';
import Chart from "chart.js/auto";
import { CategoryScale } from "chart.js";

function CompanyGraph(props){
    const [chartData, setChartData] = useState({
        labels: props.data.map((vdata) => vdata.month), 
        datasets: [
          {
            label: "Average KWH",
            data: props.data.map((vdata) => vdata.averageKwh),
            backgroundColor: [
              "rgba(75,192,192,1)",
              "#ecf0f1",
              "#50AF95",
              "#f3ba2f",
              "#2a71d0"
            ],
            borderColor: "black",
            borderWidth: 2
          }
        ]
      });
    return(
        <>
        
            <h2>{props.Name}</h2>

            <LineChart chartData={chartData}></LineChart>
            
        </>
    )
}

export default CompanyGraph