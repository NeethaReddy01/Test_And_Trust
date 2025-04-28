import {
  Button,
  Grid,
  Rating,
  TextField,
  Typography,
  useMediaQuery,
} from "@mui/material";
import React, { useEffect, useState } from "react";
import FiberManualRecordIcon from "@mui/icons-material/FiberManualRecord";
import { useDispatch, useSelector } from "react-redux";
import { createReview } from "../../../Redux/Customers/Review/Action";
import { createRating } from "../../../Redux/Customers/Review/Action";
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
  const jwt = localStorage.getItem("jwt");

  const handleRateProduct = (e, value) => {
    console.log("rating ----- ", value);
    setRating(value);
  };

  const handleChange = (e) => {
    const name = e.target.name;
    const value = e.target.value;

    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    // Get the JWT token from localStorage
    const jwt = localStorage.getItem("jwt");

    // Create and dispatch the review request
    dispatch(createReview({
      review: formData.review,
      productId: Number(productId)
    }));
    
    // Create and dispatch the rating request
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
            <p>Size: {customersProduct.product?.sizes}</p>
            {customersProduct.product?.color && <p>Color: {customersProduct.product?.color}</p>}
            <div className="flex items-center space-x-3">
              <Rating name="read-only" value={4.6} precision={0.5} readOnly />
              <p className="opacity-60 text-sm">42807 Ratings</p>
              <p className="ml-3 text-sm font-medium text-indigo-600 hover:text-indigo-500">
                {3789} reviews
              </p>
            </div>
            {/* <div>
              <p className="space-y-2 font-semibold">
                <FiberManualRecordIcon
                  sx={{ width: "15px", height: "15px" }}
                  className="text-green-600 mr-2"
                />
                <span>Delivered On Mar 03</span>{" "}
              </p>
              <p className="text-xs">Your Item Has Been Delivered</p>
            </div> */}
          </div>
        </Grid>
        <Grid item xs={12} lg={6}>
          <div className={`${!isLargeScreen ? "py-10" : ""} space-y-5`}>
            <div className="shadow-md border rounded-md p-5">
              <Typography className="font-semibold" component="legend">
                Rate This Product
              </Typography>
              <Rating
                name="simple-controlled"
                value={rating}
                onChange={(event, newValue) => {
                  handleRateProduct(event, newValue);
                }}
              />
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