import {
  Button,
  Grid,
  Rating,
  TextField,
  Typography,
  useMediaQuery,
} from "@mui/material";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { createReview, createRating } from "../../../Redux/Customers/Review/Action";
import { useNavigate, useParams } from "react-router-dom";
import { findProductById } from "../../../Redux/Customers/Product/Action";

const RateProduct = () => {
  const [formData, setFormData] = useState({ review: "" });
  const [rating, setRating] = useState(0);
  const isLargeScreen = useMediaQuery("(min-width:1200px)");
  const dispatch = useDispatch();
  const { customersProduct } = useSelector((store) => store);
  const { productId } = useParams();
  const navigate = useNavigate();

  // This function properly updates the rating state
  const handleRatingChange = (event, newValue) => {
    console.log("rating changed to:", newValue);
    setRating(newValue);
  };

  const handleChange = (e) => {
    const name = e.target.name;
    const value = e.target.value;

    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    
    console.log("Submitting with rating:", rating);
    console.log("Review text:", formData.review);

    // Create and dispatch the review request
    dispatch(createReview({
      review: formData.review,
      productId: Number(productId)
    }));
    
    // Create and dispatch the rating request with the current rating value
    dispatch(createRating({
      rating: rating,
      productId: Number(productId)
    }));
    
    // Reset form and navigate back to product page
    setFormData({ review: "" });
    setRating(0);
    navigate(`/product/${productId}`);
  };

  useEffect(() => {
    dispatch(findProductById({ productId }));
  }, [dispatch, productId]);
  return (
    <div className="px-5 lg:px-20">
      <h1 className="text-xl p-5 shadow-lg mb-8 font-bold">
        Rate & Review Product
      </h1>
      <Grid sx={{ justifyContent: "space-between" }} container>
        <Grid
          className="flex lg:items-center shadow-lg border rounded-md p-5"
          item
          xs={12}
          lg={5.8}
        >
          <div>
            <img
              className="w-[5rem] lg:w-[15rem]"
              src={customersProduct.product?.imageUrl}
              alt=""
            />
          </div>
          <div className="ml-3 lg:ml-5 space-y-2 lg:space-y-4">
            <p className="lg:text-lg">{customersProduct.product?.title}</p>
            <p className="opacity-50 font-semibold">
              {customersProduct.product?.brand}
            </p>
            <p>₹{customersProduct.product?.discountedPrice}</p>
            {customersProduct.product?.color && <p>Color: {customersProduct.product?.color}</p>}
            <div className="flex items-center space-x-3">
              <Rating name="read-only" value={(customersProduct.product?.ratings.reduce((sum, r) => sum + r.rating, 0) / customersProduct.product?.ratings.length).toFixed(1) || 0} precision={0.5} readOnly />
              <p className="opacity-60 text-sm">
                {customersProduct.product?.ratings.length || 0} Ratings
              </p>
              <p className="ml-3 text-sm font-medium text-indigo-600 hover:text-indigo-500">
                {customersProduct.product?.reviews.length || 0} reviews
              </p>
            </div>
          </div>
        </Grid>
        <Grid item xs={12} lg={6}>
          <div className={`${!isLargeScreen ? "py-10" : ""} space-y-5`}>
            <div className="shadow-md border rounded-md p-5">
              <Typography className="font-semibold" component="legend">
                Rate This Product
              </Typography>
              <Rating
                name="product-rating"
                value={rating}
                onChange={handleRatingChange}
                precision={1}
                size="large"
              />
              {rating > 0 && (
                <Typography variant="body2" className="mt-2">
                  Your rating: {rating} {rating === 1 ? 'star' : 'stars'}
                </Typography>
              )}
            </div>
            <form
              onSubmit={handleSubmit}
              className="space-y-5 p-5 shadow-md border rounded-md"
            >
              <TextField
                label="Review"
                variant="outlined"
                fullWidth
                margin="normal"
                multiline
                rows={4}
                value={formData.review}
                onChange={handleChange}
                name="review"
                required
                placeholder="Write your review here..."
              />
              <Button 
                type="submit" 
                variant="contained" 
                color="primary"
                disabled={!rating || !formData.review}
              >
                Submit Review
              </Button>
            </form>
          </div>
        </Grid>
      </Grid>
    </div>
  );
};

export default RateProduct;