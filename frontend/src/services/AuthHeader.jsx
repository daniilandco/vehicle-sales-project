import AuthService from "./AuthService";

const AuthHeader = () => {
    const user = AuthService.getCurrentUser();

    if (user && user.token) {
        return {'Authorization': user.token};
    }
}

export default AuthHeader;