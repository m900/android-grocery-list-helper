package com.luisromero.listmenu;

import java.util.List;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
 
/*	@author: Luis G Romero
 *  @param : RoutePathOverlay
 *  Purpose: Displays a red colored route on the screen using a List<GeoPoin> to draw segments on the view.
 * 
 */

public class RoutePathOverlay extends Overlay {
 
        private int pathColor;
        private final List<GeoPoint> points;
        private boolean drawStartEnd;
 
        public RoutePathOverlay(List<GeoPoint> points) {
                this(points, Color.RED, true);
        }
 
        public RoutePathOverlay(List<GeoPoint> points, int pathColor, boolean drawStartEnd) {
                this.points = points;
                this.pathColor = pathColor;
                this.drawStartEnd = drawStartEnd;
        }
 
        private void drawOval(Canvas canvas, Paint paint, Point point) {
                Paint ovalPaint = new Paint(paint);
                ovalPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                ovalPaint.setStrokeWidth(2);
                int _radius = 6;
                RectF oval = new RectF(point.x - _radius, point.y - _radius, point.x + _radius, point.y + _radius);
                canvas.drawOval(oval, ovalPaint);               
        }
 
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
                Projection projection = mapView.getProjection();
                if (shadow == false && points != null) {
                        Point startPoint = null, endPoint = null;
                        Path path = new Path();
                        //We are creating the path
                        for (int i = 0; i < points.size(); i++) {
                                GeoPoint gPointA = points.get(i);
                                Point pointA = new Point();
                                projection.toPixels(gPointA, pointA);
                                if (i == 0) { //This is the start point
                                        startPoint = pointA;
                                        path.moveTo(pointA.x, pointA.y);
                                } else {
                                        if (i == points.size() - 1)//This is the end point
                                                endPoint = pointA;
                                        path.lineTo(pointA.x, pointA.y);
                                }
                        }
 
                        Paint paint = new Paint();
                        paint.setAntiAlias(true);
                        paint.setColor(pathColor);
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setStrokeWidth(5);
                        paint.setAlpha(90);
                        if (getDrawStartEnd()) {
                                if (startPoint != null) {
                                        drawOval(canvas, paint, startPoint);
                                }
                                if (endPoint != null) {
                                        drawOval(canvas, paint, endPoint);
                                }
                        }
                        if (!path.isEmpty())
                                canvas.drawPath(path, paint);
                }
                return super.draw(canvas, mapView, shadow, when);
        }
 
        public boolean getDrawStartEnd() {
                return drawStartEnd;
        }
 
        public void setDrawStartEnd(boolean markStartEnd) {
                drawStartEnd = markStartEnd;
        }
}