import React from "react";
import { useNavigate } from "react-router-dom";

const HomeProductCard = ({ product }) => {
  // window.scrollTo(0,0);
  const navigate = useNavigate();
  const fallbackImage =
    "https://via.placeholder.com/150?text=No+Image";
    // console.log("home product " , product);

  return (
    <div
      onClick={() => navigate(`/product/${product?.id || product?._id}`)}

      className="cursor-pointer flex flex-col items-center bg-white rounded-lg shadow-lg overflow-hidden w-[15rem] mx-3 transition-all duration-300 transform hover:scale-105 hover:shadow-2xl"
    >
      <div className="h-[13rem] w-[10rem]">
        <img
          className="object-cover object-top w-full h-full"
          src={product?.image || product?.imageUrl || fallbackImage}
          alt={product?.title}
        />
      </div>

      <div className="p-4 text-center">
        <h3 className="text-lg font-medium text-gray-900 truncate">
          {product?.brand || product?.title}
        </h3>
        <p className="mt-2 text-sm text-gray-500 line-clamp-2">
          {product?.title}
        </p>
      </div>
    </div>
  );
};

export default HomeProductCard;