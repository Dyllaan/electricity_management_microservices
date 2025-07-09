import { useState } from "react";
import useAuth from "../useAuth";

/**
 * @author Louis Figes
 * button to delete account with confirm modal
 */
const DeleteAccount = () => {
    const [youSureModal, setYouSureModal] = useState(false);
    const { deleteAccount } = useAuth();

    const handleDelete = () => {
        deleteAccount();
        setYouSureModal(false);
    };

    return (
        <div className="flex items-center justify-center p-4">
            <button
                onClick={() => setYouSureModal(true)}
                className="px-4 py-2 text-white bg-red-600 rounded hover:bg-red-700"
            >
                Delete Account
            </button>
            {/** dim background and show model at fixed center */}
            {youSureModal && (
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-75">
                    <div className="bg-white p-6 rounded-md flex flex-col gap-2">
                        <h3>Are you sure?</h3>
                        <p>
                            This cannot be undone.
                        </p>
                        <div className="flex justify-end space-x-2">
                            <button
                                onClick={() => setYouSureModal(false)}
                                className="ne-button bg-gray-600 hover:bg-gray-300"
                            >
                                Cancel
                            </button>
                            <button
                                onClick={handleDelete}
                                className="ne-button bg-red-600 hover:bg-red-700"
                            >
                                Delete
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default DeleteAccount;