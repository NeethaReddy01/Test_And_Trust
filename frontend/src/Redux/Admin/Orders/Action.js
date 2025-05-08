
import api from "../../../config/api";
import {
  canceledOrderFailure,
  canceledOrderRequest,
  canceledOrderSuccess,
  confirmedOrderFailure,
  confirmedOrderRequest,
  confirmedOrderSuccess,
  deleteOrderFailure,
  deleteOrderRequest,
  deleteOrderSuccess,
  deliveredOrderFailure,
  deliveredOrderRequest,
  deliveredOrderSuccess,
  getOrdersFailure,
  getOrdersRequest,
  getOrdersSuccess,
  shipOrderFailure,
  shipOrderRequest,
  shipOrderSuccess,
} from "./ActionCreator";

export const getOrders = (reqData) => {

  return async (dispatch) => {
    dispatch(getOrdersRequest());
    try {
     
      const response = await api.get(`/api/admin/orders/`);
     
      dispatch(getOrdersSuccess(response.data));
    } catch (error) {
     
      dispatch(getOrdersFailure(error.message));
    }
  };
};

export const confirmOrder = (orderId) => async (dispatch) => {
  dispatch(confirmedOrderRequest());

  try {
    const response = await api.put(
      `/api/admin/orders/${orderId}/confirmed`
    );
    const data = response.data;
    dispatch(confirmedOrderSuccess(data));
  } catch (error) {
    dispatch(confirmedOrderFailure(error.message));
  }
};

export const shipOrder = (orderId) => {
  return async (dispatch) => {
    try {
      dispatch(shipOrderRequest());
      const {data} = await api.put(`/api/admin/orders/${orderId}/ship`);
    
      dispatch(shipOrderSuccess(data));
    } catch (error) {
      dispatch(shipOrderFailure(error.message));
    }
  };
};

export const deliveredOrder = (orderId) => async (dispatch) => {
  dispatch(deliveredOrderRequest());

  try {
    const response = await api.put(
      `/api/admin/orders/${orderId}/deliver`
    );
    const data = response.data;

    dispatch(deliveredOrderSuccess(data));
  } catch (error) {
    dispatch(deliveredOrderFailure(error.message));
  }
};

export const cancelOrder = (orderId) => async (dispatch) => {
  dispatch(canceledOrderRequest());

  try {
    const response = await api.put(
      `/api/admin/orders/${orderId}/cancel`
    );
    const data = response.data;
    dispatch(canceledOrderSuccess(data));
  } catch (error) {
    dispatch(canceledOrderFailure(error.message));
  }
};


export const deleteOrder = (orderId) => {
  return async(dispatch) => {
    dispatch(deleteOrderRequest());     
   try {
     const {data} = await api.delete(`/api/admin/orders/${orderId}/delete`);
     dispatch(deleteOrderSuccess(orderId));
   } catch (error) {
     dispatch(deleteOrderFailure(error));
   }
      
  };
};
