export const UPDATE_TERMS = 'updateTerms';
export const UPDATE_ADS = 'updateAds';
export const RESET = 'reset';

export const init = (state) => {
  return {...state};
};

export const reducer = (state, action) => {
  switch (action.type) {
    case RESET:
      return init(action.payload);
    case UPDATE_TERMS:
      return {...state, terms: action.payload};
    case UPDATE_ADS:
      return {...state, ads: action.payload};
    default:
      return state;
  }
};
