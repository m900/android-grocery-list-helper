package com.luisromero.listmenu;




import java.util.ArrayList;


//import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

public class MapMarkerOverlay extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private MapView mapView;
	private BalloonLayout balloonView;
	private View clickRegion;
	private int balloonViewOffset;
	//private Context context;
	
	public MapMarkerOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenterBottom(defaultMarker));
		this.mapView=mapView;
		//this.context=mapView.getContext();
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
	
	public void addOverlay(OverlayItem overlay, Drawable setMarker) {
		// add a custom drawable resource to the stores on the same mapOverlays list.
		overlay.setMarker(boundCenterBottom(setMarker));
		this.addOverlay(overlay);
	}
	
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);
	 /*
	  String message="";
	  message+=item.getTitle()+item.getSnippet();
	  Toast.makeText(mapView.getContext(),message , Toast.LENGTH_SHORT).show();
	  */
	  
	  final int thisIndex = index;
      final GeoPoint point = item.getPoint();
	 
	  boolean isRecycled = true;
	  if (balloonView == null) {
 
          balloonView = new BalloonLayout(mapView.getContext(), balloonViewOffset);
          clickRegion = (View) balloonView.findViewById(R.id.balloon_inner_layout);
          isRecycled = false;
	  }
	  balloonView.setVisibility(View.GONE);
	  balloonView.setText(createItem(index).getTitle() + "\n" + createItem(index).getSnippet());
	  
      MapView.LayoutParams params = new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,
                      LayoutParams.WRAP_CONTENT, point, MapView.LayoutParams.BOTTOM_CENTER);
      params.mode = MapView.LayoutParams.MODE_MAP;

      clickRegion.setOnTouchListener(new OnTouchListener() {
              public boolean onTouch(View arg0, MotionEvent arg1) {
                      if (arg1.getAction() == MotionEvent.ACTION_UP) {
                              return onBalloonTap(thisIndex);
                      }
                      return true;
              }

      });

      balloonView.setVisibility(View.VISIBLE);
      if (isRecycled) {
              balloonView.setLayoutParams(params);
      } else {
              mapView.addView(balloonView, params);
      }
      mapView.getController().animateTo(point);
	 
	  return true;
	}
	
	 public boolean hideBalloon() {
         if ((balloonView != null) && (balloonView.getVisibility() != View.GONE)) {
                 balloonView.setVisibility(View.GONE);
                 return true;
         }
         return false;
	  }
	  
	  protected boolean onBalloonTap(int index) {
         
         return true;
	  }
	
	public void draw(Canvas canvas, MapView map, boolean shadow){

	    super.draw(canvas, map, shadow);
	    Paint mPaint = new Paint();
	    mPaint.setDither(true);
	    mPaint.setColor(Color.RED);
	    mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	    mPaint.setStrokeJoin(Paint.Join.ROUND);
	    mPaint.setStrokeCap(Paint.Cap.ROUND);
	    mPaint.setStrokeWidth(2);

	    Point p1 = new Point();
	    Point p2 = new Point();
	    Path path = new Path();
	    Projection projection = map.getProjection();
	    projection.toPixels(mOverlays.get(0).getPoint(), p1);
	    projection.toPixels(mOverlays.get(1).getPoint(), p2);
	    path.moveTo(p2.x, p2.y);
	    path.moveTo(p1.x, p1.y);
	    canvas.drawPath(path, mPaint);
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
		// To show the latitude and longitude of Touched location on map.
         //---when user lifts his finger---
        /* 
		 if (event.getAction() == 1) {                
             GeoPoint p = mapView.getProjection().fromPixels(
                 (int) event.getX(),
                 (int) event.getY());
             //String place ="";
             //place+=p.getLatitudeE6()/1E6 + "," + p.getLongitudeE6()/1E6;
             
                 Toast.makeText(mapView.getContext(), 
                     p.getLatitudeE6() / 1E6 + "," + 
                     p.getLongitudeE6() /1E6 , 
                     Toast.LENGTH_SHORT).show();
              
             Geocoder geocoder=new Geocoder(mapView.getContext(),Locale.getDefault()); 
             try{
            	 List<Address> address = geocoder.getFromLocation(p.getLatitudeE6()/1E6, p.getLongitudeE6()/1E6, 1);
            	 if(address.size()>0){
            		 String display ="";
            		 for(int i=0;i<address.get(0).getMaxAddressLineIndex();i++){
            			 
            			 display+=address.get(0).getAddressLine(i)+"\n";
            		 }
            		 Toast.makeText(mapView.getContext(), display, Toast.LENGTH_SHORT).show();
            	 }
             }catch(IOException e){
            	 e.printStackTrace();
             }       
         }
	 */
		return false;                            
     }

	

	        
           
}
