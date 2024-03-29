export const UPDATE_LOADING = 'updateLoading';
export const UPDATE_ADS = 'updateAds';
export const RESET = 'reset';

export const init = (state) => {
  return {...state};
};

export const reducer = (state, action) => {
    switch (action.type) {
        case RESET:
            return init(action.payload);
        case UPDATE_LOADING:
            return {...state, loading: action.payload};
        case UPDATE_ADS:
            return {...state, ads: action.payload, loading: false};
        default:
            return state;
    }
};
