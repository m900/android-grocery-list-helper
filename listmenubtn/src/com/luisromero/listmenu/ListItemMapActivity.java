package com.luisromero.listmenu;


import java.util.ArrayList;
import java.util.List;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
//import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class ListItemMapActivity extends MapActivity{
	private MapController mc;
	private MapView mapView;
	private GeoPoint point;
	//private LocationManager locationManager;
	private MapMarkerOverlay mapOverlay;
	//private MyLocationOverlay me=null; //google own library class.
	private List<Overlay> mapOverlays;
	private Drawable location_me;
	private OverlayItem overlayItem;
	Bundle bundle;
	String storeName;
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
        
        
        mapOverlays = mapView.getOverlays();
        location_me = this.getResources().getDrawable(R.drawable.my_location); //picture to show for points
        mapOverlay = new MapMarkerOverlay(location_me,mapView); // to pass the mapView context.
        overlayItem = new OverlayItem(point, "You are here! ", "-current location");
        mapOverlay.addOverlay(overlayItem);
        mapOverlays.add(mapOverlay);
        
        ArrayList<GeoPoint> path=new ArrayList<GeoPoint>();
        path.add(point);
        bundle=getIntent().getExtras();
        //this.location_product=(String)bundle.get("productName");
        this.storeName=(String)bundle.get("storeLocation");
        
        if(this.storeName.equals("SafeWay")){
        	mapOverlay.addOverlay(new OverlayItem(safeway,"Store: ", "SafeWay" ));
        	path.add(safeway);
        }else if(this.storeName.equals("Whole Foods")){
        	mapOverlay.addOverlay(new OverlayItem(wholefood,"Store: ", "Whole Foods"));
        	path.add(wholefood);
        }else if(this.storeName.equals("Luckys")){
        	mapOverlay.addOverlay(new OverlayItem(lucky,"Store: ", "Luckys"));
        	path.add(lucky);
        }else if(this.storeName.equals("Trader Joes")){
        	mapOverlay.addOverlay(new OverlayItem(traderjoes,"Store: ", "Trader Joes"));
        	path.add(traderjoes);
        }
        
        //mapOverlay.addOverlay(new OverlayItem(safeway,"Store", "SafeWay" ));
        //mapOverlay.addOverlay(new MapDirectionPathOverlay(point,lucky));
        
       // me=new MyLocationOverlay(this, mapView);
       // mapOverlays.add(me);
        
        mapOverlays.add(new RoutePathOverlay(path));
        mc = mapView.getController();
        mc.animateTo(point);
        mc.setCenter(point);
        mc.setZoom(16);
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    @Override
	protected void onResume() {
		super.onResume();
		//me.enableMyLocation();
		//me.enableCompass();
	}

	@Override
	protected void onPause() {
		super.onResume();
		//me.disableMyLocation();
		//me.disableCompass();
	}
}
