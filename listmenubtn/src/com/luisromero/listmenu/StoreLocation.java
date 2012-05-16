package com.luisromero.listmenu;

import com.google.android.maps.GeoPoint;

/*
 *  Location class: contains the data referencing where the ListItem is located 
 *  with location store name and coordinates to serve the Google map service.
 * 
 * */

public class StoreLocation {
	/*
	 * The following variables immitate the SQLite db' (location) table.
	 * 
	 * */
	private long id; //Key and auto-increment in DB
	private String name;
	private GeoPoint coordinates;
	private String address;
	private String arrivalTimeText;
	private long arrivalTimeValue;
	private String distanceText;
	private long distanceValue;
	
	public StoreLocation(String name, GeoPoint coordinates,String address){
		this.name=name;
		this.coordinates=coordinates;
		this.address=address;
	}
	
	public StoreLocation(){
		//base constructor
	}
	
	public void setArrivalTimeValues(String timeText,Long timeValue){
		this.arrivalTimeText=timeText;
		this.arrivalTimeValue=timeValue;
	}
	
	public void setDistanceValues(String distanceText,Long distanceValue){
		this.distanceText=distanceText;
		this.distanceValue=distanceValue;
	}
	
	public String getArrivalTimeText(){
		return this.arrivalTimeText;
	}
	
	public Long getArrivalTimeValue(){
		return this.arrivalTimeValue;
	}
	
	public String getDistanceText(){
		return this.distanceText;
	}
	
	public Long getDistanceValue(){
		return this.distanceValue;
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
