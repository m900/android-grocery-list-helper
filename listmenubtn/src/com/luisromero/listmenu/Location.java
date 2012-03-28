package com.luisromero.listmenu;

/*
 *  Location class: contains the data referencing where the ListItem is located 
 *  with location store name and coordinates to serve the Google map service.
 * 
 * */

public class Location {
	/*
	 * The following variables immitate the SQLite db' (location) table.
	 * 
	 * */
	private long id; //Key and auto-increment in DB
	private String name;
	private String coordinates;
	
	public long getId(){
		return this.id;
	}
	
	public void setId(long id){
		this.id=id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getCoordinates(){
		return this.coordinates;
	}
	
	public void setCoordinates(String coordinates){
		this.coordinates=coordinates;
	}
}
