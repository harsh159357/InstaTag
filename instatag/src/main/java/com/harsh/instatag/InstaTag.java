package com.harsh.instatag;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class InstaTag extends RelativeLayout {
    private int Position_X, Position_Y, instaRootWidth, instaRootHeight;
    private ArrayList<View> instaTagList = new ArrayList<>();
    private boolean isInstaRootIsInTouch = true;
    private ViewGroup instaRoot;
    private Context instaContext;
    private InstaTagImageView instaTagImageView;
    private GestureDetector gestureDetector;
    private ImageToBeTaggedEvent imageToBeTaggedEvent;

    public interface InstaConstants {
        String CARROT_TOP = "CARROT_TOP";
        String CARROT_LEFT = "CARROT_LEFT";
        String CARROT_RIGHT = "CARROT_RIGHT";
        String CARROT_BOTTOM = "CARROT_BOTTOM";
    }

    public interface ImageToBeTaggedEvent {
        void singleTapConfirmedAndInstaRootIsInTouch(int x, int y);
    }

    public InstaTag(Context context) {
        super(context);
        initViewWithId(context);
    }

    public InstaTag(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            initView(attrs, context);
        } else {
            initView(attrs, context);
        }
    }

    private void initViewWithId(Context context) {
        instaContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.insta_tag_root, this);
        instaRoot = (ViewGroup) findViewById(R.id.insta_tag_root);
        instaTagImageView = (InstaTagImageView) findViewById(R.id.insta_tag_image_view);
        setLayoutParamsToBeSetForRootLayout(context);
        instaRoot.post(setInstaRootHeightWidth);
        instaRoot.setOnTouchListener(instaTagOnTouchListener);
        gestureDetector = new GestureDetector(instaRoot.getContext(), gestureDetectionForPhotoTaggingListener);
    }

    private void initView(AttributeSet attrs, Context context) {
        instaContext = context;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs,
                R.styleable.InstaTag, 0, 0);
        initViewWithId(context);
        obtainStyledAttributes.recycle();
    }

    private OnTouchListener instaTagOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }
    };

    private Runnable setInstaRootHeightWidth = new Runnable() {
        @Override
        public void run() {
            instaRootWidth = instaRoot.getWidth();
            instaRootHeight = instaRoot.getHeight();
        }
    };

    public void addTagView(int x, int y, String someStringForTagName) {
        if (userNotTaggedYet(someStringForTagName)) {
            LayoutInflater layoutInflater = LayoutInflater.from(instaContext);
            final View tagView = layoutInflater.inflate(R.layout.view_for_tag, instaRoot, false);
            final TextView tagTextView = (TextView) tagView.findViewById(R.id.tag_text_view);
            final ImageView removeTagImageView = (ImageView) tagView.findViewById(R.id.remove_tag_image_view);
            tagTextView.setText(someStringForTagName);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(x - tagTextView.length() * 8, y - tagTextView.length() * 2, 0, 0);
            tagView.setLayoutParams(layoutParams);
            instaTagList.add(tagView);
            instaRoot.addView(tagView);
            removeTagImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    instaTagList.remove(tagView);
                    instaRoot.removeView(tagView);
                }
            });
            tagView.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(final View view, MotionEvent event) {
                    isInstaRootIsInTouch = false;
                    final int X = (int) event.getRawX();
                    final int Y = (int) event.getRawY();
                    int pointerCount = event.getPointerCount();
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                            Position_X = X - layoutParams.leftMargin;
                            Position_Y = Y - layoutParams.topMargin;
                            removeTagImageView.setVisibility(View.VISIBLE);
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                            break;

                        case MotionEvent.ACTION_MOVE:
                            int width = tagView.getWidth();
                            int height = tagView.getHeight();
                            int xCoordinateOfTagView = (int) tagView.getX();
                            int yCoordinateOfTagView = (int) tagView.getY();

                            if (xCoordinateOfTagView <= width && yCoordinateOfTagView <= height) {
                                carrotType(InstaConstants.CARROT_TOP, tagView, pointerCount, X, Position_X, Y, Position_Y);
                            } else if (xCoordinateOfTagView + width >= instaRootWidth && yCoordinateOfTagView + height >= instaRootHeight) {
                                carrotType(InstaConstants.CARROT_BOTTOM, tagView, pointerCount, X, Position_X, Y, Position_Y);
                            } else if (xCoordinateOfTagView - width <= 0 && yCoordinateOfTagView + height >= instaRootHeight) {
                                carrotType(InstaConstants.CARROT_BOTTOM, tagView, pointerCount, X, Position_X, Y, Position_Y);
                            } else if (xCoordinateOfTagView + width >= instaRootWidth && yCoordinateOfTagView <= height / 2) {
                                carrotType(InstaConstants.CARROT_TOP, tagView, pointerCount, X, Position_X, Y, Position_Y);
                            } else if (xCoordinateOfTagView <= 0 && yCoordinateOfTagView > height && yCoordinateOfTagView + height < instaRootHeight) {
                                carrotType(InstaConstants.CARROT_LEFT, tagView, pointerCount, X, Position_X, Y, Position_Y);
                            } else if (xCoordinateOfTagView > width && xCoordinateOfTagView + width < instaRootWidth && yCoordinateOfTagView - height <= 0) {
                                carrotType(InstaConstants.CARROT_TOP, tagView, pointerCount, X, Position_X, Y, Position_Y);
                            } else if (xCoordinateOfTagView + width >= instaRootWidth && yCoordinateOfTagView > height && yCoordinateOfTagView + height < instaRootHeight) {
                                carrotType(InstaConstants.CARROT_RIGHT, tagView, pointerCount, X, Position_X, Y, Position_Y);
                            } else if (xCoordinateOfTagView > width && xCoordinateOfTagView + width < instaRootWidth && yCoordinateOfTagView + height >= instaRootHeight) {
                                carrotType(InstaConstants.CARROT_BOTTOM, tagView, pointerCount, X, Position_X, Y, Position_Y);
                            } else {
                                carrotType(InstaConstants.CARROT_TOP, tagView, pointerCount, X, Position_X, Y, Position_Y);
                            }
                            break;
                    }
                    instaRoot.invalidate();
                    return true;
                }
            });
        } else {
            Toast.makeText(instaContext, "This user is already tagged", Toast.LENGTH_SHORT).show();
        }
    }

    private static void setCarrotVisibility(View view, String carrotType) {
        switch (carrotType) {
            case InstaConstants.CARROT_LEFT:
                view.findViewById(R.id.carrot_left).setVisibility(View.VISIBLE);
                view.findViewById(R.id.carrot_right).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.carrot_top).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.carrot_bottom).setVisibility(View.INVISIBLE);
                break;
            case InstaConstants.CARROT_RIGHT:
                view.findViewById(R.id.carrot_left).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.carrot_right).setVisibility(View.VISIBLE);
                view.findViewById(R.id.carrot_top).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.carrot_bottom).setVisibility(View.INVISIBLE);
                break;
            case InstaConstants.CARROT_TOP:
                view.findViewById(R.id.carrot_left).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.carrot_right).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.carrot_top).setVisibility(View.VISIBLE);
                view.findViewById(R.id.carrot_bottom).setVisibility(View.INVISIBLE);
                break;
            case InstaConstants.CARROT_BOTTOM:
                view.findViewById(R.id.carrot_left).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.carrot_right).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.carrot_top).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.carrot_bottom).setVisibility(View.VISIBLE);
                break;
        }
    }

    private static void setLayoutParamsForTagView(String carrotType, int pointerCount, int X, int Position_X, int Y, int Position_Y, View view) {
        RelativeLayout.LayoutParams Params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        if (pointerCount == 1) {
            switch (carrotType) {
                case InstaConstants.CARROT_LEFT:
                    Params.leftMargin = X - Position_X;
                    Params.topMargin = Y - Position_Y;
                    Params.rightMargin = 0;
                    Params.bottomMargin = 0;
                    view.setLayoutParams(Params);
                    break;
                case InstaConstants.CARROT_RIGHT:
                    Params.leftMargin = X - Position_X;
                    Params.topMargin = Y - Position_Y;
                    Params.rightMargin = 0;
                    Params.bottomMargin = 0;
                    view.setLayoutParams(Params);
                    break;
                case InstaConstants.CARROT_TOP:
                    Params.leftMargin = X - Position_X;
                    Params.topMargin = Y - Position_Y;
                    Params.rightMargin = 0;
                    Params.bottomMargin = 0;
                    view.setLayoutParams(Params);
                    break;
                case InstaConstants.CARROT_BOTTOM:
                    Params.leftMargin = X - Position_X;
                    Params.topMargin = Y - Position_Y;
                    Params.rightMargin = 0;
                    Params.bottomMargin = 0;
                    view.setLayoutParams(Params);
                    break;
            }
        }
    }

    private static void carrotType(String carrotType, View tagView, int pointerCount, int X, int Position_X, int Y, int Position_Y) {
        switch (carrotType) {
            case InstaConstants.CARROT_LEFT:
                setCarrotVisibility(tagView, InstaConstants.CARROT_LEFT);
                setLayoutParamsForTagView(InstaConstants.CARROT_LEFT, pointerCount, X, Position_X, Y, Position_Y, tagView);
                break;
            case InstaConstants.CARROT_RIGHT:
                setCarrotVisibility(tagView, InstaConstants.CARROT_RIGHT);
                setLayoutParamsForTagView(InstaConstants.CARROT_RIGHT, pointerCount, X, Position_X, Y, Position_Y, tagView);
                break;
            case InstaConstants.CARROT_TOP:
                setCarrotVisibility(tagView, InstaConstants.CARROT_TOP);
                setLayoutParamsForTagView(InstaConstants.CARROT_TOP, pointerCount, X, Position_X, Y, Position_Y, tagView);
                break;
            case InstaConstants.CARROT_BOTTOM:
                setCarrotVisibility(tagView, InstaConstants.CARROT_BOTTOM);
                setLayoutParamsForTagView(InstaConstants.CARROT_BOTTOM, pointerCount, X, Position_X, Y, Position_Y, tagView);
                break;
        }
    }

    private void hideRemoveButtonFromAllTagView() {
        if (!instaTagList.isEmpty()) {
            for (View view : instaTagList) {
                view.findViewById(R.id.remove_tag_image_view).setVisibility(View.GONE);
            }
        }
    }

    public InstaTagImageView getInstaTagImageView() {
        return instaTagImageView;
    }

    private void setLayoutParamsToBeSetForRootLayout(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
//        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int rootLayoutHeightWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpWidth, getResources().getDisplayMetrics());
        ViewGroup.LayoutParams params = instaRoot.getLayoutParams();
        params.height = rootLayoutHeightWidth;
        params.width = rootLayoutHeightWidth;
        instaRoot.setLayoutParams(params);
    }

    GestureDetectionForPhotoTaggingListener gestureDetectionForPhotoTaggingListener = new GestureDetectionForPhotoTaggingListener() {

        @Override
        public boolean onDown(MotionEvent e) {
/*
            Log.d("Gesture ", " onDown");
*/
            return true;
        }


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
/*
            Log.d("Gesture ", " onSingleTapConfirmed");
*/
            if (isInstaRootIsInTouch) {
                int x = (int) e.getX();
                int y = (int) e.getY();
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                }
//                addTagView(x, y, null);
                if (imageToBeTaggedEvent != null) {
                    imageToBeTaggedEvent.singleTapConfirmedAndInstaRootIsInTouch(x, y);
                }
            } else {
                hideRemoveButtonFromAllTagView();
                isInstaRootIsInTouch = true;
            }
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
/*
            Log.d("Gesture ", " onSingleTapUp");
*/
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
/*
            Log.d("Gesture ", " onShowPress");
*/
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
/*
            Log.d("Gesture ", " onDoubleTap");
*/
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
/*
            Log.d("Gesture ", " onDoubleTapEvent");
*/
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
/*
            Log.d("Gesture ", " onLongPress");
*/
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

/*
            Log.d("Gesture ", " onScroll");
            if (e1.getY() < e2.getY()) {
                Log.d("Gesture ", " Scroll Down");
            }
            if (e1.getY() > e2.getY()) {
                Log.d("Gesture ", " Scroll Up");
            }
*/
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
/*
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
*/
            return true;

        }
    };

    public void setImageToBeTaggedEvent(ImageToBeTaggedEvent imageToBeTaggedEvent) {
        if (this.imageToBeTaggedEvent == null) {
            this.imageToBeTaggedEvent = imageToBeTaggedEvent;
        }
    }

    private boolean userNotTaggedYet(String userName) {
        boolean userFound = true;
        if (!instaTagList.isEmpty()) {
            for (View user : instaTagList) {
                if (((TextView) user.findViewById(R.id.tag_text_view)).getText().toString().equals(userName)) {
                    userFound = false;
                }
            }
        } else {
            userFound = true;
        }
        return userFound;
    }

    private int getX(Double x) {
        return (int) ((instaRootWidth * x) / 100);
    }

    private int getY(Double x) {
        return (int) ((instaRootHeight * x) / 100);
    }

    public ArrayList<UserToBeTagged> getListOfUserToBeTagged() {
        ArrayList<UserToBeTagged> userToBeTaggedArrayList = new ArrayList<>();
        if (!instaTagList.isEmpty()) {
            for (int i = 0; i < instaTagList.size(); i++) {
                View view = instaTagList.get(i);
                double x = view.getX();
                x = (x / instaRootWidth) * 100;
                double y = view.getY();
                y = (y / instaRootHeight) * 100;
                userToBeTaggedArrayList.add(new UserToBeTagged(((TextView) view.findViewById(R.id.tag_text_view)).getText().toString(), x, y));
            }
        }
        return userToBeTaggedArrayList;
    }

    public void addTagViewFromUserToBeTaggedList(ArrayList<UserToBeTagged> userToBeTaggedArrayList) {
        for (UserToBeTagged userToBeTagged : userToBeTaggedArrayList) {
            addTagView(getX(userToBeTagged.getX_co_ord()),
                    getY(userToBeTagged.getY_co_ord()),
                    userToBeTagged.getUser_unique_id());
        }
    }

    public void showTags() {
        if (!instaTagList.isEmpty()) {
            for (View view : instaTagList) {
                view.setVisibility(VISIBLE);
            }
        }
    }

    public void hideTags() {
        if (!instaTagList.isEmpty()) {
            for (View view : instaTagList) {
                view.setVisibility(GONE);
            }
        }
    }

}
