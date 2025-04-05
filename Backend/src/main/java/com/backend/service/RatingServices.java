package com.backend.service;

import java.util.List;

import com.backend.exception.ProductException;
import com.backend.modal.Rating;
import com.backend.modal.User;
import com.backend.request.RatingRequest;

public interface RatingServices {
	
	public Rating createRating(RatingRequest req,User user) throws ProductException;
	
	public List<Rating> getProductsRating(Long productId);

}

