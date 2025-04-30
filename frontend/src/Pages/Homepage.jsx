import React, { useState, useEffect } from "react";
import HomeCarousel from "../customer/Components/Carousel/HomeCarousel";
import { homeCarouselData } from "../customer/Components/Carousel/HomeCaroselData";
import HomeProductSection from "../customer/Components/Home/HomeProductSection";
import api from "../../src/config/api";

const Homepage = () => {
  const [productsByCategory, setProductsByCategory] = useState([]);
  const [loading, setLoading] = useState(true);
  const categories = [
    "Haircare", 
    "Bath", 
    "Body", 
    "Hygiene", 
    "Fragrance", 
    "Makeup", 
    "Cleanser", 
    "Moisturizer", 
    "Serum", 
    "Sunscreen"
  ];

  useEffect(() => {
    async function fetchCategoryData(category) {
      try {
        const result = await api.get(`/api/products?category=${category.toLowerCase()}`);
        // Make sure data is always an array
        const data = Array.isArray(result.data) ? result.data : [];
        return { category, data };
      } catch (error) {
        console.error(`Error fetching ${category} products:`, error);
        return { category, data: [] };
      }
    }
    
    async function fetchAllCategories() {
      setLoading(true);
      try {
        const categoryPromises = categories.map(category => fetchCategoryData(category));
        const results = await Promise.all(categoryPromises);
        console.log("results:", results);
        setProductsByCategory(results);
      } catch (error) {
        console.error("Error fetching products:", error);
      } finally {
        setLoading(false);
      }
    }

    fetchAllCategories();
  }, []);
  
  console.log("products by category:", productsByCategory);
  
  return (
    <div>
      <HomeCarousel images={homeCarouselData} />

      <div className="space-y-10 py-20">
        {loading ? (
          <div className="text-center py-8">Loading products...</div>
        ) : (
          productsByCategory.length > 0 ? productsByCategory.map((category) => {
            // Ensure data is always an array before passing to HomeProductSection
            const productData = Array.isArray(category.data) ? category.data : [];
            
            return (
              <HomeProductSection 
                key={category.category}
                data={productData} 
                category={category.category} 
                section={category.category} 
              />
            );
          }) : <div className="text-center py-8">No products found</div>
        )}
      </div>
    </div>
  );
};

export default Homepage;