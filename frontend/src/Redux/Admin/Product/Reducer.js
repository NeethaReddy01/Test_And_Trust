import {
    GET_PRODUCTS_REQUEST,
    GET_PRODUCTS_SUCCESS,
    GET_PRODUCTS_FAILURE,
    CREATE_PRODUCT_REQUEST,
    CREATE_PRODUCT_SUCCESS,
    CREATE_PRODUCT_FAILURE,
    UPDATE_PRODUCT_SUCCESS,
    UPDATE_PRODUCT_FAILURE,
    DELETE_PRODUCT_REQUEST,
    DELETE_PRODUCT_SUCCESS,
    DELETE_PRODUCT_FAILURE,
    CREATE_MULTIPLE_PRODUCTS_REQUEST,
    CREATE_MULTIPLE_PRODUCTS_SUCCESS,
    CREATE_MULTIPLE_PRODUCTS_FAILURE
  } from "./ActionTypes";
  import { UPDATE_PRODUCT_REQUEST } from "./ActionTypes";
  
  const initialState = {
    products: [],
    loading: false,
    error: null,
    uploadSuccess: null,
  uploadError: null,  
  };
  
  const productReducer = (state = initialState, action) => {
    switch (action.type) {
      case GET_PRODUCTS_REQUEST:
        return {
          ...state,
          loading: true,
          error: null,
        };
      case GET_PRODUCTS_SUCCESS:
        return {
          ...state,
          loading: false,
          products: action.payload,
        };
      case GET_PRODUCTS_FAILURE:
        return {
          ...state,
          loading: false,
          error: action.payload,
        };
      case CREATE_PRODUCT_REQUEST:
        return {
          ...state,
          loading: true,
          error: null,
        };
      case CREATE_PRODUCT_SUCCESS:
        return {
          ...state,
          loading: false,
          products: [...state.products, action.payload],
        };
      case CREATE_PRODUCT_FAILURE:
        return {
          ...state,
          loading: false,
          error: action.payload,
        };
      case UPDATE_PRODUCT_REQUEST:
        return {
          ...state,
          loading: true,
          error: null,
        };
      case UPDATE_PRODUCT_SUCCESS:
        return {
          ...state,
          loading: false,
          products: state.products.map((product) =>
            product._id === action.payload._id ? action.payload : product
          ),
        };
      case UPDATE_PRODUCT_FAILURE:
        return {
          ...state,
          loading: false,
          error: action.payload,
        };
      case DELETE_PRODUCT_REQUEST:
        return {
          ...state,
          loading: true,
          error: null,
        };
        case DELETE_PRODUCT_SUCCESS:
  return {
    ...state,
    loading: false,
    products: state.products.filter(product => product.id !== action.payload)
  };
      case DELETE_PRODUCT_FAILURE:
        return {
          ...state,
          loading: false,
          error: action.payload,
        };

        case CREATE_MULTIPLE_PRODUCTS_REQUEST:
  return {
    ...state,
    loading: true,
    error: null,
    uploadSuccess: null,
    uploadError: null,
  };
case CREATE_MULTIPLE_PRODUCTS_SUCCESS:
  return {
    ...state,
    loading: false,
    uploadSuccess: action.payload.message || "Products uploaded successfully",
  };
case CREATE_MULTIPLE_PRODUCTS_FAILURE:
  return {
    ...state,
    loading: false,
    uploadError: action.payload,
  };
      default:
        return state;
    }
  };
  
  export default productReducer;
  