import $api from "../http";

export default class AdService {
  static async fetchAds(terms) {
    const request = {
      fields: ['title', 'description'],
      terms: terms,
      sort_by: 'createdAt',
    };
    const response = await $api.get(`/ad/search/query?request=${btoa(JSON.stringify(request))}`);
    const body = await response.data;
    return await body.body;
  }

  static async fetchMainAdPhoto(id) {
      try {
          const response = await $api.get(`/ad/${id}/main_photo`);
              const body = await response.data;
              return await body.body;
      }
      catch {
        return null
      }
  }
}
