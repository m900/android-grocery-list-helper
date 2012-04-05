package com.luisromero.listmenu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;


public class ListItemMapMarker extends Overlay{
	private GeoPoint p;
	private MapView mapView; 
    private MapController mc;
    
    public ListItemMapMarker(GeoPoint p, MapView mapView, MapController mc){
    	this.p=p;
    	this.mapView=mapView;
    	this.mc=mc;
    }
    
    public ListItemMapMarker(){
    	//default empty constructor;
    }
    
    public GeoPoint getGeoPoint(){
    	return this.p;
    }
    
    public void setGeoPoint(GeoPoint p){
    	this.p=p;
    }
    
    public MapView getMapView(){
    	return this.mapView;
    }
    
    public void setMapView(MapView mapView){
    	this.mapView=mapView;
    }
    
    public MapController getMapController(){
    	return this.mc;
    }
    
    public void setMapController(MapController mc){
    	this.mc=mc;
    }
    
	@Override
    public boolean draw(Canvas canvas, MapView mapView, 
    boolean shadow, long when) 
    {
        super.draw(canvas, mapView, shadow);                   

        //---translate the GeoPoint to screen pixels---
        Point screenPts = new Point();
        mapView.getProjection().toPixels(p, screenPts);

        //---add the marker---
        Bitmap bmp = BitmapFactory.decodeResource(mapView.getContext().getResources(), R.drawable.cart_push);            
        canvas.drawBitmap(bmp, screenPts.x, screenPts.y-50, null);         
        return true;
    }

}
