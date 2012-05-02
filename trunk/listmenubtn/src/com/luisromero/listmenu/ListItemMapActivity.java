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
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
//import android.view.MenuInflater;
import android.view.MenuItem;
//mport android.widget.Toast;

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
	private GeoPoint closestLocation;
	//private LocationManager locationManager;
	private MapMarkerOverlay mapOverlay;
	//private MyLocationOverlay me=null; //google own library class.
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
	private List<Location> sameNamePlaces;
	private List<Location> nearPlaces;
	private Location newPlace;
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
        new_location_store=this.getResources().getDrawable(R.drawable.cart_push_red);
        suggested_store=this.getResources().getDrawable(R.drawable.cart_push_blue);
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
             
        if(this.storeName.equals("Safeway")){
        	mapOverlay.addOverlay(new OverlayItem(safeway, "Store: Safeway", "Product: "+this.productName + "\nQuantity: "+this.productQuantity),location_store);
        	dest=safeway;
        }else if(this.storeName.equals("Whole Foods Market")){
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
	    mapView.invalidate();
    }
	
	private List<Location> getPlacesData(GeoPoint start,String radius, String storeNames) throws JSONException{
		 String sourceLat = Double.toString(start.getLatitudeE6()/1E6);
	     String sourceLong = Double.toString(start.getLongitudeE6()/1E6);
	     String searchRadius=radius;
	     String googleAPIKey="AIzaSyAzKZWP0TWKRxqKC0ybVOp295EGl2Vb3W0";
	     String urlString = "https://maps.googleapis.com/maps/api/place/search/json?location="+sourceLat+","+sourceLong+"&"+"radius="+searchRadius +"&name="+storeNames+"&types=grocery_or_supermarket&sensor=false"+"&"+"key="+googleAPIKey;
	     String result="";
	     List<Location> newStores=new ArrayList<Location>();
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
     			
     			Location newLocation=getPlacesDistance(store);
     			
     			newLocation.setName(storeName);
     			newLocation.setCoordinates(store);
     			newLocation.setLocationAddress(storeAddress);
     			Log.d("StoreName:"+storeName, storeAddress);
     			Log.d("placeLocation",storeAddress);
     			newStores.add(newLocation);
     		}
     	}
     	
	     return newStores;
	}
	
	
	 private Location getPlacesDistance(GeoPoint destination) throws JSONException{
		 String destinations="";
		 String dest2="";
		 Location newLocation=new Location();
		 /*
		 for(int index=0;index<nearPlaces.size();index++){
			 destinations+=Double.toString(nearPlaces.get(index).getCoordinates().getLatitudeE6()/1E6)+","+Double.toString(nearPlaces.get(index).getCoordinates().getLongitudeE6()/1E6);
			 if(index==nearPlaces.size()-1){
				 destinations+="";
			 }else{
				 destinations+="|";
			 }
		 }
		 */
		 Log.d("Destinations",destinations);
		 
		 String sourceLat = Double.toString(point.getLatitudeE6()/1E6);
	     String sourceLong = Double.toString(point.getLongitudeE6()/1E6);
	     
	     String destLat = Double.toString(destination.getLatitudeE6()/1E6);
	     String destLong = Double.toString(destination.getLongitudeE6()/1E6);
	     dest2=destLat+","+destLong;

	     String urlString="https://maps.googleapis.com/maps/api/distancematrix/json?origins="+sourceLat+","+sourceLong+"&destinations="+dest2+"&mode=walking&sensor=false";
		 String distanceS="";
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
          			Double walkingTime = duration.getDouble("value");
          			String walkingTimeText = duration.getString("text");
          			Double distanceValue=distance.getDouble("value");
          			String distanceValueText=distance.getString("text");
          		   
          			Log.d("distances", "ArrayLength="+array.length() + "wTime:"+walkingTime + " "+distanceValueText);		
      		}
      	}
		 
		 return newLocation;
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
			menu.add(1,102,3,"Show more store");
			menu.add(1,103,4,"Suggest nearby store");
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
			case 102:
				try {
					
					this.sameNamePlaces=getPlacesData(point,"5000",this.storeName);
					//getPlacesDistance(this.sameNamePlaces);
						for(int index=0;index<sameNamePlaces.size();index++){
							if(sameNamePlaces.get(index).getName().equals(storeName)){
								mapOverlay.addOverlay(new OverlayItem(sameNamePlaces.get(index).getCoordinates(), "Store: "+sameNamePlaces.get(index).getName(),"Address: "+sameNamePlaces.get(index).getLocationAddress()),new_location_store);
							}
							Log.d("SameNamePlaces",sameNamePlaces.get(index).getName()+sameNamePlaces.get(index).getLocationAddress());
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
						Log.d("NearPlaces",nearPlaces.get(index).getName()+nearPlaces.get(index).getLocationAddress());
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
