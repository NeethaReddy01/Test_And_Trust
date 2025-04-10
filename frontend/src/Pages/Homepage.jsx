import React from "react";
import HomeCarousel from "../customer/Components/Carousel/HomeCarousel";
import { homeCarouselData } from "../customer/Components/Carousel/HomeCaroselData";
import HomeProductSection from "../customer/Components/Home/HomeProductSection";
import bathshower from "../Data/BathBodyHygiene/bathshower";
import fragnance from "../Data/Fragnance/fragnance";
import haircare from "../Data/Haircare/haircare";
import MakepupProducts from "../Data/Makeup/MakeupProducts";
import cleanser from "../Data/Skincare/cleanser";
import moisturizer from "../Data/Skincare/moisturizer";
import serum from "../Data/Skincare/Serum";

const Homepage = () => {

  return (
    <div className="">
      <HomeCarousel images={homeCarouselData} />

      <div className="space-y-10 py-20">
      <HomeProductSection data={cleanser} section={"Cleanser"} />
        <HomeProductSection data={moisturizer} section={"Moisturizer"} />
        <HomeProductSection data={serum} section={"serum"} />
        <HomeProductSection data={bathshower} section={"Bath & Shower"} />
        <HomeProductSection data={fragnance} section={"Fragnance"} />
        <HomeProductSection data={haircare} section={"Haircare"} />
        <HomeProductSection data={MakepupProducts} section={"Make up"} />
        {/* <HomeProductSection data={mensPantsPage1} section={"Men's Pants"} /> */}
      </div>

      
    </div>
  );
};

export default Homepage;
