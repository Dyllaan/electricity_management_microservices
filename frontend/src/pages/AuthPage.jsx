import Profile from '../components/auth/profile/Profile.jsx';
import LoginForm from '../components/auth/LoginForm';
import useAuth from '../components/auth/useAuth';
import RegisterForm from "../components/auth/RegisterForm.jsx";
import {useState} from "react";
/**
 * @author Louis Figes
 * click button to show login or register form
 */
const AuthPage = () => {
    const { signedIn } = useAuth();

    const [showLogin, setShowLogin] = useState(true);

    return (
        <div className="flex items-center justify-center h-screen">
            {signedIn ? (
                <Profile />
            ) : (
                <div className="flex flex-col gap-4 items-center justify-center my-auto">
                    <button
                        onClick={() => setShowLogin(!showLogin)}
                        className="bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600"
                    >
                        {showLogin ? 'New here? Register now' : 'Familiar face? Login'}
                    </button>
                    {showLogin ? (
                        <LoginForm/>
                    ) : (
                        <RegisterForm/>
                    )}
                </div>
            )}
        </div>
    );
};

export default AuthPage;
