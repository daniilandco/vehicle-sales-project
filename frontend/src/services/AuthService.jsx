import $api from "../http";

class AuthService {
    static CRED_ITEM = 'user';

    static async login(email, password) {
        return $api.post('/auth/login', {email, password});
    }

    static async register(firstName, secondName, email, phoneNumber, password, status, role) {
        return $api.post('/auth/register', {
            firstName,
            secondName,
            email,
            phoneNumber,
            password,
            status,
            role
        });
    }

    static async logout() {
        localStorage.removeItem('user');
        return $api.post('/auth/logout')
    }

    static getCurrentUser() {
        return JSON.parse(localStorage.getItem('user'));
    }
}

export default AuthService;