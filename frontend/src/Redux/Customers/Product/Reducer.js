
import {
  FIND_PRODUCTS_BY_CATEGORY_REQUEST,
  FIND_PRODUCTS_BY_CATEGORY_SUCCESS,
  FIND_PRODUCTS_BY_CATEGORY_FAILURE,
  FIND_PRODUCT_BY_ID_REQUEST,
  FIND_PRODUCT_BY_ID_SUCCESS,
  FIND_PRODUCT_BY_ID_FAILURE,
  SEARCH_PRODUCT_SUCCESS,
} from "./ActionTypes";

const initialState = {
  products: [],
  product: null,
  loading: false,
  error: null,
  deleteProduct:null,
  searchProducts:[]
};

const customerProductReducer = (state = initialState, action) => {
  switch (action.type) {
    case FIND_PRODUCTS_BY_CATEGORY_REQUEST:
      return { ...state, loading: true, error: null,
        
        products:[] };
        case FIND_PRODUCTS_BY_CATEGORY_SUCCESS:
      console.log("Reducer received payload:", action.payload);
      console.log("Is payload an array?", Array.isArray(action.payload));
      return { ...state,  products: Array.isArray(action.payload) ? action.payload : [], loading: false };
    case FIND_PRODUCTS_BY_CATEGORY_FAILURE:
      return { ...state, loading: false, products:[], error: action.payload };
    case FIND_PRODUCT_BY_ID_REQUEST:
      return { ...state, loading: true, error: null };
    case FIND_PRODUCT_BY_ID_SUCCESS:
      return { ...state, product: action.payload, loading: false };
    case FIND_PRODUCT_BY_ID_FAILURE:
      return { ...state, loading: false, error: action.payload };
        case SEARCH_PRODUCT_SUCCESS:
          return {
            ...state,
            loading: false,
            searchProducts: action.payload,
          };
    default:
      return state;
  }
};

export default customerProductReducer;