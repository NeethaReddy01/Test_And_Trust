import React from "react";
import { Button } from "@mui/material";
import { useDispatch } from "react-redux";
import { removeCartItem, updateCartItem } from "../../../Redux/Customers/Cart/Action";
import { IconButton } from "@mui/material";
import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline";
import RemoveCircleOutlineIcon from "@mui/icons-material/RemoveCircleOutline";
import Tooltip from "@mui/material/Tooltip";

const CartItem = ({ item, showButton }) => {
  const dispatch = useDispatch();
  const jwt = localStorage.getItem("jwt");
  
  // Handle case where item might be undefined or null
  if (!item) {
    return null; // Don't render anything if item is null or undefined
  }
  
  // Get available quantity from product data with proper null checking
  const availableQuantity = item?.product?.quantity || 0;
  const isMaxQuantityReached = (item?.quantity || 0) >= availableQuantity;

  const handleRemoveItemFromCart = () => {
    // Only try to remove if we have a valid ID
    if (item?.id) {
      const data = { cartItemId: item.id, jwt };
      dispatch(removeCartItem(data));
    }
  };
  
  const handleUpdateCartItem = (num) => {
    // Don't allow increasing beyond available quantity
    if (num > 0 && isMaxQuantityReached) {
      return;
    }
    
    // Only update if we have a valid item with id
    if (item?.id) {
      const data = {
        data: { quantity: (item.quantity || 1) + num }, 
        cartItemId: item.id, 
        jwt
      };
      dispatch(updateCartItem(data));
    }
  };
  
  // Handle the case where product might be undefined
  const product = item?.product || {};
  const currentQuantity = item?.quantity || 0;
  
  return (
    <div className="p-5 shadow-lg border rounded-md">
      <div className="flex items-center">
        <div className="w-[5rem] h-[5rem] lg:w-[9rem] lg:h-[9rem] ">
          <img
            className="w-full h-full object-cover object-top"
            src={product.imageUrl || '/api/placeholder/150/150'}
            alt={product.title || 'Product image'}
          />
        </div>
        <div className="ml-5 space-y-1">
          <p className="font-semibold">{product.title || 'Product'}</p>
          <p className="opacity-70">Size: {product.sizes || 'N/A'}</p>
          <p className="opacity-70 mt-2">Seller: {product.brand || 'N/A'}</p>
          <div className="flex space-x-2 items-center pt-3">
            <p className="opacity-50 line-through">₹{product.price || 0}</p>
            <p className="font-semibold text-lg">
              ₹{product.discountedPrice || 0}
            </p>
            <p className="text-green-600 font-semibold">
              {product.discountPersent || 0}% off
            </p>
          </div>
          
          {/* Stock information */}
          <p className={`text-sm ${availableQuantity > 5 ? 'text-green-600' : availableQuantity > 0 ? 'text-orange-500' : 'text-red-600'} font-medium mt-2`}>
            {availableQuantity > 0 
              ? `${availableQuantity} in stock` 
              : 'Out of stock'}
          </p>
        </div>
      </div>
    
      {showButton && (
        <div className="lg:flex items-center lg:space-x-10 pt-4">
          <div className="flex items-center space-x-2 ">
            <IconButton 
              onClick={() => handleUpdateCartItem(-1)} 
              disabled={currentQuantity <= 1} 
              color="primary" 
              aria-label="decrease quantity"
            >
              <RemoveCircleOutlineIcon />
            </IconButton>
            
            <span className="py-1 px-7 border rounded-sm">{currentQuantity}</span>
            
            <Tooltip title={isMaxQuantityReached ? "Maximum available quantity reached" : ""}>
              <span>
                <IconButton 
                  onClick={() => handleUpdateCartItem(1)} 
                  disabled={isMaxQuantityReached} 
                  color="primary" 
                  aria-label="increase quantity"
                >
                  <AddCircleOutlineIcon />
                </IconButton>
              </span>
            </Tooltip>
            
            {isMaxQuantityReached && (
              <span className="text-xs text-red-500 italic">Max</span>
            )}
          </div>
          
          <div className="flex text-sm lg:text-base mt-5 lg:mt-0">
            <Button onClick={handleRemoveItemFromCart} variant="text">
              Remove
            </Button>
          </div>
        </div>
      )}
    </div>
  );
};

export default CartItem;