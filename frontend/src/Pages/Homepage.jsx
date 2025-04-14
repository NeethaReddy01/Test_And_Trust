import React, { useState, useEffect } from "react";
import HomeCarousel from "../customer/Components/Carousel/HomeCarousel";
import { homeCarouselData } from "../customer/Components/Carousel/HomeCaroselData";
import HomeProductSection from "../customer/Components/Home/HomeProductSection";
import api from "../../src/config/api";

const Homepage = () => {
  const [items, setItems] = useState([]);
  const colors = "Transparent";
  const category = "haircare";

  useEffect(() => {
    async function getData() {
      try {
        const result = await api.get(`/api/products?color=${colors}&category=${category}`);
        console.log("api data", result.data);
        setItems(result.data); 
      } catch (error) {
        console.error("Error fetching products:", error);
      }
    }
    getData();
  }, []);

  return (
    <div>
      <HomeCarousel images={homeCarouselData} />

      <div className="space-y-10 py-20">
        <HomeProductSection data={items} category="Haircare" section={"Haircare"} />
      </div>
    </div>
  );
};

export default Homepage;