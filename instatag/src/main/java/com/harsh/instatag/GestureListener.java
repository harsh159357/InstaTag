/*
 * Copyright 2019 Harsh Sharma
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.harsh.instatag;

import android.view.GestureDetector;
import android.view.MotionEvent;

public interface GestureListener extends GestureDetector.OnGestureListener,
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

