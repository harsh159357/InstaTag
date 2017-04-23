package com.harsh.instagramtagview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class InstagramTaggingActivity extends AppCompatActivity {
    private int x, y, Position_X, Position_Y, rootLayoutWidth, rootLayoutHeight;
    private ArrayList<View> tagViewArrayList = new ArrayList<>();
    public static final String CARROT_TOP = "CARROT_TOP";
    public static final String CARROT_LEFT = "CARROT_LEFT";
    public static final String CARROT_RIGHT = "CARROT_RIGHT";
    public static final String CARROT_BOTTOM = "CARROT_BOTTOM";
    private boolean isRootLayoutIsInTouch = true;
    private ViewGroup rootLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagging);
        rootLayout = (ViewGroup) findViewById(R.id.rootLayout);
        rootLayout.post(new Runnable() {
            @Override
            public void run() {
                rootLayoutWidth = rootLayout.getWidth();
                rootLayoutHeight = rootLayout.getHeight();
            }
        });
        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isRootLayoutIsInTouch) {
                    x = (int) event.getX();
                    y = (int) event.getY();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_MOVE:
                        case MotionEvent.ACTION_UP:
                    }
                    addTagView(x, y);
                } else {
                    hideRemoveButtonFromAllTagView();
                    isRootLayoutIsInTouch = true;
                }
                return false;
            }
        });
    }

    private void addTagView(int x, int y) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View tagView = layoutInflater.inflate(R.layout.view_for_tag, rootLayout, false);
        final TextView tagTextView = (TextView) tagView.findViewById(R.id.tag_text_view);
        final ImageView removeTagImageView = (ImageView) tagView.findViewById(R.id.remove_tag_image_view);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(x - tagTextView.length() * 8, y - tagTextView.length() * 2, 0, 0);
        tagView.setLayoutParams(layoutParams);
        tagViewArrayList.add(tagView);
        rootLayout.addView(tagView);
        removeTagImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagViewArrayList.remove(tagView);
                rootLayout.removeView(tagView);
            }
        });
        tagView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(final View view, MotionEvent event) {
                isRootLayoutIsInTouch = false;
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
                            carrotType(CARROT_TOP, tagView, pointerCount, X, Position_X, Y, Position_Y);
                        } else if (xCoordinateOfTagView + width >= rootLayoutWidth && yCoordinateOfTagView + height >= rootLayoutHeight) {
                            carrotType(CARROT_BOTTOM, tagView, pointerCount, X, Position_X, Y, Position_Y);
                        } else if (xCoordinateOfTagView - width <= 0 && yCoordinateOfTagView + height >= rootLayoutHeight) {
                            carrotType(CARROT_BOTTOM, tagView, pointerCount, X, Position_X, Y, Position_Y);
                        } else if (xCoordinateOfTagView + width >= rootLayoutWidth && yCoordinateOfTagView <= height / 2) {
                            carrotType(CARROT_TOP, tagView, pointerCount, X, Position_X, Y, Position_Y);
                        } else if (xCoordinateOfTagView <= 0 && yCoordinateOfTagView > height && yCoordinateOfTagView + height < rootLayoutHeight) {
                            carrotType(CARROT_LEFT, tagView, pointerCount, X, Position_X, Y, Position_Y);
                        } else if (xCoordinateOfTagView > width && xCoordinateOfTagView + width < rootLayoutWidth && yCoordinateOfTagView - height <= 0) {
                            carrotType(CARROT_TOP, tagView, pointerCount, X, Position_X, Y, Position_Y);
                        } else if (xCoordinateOfTagView + width >= rootLayoutWidth && yCoordinateOfTagView > height && yCoordinateOfTagView + height < rootLayoutHeight) {
                            carrotType(CARROT_RIGHT, tagView, pointerCount, X, Position_X, Y, Position_Y);
                        } else if (xCoordinateOfTagView > width && xCoordinateOfTagView + width < rootLayoutWidth && yCoordinateOfTagView + height >= rootLayoutHeight) {
                            carrotType(CARROT_BOTTOM, tagView, pointerCount, X, Position_X, Y, Position_Y);
                        } else {
                            carrotType(CARROT_TOP, tagView, pointerCount, X, Position_X, Y, Position_Y);
                        }
                        break;
                }
                rootLayout.invalidate();
                return true;
            }
        });
    }

    private static void setCarrotVisibility(View view, String carrotType) {
        switch (carrotType) {
            case CARROT_LEFT:
                view.findViewById(R.id.carrot_left).setVisibility(View.VISIBLE);
                view.findViewById(R.id.carrot_right).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.carrot_top).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.carrot_bottom).setVisibility(View.INVISIBLE);
                break;
            case CARROT_RIGHT:
                view.findViewById(R.id.carrot_left).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.carrot_right).setVisibility(View.VISIBLE);
                view.findViewById(R.id.carrot_top).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.carrot_bottom).setVisibility(View.INVISIBLE);
                break;
            case CARROT_TOP:
                view.findViewById(R.id.carrot_left).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.carrot_right).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.carrot_top).setVisibility(View.VISIBLE);
                view.findViewById(R.id.carrot_bottom).setVisibility(View.INVISIBLE);
                break;
            case CARROT_BOTTOM:
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
                case CARROT_LEFT:
                    Params.leftMargin = X - Position_X;
                    Params.topMargin = Y - Position_Y;
                    Params.rightMargin = 0;
                    Params.bottomMargin = 0;
                    view.setLayoutParams(Params);
                    break;
                case CARROT_RIGHT:
                    Params.leftMargin = X - Position_X;
                    Params.topMargin = Y - Position_Y;
                    Params.rightMargin = 0;
                    Params.bottomMargin = 0;
                    view.setLayoutParams(Params);
                    break;
                case CARROT_TOP:
                    Params.leftMargin = X - Position_X;
                    Params.topMargin = Y - Position_Y;
                    Params.rightMargin = 0;
                    Params.bottomMargin = 0;
                    view.setLayoutParams(Params);
                    break;
                case CARROT_BOTTOM:
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
            case CARROT_LEFT:
                setCarrotVisibility(tagView, CARROT_LEFT);
                setLayoutParamsForTagView(CARROT_LEFT, pointerCount, X, Position_X, Y, Position_Y, tagView);
                break;
            case CARROT_RIGHT:
                setCarrotVisibility(tagView, CARROT_RIGHT);
                setLayoutParamsForTagView(CARROT_RIGHT, pointerCount, X, Position_X, Y, Position_Y, tagView);
                break;
            case CARROT_TOP:
                setCarrotVisibility(tagView, CARROT_TOP);
                setLayoutParamsForTagView(CARROT_TOP, pointerCount, X, Position_X, Y, Position_Y, tagView);
                break;
            case CARROT_BOTTOM:
                setCarrotVisibility(tagView, CARROT_BOTTOM);
                setLayoutParamsForTagView(CARROT_BOTTOM, pointerCount, X, Position_X, Y, Position_Y, tagView);
                break;
        }
    }

    private void hideRemoveButtonFromAllTagView() {
        if (!tagViewArrayList.isEmpty()) {
            for (View view : tagViewArrayList) {
                view.findViewById(R.id.remove_tag_image_view).setVisibility(View.GONE);
            }
        }
    }

}

