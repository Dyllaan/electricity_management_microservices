import {Link} from 'react-router-dom'

function Navbar(){
    return(
        <>
            <ul>
                <li><Link to="/">City Datatable</Link></li>
                <ul>
                    <li><Link to="/">Company A</Link></li>
                    <li><Link to="/">Company B</Link></li>
                    <li><Link to="/">Company C</Link></li>
                </ul>
                <li><Link to="/CityGraphs">Graphs</Link></li>
            </ul>
        </>
    )
}

export default Navbar