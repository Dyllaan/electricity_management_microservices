import { useContext } from 'react';
import { AuthContext } from './AuthProvider';
/**
 * Hook for the user context
 * Seeprated from auth provider for readability and separation of concerns
 */
function useAuth() {
    return useContext(AuthContext);
}

export default useAuth;