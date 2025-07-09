

function CompanyTable(props){
    return(
        <>
            <h2>{props.Name}</h2>
            
            <table>
                <tr>
                    <th>Provider</th>
                    <th>Reading Date</th>
                    <th>Average (KWH)</th>
                    <th>Total (KWH)</th>
                    <th>Manual Reports</th>
                    <th>Smart Meter Reports</th>
                    <th>Total Reports</th>
                </tr>
            
            {props.data.map(data =>
                (
                    <tr id={data.providerName + " " + data.month}>
                        <td>{data.providerName}</td>
                        <td>{data.month}</td>
                        <td>{data.averageKwh}</td>
                        <td>{data.totalKwh}</td>
                        <td>{data.citizenSubjectsCount}</td>
                        <td>{data.meterSubjectsCount}</td>
                        <td>{data.totalReadings}</td>
                    </tr>
                )
            )}
            </table>
        </>
    )
}

export default CompanyTable