import axios from "axios";

export default class DataService {
    static async getAds(terms) {
        const response = await axios.get('https://localhost:8080/ad/search/query',
            {
                params: {
                    fields: ["title", "description"],
                    terms: terms,
                    sortBy: "createdAt"
                }
            });
        return response.data;
    }
}