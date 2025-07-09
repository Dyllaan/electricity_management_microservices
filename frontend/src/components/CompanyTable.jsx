

function CompanyTable(props){
    return(
        <>
            <h2>{props.Name}</h2>
            
            <table>
                <tr>
                    <th>Reading ID</th>
                    <th>Subject ID</th>
                    <th>Reading (KWH)</th>
                    <th>Date Created</th>
                    <th>Reading Type</th>
                </tr>
            
            {props.data.map(data =>
                (
                    <tr id={data.readingId}>
                        <td>{data.readingId}</td>
                        <td>{data.subjectId}</td>
                        <td>{data.readingKwh}</td>
                        <td>{data.readingCreated}</td>
                        <td>{data.sourceName}</td>
                    </tr>
                )
            )}
            </table>
        </>
    )
}

export default CompanyTable