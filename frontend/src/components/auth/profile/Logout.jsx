import useAuth from "../useAuth.jsx";
import { useEffect } from "react";
/**
 *
 * Logs the user out, this allows for the /logout route
 * Taken from my project for the Web Application Integration module
 * @author Louis Figes
 *
 */
function Logout() {
    const { logout, signedIn } = useAuth();

    useEffect(() => {
        if(signedIn)
            logout();
    }, [signedIn]);

    return (
        <div>
            <h1>Signed Out</h1>
        </div>
    );
}

export default Logout;