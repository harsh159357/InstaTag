package com.harsh.instatag;

import android.view.GestureDetector;
import android.view.MotionEvent;

public interface GestureDetectionForPhotoTaggingListener extends GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    boolean onDown(MotionEvent e);

    boolean onSingleTapConfirmed(MotionEvent e);

    boolean onSingleTapUp(MotionEvent e);

    void onShowPress(MotionEvent e);

    boolean onDoubleTap(MotionEvent e);

    boolean onDoubleTapEvent(MotionEvent e);

    void onLongPress(MotionEvent e);

    boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);

    boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY);
}

