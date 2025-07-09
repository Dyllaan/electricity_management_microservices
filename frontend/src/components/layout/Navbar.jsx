import { Link } from 'react-router-dom';
import useAuth from "../auth/useAuth.jsx";

/**
 * @author Louis Figes
 */
const Navbar = () => {

    const { signedIn } = useAuth();

    const cities = [
        { name: 'Newcastle', path: '/newcastle' },
        { name: 'Durham', path: '/durham' },
        { name: 'Sunderland', path: '/sunderland' },
        { name: 'Darlington', path: '/darlington' },
    ];

    if(signedIn) {
        return (
            <div className="flex flex-row gap-4 px-4 items-center justify-center bg-gray-100 w-full h-fit py-4 mb-3 border-b-4 border-gray-300">
                <h1>NE Electricity</h1>
                <Link to="/logout" className="text-red-800 text-xl hover:underline">
                    Logout
                </Link>
                <Link to="/profile" className="text-green-600 text-xl hover:underline">
                    Profile
                </Link>
                {cities.map((city) => (
                    <Link
                        key={city.path}
                        to={city.path}
                        className="text-blue-700 text-xl hover:underline"
                    >
                        {city.name}
                    </Link>
                ))}
                <Link to="/group" className="text-purple-700 text-xl hover:underline">
                    Group
                </Link>
                <Link to="/simulation" className="text-indigo-500 text-xl hover:underline">
                    Simulation
                </Link>
                <Link to="/about" className="text-yellow-500 text-xl hover:underline">
                    About
                </Link>
            </div>
        );
    }
};

export default Navbar;
