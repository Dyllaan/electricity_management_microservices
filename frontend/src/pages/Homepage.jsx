import useAuth from "../components/auth/useAuth";
import AuthPage from "./AuthPage";
import GroupPage from "./GroupPage.jsx";
/**
 * @author Louis Figes
 */

function HomePage() {

    const { signedIn } = useAuth();

    if(!signedIn) {
        return (
            <div className="flex flex-col text-center gap-2">
                <AuthPage />
            </div>
        );
    }

    return (
        <GroupPage />
    );
}

export default HomePage;
