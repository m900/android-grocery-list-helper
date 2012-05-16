package com.luisromero.listmenu;

/*
 *  ListItem class: contains the data that will be stored in the Android device
 *  database and showed in the ListView - using the MainActivity.java
 * 
 * */

public class Item {
	/*
	 * The following variables imitate the SQLite db' (item) table.
	 * 
	 * */
	private int id; //Key and auto-increment in DB
	private String product;
	private String store;
	private int quantity;
	
	
	/*
	 * Default Empty Constructor. 
	 * 
	*/
	public Item(){
		
	}
	
	/*
	 * Default Constructor. 
	 * @param int id, String product, int quantity
	*/
	public Item(int id, String product,int quantity){
		this.id=id;
		this.product=product;
		this.quantity=quantity;
	}
	
	/*
	 * Default Constructor. 
	 * @param String product, int quantity
	*/
	public Item(String product,int quantity){
		this.product=product;
		this.quantity=quantity;
	}
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int id){
		this.id=id;
	}
	
	public String getProduct(){
		return this.product;
	}
	
	public void setProduct(String product){
		this.product=product;
	}
	
	public int getQuantity(){
		return this.quantity;
	}
	
	public void setQuantity(int quantity){
		this.quantity=quantity;
	}
	
	public String getLocation(){
		return this.store;
	}
	
	public void setLocation(String storeName){
		this.store=storeName;
	}
	
	/* Will be used by the ArrayAdapter to 
	*  populate the ListView with DB stored products.
	*/
	@Override
	public String toString(){
		return this.product;
	}
}
