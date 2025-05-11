import { API_BASE_URL } from '../../../config/api';
import {
  CREATE_PAYMENT_REQUEST,
  CREATE_PAYMENT_SUCCESS,
  CREATE_PAYMENT_FAILURE,
  UPDATE_PAYMENT_REQUEST,
  UPDATE_PAYMENT_SUCCESS,
  UPDATE_PAYMENT_FAILURE,
} from './ActionTypes';

import axios from 'axios';

export const createPayment = (reqData) => async (dispatch) => {
  try {
    dispatch({
      type: CREATE_PAYMENT_REQUEST,
    });

    const config = {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${reqData.jwt}`,
      },
    };

    const { data } = await axios.post(`${API_BASE_URL}/api/payments/${reqData.orderId}`, null, config);

    dispatch({
      type: CREATE_PAYMENT_SUCCESS,
      payload: data,
    });

    // Redirect to payment link if available
    if (data.payment_link_url) {
      console.log("Redirecting to payment URL:", data.payment_link_url);
      window.location.href = data.payment_link_url;
    }

    return data;
  } catch (error) {
    console.error("Payment creation error:", error);
    dispatch({
      type: CREATE_PAYMENT_FAILURE,
      payload: error.response && error.response.data.message
        ? error.response.data.message
        : error.message,
    });
    throw error;
  }
};

export const updatePayment = (reqData) => {
  return async (dispatch) => {
    console.log("Updating payment with data:", reqData);
    dispatch(updatePaymentRequest());
    
    try {
      const config = {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${reqData.jwt}`,
        },
      };
      
      // Construct a proper URL with encoded parameters
      const url = new URL(`${API_BASE_URL}/api/payments`);
      url.searchParams.append('payment_id', reqData.paymentId);
      url.searchParams.append('order_id', reqData.orderId);
      
      console.log("Making payment update request to:", url.toString());
      const response = await axios.get(url.toString(), config);
      
      console.log("Payment update successful:", response.data);
      dispatch(updatePaymentSuccess(response.data));
      return response.data;
    } catch (error) {
      console.error("Payment update error:", error);
      dispatch(updatePaymentFailure(error.message));
      throw error;
    }
  };
};

export const updatePaymentRequest = () => {
  return {
    type: UPDATE_PAYMENT_REQUEST,
  };
};

export const updatePaymentSuccess = (payment) => {
  return {
    type: UPDATE_PAYMENT_SUCCESS,
    payload: payment,
  };
};

export const updatePaymentFailure = (error) => {
  return {
    type: UPDATE_PAYMENT_FAILURE,
    payload: error,
  };
};