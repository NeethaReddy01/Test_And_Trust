package com.backend.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.backend.exception.CartItemException;
import com.backend.exception.ProductException;
import com.backend.exception.UserException;
import com.backend.modal.Cart;
import com.backend.modal.CartItem;
import com.backend.modal.Product;
import com.backend.modal.User;
import com.backend.repository.CartRepository;
import com.backend.repository.UserRepository;
import com.backend.repository.CartItemRepository;
import com.backend.request.AddItemRequest;

@Service
public class CartServiceImplementation implements CartService {
	
	private CartRepository cartRepository;
	private CartItemService cartItemService;
	private ProductService productService;
	private UserRepository userRepository;
	private CartItemRepository cartItemRepository;
	
	public CartServiceImplementation(CartRepository cartRepository, CartItemService cartItemService,
			ProductService productService, UserRepository userRepository, CartItemRepository cartItemRepository) {
		this.cartRepository = cartRepository;
		this.productService = productService;
		this.cartItemService = cartItemService;
		this.userRepository = userRepository;
		this.cartItemRepository = cartItemRepository;
	}
	
	@Override
	public Cart createCart(User user) {
		Cart cart = new Cart();
		cart.setUser(user);
		Cart createdCart = cartRepository.save(cart);
		return createdCart;
	}
	
	@Override
	public Cart findUserCart(Long userId) {
		Cart cart = cartRepository.findByUserId(userId);
		int totalPrice = 0;
		int totalDiscountedPrice = 0;
		int totalItem = 0;
		for(CartItem cartsItem : cart.getCartItems()) {
			totalPrice += cartsItem.getPrice();
			totalDiscountedPrice += cartsItem.getDiscountedPrice();
			totalItem += cartsItem.getQuantity();
		}
		
		cart.setTotalPrice(totalPrice);
		cart.setTotalItem(cart.getCartItems().size());
		cart.setTotalDiscountedPrice(totalDiscountedPrice);
		cart.setDiscounte(totalPrice - totalDiscountedPrice);
		cart.setTotalItem(totalItem);
		
		return cartRepository.save(cart);
	}
	
	@Override
	public CartItem addCartItem(Long userId, AddItemRequest req) throws ProductException {
		Cart cart = cartRepository.findByUserId(userId);
		Product product = productService.findProductById(req.getProductId());
		
		CartItem isPresent = cartItemService.isCartItemExist(cart, product, req.getSize(), userId);
		
		if(isPresent == null) {
			CartItem cartItem = new CartItem();
			cartItem.setProduct(product);
			cartItem.setCart(cart);
			cartItem.setQuantity(req.getQuantity());
			cartItem.setUserId(userId);
			
			int price = req.getQuantity() * product.getDiscountedPrice();
			cartItem.setPrice(price);
			cartItem.setSize(req.getSize());
			
			CartItem createdCartItem = cartItemService.createCartItem(cartItem);
			cart.getCartItems().add(createdCartItem);
			return createdCartItem;
		}
		
		return isPresent;
	}
	
	@Override
	public void clearCart(Long userId) throws UserException , CartItemException{
		Cart cart = cartRepository.findByUserId(userId);
		
		if (cart != null) {
			// Remove all cart items using CartItemRepository
			for (CartItem item : new ArrayList<>(cart.getCartItems())) {
				cartItemService.removeCartItem(userId, item.getId());
			}
			
			// Clear the cart items collection
			cart.getCartItems().clear();
			
			// Reset cart totals
			cart.setTotalPrice(0);
			cart.setTotalItem(0);
			cart.setTotalDiscountedPrice(0);
			cart.setDiscounte(0);
			
			// Save the empty cart
			cartRepository.save(cart);
		} else {
			throw new UserException("Cart not found for user id: " + userId);
		}
	}
}