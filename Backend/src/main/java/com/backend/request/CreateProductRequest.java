package com.backend.request;

import java.util.HashSet;
import java.util.Set;
import com.backend.modal.Category;
//import com.backend.modal.Size;
//import com.backend.user.domain.ProductSize;


public class CreateProductRequest {
	
    private String title;

    private String description;

    private int price;

    private int discountedPrice;
   
    private int discountPersent;

    private int quantity;

    private String brand;

    private String color;

    private String size;

    private String imageUrl;

        // Your existing fields
        private String level1Category; // e.g. "skincare"
        private String level2Category; // e.g. "cleanser", "serum"
        
        // Getters and setters
    

	public String getSizes() {
		return size;
	}
	public String getLevel1Category() {
		return level1Category;
	}
	public void setLevel1Category(String level1Category) {
		this.level1Category = level1Category;
	}
	public String getLevel2Category() {
		return level2Category;
	}
	public void setLevel2Category(String level2Category) {
		this.level2Category = level2Category;
	}
	public void setSizes(String size) {
		this.size = size;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(int discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	public int getDiscountPersent() {
		return discountPersent;
	}

	public void setDiscountPersent(int discountPersent) {
		this.discountPersent = discountPersent;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

//	public String getTopLavelCategory() {
//		return topLavelCategory;
//	}
//
//	public void setTopLavelCategory(String topLavelCategory) {
//		this.topLavelCategory = topLavelCategory;
//	}
//
//	public String getSecondLavelCategory() {
//		return secondLavelCategory;
//	}
//
//	public void setSecondLavelCategory(String secondLavelCategory) {
//		this.secondLavelCategory = secondLavelCategory;
//	}
//
//	public String getThirdLavelCategory() {
//		return thirdLavelCategory;
//	}
//
//	public void setThirdLavelCategory(String thirdLavelCategory) {
//		this.thirdLavelCategory = thirdLavelCategory;
//	}

	
    
    
}

