import * as api from '../../http/crud.jsx';

/**
 * @author Louis Figes
 * Allows you to start a simulation and aggregation from the frontend avoiding postman entirely.
 */

export default function useSimulation() {

    const cities = ['newcastle', 'durham', 'darlington', 'sunderland'];

    async function startAllSimulations(accessToken) {
        const statuses = new Map();

        for (const city of cities) {
            const resp = await startSimForCity(city, accessToken);
            statuses.set(city, resp);
        }
        return statuses;
    }

    async function startAllAggregates(accessToken) {
        const statuses = new Map();

        for (const city of cities) {
            const resp = await startAggForCity(city, accessToken);
            statuses.set(city, resp);
        }
        return statuses;
    }

    /**
     * @author Louis Figes
     * Trigger readings for city
     */
    async function startSimForCity(city, accessToken) {
        const response = await api.post(`${city}/simulator/simulation`, {}, { Authorization: `Bearer ${accessToken}` });

        return response.statusText;
    }

    async function startAggForCity(city, accessToken) {
        const response = await api.get(`${city}/smart-city/aggregate`, { Authorization: `Bearer ${accessToken}` });

        return response.statusText;
    }

    return {
        startAllSimulations,
        startAllAggregates
    };
}
