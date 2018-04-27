/*
 * Copyright 2017 Harsh Sharma
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
 */

package com.harsh.instatag;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class InstaTag extends RelativeLayout {

    private int mPosX;
    private int mPosY;
    private int mRootWidth;
    private int mRootHeight;

    private Context mContext;

    private int mTagTextColor;
    private int mTagBackgroundColor;
    private int mCarrotTopBackGroundColor;
    private int mCarrotLeftBackGroundColor;
    private int mCarrotRightBackGroundColor;
    private int mCarrotBottomBackGroundColor;

    private Drawable mTagTextDrawable;
    private Drawable mCarrotTopDrawable;
    private Drawable mCarrotLeftDrawable;
    private Drawable mCarrotRightDrawable;
    private Drawable mCarrotBottomDrawable;

    private boolean mTagsAreAdded;
    private boolean mCanWeAddTags;
    private boolean mShowAllCarrots;
    private boolean mIsRootIsInTouch = true;

    private Animation mShowAnimation;
    private Animation mHideAnimation;

    private GestureDetector mGestureDetector;

    private TaggedImageEvent mTaggedImageEvent;

    private ViewGroup mRoot;
    private TagImageView mTagImageView;
    private final ArrayList<View> mTagList = new ArrayList<>();

    private final Runnable mSetRootHeightWidth = new Runnable() {
        @Override
        public void run() {
            mRootWidth = mRoot.getWidth();
            mRootHeight = mRoot.getHeight();
        }
    };

    private final OnTouchListener mTagOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return mGestureDetector.onTouchEvent(event);
        }
    };

    private final TagGestureListener mTagGestureListener = new TagGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (mCanWeAddTags) {
                if (mIsRootIsInTouch) {
                    int x = (int) e.getX();
                    int y = (int) e.getY();
                    switch (e.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_MOVE:
                        case MotionEvent.ACTION_UP:
                    }
                    if (mTaggedImageEvent != null) {
                        mTaggedImageEvent.singleTapConfirmedAndRootIsInTouch(x, y);
                    }
                } else {
                    hideRemoveButtonFromAllTagView();
                    mIsRootIsInTouch = true;
                }
            }
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }
    };

    public interface Constants {
        int DEFAULT_COLOR = 0xFF303F9F;
        int TAG_TEXT_COLOR = Color.WHITE;

        String CARROT_TOP = "CARROT_TOP";
        String CARROT_LEFT = "CARROT_LEFT";
        String CARROT_RIGHT = "CARROT_RIGHT";
        String CARROT_BOTTOM = "CARROT_BOTTOM";
    }

    public interface TaggedImageEvent {
        void singleTapConfirmedAndRootIsInTouch(int x, int y);
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
        mContext = context;

        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.tag_root, this);

        mRoot = findViewById(R.id.tag_root);
        mTagImageView = findViewById(R.id.tag_image_view);

        setLayoutParamsToBeSetForRootLayout(mContext);
        mRoot.post(mSetRootHeightWidth);
        mRoot.setOnTouchListener(mTagOnTouchListener);
        mGestureDetector = new GestureDetector(mRoot.getContext(), mTagGestureListener);
    }

    private void initView(AttributeSet attrs, Context context) {
        mContext = context;

        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs,
                R.styleable.InstaTag, 0, 0);

        mTagTextDrawable = obtainStyledAttributes.
                getDrawable(R.styleable.InstaTag_tagTextBackground);
        mCarrotTopDrawable = obtainStyledAttributes.
                getDrawable(R.styleable.InstaTag_carrotTopBackground);
        mCarrotLeftDrawable = obtainStyledAttributes.
                getDrawable(R.styleable.InstaTag_carrotLeftBackground);
        mCarrotRightDrawable = obtainStyledAttributes.
                getDrawable(R.styleable.InstaTag_carrotRightBackground);
        mCarrotBottomDrawable = obtainStyledAttributes.
                getDrawable(R.styleable.InstaTag_carrotBottomBackground);

        mCanWeAddTags = obtainStyledAttributes.
                getBoolean(R.styleable.InstaTag_canWeAddTags, false);
        mShowAllCarrots = obtainStyledAttributes.
                getBoolean(R.styleable.InstaTag_showAllCarrots, false);

        mTagTextColor = obtainStyledAttributes.
                getColor(R.styleable.InstaTag_instaTextColor, Constants.TAG_TEXT_COLOR);

        int overrideDefaultColor = obtainStyledAttributes.
                getColor(R.styleable.InstaTag_overrideDefaultColor, Constants.DEFAULT_COLOR);

        if (overrideDefaultColor == Constants.DEFAULT_COLOR) {
            mCarrotTopBackGroundColor = obtainStyledAttributes.
                    getColor(R.styleable.InstaTag_carrotTopColor, Constants.DEFAULT_COLOR);
            mCarrotLeftBackGroundColor = obtainStyledAttributes.
                    getColor(R.styleable.InstaTag_carrotLeftColor, Constants.DEFAULT_COLOR);
            mCarrotRightBackGroundColor = obtainStyledAttributes.
                    getColor(R.styleable.InstaTag_carrotRightColor, Constants.DEFAULT_COLOR);
            mCarrotBottomBackGroundColor = obtainStyledAttributes.
                    getColor(R.styleable.InstaTag_carrotBottomColor, Constants.DEFAULT_COLOR);
            mTagBackgroundColor = obtainStyledAttributes.
                    getColor(R.styleable.InstaTag_instaBackgroundColor, Constants.DEFAULT_COLOR);
        } else {
            mTagBackgroundColor = overrideDefaultColor;
            mCarrotTopBackGroundColor = overrideDefaultColor;
            mCarrotLeftBackGroundColor = overrideDefaultColor;
            mCarrotRightBackGroundColor = overrideDefaultColor;
            mCarrotBottomBackGroundColor = overrideDefaultColor;
        }

        mHideAnimation = AnimationUtils.loadAnimation(context, obtainStyledAttributes.
                getResourceId(R.styleable.InstaTag_hideAnimation, R.anim.zoom_out));

        mShowAnimation = AnimationUtils.loadAnimation(context, obtainStyledAttributes.
                getResourceId(R.styleable.InstaTag_showAnimation, R.anim.zoom_in));

        initViewWithId(context);
        obtainStyledAttributes.recycle();
    }

    private void setCarrotVisibility(View tagView, String carrotType) {
        if (!mShowAllCarrots) {
            switch (carrotType) {
                case Constants.CARROT_TOP:
                    tagView.findViewById(R.id.carrot_top).setVisibility(View.VISIBLE);

                    tagView.findViewById(R.id.carrot_left).setVisibility(View.INVISIBLE);
                    tagView.findViewById(R.id.carrot_right).setVisibility(View.INVISIBLE);
                    tagView.findViewById(R.id.carrot_bottom).setVisibility(View.INVISIBLE);
                    break;
                case Constants.CARROT_LEFT:
                    tagView.findViewById(R.id.carrot_left).setVisibility(View.VISIBLE);

                    tagView.findViewById(R.id.carrot_top).setVisibility(View.INVISIBLE);
                    tagView.findViewById(R.id.carrot_right).setVisibility(View.INVISIBLE);
                    tagView.findViewById(R.id.carrot_bottom).setVisibility(View.INVISIBLE);
                    break;
                case Constants.CARROT_RIGHT:
                    tagView.findViewById(R.id.carrot_right).setVisibility(View.VISIBLE);

                    tagView.findViewById(R.id.carrot_top).setVisibility(View.INVISIBLE);
                    tagView.findViewById(R.id.carrot_left).setVisibility(View.INVISIBLE);
                    tagView.findViewById(R.id.carrot_bottom).setVisibility(View.INVISIBLE);
                    break;
                case Constants.CARROT_BOTTOM:
                    tagView.findViewById(R.id.carrot_bottom).setVisibility(View.VISIBLE);

                    tagView.findViewById(R.id.carrot_top).setVisibility(View.INVISIBLE);
                    tagView.findViewById(R.id.carrot_left).setVisibility(View.INVISIBLE);
                    tagView.findViewById(R.id.carrot_right).setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }

    private void actionTagMove(View tagView, int pointerCount, int X, int Y) {
        int width = tagView.getWidth();
        int height = tagView.getHeight();
        int x = (int) tagView.getX();
        int y = (int) tagView.getY();

        if (x <= width && y <= height) {
            carrotType(Constants.CARROT_TOP, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else if (x + width >= mRootWidth && y + height >= mRootHeight) {
            carrotType(Constants.CARROT_BOTTOM, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else if (x - width <= 0 && y + height >= mRootHeight) {
            carrotType(Constants.CARROT_BOTTOM, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else if (x + width >= mRootWidth && y <= height / 2) {
            carrotType(Constants.CARROT_TOP, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else if (x <= 0 && y > height && y + height < mRootHeight) {
            carrotType(Constants.CARROT_LEFT, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else if (x > width && x + width < mRootWidth && y - height <= 0) {
            carrotType(Constants.CARROT_TOP, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else if (x + width >= mRootWidth && y > height && y + height < mRootHeight) {
            carrotType(Constants.CARROT_RIGHT, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else if (x > width && x + width < mRootWidth && y + height >= mRootHeight) {
            carrotType(Constants.CARROT_BOTTOM, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else {
            carrotType(Constants.CARROT_TOP, tagView, pointerCount, X, mPosX, Y, mPosY);
        }
    }

    private void carrotType(String carrotType,
                            View tagView,
                            int pointerCount, int X, int posX, int Y, int posY) {
        switch (carrotType) {
            case Constants.CARROT_TOP:
                setCarrotVisibility(tagView, Constants.CARROT_TOP);
                setLayoutParamsForTagView(Constants.CARROT_TOP,
                        pointerCount, X, posX, Y, posY, tagView);
                break;
            case Constants.CARROT_LEFT:
                setCarrotVisibility(tagView, Constants.CARROT_LEFT);
                setLayoutParamsForTagView(Constants.CARROT_LEFT,
                        pointerCount, X, posX, Y, posY, tagView);
                break;
            case Constants.CARROT_RIGHT:
                setCarrotVisibility(tagView, Constants.CARROT_RIGHT);
                setLayoutParamsForTagView(Constants.CARROT_RIGHT,
                        pointerCount, X, posX, Y, posY, tagView);
                break;
            case Constants.CARROT_BOTTOM:
                setCarrotVisibility(tagView, Constants.CARROT_BOTTOM);
                setLayoutParamsForTagView(Constants.CARROT_BOTTOM,
                        pointerCount, X, posX, Y, posY, tagView);
                break;
        }
    }

    private void setLayoutParamsForTagView(String carrotType,
                                           int pointerCount, int X, int posX, int Y, int posY,
                                           View tagView) {
        int left = X - posX;
        int top = Y - posY;

        if (left < 0) {
            left = 0;
        } else if (left + tagView.getWidth() > mRootWidth) {
            left = mRootWidth - tagView.getWidth();
        }

        if (top < 0) {
            top = 0;
        } else if (top + tagView.getHeight() > mRootHeight) {
            top = mRootHeight - tagView.getHeight();
        }

        RelativeLayout.LayoutParams tagViewLayoutParams =
                (RelativeLayout.LayoutParams) tagView.getLayoutParams();
        if (pointerCount == 1) {
            switch (carrotType) {
                case Constants.CARROT_TOP:
                case Constants.CARROT_LEFT:
                case Constants.CARROT_RIGHT:
                case Constants.CARROT_BOTTOM:
                    tagViewLayoutParams.setMargins(left, top, 0, 0);
                    tagView.setLayoutParams(tagViewLayoutParams);
                    break;
            }
        }
    }


    private void setColorForTag(View tagView) {
        ((TextView) tagView.findViewById(R.id.tag_text_view)).setTextColor(mTagTextColor);

        if (mCarrotTopDrawable == null) {
            setColor((tagView.findViewById(R.id.carrot_top)).
                    getBackground(), mCarrotTopBackGroundColor);
        }
        if (mCarrotLeftDrawable == null) {
            setColor((tagView.findViewById(R.id.carrot_left)).
                    getBackground(), mCarrotLeftBackGroundColor);
        }
        if (mCarrotRightDrawable == null) {
            setColor((tagView.findViewById(R.id.carrot_right)).
                    getBackground(), mCarrotRightBackGroundColor);
        }
        if (mCarrotBottomDrawable == null) {
            setColor((tagView.findViewById(R.id.carrot_bottom)).
                    getBackground(), mCarrotBottomBackGroundColor);
        }

        if (mTagTextDrawable == null) {
            setColor((tagView.findViewById(R.id.tag_text_container)).
                    getBackground(), mTagBackgroundColor);
        }
    }

    private void hideRemoveButtonFromAllTagView() {
        if (!mTagList.isEmpty()) {
            for (View view : mTagList) {
                view.findViewById(R.id.remove_tag_image_view).setVisibility(View.GONE);
            }
        }
    }

    private boolean tagNotTaggedYet(String tagName) {
        boolean tagFound = true;
        if (!mTagList.isEmpty()) {
            for (View tagView : mTagList) {
                if (((TextView) tagView.
                        findViewById(R.id.tag_text_view)).
                        getText().toString().equals(tagName)) {
                    tagFound = false;
                    break;
                }
            }
        } else {
            tagFound = true;
        }
        return tagFound;
    }

    private void setColor(Drawable drawable, int color) {
        if (drawable instanceof ShapeDrawable) {
            ((ShapeDrawable) drawable).getPaint().setColor(color);
        } else if (drawable instanceof GradientDrawable) {
            ((GradientDrawable) drawable).setColor(color);
        } else if (drawable instanceof ColorDrawable) {
            ((ColorDrawable) drawable).setColor(color);
        } else if (drawable instanceof LayerDrawable) {
            LayerDrawable layerDrawable = (LayerDrawable) drawable;
            RotateDrawable rotateDrawable =
                    (RotateDrawable) layerDrawable.findDrawableByLayerId(R.id.carrot_shape_top);
            setColor(rotateDrawable.getDrawable(), color);
        } else if (drawable instanceof RotateDrawable) {
            setColor(((RotateDrawable) drawable).getDrawable(), color);
        }
    }

    private void setLayoutParamsToBeSetForRootLayout(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;

        int rootLayoutHeightWidth =
                (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, dpWidth, getResources().getDisplayMetrics());

        ViewGroup.LayoutParams params = mRoot.getLayoutParams();

        params.height = rootLayoutHeightWidth;
        params.width = rootLayoutHeightWidth;

        mRoot.setLayoutParams(params);
    }

    private int getXWhileAddingTag(Double x) {
        return (mRootWidth * x.intValue()) / 100;
    }

    private int getYWhileAddingTag(Double y) {
        return (mRootHeight * y.intValue()) / 100;
    }

    public void addTag(int x, int y, String tagText) {
        if (tagNotTaggedYet(tagText)) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);

            final View tagView = layoutInflater.
                    inflate(R.layout.view_for_tag, mRoot, false);
            final TextView tagTextView = tagView.findViewById(R.id.tag_text_view);

            final LinearLayout carrotTopContainer =
                    tagView.findViewById(R.id.carrot_top);
            final LinearLayout carrotLeftContainer =
                    tagView.findViewById(R.id.carrot_left);
            final LinearLayout carrotRightContainer =
                    tagView.findViewById(R.id.carrot_right);
            final LinearLayout carrotBottomContainer =
                    tagView.findViewById(R.id.carrot_bottom);

            final ImageView removeTagImageView =
                    tagView.findViewById(R.id.remove_tag_image_view);
            final LinearLayout textContainer =
                    tagView.findViewById(R.id.tag_text_container);

            if (mTagTextDrawable != null) {
                ViewCompat.setBackground(textContainer, mTagTextDrawable);
            }
            if (mCarrotTopDrawable != null) {
                ViewCompat.setBackground(carrotTopContainer, mCarrotTopDrawable);
            }
            if (mCarrotLeftDrawable != null) {
                ViewCompat.setBackground(carrotLeftContainer, mCarrotLeftDrawable);
            }
            if (mCarrotRightDrawable != null) {
                ViewCompat.setBackground(carrotRightContainer, mCarrotRightDrawable);
            }
            if (mCarrotBottomDrawable != null) {
                ViewCompat.setBackground(carrotBottomContainer, mCarrotBottomDrawable);
            }

            if (mShowAllCarrots) {
                tagView.findViewById(R.id.carrot_top).setVisibility(View.VISIBLE);
                tagView.findViewById(R.id.carrot_left).setVisibility(View.VISIBLE);
                tagView.findViewById(R.id.carrot_right).setVisibility(View.VISIBLE);
                tagView.findViewById(R.id.carrot_bottom).setVisibility(View.VISIBLE);
            }

            tagTextView.setText(tagText);
            setColorForTag(tagView);

            RelativeLayout.LayoutParams layoutParams =
                    new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.
                    setMargins(x - tagTextView.length() * 8,
                            y - tagTextView.length() * 2,
                            0,
                            0);
            tagView.setLayoutParams(layoutParams);

            mTagList.add(tagView);
            mRoot.addView(tagView);

            removeTagImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTagList.remove(tagView);
                    mRoot.removeView(tagView);
                }
            });

            tagView.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(final View view, MotionEvent event) {
                    if (mCanWeAddTags) {
                        mIsRootIsInTouch = false;

                        final int X = (int) event.getRawX();
                        final int Y = (int) event.getRawY();

                        int pointerCount = event.getPointerCount();

                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_DOWN:
                                RelativeLayout.LayoutParams layoutParams =
                                        (RelativeLayout.LayoutParams) view.getLayoutParams();

                                mPosX = X - layoutParams.leftMargin;
                                mPosY = Y - layoutParams.topMargin;

                                removeTagImageView.setVisibility(View.VISIBLE);
                                break;
                            case MotionEvent.ACTION_UP:
                                break;
                            case MotionEvent.ACTION_POINTER_DOWN:
                                break;
                            case MotionEvent.ACTION_POINTER_UP:
                                break;
                            case MotionEvent.ACTION_MOVE:
                                actionTagMove(tagView, pointerCount, X, Y);
                                break;
                        }
                        mRoot.invalidate();
                    }
                    return true;
                }
            });
        } else {
            Toast.makeText(mContext, "This user is already tagged", Toast.LENGTH_SHORT).show();
        }
    }

    public void addTagViewFromTagsToBeTagged(ArrayList<TagToBeTagged> tagsToBeTagged) {
        if (!mTagsAreAdded) {
            for (TagToBeTagged tagToBeTagged : tagsToBeTagged) {
                addTag(getXWhileAddingTag(tagToBeTagged.getX_co_ord()),
                        getYWhileAddingTag(tagToBeTagged.getY_co_ord()),
                        tagToBeTagged.getUnique_tag_id());
            }
            mTagsAreAdded = true;
        }
    }

    public TagImageView getTagImageView() {
        return mTagImageView;
    }

    public ArrayList<TagToBeTagged> getListOfTagsToBeTagged() {
        ArrayList<TagToBeTagged> tagsToBeTagged = new ArrayList<>();
        if (!mTagList.isEmpty()) {
            for (int i = 0; i < mTagList.size(); i++) {
                View view = mTagList.get(i);
                double x = view.getX();
                x = (x / mRootWidth) * 100;
                double y = view.getY();
                y = (y / mRootHeight) * 100;
                tagsToBeTagged.
                        add(new TagToBeTagged(((TextView) view.
                                findViewById(R.id.tag_text_view)).getText().toString(), x, y));
            }
        }
        return tagsToBeTagged;
    }

    public void setImageToBeTaggedEvent(TaggedImageEvent taggedImageEvent) {
        if (this.mTaggedImageEvent == null) {
            this.mTaggedImageEvent = taggedImageEvent;
        }
    }

    public void showTags() {
        if (!mTagList.isEmpty()) {
            for (View tagView : mTagList) {
                tagView.setVisibility(VISIBLE);
                tagView.startAnimation(mShowAnimation);
            }
        }
    }

    public void hideTags() {
        if (!mTagList.isEmpty()) {
            for (View tagView : mTagList) {
                tagView.startAnimation(mHideAnimation);
                tagView.setVisibility(GONE);
            }
        }
    }

    public void removeTags() {
        if (!mTagList.isEmpty()) {
            for (View tagView : mTagList) {
                mRoot.removeView(tagView);
            }
            mTagList.clear();
        }
    }

    public int getRootWidth() {
        return mRootWidth;
    }

    public void setRootWidth(int mRootWidth) {
        this.mRootWidth = mRootWidth;
    }

    public int getRootHeight() {
        return mRootHeight;
    }

    public void setRootHeight(int mRootHeight) {
        this.mRootHeight = mRootHeight;
    }

    public int getTagTextColor() {
        return mTagTextColor;
    }

    public int getTagBackgroundColor() {
        return mTagBackgroundColor;
    }

    public int getCarrotTopBackGroundColor() {
        return mCarrotTopBackGroundColor;
    }

    public int getCarrotLeftBackGroundColor() {
        return mCarrotLeftBackGroundColor;
    }

    public int getCarrotRightBackGroundColor() {
        return mCarrotRightBackGroundColor;
    }

    public int getCarrotBottomBackGroundColor() {
        return mCarrotBottomBackGroundColor;
    }

    public Drawable getTagTextDrawable() {
        return mTagTextDrawable;
    }

    public Drawable getCarrotTopDrawable() {
        return mCarrotTopDrawable;
    }

    public Drawable getCarrotLeftDrawable() {
        return mCarrotLeftDrawable;
    }

    public Drawable getCarrotRightDrawable() {
        return mCarrotRightDrawable;
    }

    public Drawable getCarrotBottomDrawable() {
        return mCarrotBottomDrawable;
    }

    public boolean canWeAddTags() {
        return mCanWeAddTags;
    }

    public void setCanWeAddTags(boolean mCanWeAddTags) {
        this.mCanWeAddTags = mCanWeAddTags;
    }

    public boolean isShowAllCarrots() {
        return mShowAllCarrots;
    }
}

