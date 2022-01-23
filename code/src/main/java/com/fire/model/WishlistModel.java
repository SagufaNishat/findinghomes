package com.fire.model;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.firestore.DocumentSnapshot;

public class WishlistModel {

	public List<String> wishlist;

	public WishlistModel(List<String> wishlist) {
		super();
		this.wishlist = wishlist;
	}

	public WishlistModel() {
		super();
	}

	public List<String> getWishlist() {
		return wishlist;
	}

	public void setWishlist(List<String> wishlist) {
		this.wishlist = wishlist;
	}
	
	public static WishlistModel createWishlistAdd(String leaseId) {
		List<String> wishlist = new ArrayList<>();
		wishlist.add(leaseId);
		
		return new WishlistModel(wishlist);
	}
	
	public static WishlistModel createWishlistAdd(DocumentSnapshot document, String leaseId) {
		List<String> wishlist = (List<String>) document.get("wishlist");
		if(!wishlist.contains(leaseId)) {
			wishlist.add(leaseId);		
		}
		
		return new WishlistModel(wishlist);
	}

	@Override
	public String toString() {
		return "WishlistModel [wishlist=" + wishlist + "]";
	}

	public static WishlistModel createWishlistRemove(DocumentSnapshot document, String leaseId) {
		List<String> wishlist = (List<String>) document.get("wishlist");
		if(wishlist.contains(leaseId)) {
			wishlist.remove(leaseId);
		}
		
		return new WishlistModel(wishlist);
	}

}
