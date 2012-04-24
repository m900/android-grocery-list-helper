package com.luisromero.listmenu;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
	private Drawable location_store;
	private OverlayItem overlayItem;
	private Bundle bundle;
	private String storeName;
	private String productName;
	private String productQuantity;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_list_item);
        mapView = (MapView) findViewById(R.id.mapview);//id in map_list_item.xml
        mapView.setBuiltInZoomControls(true);

        point = new GeoPoint(37779300, -122419200);// my default location in San Francisco   
        GeoPoint traderjoes = new GeoPoint(37771885,-122420119);
        GeoPoint safeway=new GeoPoint(37778804,-122416945);
        GeoPoint wholefood=new GeoPoint(37780654,-122423703);
        GeoPoint lucky=new GeoPoint(37784893,-122419707);
        GeoPoint start=null;
        GeoPoint dest=null;
        mapOverlays = mapView.getOverlays();
        location_me = this.getResources().getDrawable(R.drawable.my_location); //picture to show for points
        location_store=this.getResources().getDrawable(R.drawable.cart_push);
        mapOverlay = new MapMarkerOverlay(location_me,mapView); // to pass the mapView context.
        overlayItem = new OverlayItem(point, "You are here! ", "current location");
        mapOverlay.addOverlay(overlayItem);
        mapOverlays.add(mapOverlay);
        
        ArrayList<GeoPoint> path=new ArrayList<GeoPoint>();
        //path.add(point);
        bundle=getIntent().getExtras();
        this.storeName=(String)bundle.get("storeLocation");
        this.productName=(String)bundle.get("productName");
        this.productQuantity=(String)bundle.get("productQuantity");
             
        if(this.storeName.equals("SafeWay")){
        	mapOverlay.addOverlay(new OverlayItem(safeway, "Store: SafeWay", "Product: "+this.productName + "\nQuantity: "+this.productQuantity),location_store);
        	dest=safeway;
        }else if(this.storeName.equals("Whole Foods")){
        	mapOverlay.addOverlay(new OverlayItem(wholefood, "Store: Whole Foods", "Product: "+this.productName + "\nQuantity: "+this.productQuantity),location_store);
        	dest=wholefood;      	
        }else if(this.storeName.equals("Luckys")){
        	mapOverlay.addOverlay(new OverlayItem(lucky, "Store: Luckys", "Product: "+this.productName + "\nQuantity: "+this.productQuantity),location_store);
        	dest=lucky;       	
        }else if(this.storeName.equals("Trader Joes")){
        	mapOverlay.addOverlay(new OverlayItem(traderjoes, "Store: Trader Joes", "Product: "+this.productName + "\nQuantity: "+this.productQuantity),location_store);
        	dest=traderjoes;
        }
       
        String pairs[] = getDirectionData(point,dest);
        String[] lngLat = pairs[0].split(",");

	      // STARTING POINT
	    start = new GeoPoint((int) (Double.parseDouble(lngLat[1]) * 1E6), (int) (Double.parseDouble(lngLat[0]) * 1E6));
	    path.add(start);
	      
	      for (int i = 1; i < pairs.length; i++) {
	    	  lngLat = pairs[i].split(",");
	    	  GeoPoint gp2 = new GeoPoint((int) (Double.parseDouble(lngLat[1]) * 1E6),(int) (Double.parseDouble(lngLat[0]) * 1E6));
	    	  path.add(gp2);
	    	  Log.d("xxx", "pair:" + pairs[i]);
	      }
	    path.add(dest);
	    mapOverlays.add(mapOverlay);
        mapOverlays.add(new RoutePathOverlay(path));        
	    mc = mapView.getController();
	    mc.setCenter(point);
	    mc.setZoom(16);
	    //mapView.invalidate();
    }
	
	 private String[] getDirectionData(GeoPoint start, GeoPoint end) {
		 String sourceLat = Double.toString(start.getLatitudeE6()/1E6);
	     String sourceLong = Double.toString(start.getLongitudeE6()/1E6);
	     String destinationLat = Double.toString(end.getLatitudeE6()/1E6);
	     String destinationLong = Double.toString(end.getLongitudeE6()/1E6);
   	 
	     String urlString = "http://maps.google.com/maps?f=d&hl=en&" +"saddr="+sourceLat+","+sourceLong+"&daddr="+destinationLat+","+destinationLong + "&ie=UTF8&0&om=0&output=kml";
	     Log.d("URL", urlString);
	     Document doc = null;
	     HttpURLConnection urlConnection = null;
	     URL url = null;
	     String pathContent = "";
	   	 try {
	
	   		 url = new URL(urlString.toString());
	   		 urlConnection = (HttpURLConnection) url.openConnection();
	   		 urlConnection.setRequestMethod("GET");
	   		 urlConnection.setDoOutput(true);
	   		 urlConnection.setDoInput(true);
	   		 urlConnection.connect();
	   		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	   		 DocumentBuilder db = dbf.newDocumentBuilder();
	   		 doc = db.parse(urlConnection.getInputStream());
	
	   	 } catch (Exception e) {
	   	 }

	   	 NodeList nl = doc.getElementsByTagName("LineString");
	   	 for (int s = 0; s < nl.getLength(); s++) {
	   		 Node rootNode = nl.item(s);
	   		 NodeList configItems = rootNode.getChildNodes();
	   		 for (int x = 0; x < configItems.getLength(); x++) {
	   			 Node lineStringNode = configItems.item(x);
	   			 NodeList path = lineStringNode.getChildNodes();
	   			 pathContent = path.item(0).getNodeValue();
	   		 }
	   	 }
	   	 String[] tempContent = pathContent.split(" ");
	   	 return tempContent;
	}
  
	 public boolean onCreateOptionsMenu(Menu menu) {
			super.onCreateOptionsMenu(menu);

			menu.add(1, 100, 1, "Show directions");
			menu.add(1, 101, 2, "Go back");
			return true;
		}

		public boolean onOptionsItemSelected(MenuItem item) {
			super.onOptionsItemSelected(item);

			switch (item.getItemId()) {
			case 100:
				String currentLatitude="37.779300";
    			String currentLongitude="-122.419200";
    			String fixedLatitude="37.780654";
    			String fixedLongitude="-122.423703";
    			String uri = "http://maps.google.com/maps?saddr=" + currentLatitude+","+currentLongitude+"&daddr="+fixedLatitude+","+fixedLongitude+"&dirflg=w";
    			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
    			intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
    			startActivity(intent);
				break;
			case 101:
				ListItemMapActivity.this.finish();
	            System.exit(0);
				break;
			}
			return true;
		}
	 
	 /*
	@Override //to create option menu from xml file
	    public boolean onCreateOptionsMenu(Menu menu) {
	    	MenuInflater inflater= getMenuInflater();
	    	inflater.inflate(R.menu.directions, menu);
	    	return true;
	} 
	 
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
		 	if(item.getItemId()==R.id.item1){
		 		String currentLatitude="37.779300";
    			String currentLongitude="-122.419200";
    			String fixedLatitude="37.780654";
    			String fixedLongitude="-122.423703";
    			String uri = "http://maps.google.com/maps?saddr=" + currentLatitude+","+currentLongitude+"&daddr="+fixedLatitude+","+fixedLongitude+"'&dirflg=w";
    			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
    			intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
    			startActivity(intent);
		 	}
		 
		 return super.onOptionsItemSelected(item);
	 }
	 */
	 
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
             if (((keyCode == KeyEvent.KEYCODE_BACK) || (keyCode == KeyEvent.KEYCODE_DPAD_CENTER)) && (mapOverlay.hideBalloon())) {
                     return true;
             }
             return super.onKeyDown(keyCode, event);
     }
}
