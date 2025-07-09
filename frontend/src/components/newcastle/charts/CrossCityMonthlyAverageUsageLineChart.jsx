import { Line } from 'react-chartjs-2';
import PropTypes from 'prop-types';

/**
 * @author Louis Figes
 * modification of my MonthlyAverageUsageLineChart but can compare multiple cities and providers
 */
const CrossCityMonthlyAverageUsageLineChart = ({ aggregate, usage, combineProviders }) => {
    const uniqueMonths = Array.from(new Set(aggregate.map((item) => item.month))).sort(
        (a, b) => new Date(a) - new Date(b)
    );

    const uniqueCities = Array.from(new Set(aggregate.map((item) => item.cityName))).sort();
    const uniqueProviders = Array.from(new Set(aggregate.map((item) => item.providerName))).sort();

    const calculateMonthlyUsage = (averages) => {
        return averages.map((current, index) => {
            if (index === 0) return 0;
            return current - averages[index - 1];
        });
    };

    /**
     * for each unique city and provider, calculate the average kwh for each month
     */
    const getDatasets = (combine) => {
        if(combine) {
            return combineDatasets();
        }
        return getCrossCityData();
    };

    const getCrossCityData = () => {
        const perCityProviderAverages = {};
        const perCityProviderUsages = {};
        uniqueCities.forEach((city) => {
            uniqueProviders.forEach((provider) => {
                const key = `${city}-${provider}`;
                const providerAverages = uniqueMonths.map((month) => {
                    const monthlyCityProviderData = aggregate.filter(
                        (item) => item.cityName === city && item.providerName === provider && item.month === month
                    );
                    const sumAverageKwh = monthlyCityProviderData.reduce((acc, item) => acc + item.averageKwh, 0);
                    return monthlyCityProviderData.length > 0 ? sumAverageKwh / monthlyCityProviderData.length : 0;
                });
                perCityProviderAverages[key] = providerAverages;
                perCityProviderUsages[key] = calculateMonthlyUsage(providerAverages);
            });
        });
        return { averages: perCityProviderAverages, usages: perCityProviderUsages };
    }

    const combineDatasets = () => {
        const perProviderAverages = {};
        const perProviderUsages = {};
        uniqueProviders.forEach((provider) => {
            const providerAverages = uniqueMonths.map((month) => {
                const monthlyProviderData = aggregate.filter(
                    (item) => item.providerName === provider && item.month === month
                );
                const sumAverageKwh = monthlyProviderData.reduce((acc, item) => acc + item.averageKwh, 0);
                return monthlyProviderData.length > 0 ? sumAverageKwh : 0;
            });
            perProviderAverages[provider] = providerAverages;
            perProviderUsages[provider] = calculateMonthlyUsage(providerAverages);
        });
        return { averages: perProviderAverages, usages: perProviderUsages };
    }

    const datasets = getDatasets(combineProviders);
    const chartDatasets = [];
    const colors = [
        'rgba(0, 0, 255, 1)',
        'rgba(255, 0, 0, 1)',
        'rgba(0, 255, 0, 1)',
        'rgba(255, 165, 0, 1)',
        'rgba(128, 0, 128, 1)',
        'rgba(0, 128, 128, 1)',
        'rgba(128, 128, 0, 1)',
        'rgba(0, 0, 0, 1)',
        'rgba(192, 192, 192, 1)',
    ];

    Object.keys(datasets.averages).forEach((key, index) => {
        const [city, provider] = key.split('-');
        const color = colors[index % colors.length];
        chartDatasets.push({
            label: usage
                ? `${city} - ${provider} (Monthly Average Usage in kWh per Subject)`
                : `${city} - ${provider} (Monthly Average Total kWh per Subject)`,
            data: usage ? datasets.usages[key] : datasets.averages[key],
            fill: false,
            borderColor: color,
            tension: 0.1,
        });
    });

    const options = {
        responsive: true,
        plugins: {
            title: {
                display: true,
                text: usage
                    ? 'Monthly kWh Usage per Subject by City and Provider'
                    : 'Monthly Average Total kWh per Subject by City and Provider',
            },
        },
        scales: {
            y: {
                title: {
                    display: true,
                    text: 'kWh usage',
                },
            },
            x: {
                title: {
                    display: true,
                    text: 'Month',
                },
            },
        },
    };

    const chart = {
        labels: uniqueMonths,
        datasets: chartDatasets,
    };

    return <Line data={chart} options={options} />;
};

CrossCityMonthlyAverageUsageLineChart.propTypes = {
    aggregate: PropTypes.arrayOf(
        PropTypes.shape({
            cityName: PropTypes.string,
            providerName: PropTypes.string,
            totalReadings: PropTypes.number,
            averageKwh: PropTypes.number,
            month: PropTypes.string,
        })
    ).isRequired,
    usage: PropTypes.bool.isRequired,
    combineProviders: PropTypes.bool.isRequired,
};

export default CrossCityMonthlyAverageUsageLineChart;
