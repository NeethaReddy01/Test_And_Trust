
import {
  FIND_PRODUCTS_BY_CATEGORY_REQUEST,
  FIND_PRODUCTS_BY_CATEGORY_SUCCESS,
  FIND_PRODUCTS_BY_CATEGORY_FAILURE,
  FIND_PRODUCT_BY_ID_REQUEST,
  FIND_PRODUCT_BY_ID_SUCCESS,
  FIND_PRODUCT_BY_ID_FAILURE,
  SEARCH_PRODUCT_REQUEST,
  SEARCH_PRODUCT_SUCCESS,
  SEARCH_PRODUCT_FAILURE,
} from "./ActionTypes";
import api from "../../../config/api";

export const findProducts = (reqData) => async (dispatch) => {
  const {
    category,
  } = reqData;

  try {
    dispatch({ type: FIND_PRODUCTS_BY_CATEGORY_REQUEST });

    const { data } = await api.get(
       `/api/products?category=${category}`
    );

    dispatch({
      type: FIND_PRODUCTS_BY_CATEGORY_SUCCESS,
      
      payload: data,
    });
  } catch (error) {
    dispatch({
      type: FIND_PRODUCTS_BY_CATEGORY_FAILURE,
      payload:
        error.response && error.response.data.message
          ? error.response.data.message
          : error.message,
    });
  }
};

export const findProductById = (reqData) => async (dispatch) => {
  try {
    dispatch({ type: FIND_PRODUCT_BY_ID_REQUEST });

    const { data } = await api.get(`/api/products/id/${reqData.productId}`);


    dispatch({
      type: FIND_PRODUCT_BY_ID_SUCCESS,
      payload: data,
    });
  } catch (error) {
    dispatch({
      type: FIND_PRODUCT_BY_ID_FAILURE,
      payload:
        error.response && error.response.data.message
          ? error.response.data.message
          : error.message,
    });
  }
};

export const searchProduct = (keyword , sort) => async (dispatch) => {
  try {
    dispatch({ type: SEARCH_PRODUCT_REQUEST });

    const { data } = await api.get(`/api/products/search`,{
      params:{
        q:keyword,
        sort: sort || "newest"
      }
    });

    dispatch({
      type: SEARCH_PRODUCT_SUCCESS,
      payload: data,
    });
  } catch (error) {
    dispatch({
      type: SEARCH_PRODUCT_FAILURE,
      payload:
        error.response && error.response.data.message
          ? error.response.data.message
          : error.message,
    });
  }
};




