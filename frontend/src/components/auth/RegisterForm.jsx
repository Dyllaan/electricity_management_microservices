import { useState, useEffect } from 'react';
import useAuth from './useAuth';
/**
 * @author Louis Figes
 */
const RegisterForm = () => {
    const { register } = useAuth();

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');

    function isStrong(password) {
        const hasNumber = /\d/;
        const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/;
        return password.length > 8 && hasNumber.test(password) && hasSpecialChar.test(password);
    }

    const valid =
        username.length > 0 &&
        password.length > 0 &&
        confirmPassword.length > 0 &&
        isStrong(password) &&
        password === confirmPassword;

    useEffect(() => {
        if(!valid) {
            setError('Password must be at least 8 characters long, contain a number and special character');
        } else {
            setError('');
        }
    }, [username, password, confirmPassword, valid]);

    const handleRegister = async () => {
        if(!isStrong(password)) {
            setError('Password must be at least 8 characters long and contain a number and special character');
            return;
        }

        if(password !== confirmPassword) {
            setError('Passwords do not match');
            return;
        }

        setError('');
        register(username, password);
    };


    return (
        <div className="flex items-center justify-center w-[50vw]">
            <div className="rounded w-full">
                <h2 className="text-xl font-bold text-center mb-4">Register</h2>
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
                    <div>
                        <label className="block text-sm font-medium">Confirm Password</label>
                        <input
                            type="password"
                            className="w-full p-2 border rounded"
                            placeholder="Confirm your password"
                            onChange={(e) => setConfirmPassword(e.target.value)}
                        />
                    </div>
                    {error && <p className="text-red-500 text-sm">{error}</p>}
                    <button
                        type="button"
                        onClick={handleRegister}
                        className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600 disabled:opacity-50"
                        disabled={!valid}
                    >
                        Register
                    </button>
                </form>
            </div>
        </div>
    );
};

export default RegisterForm;
