package com.luisromero.listmenu;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import android.os.Bundle;


public class MapActivity extends com.google.android.maps.MapActivity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_list_item);
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        GeoPoint point = new GeoPoint(37779300, -122419200);
        /**
         * MapController is needed to set view location and zooming.
         */
        MapController mc = mapView.getController();
        mc.setCenter(point);
        mc.setZoom(14);
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }



}
