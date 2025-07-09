import { useEffect, useState } from 'react';
import TableHeader from './TableHeader';
import TableItem from './TableItem';
import PropTypes from 'prop-types';

/**
 * @author Louis Figes
 * A sortable table component with clickable headers for ascending/descending sorting.
 */
const SortableTable = ({ data, columns }) => {
    const [sortConfig, setSortConfig] = useState({ key: 'month', direction: 'desc' });
    const [sortedData, setSortedData] = useState([]);

    useEffect(() => {
        setSortedData(data || []);
    }, [data]);

    /**
     * Sorts the data based on the key and direction
     */
    const handleSort = (key) => {
        const direction = sortConfig.key === key && sortConfig.direction === 'asc' ? 'desc' : 'asc';
        const sorted = [...data].sort((a, b) => {
            const [x, y] = [a[key], b[key]];
            return x < y ? (direction === 'asc' ? -1 : 1) : x > y ? (direction === 'asc' ? 1 : -1) : 0;
        });
        setSortedData(sorted);
        setSortConfig({ key, direction });
    };

    return (
        <div className="container mx-auto p-4 bg-gray-300">
            <div className="h-96 overflow-auto border border-gray-700 overflow-y-scroll">
                <table className="border border-gray-700">
                    <thead className="sticky top-0 z-10">
                    <tr className="bg-gray-700">
                        {columns.map(({ key, label }) => (
                            <TableHeader
                                key={key}
                                columnKey={key}
                                label={label}
                                sortConfig={sortConfig}
                                handleSort={handleSort}
                            />
                        ))}
                    </tr>
                    </thead>
                    <tbody>
                    {sortedData.map((row, index) => (
                        <tr
                            key={index}
                            className={`${index % 2 === 0 ? 'bg-white' : 'bg-gray-50'} hover:bg-gray-100`}
                        >
                            {columns.map(({ key, format }) => (
                                <TableItem
                                    key={key}
                                    content={row[key] !== undefined && row[key] !== null ? (format ? format(row[key]) : row[key]) : '-'}
                                />
                            ))}
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

SortableTable.propTypes = {
    data: PropTypes.array.isRequired,
    columns: PropTypes.arrayOf(
        PropTypes.shape({
            key: PropTypes.string.isRequired,
            label: PropTypes.string.isRequired,
            format: PropTypes.func,
            isNumber: PropTypes.bool,
        })
    ).isRequired,
};

export default SortableTable;
