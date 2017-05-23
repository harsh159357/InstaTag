package com.harsh.instatag;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GestureDetectionForPhotoTaggingListener implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {


    @Override
    public boolean onDown(MotionEvent e) {
        Log.d("Gesture ", " onDown");
        return true;
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d("Gesture ", " onSingleTapConfirmed");
        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d("Gesture ", " onSingleTapUp");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d("Gesture ", " onShowPress");
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d("Gesture ", " onDoubleTap");
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.d("Gesture ", " onDoubleTapEvent");
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d("Gesture ", " onLongPress");
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        Log.d("Gesture ", " onScroll");
        if (e1.getY() < e2.getY()) {
            Log.d("Gesture ", " Scroll Down");
        }
        if (e1.getY() > e2.getY()) {
            Log.d("Gesture ", " Scroll Up");
        }
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() < e2.getX()) {
            Log.d("Gesture ", "Left to Right swipe: " + e1.getX() + " - " + e2.getX());
            Log.d("Speed ", String.valueOf(velocityX) + " pixels/second");
        }
        if (e1.getX() > e2.getX()) {
            Log.d("Gesture ", "Right to Left swipe: " + e1.getX() + " - " + e2.getX());
            Log.d("Speed ", String.valueOf(velocityX) + " pixels/second");
        }
        if (e1.getY() < e2.getY()) {
            Log.d("Gesture ", "Up to Down swipe: " + e1.getX() + " - " + e2.getX());
            Log.d("Speed ", String.valueOf(velocityY) + " pixels/second");
        }
        if (e1.getY() > e2.getY()) {
            Log.d("Gesture ", "Down to Up swipe: " + e1.getX() + " - " + e2.getX());
            Log.d("Speed ", String.valueOf(velocityY) + " pixels/second");
        }
        return true;

    }
}

