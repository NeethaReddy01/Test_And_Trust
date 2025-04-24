import React from "react";
import { Avatar, Rating, Box, Grid } from "@mui/material";

const ProductReviewCard = ({ item, ratings }) => {
  // Format the review date properly
  const formatDate = (dateString) => {
    if (!dateString) return "No date available";
    
    // Parse the date string into a Date object
    const date = new Date(dateString);
    
    // Check if the date is valid
    if (isNaN(date.getTime())) return "Invalid date";
    
    // Format date as "Month Day, Year"
    return date.toLocaleDateString('en-US', {
      month: 'long',
      day: 'numeric',
      year: 'numeric'
    });
  };

  // Improved function to find matching rating
  const findMatchingRating = () => {
    if (!ratings || !Array.isArray(ratings)) return 0;
    
    // First try matching by createdAt timestamps
    const matchingRating = ratings.find(
      rating => rating.createdAt === item.createdAt
    );
    
    // If found, ensure we're properly converting to a number between 0-5
    if (matchingRating) {
      const ratingValue = parseFloat(matchingRating.rating);
      return isNaN(ratingValue) ? 0 : Math.min(Math.max(ratingValue, 0), 5);
    }
    
    // If no exact timestamp match, try matching by user ID if available
    if (item.user?.id) {
      const userRating = ratings.find(
        rating => rating.userId === item.user.id
      );
      if (userRating) {
        const ratingValue = parseFloat(userRating.rating);
        return isNaN(ratingValue) ? 0 : Math.min(Math.max(ratingValue, 0), 5);
      }
    }
    
    return 0;
  };

  return (
    <div className="">
      <Grid container spacing={2} gap={3}>
        <Grid item xs={1}>
          <Box>
            <Avatar
              className="text-white"
              sx={{ width: 56, height: 56, bgcolor: "#9155FD" }}
              alt={item.user?.firstName || "User"}
              src=""
            >
              {item.user?.firstName ? item.user.firstName[0].toUpperCase() : "U"}
            </Avatar>
          </Box>
        </Grid>
        <Grid item xs={9}>
          <div className="space-y-2">
            <div className="">
              <p className="font-semibold text-lg">{item.user?.firstName || "Anonymous"}</p>
              <p className="opacity-70">{formatDate(item.createdAt)}</p>
            </div>
            <div>
              <Rating
                value={findMatchingRating()}
                readOnly
                name="read-only"
                precision={0.5}
              />
            </div>
            <p>{item.review || "No review text provided."}</p>
          </div>
        </Grid>
      </Grid>
    </div>
  );
};

export default ProductReviewCard;