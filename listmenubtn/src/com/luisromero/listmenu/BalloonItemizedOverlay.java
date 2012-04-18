package com.luisromero.listmenu;

import java.util.ArrayList;


import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;

import com.google.android.maps.OverlayItem;


public class BalloonItemizedOverlay extends ItemizedOverlay<OverlayItem>{
	private final static String TAG = "BALLOON OVERLAY";
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private MapView mapView;
	
	private BalloonLayout balloonView;
	private View clickRegion;
	private int balloonViewOffset;
	
	public BalloonItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenterBottom(defaultMarker));
        this.mapView = mapView;
        //balloonViewOffset = defaultMarker.getIntrinsicHeight();
        //populate();
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

  
	@Override
	protected boolean onTap(int index) {
	 OverlayItem item = mOverlays.get(index);
	  final int thisIndex = index;
      final GeoPoint point = item.getPoint();
	 
	  boolean isRecycled = true;
	  if (balloonView == null) {
          Log.d(TAG, "New balloonView");
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
          Log.d(TAG, "onBalloonTap not catched!");
          return true;
	  }

  
}