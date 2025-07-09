import PropTypes from 'prop-types';

/**
 * @author Louis Figes
 * is the item in the table, just a simple bit of styling
 */
const TableItem = ({ content }) => {
    return (
        <td
            className={`border border-gray-300 px-4 py-2`}
        >
            {content}
        </td>
    );
};

TableItem.propTypes = {
    content: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
}

export default TableItem;
