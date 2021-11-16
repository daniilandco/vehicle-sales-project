export default class AdService {
  static async fetchAds(terms) {
    const request = {
      fields: ['title', 'description'],
      terms: terms,
      sortBy: 'createdAt',
    };
    const response = await fetch(`/ad/search/query?request=${btoa(JSON.stringify(request))}`);
    const body = await response.json();
    return await body.body;
  }

  static async fetchMainAdPhoto(id) {
    const response = await fetch(`/ad/${id}/main_photo`);
    const body = await response.json();
    return await body.body;
  }
}
