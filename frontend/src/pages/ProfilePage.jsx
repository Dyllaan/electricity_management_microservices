import Profile from "../components/auth/profile/Profile.jsx";
import ChangePassword from "../components/auth/profile/ChangePassword.jsx";
import DeleteAccount from "../components/auth/profile/DeleteAccount.jsx";
/**
 * @author Louis Figes
 */

function ProfilePage() {

    return (
        <div className="flex flex-col">
            <Profile/>
            <ChangePassword/>
            <DeleteAccount/>
        </div>
    );
}

export default ProfilePage;
