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
        return { category, data: result.data };
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
        console.log("results:",results);
        const newProductsByCategory = {};
        results.forEach(result => {
          newProductsByCategory[result.category] = result.data;
        });
        
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
          productsByCategory ?  productsByCategory.map((category) => (
            <HomeProductSection 
              key={category.category}
              data={category.data || []} 
              category={category.category} 
              section={category.category} 
            />
          )): ""
        )
    } 
      </div>
    </div>
  );
};

export default Homepage;