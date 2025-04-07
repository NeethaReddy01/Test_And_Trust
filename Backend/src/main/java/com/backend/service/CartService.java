package com.backend.service;

import com.backend.exception.ProductException;
import com.backend.modal.Cart;
import com.backend.modal.User;

public interface CartService {
	
	public Cart createCart(User user);
	
	public CartItem addCartItem(Long userId,AddItemRequest req) throws ProductException;
	
	public Cart findUserCart(Long userId);

}
