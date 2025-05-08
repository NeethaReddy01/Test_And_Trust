
import React from "react";
import { Routes, Route } from "react-router-dom";
import Homepage from "../Pages/Homepage";
import ProductDetails from "../customer/Components/Product/ProductDetails/ProductDetails";
import Cart from "../customer/Components/Product/Cart/Cart";
import AdminPannel from "../Admin/AdminPannel";
import Navigation from "../customer/Components/Navbar/Navigation";

const Routers = () => {

  return (
    <div>
        <div>
             <Navigation/>
        </div>
       <div className="">
        <Routes>

        <Route path="/" element={<Homepage/>}></Route>
        <Route path="/home" element={<Homepage/>}></Route>
        <Route path="/product/:productId" element={<ProductDetails/>}></Route>
        <Route path="/cart" element={<Cart/>}></Route>
      

        <Route path="/admin" element={<AdminPannel/>}></Route>

      </Routes>
       </div>
    </div>
  );
};

export default Routers;