import {makeAutoObservable} from "mobx";
import AuthService from "../services/AuthService";

class Store {
    user;
    isAuth = false;

    constructor() {
        makeAutoObservable(this);
    }

    setAuth(bool) {
        this.isAuth = bool;
    }

    setUser(user) {
        this.user = user;
    }

    async login(email, password) {
        try {
            const response = await AuthService.login(email, password);
            console.log(response);
            localStorage.setItem('token', response.data.body.token);
            this.setAuth(true);
            this.setUser(response.data.body.user);
        } catch (e) {
            console.log(e.response?.data?.message);
        }
    }

    async register(firstName, secondName, email, password) {
        await AuthService.register(firstName, secondName, email, password);
    }

    async logout() {
        try {
            const response = await AuthService.logout();
            localStorage.removeItem('token');
            this.setAuth(false);
            this.setUser({});
        } catch (e) {
            console.log(e.response?.data?.message);
        }
    }

}

export default Store;