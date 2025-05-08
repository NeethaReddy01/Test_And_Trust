import { useNavigate, useParams } from "react-router-dom";
import ProductReviewCard from "./ProductReviewCard";
import { Box, Button, Grid, LinearProgress, Rating } from "@mui/material";
import HomeProductCard from "../../Home/HomeProductCard";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { findProductById, findProducts } from "../../../../Redux/Customers/Product/Action";
import { addItemToCart } from "../../../../Redux/Customers/Cart/Action";
import { getAllRatings, getAllReviews } from "../../../../Redux/Customers/Review/Action";

const product = {
  name: "Basic Tee 6-Pack",
  price: "₹996",
  href: "#",
  description:
    'The Basic Tee 6-Pack allows you to fully express your vibrant personality with three grayscale options. Feeling adventurous? Put on a heather gray tee. Want to be a trendsetter? Try our exclusive colorway: "Black". Need to add an extra pop of color to your outfit? Our white tee has you covered.',
  highlights: [
    "Hand cut and sewn locally",
    "Dyed with our proprietary colors",
    "Pre-washed & pre-shrunk",
    "Ultra-soft 100% cotton",
  ],
  details:
    'The 6-Pack includes two black, two white, and two heather gray Basic Tees. Sign up for our subscription service and be the first to get new, exciting colors, like our upcoming "Charcoal Gray" limited release.',
};



export default function ProductDetails() {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  
  // Access your Redux store according to its actual structure
  const { customersProduct } = useSelector((store) => store);
  
  // Access the products state based on your actual Redux store structure
  const productsState = useSelector((store) => store.products || store.customersProduct || {});
  const similarProducts = productsState.products || [];
  const loadingSimilarProducts = productsState.loading || false;
  
  const { productId } = useParams();
  const jwt = localStorage.getItem("jwt");

  // Get review state from Redux store
  const reviewState = useSelector((state) => state.review || {});
  const ratings = reviewState.ratings || [];
  const reviews = reviewState.reviews || [];

  // Calculate average rating
  const calculateAverageRating = () => {
    if (!ratings || ratings.length === 0) {
      return 0;
    }
    const sum = ratings.reduce((total, rating) => total + rating.rating, 0);
    return (sum / ratings.length).toFixed(1);
  };

  const averageRating = calculateAverageRating();

  // Count ratings by value (5 star, 4 star, etc.)
  const getRatingCounts = () => {
    const counts = {
      5: 0, // Excellent
      4: 0, // Very Good
      3: 0, // Good
      2: 0, // Average
      1: 0, // Poor
    };
    
    if (ratings && ratings.length > 0) {
      ratings.forEach(rating => {
        if (counts[rating.rating] !== undefined) {
          counts[rating.rating]++;
        }
      });
    }
    
    return counts;
  };

  const ratingCounts = getRatingCounts();
  const totalRatings = ratings ? ratings.length : 0;

  // Calculate percentage for progress bars
  const calculatePercentage = (count) => {
    if (totalRatings === 0) return 0;
    return (count / totalRatings) * 100;
  };
  
  const handleSubmit = (e) => {
    console.log("jwt", jwt);
    e.preventDefault();
    if(!jwt  ){
      console.log("entered");
      
      navigate("/");
    }
    else{

      const data = { productId };
      dispatch(addItemToCart({ data, jwt }));
      navigate("/cart");
    }
  };

  useEffect(() => {
    window.scrollTo(0, 0);
    const data = { productId: Number(productId), jwt };
    dispatch(findProductById(data));
    dispatch(getAllReviews(productId));
    dispatch(getAllRatings(productId));
    console.log("Loading product:", productId);
  }, [productId, dispatch]);

  // Fetch similar products when the product category is available
  useEffect(() => {
    if (customersProduct.product?.category?.name) {
      // Fetch products with the same category
      dispatch(findProducts({ category: customersProduct.product.category.name.toLowerCase() }));
      console.log("Fetching similar products for category:", customersProduct.product.category.name);
    }
  }, [dispatch, customersProduct.product?.category?.name]);

  return (
    <div className="bg-white lg:px-20">
      <div className="pt-6">
        
        {/* product details section */}
        <section className="grid grid-cols-1 gap-x-8 gap-y-10 lg:grid-cols-2 px-4 pt-10">
          {/* Image gallery */}
          <div className="flex flex-col items-center ">
            <div className=" overflow-hidden rounded-lg max-w-[30rem] max-h-[35rem]">
              <img
                src={customersProduct.product?.imageUrl}
                alt={customersProduct.product?.title || "Product image"}
                className="h-full w-full object-cover object-center"
              />
            </div>
            
          </div>

          {/* Product info */}
          <div className="lg:col-span-1 mx-auto max-w-2xl px-4 pb-16 sm:px-6  lg:max-w-7xl  lg:px-8 lg:pb-24">
            <div className="lg:col-span-2">
              <h1 className="text-lg lg:text-xl font-semibold tracking-tight text-gray-900">
                {customersProduct.product?.brand}
              </h1>
              <h1 className="text-lg lg:text-xl tracking-tight text-gray-900 opacity-60 pt-1">
                {customersProduct.product?.title}
              </h1>
            </div>

            {/* Options */}
            <div className="mt-4 lg:row-span-3 lg:mt-0">
              <h2 className="sr-only">Product information</h2>
              <div className="flex space-x-5 items-center text-lg lg:text-xl tracking-tight text-gray-900 mt-6">
                <p className="font-semibold">
                  ₹{customersProduct.product?.discountedPrice}
                </p>
                <p className="opacity-50 line-through">
                  ₹{customersProduct.product?.price}
                </p>
                <p className="text-green-600 font-semibold">
                  {customersProduct.product?.discountPersent}% Off
                </p> 
                <p className="font-semibold">
                  {customersProduct.product?.sizes}
                </p>
              </div>

              {/* Reviews */}
              <div className="mt-6">
                <h3 className="sr-only">Reviews</h3>

                <div className="flex items-center space-x-3">
                  <Rating
                    name="read-only"
                    value={parseFloat(averageRating) || 0}
                    precision={0.5}
                    readOnly
                  />

                  <p className="opacity-60 text-sm">{totalRatings} Ratings</p>
                  <p className="ml-3 text-sm font-medium text-indigo-600 hover:text-indigo-500">
                    {reviews.length || 0} reviews
                  </p>
                </div>
              </div>

              <form className="mt-10" onSubmit={handleSubmit}>
                <Button
                  variant="contained"
                  type="submit"
                  sx={{ padding: ".8rem 2rem", marginTop: "2rem" }}
                >
                  Add To Cart
                </Button>
              </form>
            </div>

            <div className="py-10 lg:col-span-2 lg:col-start-1 lg:border-r lg:border-gray-200 lg:pb-16 lg:pr-8 lg:pt-6">
              {/* Description and details */}
              <div>
              <h3 className="text-sm font-medium text-gray-900">
                  Description
                </h3>

                <div className="space-y-6 mt-4">
                  <p className="text-base text-gray-900">
                    {customersProduct.product?.description}
                  </p>
                </div>
              </div>

              <div className="mt-10">
                <h3 className="text-sm font-medium text-gray-900">
                  Highlights
                </h3>

                <div className="mt-4">
                  <ul role="list" className="list-disc space-y-2 pl-4 text-sm">
                    {product.highlights.map((highlight) => (
                      <li key={highlight} className="text-gray-400">
                        <span className="text-gray-600">{highlight}</span>
                      </li>
                    ))}
                  </ul>
                </div>
              </div>

              <div className="mt-10">
                <h2 className="text-sm font-medium text-gray-900">Details</h2>

                <div className="mt-4 space-y-6">
                  <p className="text-sm text-gray-600">{product.details}</p>
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* rating and review section - UPDATED */}
        <section className="">
          <h1 className="font-semibold text-lg pb-4">
            Recent Review & Ratings
          </h1>
                    
          <div className="border p-5">
            <Grid container spacing={7}>
              <Grid item xs={7}>
                <div className="space-y-5">
                  {reviews && reviews.length > 0 ? (
                    reviews.map((review, i) => (
                      <ProductReviewCard 
                        key={i} 
                        item={review} 
                        ratings={ratings} 
                      />
                    ))
                  ) : (
                    <p>No reviews yet. Be the first to review this product!</p>
                  )}
                  {/* For debugging - remove in production */}
                  <div style={{ display: 'none' }}>
                    <p>Debug ratings: {JSON.stringify(ratings)}</p>
                    <p>Debug reviews: {JSON.stringify(reviews)}</p>
                  </div>
                </div>
              </Grid>

              <Grid item xs={5}>
                <h1 className="text-xl font-semibold pb-1">Product Ratings</h1>
                <div className="flex items-center space-x-3 pb-10">
                  <Rating
                    name="read-only"
                    value={parseFloat(averageRating) || 0}
                    precision={0.5}
                    readOnly
                  />

                  <p className="opacity-60">{totalRatings} Ratings</p>
                </div>
                <Box>
                  <Grid
                    container
                    justifyContent="center"
                    alignItems="center"
                    gap={2}
                  >
                    <Grid xs={2}>
                      <p className="p-0">Excellent</p>
                    </Grid>
                    <Grid xs={7}>
                      <LinearProgress
                        className=""
                        sx={{ bgcolor: "#d0d0d0", borderRadius: 4, height: 7 }}
                        variant="determinate"
                        value={calculatePercentage(ratingCounts[5])}
                        color="success"
                      />
                    </Grid>
                    <Grid xs={2}>
                      <p className="opacity-50 p-2">{ratingCounts[5]}</p>
                    </Grid>
                  </Grid>
                </Box>
                <Box>
                  <Grid
                    container
                    justifyContent="center"
                    alignItems="center"
                    gap={2}
                  >
                    <Grid xs={2}>
                      <p className="p-0">Very Good</p>
                    </Grid>
                    <Grid xs={7}>
                      <LinearProgress
                        className=""
                        sx={{ bgcolor: "#d0d0d0", borderRadius: 4, height: 7 }}
                        variant="determinate"
                        value={calculatePercentage(ratingCounts[4])}
                        color="success"
                      />
                    </Grid>
                    <Grid xs={2}>
                      <p className="opacity-50 p-2">{ratingCounts[4]}</p>
                    </Grid>
                  </Grid>
                </Box>
                <Box>
                  <Grid
                    container
                    justifyContent="center"
                    alignItems="center"
                    gap={2}
                  >
                    <Grid xs={2}>
                      <p className="p-0">Good</p>
                    </Grid>
                    <Grid xs={7}>
                      <LinearProgress
                        className="bg-[#885c0a]"
                        sx={{ bgcolor: "#d0d0d0", borderRadius: 4, height: 7 }}
                        variant="determinate"
                        value={calculatePercentage(ratingCounts[3])}
                        color="warning"
                      />
                    </Grid>
                    <Grid xs={2}>
                      <p className="opacity-50 p-2">{ratingCounts[3]}</p>
                    </Grid>
                  </Grid>
                </Box>
                <Box>
                  <Grid
                    container
                    justifyContent="center"
                    alignItems="center"
                    gap={2}
                  >
                    <Grid xs={2}>
                      <p className="p-0">Average</p>
                    </Grid>
                    <Grid xs={7}>
                      <LinearProgress
                        className=""
                        sx={{
                          bgcolor: "#d0d0d0",
                          borderRadius: 4,
                          height: 7,
                          "& .MuiLinearProgress-bar": {
                            bgcolor: "#885c0a", // stroke color
                          },
                        }}
                        variant="determinate"
                        value={calculatePercentage(ratingCounts[2])}
                      />
                    </Grid>
                    <Grid xs={2}>
                      <p className="opacity-50 p-2">{ratingCounts[2]}</p>
                    </Grid>
                  </Grid>
                </Box>
                <Box>
                  <Grid
                    container
                    justifyContent="center"
                    alignItems="center"
                    gap={2}
                  >
                    <Grid xs={2}>
                      <p className="p-0">Poor</p>
                    </Grid>
                    <Grid xs={7}>
                      <LinearProgress
                        className=""
                        sx={{ bgcolor: "#d0d0d0", borderRadius: 4, height: 7 }}
                        variant="determinate"
                        value={calculatePercentage(ratingCounts[1])}
                        color="error"
                      />
                    </Grid>
                    <Grid xs={2}>
                      <p className="opacity-50 p-2">{ratingCounts[1]}</p>
                    </Grid>
                  </Grid>
                </Box>
              </Grid>
            </Grid>
          </div>
        </section>

        {/* similar products section */}
        <section className="pt-10">
          <h1 className="py-5 text-xl font-bold">Similar Products</h1>
          
          {loadingSimilarProducts ? (
            <div className="flex justify-center">
              <LinearProgress sx={{ width: '50%' }} />
            </div>
          ) : (
            <div className="flex flex-wrap space-y-5">
              {similarProducts && similarProducts.length > 0 ? (
                similarProducts
                  .filter(item => item.id !== Number(productId)) // Exclude current product
                  .slice(0, 10) // Limit to 10 similar products
                  .map((item) => (
                    <div key={item.id}>
                      <HomeProductCard product={item} />
                    </div>
                  ))
              ) : (
                <p>No similar products found</p>
              )}
            </div>
          )}
        </section>
      </div>
    </div>
  );
}