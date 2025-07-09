import * as api from '../http/crud';
import { toast } from 'react-toastify';

export default function useDurham() {

    /**
     * @author Louis Figes
     * Ok so basically this will request durham smart city for the aggregations of the readings
     * for charting
     */
    async function getAggregations(accessToken) {
        const response = await api.get('durham/smart-city/reading/aggregate', { Authorization: `Bearer ${accessToken}` });

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
