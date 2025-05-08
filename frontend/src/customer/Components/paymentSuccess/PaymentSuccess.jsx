import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { updatePayment } from "../../../Redux/Customers/Payment/Action";
import { Alert, AlertTitle, Grid } from "@mui/material";
import { getOrderById } from "../../../Redux/Customers/Order/Action";
import OrderTraker from "../orders/OrderTraker";
import AddressCard from "../adreess/AdreessCard";
import { useLocation } from "react-router-dom";
import { clearCart } from "../../../Redux/Customers/Cart/Action";
import { getCart } from "../../../Redux/Customers/Cart/Action";

const PaymentSuccess = () => {
  const location = useLocation();
  const dispatch = useDispatch();
  const { order } = useSelector((store) => store);
  const jwt = localStorage.getItem("jwt");

  // Extract query parameters
  const urlParams = new URLSearchParams(location.search);
  const orderId = urlParams.get("order_id");
  const paymentIdFromUrl = urlParams.get("razorpay_payment_id");
  const referenceIdFromUrl = urlParams.get("razorpay_payment_link_reference_id");
  const paymentStatusFromUrl = urlParams.get("razorpay_payment_link_status");

  const [paymentId, setPaymentId] = useState("");
  const [referenceId, setReferenceId] = useState("");
  const [paymentStatus, setPaymentStatus] = useState("");
  const [cartCleared, setCartCleared] = useState(false);

  useEffect(() => {
    console.log("orderId:", orderId);
    console.log("paymentId:", paymentIdFromUrl);
    console.log("status:", paymentStatusFromUrl);

    setPaymentId(paymentIdFromUrl);
    setReferenceId(referenceIdFromUrl);
    setPaymentStatus(paymentStatusFromUrl);
  }, [location.search]);

  useEffect(() => {
    if (paymentId && paymentStatus === "paid" && orderId) {
      const data = { orderId: Number(orderId), paymentId, jwt };
      dispatch(updatePayment(data));
      dispatch(getOrderById(orderId));

      if (!cartCleared) {
        try {
          dispatch(clearCart(jwt)).then(() => {
            // Force refresh cart data from server after clearing
            dispatch(getCart(jwt));
            setCartCleared(true); 
          });
        } catch (error) {
          console.error("Error clearing cart:", error);
        }
      }
    }
  }, [orderId, paymentId, paymentStatus , cartCleared]);

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
        </>
      ) : (
        <div className="text-center text-red-500 mt-10 font-semibold">
          Order not found or not yet processed.
        </div>
      )}
    </div>
  );
};

export default PaymentSuccess;
