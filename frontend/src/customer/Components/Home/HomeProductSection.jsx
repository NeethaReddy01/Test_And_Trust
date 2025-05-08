import AliceCarousel from "react-alice-carousel";
import HomeProductCard from "./HomeProductCard";
import { Button } from "@mui/material";
import ArrowForwardIosIcon from "@mui/icons-material/ArrowForwardIos";
import { useEffect, useState, useRef } from "react";

const HomeProductSection = ({ section, data }) => {
  const [activeIndex, setActiveIndex] = useState(0);
  const [maxIndex, setMaxIndex] = useState(0);
  const carouselRef = useRef(null);
  
  // Ensure data is always an array
  const safeData = Array.isArray(data) ? data : [];
  
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
      setMaxIndex(Math.max(0, safeData.length - itemsPerPage));
    };

    handleResize();
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, [safeData.length]);

  const slidePrev = () => {
    if (carouselRef.current) {
      carouselRef.current.slidePrev();
      setActiveIndex(prev => Math.max(0, prev - 1));
    }
  };

  const slideNext = () => {
    if (carouselRef.current) {
      carouselRef.current.slideNext();
      setActiveIndex(prev => Math.min(maxIndex, prev + 1));
    }
  };

  const syncActiveIndex = ({ item }) => {
    setActiveIndex(item);
  };

  // Create carousel items only if data is an array and has items
  const items = safeData.map((item) => (
    <div key={item.id || item._id} className="carousel-item">
      <HomeProductCard product={item} />
    </div>
  ));

  // Don't render the section if there are no items
  if (safeData.length === 0) {
    return null;
  }

  return (
    <div className="relative px-4 sm:px-6 lg:px-8">
      <h2 className="text-2xl font-extrabold text-gray-900 py-5">{section}</h2>
      <div className="relative border p-5">
        <AliceCarousel
          ref={carouselRef}
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
              top: "50%",
              right: "0",
              transform: "translateY(-50%)",
              zIndex: 10,
              minWidth: "40px",
              width: "40px",
              height: "40px",
              borderRadius: "50%",
              padding: 0,
              backgroundColor: "white",
              color: "#000",
              boxShadow: "0px 0px 5px rgba(0,0,0,0.2)",
              "&:hover": {
                backgroundColor: "#f5f5f5",
              }
            }}
          >
            <ArrowForwardIosIcon fontSize="small" />
          </Button>
        )}

        {activeIndex > 0 && (
          <Button
            onClick={slidePrev}
            variant="contained" 
            sx={{
              position: "absolute",
              top: "50%",
              left: "0",
              transform: "translateY(-50%)",
              zIndex: 10,
              minWidth: "40px",
              width: "40px",
              height: "40px",
              borderRadius: "50%",
              padding: 0,
              backgroundColor: "white",
              color: "#000",
              boxShadow: "0px 0px 5px rgba(0,0,0,0.2)",
              "&:hover": {
                backgroundColor: "#f5f5f5",
              }
            }}
          >
            <ArrowForwardIosIcon 
              fontSize="small"
              sx={{ transform: "rotate(180deg)" }}
            />
          </Button>
        )}
      </div>
    </div>
  );
};

export default HomeProductSection;