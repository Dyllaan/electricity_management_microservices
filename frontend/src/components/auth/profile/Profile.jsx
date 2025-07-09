import useAuth from "../useAuth.jsx";
/**
 * @author Louis Figes
 */
const Profile = () => {
    const { username, logout } = useAuth();

    const handleLogout = () => {
        logout();
    }

    return (
        <div className="p-8 text-center">
            <h2 className="text-2xl font-bold mb-4">Welcome Back {username}!</h2>
            <p className="text-gray-700 mb-6">You are now logged in.</p>
            <button
                onClick={handleLogout}
                className="bg-red-500 text-white py-2 px-4 rounded-md hover:bg-red-600"
            >
                Logout
            </button>
        </div>
    );
};

export default Profile;