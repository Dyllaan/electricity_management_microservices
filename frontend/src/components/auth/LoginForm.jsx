import { useState } from 'react';
import useAuth from './useAuth';
/**
 * @author Louis Figes
 */
const LoginForm = () => {
    const { login } = useAuth();

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = async () => {
        login(username, password);
    };

    const valid = username.length > 0 && password.length > 0;

    return (
        <div className="flex items-center justify-center w-[50vw]">
            <div className="rounded w-full">
                <h2 className="text-xl font-bold text-center mb-4">Login</h2>
                <form className="space-y-4">
                    <div>
                        <label className="block text-sm font-medium">Username</label>
                        <input
                            type="text"
                            className="w-full p-2 border rounded"
                            placeholder="Enter your username"
                            onChange={(e) => setUsername(e.target.value)}
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium">Password</label>
                        <input
                            type="password"
                            className="w-full p-2 border rounded"
                            placeholder="Enter your password"
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>
                    <button
                        type="button"
                        onClick={handleLogin}
                        className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600 disabled:opacity-50"
                        disabled={!valid}
                    >
                        Login
                    </button>
                </form>
            </div>
        </div>
    );
};

export default LoginForm;
