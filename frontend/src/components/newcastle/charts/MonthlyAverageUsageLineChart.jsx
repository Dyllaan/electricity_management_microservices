import { Line } from 'react-chartjs-2';
import PropTypes from 'prop-types';

const MonthlyAverageUsageLineChart = ({ aggregate, usage }) => {
    const uniqueMonths = Array.from(new Set(aggregate.map((item) => item.month))).sort(
        (a, b) => new Date(a) - new Date(b)
    );

    const uniqueProviders = Array.from(new Set(aggregate.map((item) => item.providerName))).sort();

    const calculateMonthlyUsage = (averages) => {
        return averages.map((current, index) => {
            if (index === 0) return 0;
            return current - averages[index - 1];
        });
    };

    const overallAverageByMonth = uniqueMonths.map((month) => {
        const monthlyData = aggregate.filter((item) => item.month === month);
        const sumAverageKwh = monthlyData.reduce((acc, item) => acc + item.averageKwh, 0);
        return monthlyData.length > 0 ? sumAverageKwh / monthlyData.length : 0;
    });

    const overallUsageByMonth = calculateMonthlyUsage(overallAverageByMonth);

    const perProviderAverages = {};
    const perProviderUsages = {};

    uniqueProviders.forEach((provider) => {
        const providerAverages = uniqueMonths.map((month) => {
            const monthlyProviderData = aggregate.filter(
                (item) => item.providerName === provider && item.month === month
            );
            const sumAverageKwh = monthlyProviderData.reduce((acc, item) => acc + item.averageKwh, 0);
            return monthlyProviderData.length > 0 ? sumAverageKwh / monthlyProviderData.length : 0;
        });
        perProviderAverages[provider] = providerAverages;
        perProviderUsages[provider] = calculateMonthlyUsage(providerAverages);
    });

    const chartDatasets = [];

    /**
     * Conditionally set the data for all providers based on the usage boolean
      */
    chartDatasets.push({
        label: usage
            ? 'All Providers (Monthly Average Usage in kWh per Subject)'
            : 'All Providers (Monthly Average Total kWh per Subject)',
        data: usage ? overallUsageByMonth : overallAverageByMonth,
        fill: false,
        borderColor: 'rgba(0, 0, 0, 1)',
        borderDash: [5, 5],
        tension: 0.1,
    });

    const colors = [
        'rgba(0, 0, 255, 1)',
        'rgba(255, 0, 0, 1)',
        'rgba(0, 255, 0, 1)',
        'rgba(0, 0, 0, 1)',
    ];

    uniqueProviders.forEach((provider, index) => {
        const color = colors[index % colors.length];
        chartDatasets.push({
            label: usage
                ? `${provider} (Monthly Average Usage in kWh per Subject)`
                : `${provider} (Monthly Average Total kWh per Subject)`,
            data: usage ? perProviderUsages[provider] : perProviderAverages[provider],
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
                    ? 'Monthly kWh Usage per Subject'
                    : 'Monthly Average Total kWh per Subject',
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


MonthlyAverageUsageLineChart.propTypes = {
    aggregate: PropTypes.arrayOf(
        PropTypes.shape({
            providerName: PropTypes.string,
            totalReadings: PropTypes.number,
            averageKwh: PropTypes.number,
            month: PropTypes.string,
        })
    ).isRequired,
    usage: PropTypes.bool.isRequired,
};

export default MonthlyAverageUsageLineChart;
