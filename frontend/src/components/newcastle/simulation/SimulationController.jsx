import { useState, useEffect } from 'react';
import useAuth from '../../auth/useAuth.jsx';
import useSimulation from './useSimulation';
/**
 * @author Louis Figes
 * This component is a controller for the simulation and aggregation of the smart city readings.
 */
const SimulationController = () => {
    const { accessToken } = useAuth();

    const [isSimulating, setIsSimulating] = useState(false);
    const [timeLeft, setTimeLeft] = useState(0);
    const [hasSimulated, setHasSimulated] = useState(false);

    const [responses, setResponses] = useState(new Map());
    const [aggResponses, setAggResponses] = useState(new Map());

    const { startAllSimulations, startAllAggregates } = useSimulation();

    useEffect(() => {
        if (!isSimulating) return;

        setTimeLeft(90);

        const interval = setInterval(() => {
            setTimeLeft((prev) => {
                if (prev <= 1) {
                    clearInterval(interval);
                    setIsSimulating(false);
                    setHasSimulated(true);
                    return 0;
                }
                return prev - 1;
            });
        }, 1000);

        return () => clearInterval(interval);
    }, [isSimulating]);

    const startSimulation = async () => {
        if (isSimulating) return;

        setIsSimulating(true);
        setResponses(await startAllSimulations(accessToken));
    };
    
    const startAgg = async () => {
        if (isSimulating) return;
        setAggResponses(await startAllAggregates(accessToken));
    }


    return (
        <div className="flex flex-col max-w-md mx-auto gap-2">
            <button className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600 disabled:opacity-50"
                    onClick={startSimulation} disabled={isSimulating}>
                {isSimulating ? "Simulating..." : "Start Simulation"}
            </button>
            {isSimulating && (
                <p>{`Time Left: ${Math.floor(timeLeft / 60)}:${String(timeLeft % 60).padStart(2, "0")}`}</p>
            )}
            <button className="w-full text-slate-700 bg-green-500 py-2 rounded hover:bg-green-600 disabled:opacity-50"
                    onClick={startAgg} disabled={!hasSimulated}>
                Start Aggregation
            </button>
            <div>
                <h3>Simulation Responses:</h3>
                {
                    Array.from(responses.keys()).map((city) => (
                        <p key={city}>{`${city}: ${responses.get(city)}`}</p>
                    ))
                }
                <h3>Aggregation Responses:</h3>
                {
                    Array.from(aggResponses.keys()).map((city) => (
                        <p key={city}>{`${city}: ${aggResponses.get(city)}`}</p>
                    ))
                }
            </div>
        </div>
    );
};

export default SimulationController;
