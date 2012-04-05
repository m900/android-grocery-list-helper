package com.luisromero.listmenu;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class ListItemMapActivity extends MapActivity{

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_list_item);
        MapView mapView = (MapView) findViewById(R.id.mapview);//id in map_list_item.xml
        mapView.setBuiltInZoomControls(true);
        //mapView.setStreetView(true);
        GeoPoint point = new GeoPoint(37779300, -122419200);
        MapController mc = mapView.getController();
        mc.setCenter(point);
        //mc.animateTo(point);
        mc.setZoom(14);
        
       // ListItemMapMarker mapOverlay = new ListItemMapMarker(point,mapView,mc);
        //List<Overlay> listOfOverlays = mapView.getOverlays();
        //listOfOverlays.clear();
        //listOfOverlays.add(mapOverlay);        
 
        //mapView.invalidate();
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.cart_push);
        MapMarkerOverlay itemizedoverlay = new MapMarkerOverlay(drawable);
        
        OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}
