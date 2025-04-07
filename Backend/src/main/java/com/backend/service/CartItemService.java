package com.backend.service;

import com.backend.exception.CartItemException;
import com.backend.exception.UserException;
import com.backend.modal.Cart;
import com.backend.modal.CartItem;
import com.backend.modal.Product;

public interface CartItemService {
	
	
	public CartItem createCartItem(CartItem cartItem);
	
	public CartItem updateCartItem(Long userId, Long id,CartItem cartItem) throws CartItemException, UserException;
	
	public CartItem isCartItemExist(Cart cart,Product product,String size, Long userId);
	
	public void removeCartItem(Long userId,Long cartItemId) throws CartItemException, UserException;
	
	public CartItem findCartItemById(Long cartItemId) throws CartItemException;
	
}
