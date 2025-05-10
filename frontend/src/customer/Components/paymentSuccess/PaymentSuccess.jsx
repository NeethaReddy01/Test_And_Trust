import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { updatePayment } from "../../../Redux/Customers/Payment/Action";
import { Alert, AlertTitle, Grid, CircularProgress, Button } from "@mui/material";
import { getOrderById } from "../../../Redux/Customers/Order/Action";
import OrderTraker from "../orders/OrderTraker";
import AddressCard from "../adreess/AdreessCard";
import { useLocation, useNavigate } from "react-router-dom";
import { clearCart, getCart } from "../../../Redux/Customers/Cart/Action";

const PaymentSuccess = () => {
  const location = useLocation();
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { order } = useSelector((store) => store);
  const jwt = localStorage.getItem("jwt");

  // Extract query parameters
  const urlParams = new URLSearchParams(location.search);
  const orderId = urlParams.get("order_id");
  const paymentIdFromUrl = urlParams.get("razorpay_payment_id") || urlParams.get("payment_id");
  const paymentLinkId = urlParams.get("razorpay_payment_link_id");
  const paymentStatusFromUrl = urlParams.get("razorpay_payment_link_status");

  const [paymentId, setPaymentId] = useState("");
  const [cartCleared, setCartCleared] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    console.log("orderId:", orderId);
    console.log("paymentId:", paymentIdFromUrl);
    console.log("paymentLinkId:", paymentLinkId);
    console.log("status:", paymentStatusFromUrl);

    if (paymentIdFromUrl) {
      setPaymentId(paymentIdFromUrl);
    }
  }, [location.search]);

  useEffect(() => {
    const processPayment = async () => {
      try {
        if (orderId) {
          // If we have a payment ID directly from Razorpay, update payment on backend
          if (paymentId) {
            const data = { orderId: Number(orderId), paymentId, jwt };
            await dispatch(updatePayment(data));
          }
          
          // Get order details
          await dispatch(getOrderById(orderId));
          
          // Clear cart if not already done
          if (!cartCleared) {
            try {
              await dispatch(clearCart(jwt));
              // Force refresh cart data from server after clearing
              dispatch(getCart(jwt));
              setCartCleared(true);
            } catch (cartError) {
              console.error("Error clearing cart:", cartError);
            }
          }
          
          setLoading(false);
        } else {
          setError("Order ID not found in URL parameters");
          setLoading(false);
        }
      } catch (err) {
        console.error("Error processing payment:", err);
        setError("Failed to process payment. Please check your order status.");
        setLoading(false);
      }
    };

    if (orderId) {
      processPayment();
    }
  }, [orderId, paymentId, dispatch, jwt, cartCleared]);

  const handleContinueShopping = () => {
    navigate('/');
  };

  const handleViewOrders = () => {
    navigate('/account/order');
  };

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh] px-4">
        <CircularProgress />
        <p className="mt-4 text-lg">Processing your payment...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh] px-4">
        <Alert variant="filled" severity="error" sx={{ mb: 4, width: "100%", maxWidth: "500px" }}>
          <AlertTitle>Payment Error</AlertTitle>
          {error}
        </Alert>
        <div className="flex gap-4">
          <Button variant="outlined" onClick={handleContinueShopping}>
            Continue Shopping
          </Button>
          <Button variant="contained" onClick={handleViewOrders}>
            View Orders
          </Button>
        </div>
      </div>
    );
  }

  return (
    <div className="px-2 lg:px-36">
      <div className="flex flex-col justify-center items-center">
        <Alert
          variant="filled"
          severity="success"
          sx={{ mb: 6, width: "fit-content" }}
        >
          <AlertTitle>Payment Success</AlertTitle>
          Congratulations! Your Order Has Been Placed.
        </Alert>
      </div>

      {order.order?.orderId ? (
        <>
          <OrderTraker activeStep={1} />
          <Grid container className="space-y-5 py-5 pt-20">
            {order.order.orderItems.map((item, idx) => (
              <Grid
                container
                item
                key={idx}
                className="shadow-xl rounded-md p-5 border"
                sx={{ alignItems: "center", justifyContent: "space-between" }}
              >
                <Grid item xs={6}>
                  <div className="flex items-center">
                    <img
                      className="w-[5rem] h-[5rem] object-cover object-top"
                      src={item?.product.imageUrl}
                      alt={item.product.title}
                    />
                    <div className="ml-5 space-y-2">
                      <p>{item.product.title}</p>
                      <p className="opacity-50 text-xs font-semibold space-x-5">
                        <span>Color: {item.product.color}</span> <span>Size: {item?.product.sizes}</span>
                      </p>
                      <p>Seller: {item.product.brand}</p>
                      <p>₹{item.price}</p>
                    </div>
                  </div>
                </Grid>
                <Grid item>
                  <AddressCard address={order.order?.shippingAddress} />
                </Grid>
              </Grid>
            ))}
          </Grid>
          
          <div className="flex justify-center mt-8 mb-12 gap-4">
            <Button variant="outlined" onClick={handleContinueShopping}>
              Continue Shopping
            </Button>
            <Button variant="contained" onClick={handleViewOrders}>
              View All Orders
            </Button>
          </div>
        </>
      ) : (
        <div className="text-center text-red-500 mt-10 font-semibold">
          Order not found or not yet processed. Please check your account orders.
          <div className="mt-6 flex justify-center gap-4">
            <Button variant="outlined" onClick={handleContinueShopping}>
              Continue Shopping
            </Button>
            <Button variant="contained" onClick={handleViewOrders}>
              View Orders
            </Button>
          </div>
        </div>
      )}
    </div>
  );
};

export default PaymentSuccess;