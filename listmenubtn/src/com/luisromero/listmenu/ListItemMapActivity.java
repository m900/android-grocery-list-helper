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
	private Drawable drawable;
	private OverlayItem overlayItem;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_list_item);
        mapView = (MapView) findViewById(R.id.mapview);//id in map_list_item.xml
        //mapView.setOnTouchListener((OnTouchListener) this);
        mapView.setBuiltInZoomControls(true);

        point = new GeoPoint(37779300, -122419200);//san francisco   
        GeoPoint point1 = new GeoPoint(37778313,-122419607);
        mc = mapView.getController();
        mc.setCenter(point);
        mc.setZoom(13);
        mapOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.cart_push); //picture to show for points
       
        mapOverlay = new MapMarkerOverlay(drawable,mapView); // to pass the mapView context.
        
        overlayItem = new OverlayItem(point, "Hello there!", "This is San Francisco Civic Center!");
        
        mapOverlay.addOverlay(overlayItem);
        mapOverlays.add(mapOverlay);
        
        mapOverlay.addOverlay(new OverlayItem(point1,"Hello There", "This is the second location test"));
        
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
