package com.luisromero.listmenu;

import com.google.android.maps.GeoPoint;

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
	private GeoPoint coordinates;
	private String address;
	private String arrivalTimeText;
	private Long arrivalTimeValue;
	private String distanceText;
	private Long distanceValue;
	
	public Location(String name, GeoPoint coordinates,String address){
		this.name=name;
		this.coordinates=coordinates;
		this.address=address;
	}
	
	public Location(){
		
	}
	
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
	
	public void setLocationAddress(String address){
		this.address=address;
	}
	
	public String getLocationAddress(){
		return this.address;
	}
	
	
	public GeoPoint getCoordinates(){
		return this.coordinates;
	}
	
	public void setCoordinates(GeoPoint coordinates){
		this.coordinates=coordinates;
	}
}
