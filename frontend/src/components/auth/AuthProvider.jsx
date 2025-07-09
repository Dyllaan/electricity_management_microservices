import { createContext, useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import Loading from '../Loading';
import useUser from './useUser';
import * as api from '../http/crud';
/**
 * @author Louis Figes
 */
export const AuthContext = createContext();

export default function AuthProvider({ children }) {
  const [signedIn, setSignedIn] = useState(false);
  const [accessToken, setAccessToken] = useState(null);
  const [username, setUsername] = useState(null);
  const [loading, setLoading] = useState(true);

  const { login, register, deleteAccount, logout, editProfile, changePassword } = useUser({
    setUsername,
    setSignedIn,
    setAccessToken,
    setLoading,
    accessToken,
  });

  useEffect(() => {
    checkToken();
  }, []);

  if (loading) {
    return <Loading />;
  }

  async function checkToken() {
    if (localStorage.getItem('access') && !signedIn) {
      const token = localStorage.getItem('access');
      await currentUser(token);
    } else {
      setLoading(false);
    }
  }

  async function currentUser(token) {
    setLoading(true);
    const response = await api.get('auth/user', { Authorization: `Bearer ${token}` });
    if (response.success) {
      setUsername(response.data.username);
      setAccessToken(token);
      setSignedIn(true);
    }
    setLoading(false);
  }

  return (
      <AuthContext.Provider value={{ signedIn, accessToken, username, login, logout, register, editProfile, deleteAccount, changePassword }}>
        {children}
      </AuthContext.Provider>
  );
}

AuthProvider.propTypes = {
  children: PropTypes.node.isRequired,
};
