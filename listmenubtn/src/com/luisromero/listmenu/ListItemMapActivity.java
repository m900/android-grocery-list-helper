package com.luisromero.listmenu;


import java.util.List;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class ListItemMapActivity extends MapActivity{
	private MapController mc;
	private MapView mapView;
	private GeoPoint point;
	//private LocationManager locationManager;
	private MapMarkerOverlay mapOverlay;
	private MyLocationOverlay me=null; //google own library class.
	private List<Overlay> mapOverlays;
	private Drawable location_me;
	private OverlayItem overlayItem;
	Bundle bundle;
	String location_name;
	String location_product;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_list_item);
        mapView = (MapView) findViewById(R.id.mapview);//id in map_list_item.xml
        //mapView.setOnTouchListener((OnTouchListener) this);
        mapView.setBuiltInZoomControls(true);

        point = new GeoPoint(37779300, -122419200);// my default location in San Francisco   
        GeoPoint traderjoes = new GeoPoint(37771885,-122420119);
        GeoPoint safeway=new GeoPoint(37778804,-122416945);
        GeoPoint wholefood=new GeoPoint(37780654,-122423703);
        GeoPoint lucky=new GeoPoint(37784893,-122419707);
        
        mc = mapView.getController();
        mc.setCenter(point);
        mc.setZoom(13);
        mapOverlays = mapView.getOverlays();
        location_me = this.getResources().getDrawable(R.drawable.my_location); //picture to show for points
        mapOverlay = new MapMarkerOverlay(location_me,mapView); // to pass the mapView context.
        overlayItem = new OverlayItem(point, "You are here! ", "-current location");
        mapOverlay.addOverlay(overlayItem);
        mapOverlays.add(mapOverlay);
        
        
        bundle=getIntent().getExtras();
        //this.location_product=(String)bundle.get("productName");
        this.location_name=(String)bundle.get("storeLocation");
        
        if(this.location_name.equals("SafeWay")){
        	mapOverlay.addOverlay(new OverlayItem(safeway,"Store: ", "SafeWay" ));
        }else if(this.location_name.equals("Whole Foods")){
        	mapOverlay.addOverlay(new OverlayItem(wholefood,"Store: ", "Whole Foods"));
        	
        }else if(this.location_name.equals("Luckys")){
        	mapOverlay.addOverlay(new OverlayItem(lucky,"Store: ", "Luckys"));
        	
        }else if(this.location_name.equals("Trader Joes")){
        	mapOverlay.addOverlay(new OverlayItem(traderjoes,"Store: ", "Trader Joes"));
        }
        
        //mapOverlay.addOverlay(new OverlayItem(safeway,"Store", "SafeWay" ));
        //mapOverlay.addOverlay(new MapDirectionPathOverlay(point,lucky));
        
        me=new MyLocationOverlay(this, mapView);
        mapOverlays.add(me);
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    @Override
	protected void onResume() {
		super.onResume();
		me.enableMyLocation();
		me.enableCompass();
	}

	@Override
	protected void onPause() {
		super.onResume();
		me.disableMyLocation();
		me.disableCompass();
	}
}
