import * as api from '../http/crud';
import { toast } from 'react-toastify';

export default function useUser({ setUsername, setSignedIn, setAccessToken, setLoading, accessToken }) {
    async function login(username, password) {
        const data = {
            username,
            password,
        };

        const response = await api.post('auth/user/login', JSON.stringify(data), {
            'Content-Type': 'application/json',
        });

        if (response.success) {
            setUserFromLogin(response.data);
        } else {
            toast.error(response.data.cause);
        }
    }

    function setUserFromLogin(response) {
        localStorage.setItem('access', response.token);
        setAccessToken(response.token);
        setUsername(response.username);
        setSignedIn(true);
        setLoading(false);
    }

    async function register(username, password) {
        const data = {
            username,
            password,
        };

        const response = await api.post('auth/user/register', JSON.stringify(data), {
            'Content-Type': 'application/json',
        });

        if (response.success) {
            setUserFromLogin(response.data);
            return true;
        } else {
            toast.error(response.data.cause);
        }
        return false;
    }

    async function changePassword(oldPassword, newPassword) {
        const data = {
            oldPassword,
            newPassword,
        };

        const response = await api.put('auth/user', JSON.stringify(data), {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${accessToken}`,
        });

        if (response.success) {
            toast.success('Password changed successfully');
        } else {
            toast.error(response.data.cause);
        }
    }

    async function deleteAccount() {
        const response = await api.del('auth/user', {}, {
            Authorization: `Bearer ${accessToken}`,
        });

        if (response.success) {
            logout();
        } else {
            toast.error(response.data.cause);
        }
    }

    function logout() {
        localStorage.removeItem('access');
        setSignedIn(false);
        setUsername(null);
        setAccessToken(null);
        setLoading(false);
    }

    return {
        login,
        register,
        logout,
        changePassword,
        deleteAccount,
        accessToken,
    };
}
