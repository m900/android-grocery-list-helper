package com.luisromero.listmenu;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/*	@author: Luis G Romero
 *  @param : BalloonLayout
 *  Purpose: Structures the information displayed in the BalloonItemizedOverlay object.
 * 
 */
public class BalloonLayout extends FrameLayout {

    private final LinearLayout layout;
    private final TextView title;
 
    public BalloonLayout(Context context, int balloonBottomOffset) {
            super(context);
         
            setPadding(10, 0, 10, balloonBottomOffset);
            layout = new LinearLayout(context);
            layout.setVisibility(VISIBLE);

            final LayoutInflater inflater = (LayoutInflater) context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            
            title = (TextView) inflater.inflate(R.layout.balloon, layout).findViewById(
                            R.id.balloon_item_title);
            
            final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.NO_GRAVITY;
            addView(layout, params);
    }

    /**
     * Sets the text shown into the balloon.
     * 
     * @param text
     */
    public void setText(String text) {
            layout.setVisibility(VISIBLE);
            if (text != null) {
                    title.setVisibility(VISIBLE);
                    title.setText(text);
                    
            } else {
                    title.setVisibility(GONE);
            }
    }
 
}

