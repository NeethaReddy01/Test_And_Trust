import React from "react";
import CartItem from "./CartItem";
import { Button } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { useEffect } from "react";
import { getCart } from "../../../Redux/Customers/Cart/Action";
import Alert from "@mui/material/Alert";

const Cart = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const jwt = localStorage.getItem("jwt");
  const { cart } = useSelector(store => store);
  console.log("cart ", cart);

  // Make sure cart items exists and is an array before using array methods
  const cartItems = cart?.cartItems || [];

  // Check if any cart item exceeds available quantity - with improved null safety
  const hasInvalidQuantities = cartItems.some(
    item => item && 
           item.quantity > 
           ((item.product && typeof item.product.quantity === 'number') ? item.product.quantity : 0)
  );
  
  // Check if any product is out of stock (quantity 0) - with improved null safety
  const hasOutOfStockItems = cartItems.some(
    item => item && 
           item.product && 
           typeof item.product.quantity === 'number' && 
           item.product.quantity === 0
  );

  useEffect(() => {
    dispatch(getCart(jwt));
  }, [jwt, dispatch]);

  // Safely get cart details with null checks
  const totalItems = cart?.cart?.totalItem || 0;
  const totalPrice = cart?.cart?.totalPrice || 0;
  const discount = cart?.cart?.discounte || 0;
  const totalDiscountedPrice = cart?.cart?.totalDiscountedPrice || 0;

  return (
    <div className="">
      {cartItems.length > 0 ? (
        <div className="lg:grid grid-cols-3 lg:px-16 relative">
          <div className="lg:col-span-2 lg:px-5 bg-white">
            {/* Stock warnings */}
            {hasInvalidQuantities && (
              <Alert severity="warning" className="mb-4">
                Some items in your cart exceed available quantities. Quantities have been adjusted.
              </Alert>
            )}
            {hasOutOfStockItems && (
              <Alert severity="error" className="mb-4">
                Some items in your cart are out of stock. Please remove them to continue checkout.
              </Alert>
            )}
            
            <div className="space-y-3">
              {cartItems.map((item, index) => (
                // Use index as key when item or item.id is undefined
                <CartItem 
                  key={item?.id || `cart-item-${index}`} 
                  item={item} 
                  showButton={true} 
                />
              ))}
            </div>
          </div>
          <div className="px-5 sticky top-0 h-[100vh] mt-5 lg:mt-0 ">
            <div className="border p-5 bg-white shadow-lg rounded-md">
              <p className="font-bold opacity-60 pb-4">PRICE DETAILS</p>
              <hr />
              
              <div className="space-y-3 font-semibold">
                <div className="flex justify-between pt-3 text-black ">
                  <span>Price ({totalItems} item)</span>
                  <span>₹{totalPrice}</span>
                </div>
                <div className="flex justify-between">
                  <span>Discount</span>
                  <span className="text-green-700">-₹{discount}</span>
                </div>
                <div className="flex justify-between">
                  <span>Delivery Charges</span>
                  <span className="text-green-700">Free</span>
                </div>
                <hr />
                <div className="flex justify-between font-bold text-lg">
                  <span>Total Amount</span>
                  <span className="text-green-700">₹{totalDiscountedPrice}</span>
                </div>
              </div>
              
              <Button
                onClick={() => navigate("/checkout?step=2")}
                variant="contained"
                type="submit"
                disabled={hasInvalidQuantities || hasOutOfStockItems || cartItems.length === 0}
                sx={{ 
                  padding: ".8rem 2rem", 
                  marginTop: "2rem", 
                  width: "100%",
                  opacity: (hasInvalidQuantities || hasOutOfStockItems) ? 0.7 : 1
                }}
              >
                Check Out
              </Button>
              
              {(hasInvalidQuantities || hasOutOfStockItems) && (
                <p className="text-red-500 text-xs mt-2 text-center">
                  Please resolve inventory issues before checkout
                </p>
              )}
            </div>
          </div>
        </div>
      ) : (
        <div className="h-[85vh] flex justify-center items-center flex-col">
          <div className="text-center py-5">
            <h1 className="text-lg font-medium">Hmm, it feels so light!</h1>
            <p className="text-gray-500 text-sm">
              There is nothing in your bag, let's add some items
            </p>
          </div>
          <Button onClick={() => navigate("/")} variant="outlined" sx={{ py: "11px" }}>
            Add Item From Product List
          </Button>
        </div>
      )}
    </div>
  );
};

export default Cart;