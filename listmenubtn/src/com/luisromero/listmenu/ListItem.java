package com.luisromero.listmenu;

/*
 *  ListItem class: contains the data that will be stored in the Android device
 *  database and showed in the ListView - using the MainActivity.java
 * 
 * */

public class ListItem {
	/*
	 * The following variables immitate the SQLite db' (item) table.
	 * 
	 * */
	private long id; //Key and auto-increment in DB
	private String product;
	private long quantity;
	private String location;
	
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
	
	public String getLocation(){
		return this.location;
	}
	
	public void setLocation(String location){
		this.location=location;
	}
	
	/* Will be used by the ArrayAdapter to 
	*  populate the ListView with DB stored products.
	*/
	@Override
	public String toString(){
		return this.product;
	}
}
