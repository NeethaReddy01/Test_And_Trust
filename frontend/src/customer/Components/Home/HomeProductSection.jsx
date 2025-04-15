import AliceCarousel from "react-alice-carousel";
import HomeProductCard from "./HomeProductCard";
import "./HomeProductSection.css";
import { Button } from "@mui/material";
import ArrowForwardIosIcon from "@mui/icons-material/ArrowForwardIos";
import { useEffect, useState } from "react";

const HomeProductSection = ({ section,category , data }) => {
  const [activeIndex, setActiveIndex] = useState(0);
  const [maxIndex, setMaxIndex] = useState(0);
  const responsive = {
    0: { items: 2 },
    568: { items: 3 },
    1024: { items: 5 },
  };

  useEffect(() => {
    const handleResize = () => {
      const width = window.innerWidth;
      let itemsPerPage = 5;
      if (width < 568) itemsPerPage = 2;
      else if (width < 1024) itemsPerPage = 3;
      setMaxIndex(Math.max(0, data.length - itemsPerPage));
    };

    handleResize();
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, [data.length]);

  const slidePrev = () => setActiveIndex(Math.max(0, activeIndex - 1));
  const slideNext = () => setActiveIndex(Math.min(maxIndex, activeIndex + 1));
  const syncActiveIndex = ({ item }) => setActiveIndex(item);
  const items = data.map((item) => (
    <div key={item.id}>
      <HomeProductCard product={item} />
    </div>
  ));

  return (
    
    <div className="relative px-4 sm:px-6 lg:px-8">
      <h2 className="text-2xl font-extrabold text-gray-900 py-5">{section}</h2>
      <div className="relative border p-5">
        <AliceCarousel
          disableButtonsControls
          disableDotsControls
          mouseTracking
          items={items}
          activeIndex={activeIndex}
          responsive={responsive}
          onSlideChanged={syncActiveIndex}
          animationType="fadeout"
          animationDuration={600}
        />

        {activeIndex < maxIndex && (
          <Button
            onClick={slideNext}
            variant="contained"
            sx={{
              position: "absolute",
              top: "8rem",
              right: "0rem",
              transform: "translateX(50%) rotate(90deg)",
            }}
          >
            <ArrowForwardIosIcon sx={{ transform: "rotate(-90deg)" }} />
          </Button>
        )}

        {activeIndex > 0 && (
          <Button
            onClick={slidePrev}
            variant="contained"
            sx={{
              position: "absolute",
              top: "8rem",
              left: "0rem",
              transform: "translateX(-50%) rotate(90deg)",
            }}
          >
            <ArrowForwardIosIcon sx={{ transform: "rotate(90deg)" }} />
          </Button>
        )}
      </div>
    </div>
  );
};

export default HomeProductSection;