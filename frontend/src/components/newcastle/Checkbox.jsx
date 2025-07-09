import PropTypes from "prop-types";

/**
 * @author Louis Figes
 */
const Checkbox = ({ label, checked, onChange }) => {
    return (
        <label className="flex items-center space-x-2 cursor-pointer">
            <input
                type="checkbox"
                className="w-8 h-8 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 focus:ring-2"
                checked={checked}
                onChange={onChange}
            />
            <span className="text-gray-800 text-lg">{label}</span>
        </label>
    );
};

Checkbox.propTypes = {
    label: PropTypes.string.isRequired,
    checked: PropTypes.bool.isRequired,
    onChange: PropTypes.func.isRequired,
}

export default Checkbox;
