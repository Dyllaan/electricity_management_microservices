import { useState, useEffect } from 'react';
import useAuth from '../components/auth/useAuth';
import * as api from '../components/http/crud';
import { toast } from 'react-toastify';
import { redirect } from 'react-router-dom';
/**
 * My hook ive used in a few projects to handle fetching data from an endpoint
 * @author Louis Figes
 */
const useFetchData = (initialEndpoint, initialRun = true) => {

    const { accessToken, loading : userLoading, signOut } = useAuth();
    const [endpoint, setEndpoint] = useState(initialEndpoint);
    const [loading, setLoading] = useState(true);
    const [data, setData] = useState([]);
    const [run, setRun] = useState(initialRun);

    useEffect(() => {
        if (loading && !userLoading && run) {
            fetchData();
        }
    }, [endpoint, loading, userLoading, run]);

    function doRun() {
        setRun(true);
    }

    async function fetchData() {
        try {
            const headers = accessToken ? { Authorization: `Bearer ${accessToken}` } : {};
            const response = await api.get(endpoint, headers);
            if (response.success) {
                setData(response.data);
            } else {
                if(response.status == 401) {
                    toast.error("Please log back in to continue");
                    signOut();
                    redirect('/login');
                }
                toast.error(response.error);
            }
        } catch (error) {
            /** 
             * Don't toast the error, sometimes options requests by the browser can cause this
             * toast.error("An error occurred, please try again later");
             */
            console.log(error);
        } finally {
            setLoading(false);
            if(!initialRun) {
                setRun(false);
            }
        }
    }

    return { 
        loading, 
        data, 
        reloadData: () => setLoading(true),
        setEndpoint,
        doRun
    };
};

export default useFetchData;
