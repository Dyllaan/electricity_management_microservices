import * as api from '../components/http/crud';
import { toast } from 'react-toastify';

export default function useGroup() {

    /**
     * @author Louis Figes
     * Ok so basically this will request newcastle smart city for the aggregations of the readings
     * for charting
     * when other cities work this will be refactored
     */
    async function getAggregations(accessToken) {
        const response = await api.get('newcastle/smart-city/reading/aggregate', { Authorization: `Bearer ${accessToken}` });

        if (response.success) {
            return response.data;
        } else {
            toast.error(response.data.cause);
        }
    }

    return {
        getAggregations,
    };
}
