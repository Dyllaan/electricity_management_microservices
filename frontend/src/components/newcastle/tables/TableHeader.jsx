import PropTypes from "prop-types";

const TableHeader = ({ columnKey, label, sortConfig, handleSort }) => {
    const getHeaderBg = (key) => {
        if (sortConfig.key !== key) return '';
        return sortConfig.direction === 'asc' ? 'bg-green-500' : 'bg-red-500';
    };

    return (
        <th
            className={`${getHeaderBg(columnKey)} border border-gray-300 px-4 py-2 text-left cursor-pointer`}
            onClick={() => handleSort(columnKey)}
        >
            {label}
        </th>
    );
};

TableHeader.propTypes = {
    columnKey: PropTypes.string.isRequired,
    label: PropTypes.string.isRequired,
    sortConfig: PropTypes.object.isRequired,
    handleSort: PropTypes.func.isRequired,
}

export default TableHeader;
