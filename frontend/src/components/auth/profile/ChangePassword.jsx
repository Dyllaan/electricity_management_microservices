import { useState } from "react";
import {toast} from "react-toastify";
import useAuth from "../useAuth";

/**
 * @author Louis Figes
 * simple form to change password
 */
const ChangePassword = () => {
    const [form, setForm] = useState({ current: "", new: "", confirm: "" });
    const { changePassword } = useAuth();

    const handleChange = (e) =>
        setForm({ ...form, [e.target.name]: e.target.value });

    const handleSubmit = (e) => {
        e.preventDefault();
        if (form.new !== form.confirm) toast.error("Passwords do not match!");
        else changePassword(form.current, form.new);
    };

    return (
        <form
            onSubmit={handleSubmit}
            className="mx-auto p-4 bg-white rounded shadow space-y-4"
        >
            {["Current Password", "New Password", "Confirm Password"].map((label, idx) => (
                <div key={idx}>
                    <label className="block text-sm font-medium">{label}</label>
                    <input
                        type="password"
                        name={["current", "new", "confirm"][idx]}
                        onChange={handleChange}
                        value={form[["current", "new", "confirm"][idx]]}
                        className="w-full mt-1 p-2 border rounded"
                        required
                    />
                </div>
            ))}
            <button
                className="bg-blue-500 hover:bg-blue-600 ne-button"
            >
                Change
            </button>
        </form>
    );
};

export default ChangePassword;
