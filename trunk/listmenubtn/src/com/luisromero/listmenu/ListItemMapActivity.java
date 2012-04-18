package com.luisromero.listmenu;


import java.util.ArrayList;
import java.util.List;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;

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
	//private Drawable location_store;
	private OverlayItem overlayItem;
	Bundle bundle;
	String storeName;
	String productName;
	String productQuantity;
	private MapMarkerOverlay bOverlay;
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
        //location_store=this.getResources().getDrawable(R.drawable.cart_push);
        mapOverlay = new MapMarkerOverlay(location_me,mapView); // to pass the mapView context.
        overlayItem = new OverlayItem(point, "You are here! ", "current location");
        mapOverlay.addOverlay(overlayItem);
        mapOverlays.add(mapOverlay);
        
        ArrayList<GeoPoint> path=new ArrayList<GeoPoint>();
        path.add(point);
        bundle=getIntent().getExtras();
        this.storeName=(String)bundle.get("storeLocation");
        this.productName=(String)bundle.get("productName");
        this.productQuantity=(String)bundle.get("productQuantity");
       
       
        //bOverlay=new MapMarkerOverlay(location_store,mapView);
        
        
        if(this.storeName.equals("SafeWay")){
        	//bOverlay.addOverlay(new OverlayItem(safeway, "Store: SafeWay", "Product: "+this.productName + "\nQuantity: "+this.productQuantity));
        	mapOverlay.addOverlay(new OverlayItem(safeway, "Store: SafeWay", "Product: "+this.productName + "\nQuantity: "+this.productQuantity));
        	path.add(safeway);
        	//mapOverlays = mapView.getOverlays();
        }else if(this.storeName.equals("Whole Foods")){
        	//bOverlay.addOverlay(new OverlayItem(wholefood, "Store: Whole Foods", "Product: "+this.productName + "\nQuantity: "+this.productQuantity));
        	mapOverlay.addOverlay(new OverlayItem(wholefood, "Store: Whole Foods", "Product: "+this.productName + "\nQuantity: "+this.productQuantity));
        	path.add(wholefood);
        	//mapOverlays = mapView.getOverlays();
        }else if(this.storeName.equals("Luckys")){
        	//bOverlay.addOverlay(new OverlayItem(lucky, "Store: Luckys", "Product: "+this.productName + "\nQuantity: "+this.productQuantity));
        	mapOverlay.addOverlay(new OverlayItem(lucky, "Store: Luckys", "Product: "+this.productName + "\nQuantity: "+this.productQuantity));
        	path.add(lucky);
        	//mapOverlays = mapView.getOverlays();
        }else if(this.storeName.equals("Trader Joes")){
        	//bOverlay.addOverlay(new OverlayItem(traderjoes, "Store: Trader Joes", "Product: "+this.productName + "\nQuantity: "+this.productQuantity));
        	mapOverlay.addOverlay(new OverlayItem(traderjoes, "Store: Trader Joes", "Product: "+this.productName + "\nQuantity: "+this.productQuantity));
        	path.add(traderjoes);
        	//mapOverlays = mapView.getOverlays();
        }
		
        //mapOverlay.addOverlay(new OverlayItem(wholefood,"Store: ", "Whole Foods"));
        //mapOverlay.addOverlay(new OverlayItem(traderjoes,"Store: ", "Trader Joes"));
        //bOverlay.addOverlay(new OverlayItem(traderjoes, "Store: Trader Joes", "Product: "+this.productName + "\nQuantity: "+this.productQuantity));
        //path.add(traderjoes);
        //mapOverlays.add(bOverlay);
        mapOverlays.add(new RoutePathOverlay(path));
        //mapOverlays=mapView.getOverlays();
        
        
        /*
        //mapOverlays.add(bOverlay);
        if(savedInstanceState==null){
        	final MapController mc1= mapView.getController();
        	mc1.animateTo(traderjoes);
        	mc1.setZoom(16);
        }else{
        	int focused;
        	focused=savedInstanceState.getInt("focused_1",-1);
        	if(focused>=0){
        		mapOverlay.setFocus(mapOverlay.getItem(focused));
        	}
        	focused=savedInstanceState.getInt("focused_2",-1);
        	if(focused>=0){
        		bOverlay.setFocus(bOverlay.getItem(focused));
        	}
        }
        */
        
        /*
         * problem lies in that mapOverlays can only handle one list of one kind of overlays with the icon at a time- }
         * however it can be managed with focus- elements
         * 
         * */
        
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
	
	
	
	 @Override
     public boolean onKeyDown(int keyCode, KeyEvent event) {
             if ((keyCode == KeyEvent.KEYCODE_BACK) && (bOverlay.hideBalloon())) {
                     return true;
             }
             return super.onKeyDown(keyCode, event);
     }
}