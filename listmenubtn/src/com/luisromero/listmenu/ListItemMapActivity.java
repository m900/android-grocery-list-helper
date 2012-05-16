package com.luisromero.listmenu;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/*	@author: Luis G Romero
 *  @param : ListItemMapActivity 
 *  Purpose: Displays a Google Map Activity on Android phone.
 *  		 Also, shows the current location and stores nearby with a
 *  		 route to them.
 */
public class ListItemMapActivity extends MapActivity implements LocationListener{
	private MapController mc;
	private MapView mapView;
	private GeoPoint point;
	private ArrayList<String> toDoList;
	private ArrayList<Item> items;
	private ArrayList<String> mapItems;
	private ArrayList<Item> itemsForMap;
	private LocationManager lcManager;
	private DbHelper dbHelper;
	private MapMarkerOverlay mapOverlay;
	private List<Overlay> mapOverlays;
	private Drawable location_me;
	private Drawable location_store;
	private Drawable new_location_store;
	private Drawable suggested_store;
	private OverlayItem overlayItem;
	private Bundle bundle;
	private String storeName;
	private String productName;
	private String productQuantity;
	private List<StoreLocation> sameNamePlaces;
	private List<StoreLocation> nearPlaces;
	private String towers;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_list_item);
        mapView = (MapView) findViewById(R.id.mapview);//id in map_list_item.xml
        mapView.setBuiltInZoomControls(true);
        dbHelper=new DbHelper(this);
        point = new GeoPoint(37779300, -122419200);// my default location in San Francisco   
        
        GeoPoint start=null;
        GeoPoint dest=null;
        mapOverlays = mapView.getOverlays();
        location_me = this.getResources().getDrawable(R.drawable.my_location); //picture to show for points
        location_store=this.getResources().getDrawable(R.drawable.cart_push);
        new_location_store=this.getResources().getDrawable(R.drawable.cart_push_red);
        suggested_store=this.getResources().getDrawable(R.drawable.cart_push_blue);
        mapOverlay = new MapMarkerOverlay(location_me,mapView); // to pass the mapView context.
        
        overlayItem = new OverlayItem(point, "You are here! ", "current location");
        mapOverlay.addOverlay(overlayItem);
        mapOverlays.add(mapOverlay);
        
        
        /*
         * 
         *  To show current location from a mobile phone
         * 	LocationManager and LocationListener implemented methods update the current location
         * 
         *  For emulator testing purposes - GeoPoint point is set with a mock location.
         * 
        lcManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria=new Criteria();
        towers=lcManager.getBestProvider(criteria, false);
        lcManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0, 0, this);
        Location location = lcManager.getLastKnownLocation(towers);
        if(location!=null){
        	
        	point=new GeoPoint(((int)(location.getLatitude()*1E6)),((int)(location.getLongitude()*1E6)));
        	overlayItem = new OverlayItem(point, "You are here! ", "current location");
            mapOverlay.addOverlay(overlayItem);
            mapOverlays.add(mapOverlay);
        }
        */

        ArrayList<GeoPoint> path=new ArrayList<GeoPoint>();
        bundle=getIntent().getExtras();
        mapItems=bundle.getStringArrayList("mapItems");
        toDoList=dbHelper.getAllProducts();
        items=dbHelper.getAllItems();
        itemsForMap=dbHelper.getSelectedItems(mapItems);
  
        GeoPoint temp=null;
        temp=point;
        for(int i=0;i<itemsForMap.size();i++){
        	this.storeName=itemsForMap.get(i).getLocation();
        	this.productName=itemsForMap.get(i).getProduct();
        	this.productQuantity=Integer.toString(itemsForMap.get(i).getQuantity());
        	
        	 if(this.storeName.equals("Safeway")){
        		 StoreLocation store=null;
        		 GeoPoint sameStore=null;
        		 try {
					this.sameNamePlaces=getPlacesData(point,"5000",this.storeName);
					store=this.sameNamePlaces.get(0);
					sameStore=store.getCoordinates();
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
             	mapOverlay.addOverlay(new OverlayItem(sameStore, "Store: Safeway", "Product: "+this.productName + "\nQuantity: "+this.productQuantity+"\nAddress: "+store.getLocationAddress()),location_store);
             	dest=sameStore;
             }else if(this.storeName.equals("Whole Foods Market")){
            	 StoreLocation store=null;
        		 GeoPoint sameStore=null;
        		 try {
					this.sameNamePlaces=getPlacesData(point,"5000",this.storeName);
					store=this.sameNamePlaces.get(0);
					sameStore=store.getCoordinates();					
				} catch (JSONException e) {
					e.printStackTrace();
				}
            	  
            	mapOverlay.addOverlay(new OverlayItem(sameStore, "Store: Whole Foods Market", "Product: "+this.productName + "\nQuantity: "+this.productQuantity+"\nAddress: "+store.getLocationAddress()),location_store);
             	dest=sameStore;      	
             }else if(this.storeName.equals("Luckys")){
            	 StoreLocation store=null;
        		 GeoPoint sameStore=null;
        		 try {
					this.sameNamePlaces=getPlacesData(point,"5000",this.storeName);
					store=this.sameNamePlaces.get(0);
					sameStore=store.getCoordinates();
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
            	 mapOverlay.addOverlay(new OverlayItem(sameStore, "Store: Luckys", "Product: "+this.productName + "\nQuantity: "+this.productQuantity+"\nAddress: "+store.getLocationAddress()),location_store);
            	 dest=sameStore;       	
             }else if(this.storeName.equals("Trader Joes")){
            	 StoreLocation store=null;
        		 GeoPoint sameStore=null;
        		 
        		 try {
					this.sameNamePlaces=getPlacesData(point,"5000",this.storeName);
					store=this.sameNamePlaces.get(0);
					sameStore=store.getCoordinates();
				} catch (JSONException e) {
					e.printStackTrace();
				}
            	 
            	mapOverlay.addOverlay(new OverlayItem(sameStore, "Store: Trader Joe's", "Product: "+this.productName + "\nQuantity: "+this.productQuantity+"\nAddress: "+store.getLocationAddress()),location_store);
             	dest=sameStore;
             }
             
             
             String pairs[] = getDirectionData(point,dest);        
             point=dest;
             String[] lngLat = pairs[0].split(",");

     	      // STARTING POINT for ROUTE or PATH overlay on emulator
     	     start = new GeoPoint((int) (Double.parseDouble(lngLat[1]) * 1E6), (int) (Double.parseDouble(lngLat[0]) * 1E6));
     	     path.add(start);
     	      
     	      for (int j = 1; j < pairs.length; j++) {
     	    	  lngLat = pairs[j].split(",");
     	    	  GeoPoint gp2 = new GeoPoint((int) (Double.parseDouble(lngLat[1]) * 1E6),(int) (Double.parseDouble(lngLat[0]) * 1E6));
     	    	  path.add(gp2);
     	    	  Log.d("xxx", "pair:" + pairs[j]);
     	      }
     	    // ENDING POINT for ROUTE or PATH overlay on emulator
     	    path.add(dest);
        	
        }
        point=temp;
      
	    mapOverlays.add(mapOverlay);
        mapOverlays.add(new RoutePathOverlay(path));       

	    mc = mapView.getController();
	    mc.setCenter(point);
	    mc.setZoom(16);
	    mapView.invalidate();
    }
	
	/*	
	 *  @param: GeoPoint start, String radius, String storeNames
	 *  
	 *  getPlacesData()
	 *  Return: List<Location> 
	 *  
	 *  Purpose: Get places data from Google Places API with a origin
	 *  		 GeoPoint.
	 *  
	 * */
	private List<StoreLocation> getPlacesData(GeoPoint start,String radius, String storeNames) throws JSONException{
		 String sourceLat = Double.toString(start.getLatitudeE6()/1E6);
	     String sourceLong = Double.toString(start.getLongitudeE6()/1E6);
	     String name=storeNames;
	     String googleAPIKey="AIzaSyAzKZWP0TWKRxqKC0ybVOp295EGl2Vb3W0";
	     String urlString = "https://maps.googleapis.com/maps/api/place/search/json?location="+sourceLat+","+sourceLong+"&rankby=distance"+"&name="+name.replace(" ", "%20")+"&types=grocery_or_supermarket&sensor=false"+"&"+"key="+googleAPIKey;
	     String result="";
	     List<StoreLocation> newStores=new ArrayList<StoreLocation>();
	     GeoPoint store;
	     String storeName="";
	     String storeAddress="";
	     HttpClient httpclient = new DefaultHttpClient();  
         HttpGet request = new HttpGet(urlString);  
         ResponseHandler<String> handler = new BasicResponseHandler();  
         try {  
             result = httpclient.execute(request, handler);  
         } catch (ClientProtocolException e) {  
             e.printStackTrace();  
         } catch (IOException e) {  
             e.printStackTrace();  
         }  
         httpclient.getConnectionManager().shutdown();  
	     
         JSONObject obj = new JSONObject(result);
     	if((obj.getString("status")).equals("OK")){
     		JSONArray array = obj.getJSONArray("results");
     		int count = array.length();
     		for(int i = 0; i < count; i++){
     			JSONObject item = array.getJSONObject(i);
     			JSONObject geometry = item.getJSONObject("geometry");
     			JSONObject geo_Location = geometry.getJSONObject("location");
     			Double lat = geo_Location.getDouble("lat");
     			Double lng = geo_Location.getDouble("lng");
     			storeAddress=item.getString("vicinity");
     			store=new GeoPoint((int)(lat*1E6),(int)(lng*1E6));
     			storeName = item.getString("name");
     			StoreLocation newLocation=new StoreLocation();
     			newLocation.setName(storeName);
     			newLocation.setCoordinates(store);
     			newLocation.setLocationAddress(storeAddress);
     			Log.d("StoreName"+": "+storeName, storeAddress);
     			newStores.add(newLocation);
     		}
     	}
	     return newStores;
	}
	
	/*	
	 *  @param: GeoPoint destination
	 *  
	 *  getPlacesDistance()
	 *  Return: Location
	 *  
	 *  Purpose: Get distances from Google Distance Matrix API with a origin and destination
	 *  		 GeoPoints.
	 *  
	 * */
	 private StoreLocation getPlacesDistance(GeoPoint destination) throws JSONException{
		 String destinations="";
		 StoreLocation newLocation=new StoreLocation();
		 
		 String sourceLat = Double.toString(point.getLatitudeE6()/1E6);
	     String sourceLong = Double.toString(point.getLongitudeE6()/1E6);
	     
	     String destLat = Double.toString(destination.getLatitudeE6()/1E6);
	     String destLong = Double.toString(destination.getLongitudeE6()/1E6);
	     destinations=destLat+","+destLong;

	     String urlString="https://maps.googleapis.com/maps/api/distancematrix/json?origins="+sourceLat+","+sourceLong+"&destinations="+destinations+"&mode=walking&sensor=false";
		 //String distanceS="";
		 String result="";
		 HttpClient httpclient = new DefaultHttpClient();  
         HttpGet request = new HttpGet(urlString);  

         ResponseHandler<String> handler = new BasicResponseHandler();  
         try {  
             result = httpclient.execute(request, handler);  
         } catch (ClientProtocolException e) {  
             e.printStackTrace();  
         } catch (IOException e) {  
             e.printStackTrace();  
         }  
         httpclient.getConnectionManager().shutdown();  
	     
         JSONObject obj = new JSONObject(result);
         if((obj.getString("status")).equals("OK")){
      		JSONArray array = obj.getJSONArray("rows");
      		JSONObject objects=array.getJSONObject(0);
      		JSONArray elements=objects.getJSONArray("elements");
      		int count = elements.length();
      		for(int i = 0; i < count; i++){
      				JSONObject item = elements.getJSONObject(i);
          			JSONObject duration = item.getJSONObject("duration");
          			JSONObject distance = item.getJSONObject("distance");
          			Long walkingTime = duration.getLong("value");
          			String walkingTimeText = duration.getString("text");
          			Long distanceValue=distance.getLong("value");
          			String distanceValueText=distance.getString("text");
          			newLocation.setArrivalTimeValues(walkingTimeText, walkingTime);
          			newLocation.setDistanceValues(distanceValueText, distanceValue);
          			//Log.d("distances", "wTime:"+walkingTime +" "+walkingTimeText+"|-|"+distanceValueText+" "+distanceValue);		
      		}
      	}
		 return newLocation;
	 }
	 
	 
	/*	
	 *  @param: GeoPoint start, GeoPoint end
	 *  
	 *  getDirectionData()
	 *  Return: String[]
	 *  
	 *  Purpose: Get direction data between two GeoPoints. Returning a String[] array with
	 *  GeoPoints instructing turn by turn directions to draw a route on the Map.
	 *  
	 * */
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
			menu.add(1,102,3,"Show more store");
			menu.add(1,103,4,"Suggest nearby store");
			return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
			super.onOptionsItemSelected(item);

			switch (item.getItemId()) {
			case 100:
				/*
				 *  Research - future development 
				 * 	
				 *  Turn by turn instructions in text form
				 *  using Google api.
				 * 
				 * */
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
			case 102:
				try {
					this.sameNamePlaces=getPlacesData(point,"5000",this.storeName);
						for(int index=0;index<sameNamePlaces.size();index++){
							mapOverlay.addOverlay(new OverlayItem(sameNamePlaces.get(index).getCoordinates(), "Store: "+sameNamePlaces.get(index).getName(),"Address: "+sameNamePlaces.get(index).getLocationAddress()),new_location_store);
						}
						mapOverlays=mapView.getOverlays();  
						mc = mapView.getController();
						mc.setCenter(point);
						mc.setZoom(16);
						mapView.invalidate();
	
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case 103:
				try {
					nearPlaces=getPlacesData(point,"400","");
					for(int index=0;index<nearPlaces.size();index++){
							mapOverlay.addOverlay(new OverlayItem(nearPlaces.get(index).getCoordinates(), "Store: "+nearPlaces.get(index).getName(),"Address: "+nearPlaces.get(index).getLocationAddress()),suggested_store);
					}
					mapOverlays=mapView.getOverlays();
				    mc = mapView.getController();
				    mc.setCenter(point);
				    mc.setZoom(16);
				    mapView.invalidate();
	
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
				
			}
			return true;
	}
	
	/*
	 *  reverseGeoPoint()
	 *  
	 *  @param: GeoPoint p
	 * 
	 *  Takes a GeoPoint and returns the String version of the Street address - using reverse geocoding.
	 *  This takes too long and slows the phone.
	 *  
	 *  
	 * */	
	public String reverseGeoPoint(GeoPoint p){
		 Geocoder geocoder=new Geocoder(mapView.getContext(),Locale.getDefault()); 
		 String display="";
         try{
        	 List<Address> address = geocoder.getFromLocation(p.getLatitudeE6()/1E6, p.getLongitudeE6()/1E6, 1);
        	 if(address.size()>0){
        		 display ="";
        		 for(int i=0;i<address.get(0).getMaxAddressLineIndex();i++){
        			 display+=address.get(0).getAddressLine(i)+"\n";
        		 }
        	 }
         }catch(IOException e){
        	 e.printStackTrace();
         } 
		return display;
	}
		
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    @Override
	protected void onResume() {
		super.onResume();
		//lcManager.requestLocationUpdates(towers, 50, 1, this);
		
	}

	@Override
	protected void onPause() {
		super.onResume();
		//lcManager.removeUpdates(this);
	}
	
	 @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
         if (((keyCode == KeyEvent.KEYCODE_BACK) || (keyCode == KeyEvent.KEYCODE_DPAD_CENTER)) && (mapOverlay.hideBalloon())) {
        	 return true;
         }
         return super.onKeyDown(keyCode, event);
    }

	public void onLocationChanged(Location location) {
	
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			point = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
			overlayItem = new OverlayItem(point, "You are here! ", "current location");
            mapOverlay.addOverlay(overlayItem);
            mapOverlays=mapView.getOverlays();
            mapOverlays.add(mapOverlay);
			mc.animateTo(point);
		    mc.setCenter(point);
		    mc.setZoom(16);
			mapView.invalidate();
		}
		
	}

	public void onProviderDisabled(String provider) {
		
	}

	public void onProviderEnabled(String provider) {
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	
	} 
}
