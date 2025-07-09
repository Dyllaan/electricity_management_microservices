import useAuth from "../useAuth.jsx";
import PropTypes from 'prop-types';
import { Navigate } from 'react-router-dom';
/**
 *
 * Prevents logged in users from accessing a page
 * Taken from my project for the Web Application Integration module
 * @author Louis Figes
 *
 */
function Restricted({children}) {
    const { signedIn, loading } = useAuth();
    if (signedIn && !loading) {
        return children;
    } else {
        return <Navigate to='/login' />
    }
}

export default Restricted;

Restricted.propTypes = {
    children: PropTypes.node.isRequired
}