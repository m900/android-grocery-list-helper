package com.luisromero.listmenu;

/*
 *  LocationStock class: contains data about the products 
 *  in Stock based on location;
 * */

public class LocationStock {
	/*
	 * The following variables imitate the SQLite DB' (LocationStock) table.
	 * 
	 * */
	private long id; //Key and auto-increment in DB
	private String product;
	private long quantity;
	private boolean inStock;
	
	public long getId(){
		return this.id;
	}
	
	public void setId(long id){
		this.id=id;
	}
	
	public String getProduct(){
		return this.product;
	}
	
	public void setProduct(String product){
		this.product=product;
	}
	
	public long getQuantity(){
		return this.quantity;
	}
	
	public void setQuantity(long quantity){
		this.quantity=quantity;
	}
	
	public boolean isInStock(){
		return this.inStock;
	}
	
	public void getIsInStock(boolean inStock){
		this.inStock=inStock;
	}
	
}
