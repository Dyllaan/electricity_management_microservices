import AuthPage from './pages/AuthPage'
import './App.css'
import AuthProvider from "./components/auth/AuthProvider";
import { ToastContainer } from 'react-toastify';
import HomePage from './pages/Homepage';
import Guest from './components/auth/access-control/Guest';
import Logout from './components/auth/profile/Logout';
import Restricted from "./components/auth/access-control/Restricted";
import { Routes, Route } from 'react-router-dom';
import ProfilePage from "./pages/ProfilePage";
import Navbar from "./components/layout/Navbar";
import NewcastlePage from "./pages/NewcastlePage";
import DurhamPage from "./pages/DurhamPage";
import GroupPage from "./pages/GroupPage";
import {
    CategoryScale,
    Chart as ChartJS,
    Legend,
    LinearScale,
    LineElement,
    BarElement,
    PointElement,
    Title,
    Tooltip
} from "chart.js";
import DarlingtonPage from './pages/DarlingtonPage';
import SimulationPage from "./pages/SimulationPage.jsx";
import AboutPage from "./pages/AboutPage.jsx";

/**
 * @author Louis Figes
 * @author Jake Cooper
 * @author Callum Dobson
 */

function App() {

    ChartJS.register(
        CategoryScale,
        LinearScale,
        BarElement,
        PointElement,
        LineElement,
        Title,
        Tooltip,
        Legend
    );

    return (
        <div className="flex flex-col min-h-screen">
            <AuthProvider>
                <Navbar/>
                <div className="flex-grow overflow-x-hidden">
                    <Routes>
                        <Route path="/" element={<HomePage/>}/>
                        <Route path="/login" element={<Guest><AuthPage/></Guest>}/>
                        <Route path="/logout" element={<Restricted><Logout/></Restricted>}/>
                        <Route path="/profile" element={<Restricted><ProfilePage/></Restricted>}/>

                        <Route path="/newcastle" element={<Restricted><NewcastlePage/></Restricted>}/>
                        <Route path="/darlington" element={<Restricted><DarlingtonPage/></Restricted>}></Route>
                        <Route path="/durham" element={<Restricted><DurhamPage /></Restricted>} /> 
                        <Route path="/newcastle" element={<Restricted><NewcastlePage/></Restricted>}/>
                        <Route path="/group" element={<Restricted><GroupPage/></Restricted>}/>
                        <Route path="/simulation" element={<Restricted><SimulationPage/></Restricted>}/>
                        <Route path="/about" element={<Restricted><AboutPage/></Restricted>} />
                    </Routes>
                </div>
                <ToastContainer/>
            </AuthProvider>
        </div>
);
}

export default App
