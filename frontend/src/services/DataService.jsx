export default class DataService {
  static async getAds(terms) {
    const request = {
      fields: ['title', 'description'],
      terms: terms,
      sortBy: 'createdAt',
    };
    const response = await fetch(`/ad/search/query?request=${btoa(JSON.stringify(request))}`);
    const body = await response.json();
    console.log('DataService calling 1');
    return await body.body;
  }

  static async getMainAdPhoto(id) {
    const response = await fetch(`/ad/${id}/main_photo`);
    const body = await response.json();
    console.log('DataService calling 2');
    return await body.body;
  }
}
