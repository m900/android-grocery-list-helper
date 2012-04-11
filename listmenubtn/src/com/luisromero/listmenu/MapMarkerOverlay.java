package com.luisromero.listmenu;



import java.util.ArrayList;
import java.util.List;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MapMarkerOverlay extends ItemizedOverlay<OverlayItem> {
	private List<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private MapView mapView;
	
	public MapMarkerOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenterBottom(defaultMarker));
		this.mapView=mapView;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);
	  //AlertDialog.Builder dialog = new AlertDialog.Builder(this.mapView.getContext()); //needs a context - getContext();
	  //dialog.setTitle(item.getTitle());
	  //dialog.setMessage(item.getSnippet());
	  //dialog.show();
	  String message="";
	  message+=item.getTitle()+item.getSnippet();
	  Toast.makeText(mapView.getContext(),message , Toast.LENGTH_SHORT).show();
	  return true;
	}
	
	
	/*
	 * This onTouchEvent:
	 * Returns on a Toast message the latitude and longitude of the location touched in Android device screen.
	 * This location can be -Reverse- geocoded to tell the name of the place- 
	 * 
	 * */
	 @Override
     public boolean onTouchEvent(MotionEvent event, MapView mapView) 
     {
		return false;   // To show the latitude and longitude of Touched location on map.
         //---when user lifts his finger---
         /*
		 if (event.getAction() == 1) {                
             GeoPoint p = mapView.getProjection().fromPixels(
                 (int) event.getX(),
                 (int) event.getY());
             String place ="";
             place+=p.getLatitudeE6()/1E6 + "," + p.getLongitudeE6()/1E6;
             
                 Toast.makeText(mapView.getContext(), 
                     p.getLatitudeE6() / 1E6 + "," + 
                     p.getLongitudeE6() /1E6 , 
                     Toast.LENGTH_SHORT).show();
                 
                 //this.addOverlay(new OverlayItem(p,"Hello There", place));
                 
         }
		return false;                            
         */
		 //---when user lifts his finger---
		 
		 /*
         if (event.getAction() == 1) {                
             GeoPoint p = mapView.getProjection().fromPixels(
                 (int) event.getX(),
                 (int) event.getY());

             Geocoder geoCoder = new Geocoder(
                 mapView.getContext(), Locale.getDefault());
             try {
                 List<Address> addresses = geoCoder.getFromLocation(
                     p.getLatitudeE6()  / 1E6, 
                     p.getLongitudeE6() / 1E6,1);

                 String add = "";
                 if (addresses.size() > 0) 
                 {
                     for (int i=0; i<addresses.get(0).getMaxAddressLineIndex(); 
                          i++)
                        add += addresses.get(0).getAddressLine(i) + "\n";
                 }

                 Toast.makeText(mapView.getContext(), add, Toast.LENGTH_SHORT).show();
             }
             catch (IOException e) {                
                 e.printStackTrace();
             }   
             return true;
         }
         else{                
             return false;
         }
         */
     }        
           
}
