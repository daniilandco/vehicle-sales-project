import $api from "../http";
import axios from "axios";

class AuthService {
    static CRED_ITEM = 'user';

    static async login(email, password) {
        const request = {
            email: email,
            password: password
        };
        return axios.create().post('http://localhost:8080/auth/login', JSON.stringify(request), {
            headers: {
                'content-type': 'application/json'
            }
        });
    }

    static async register(firstName, secondName, email, password) {
        return $api.post('/auth/register', {firstName, secondName, email, password});
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