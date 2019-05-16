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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
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
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class InstaTag extends RelativeLayout {

    private int mRootWidth;
    private int mRootHeight;

    private Context mContext;

    private int mTagTextColor;
    private int mTagBackgroundColor;
    private int mCarrotTopBackGroundColor;

    private Drawable mTagTextDrawable;
    private Drawable mCarrotTopDrawable;

    private boolean tagsAreAdded;
    private boolean canWeAddTags;
    private boolean mIsRootIsInTouch = true;

    private Animation mShowAnimation;
    private Animation mHideAnimation;

    private GestureDetector mGestureDetector;

    private PhotoEvent mPhotoEvent;

    private ViewGroup mRoot;
    private ImageView mLikeImage;
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
            if (canWeAddTags) {
                if (mIsRootIsInTouch) {
                    int x = (int) e.getX();
                    int y = (int) e.getY();
                    switch (e.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_MOVE:
                        case MotionEvent.ACTION_UP:
                    }
                    if (mPhotoEvent != null) {
                        mPhotoEvent.singleTapConfirmedAndRootIsInTouch(x, y);
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
            if (mPhotoEvent != null) {
                return mPhotoEvent.onDoubleTap(e);
            } else {
                return true;
            }
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            if (mPhotoEvent != null) {
                return mPhotoEvent.onDoubleTapEvent(e);
            } else {
                return true;
            }
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (mPhotoEvent != null) {
                mPhotoEvent.onLongPress(e);
            }
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
    }

    public interface PhotoEvent {
        void singleTapConfirmedAndRootIsInTouch(int x, int y);

        boolean onDoubleTap(MotionEvent e);

        boolean onDoubleTapEvent(MotionEvent e);

        void onLongPress(MotionEvent e);
    }

    public InstaTag(Context context) {
        super(context);
        initViewWithId(context, null);
    }

    public InstaTag(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            initView(attrs, context);
        } else {
            initView(attrs, context);
        }
    }

    private void initViewWithId(Context context, TypedArray obtainStyledAttributes) {
        mContext = context;

        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.tag_root, this);

        mRoot = findViewById(R.id.tag_root);
        mTagImageView = findViewById(R.id.tag_image_view);
        mLikeImage = new ImageView(context);

        int likeColor, likeSrc, likeSize;
        if (obtainStyledAttributes != null) {
            likeColor = obtainStyledAttributes.getColor(R.styleable.InstaTag_likeColor,
                    ContextCompat.getColor(context, R.color.colorAccent));
            likeSrc = obtainStyledAttributes.getResourceId(R.styleable.InstaTag_likeSrc,
                    R.drawable.ic_like);
            likeSize = obtainStyledAttributes
                    .getDimensionPixelSize(R.styleable.InstaTag_likeSize,
                            getResources().getDimensionPixelSize(R.dimen.dp256));
        } else {
            likeColor = ContextCompat.getColor(context, R.color.colorAccent);
            likeSrc = R.drawable.ic_like;
            likeSize = getResources().getDimensionPixelSize(R.dimen.dp256);
        }
        LayoutParams heartParams = new LayoutParams(likeSize, likeSize);
        heartParams.addRule(CENTER_IN_PARENT, TRUE);

        mLikeImage.setLayoutParams(heartParams);
        mLikeImage.setVisibility(GONE);
        mLikeImage.setImageResource(likeSrc);
        mLikeImage.setColorFilter(likeColor);

        setRootLayoutParams(mContext);
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

        canWeAddTags = obtainStyledAttributes.
                getBoolean(R.styleable.InstaTag_canWeAddTags, false);

        mTagTextColor = obtainStyledAttributes.
                getColor(R.styleable.InstaTag_instaTextColor, Constants.TAG_TEXT_COLOR);

        int overrideDefaultColor = obtainStyledAttributes.
                getColor(R.styleable.InstaTag_overrideDefaultColor, Constants.DEFAULT_COLOR);

        if (overrideDefaultColor == Constants.DEFAULT_COLOR) {
            mCarrotTopBackGroundColor = obtainStyledAttributes.
                    getColor(R.styleable.InstaTag_carrotTopColor, Constants.DEFAULT_COLOR);
            mTagBackgroundColor = obtainStyledAttributes.
                    getColor(R.styleable.InstaTag_instaBackgroundColor, Constants.DEFAULT_COLOR);
        } else {
            mTagBackgroundColor = overrideDefaultColor;
            mCarrotTopBackGroundColor = overrideDefaultColor;
        }

        mHideAnimation = AnimationUtils.loadAnimation(context, obtainStyledAttributes.
                getResourceId(R.styleable.InstaTag_hideAnimation, R.anim.zoom_out));

        mShowAnimation = AnimationUtils.loadAnimation(context, obtainStyledAttributes.
                getResourceId(R.styleable.InstaTag_showAnimation, R.anim.zoom_in));

        initViewWithId(context, obtainStyledAttributes);
        obtainStyledAttributes.recycle();
    }

    private void setColorForTag(View tagView) {
        ((TextView) tagView.findViewById(R.id.tag_text_view)).setTextColor(mTagTextColor);

        if (mCarrotTopDrawable == null) {
            setColor((tagView.findViewById(R.id.carrot_top)).
                    getBackground(), mCarrotTopBackGroundColor);
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

    private boolean isTagged(String tagName) {
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

    private void setRootLayoutParams(Context context) {
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

    private float getXCoOrdForTag(float x) {
        return (mRootWidth * x) / 100;
    }

    private float getYCoOrdForTag(float y) {
        return (mRootHeight * y) / 100;
    }

    public void addTag(int x, int y, String tagText) {
        if (isTagged(tagText)) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);

            final View tagView = layoutInflater.
                    inflate(R.layout.view_for_tag, mRoot, false);
            final TextView tagTextView = tagView.findViewById(R.id.tag_text_view);

            final LinearLayout carrotTopContainer =
                    tagView.findViewById(R.id.carrot_top);

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

            TagOnTouchListener tagOnTouchListener = new TagOnTouchListener(tagView);
            tagOnTouchListener.setOnDragActionListener((new TagOnTouchListener.OnDragActionListener() {
                @Override
                public void onDragStart(View view) {
                    if (canWeAddTags) {
                        mIsRootIsInTouch = false;
                        removeTagImageView.setVisibility(VISIBLE);
                    }
                }

                @Override
                public void onDragEnd(View view) {
                }
            }));
            if (canWeAddTags)
                tagView.setOnTouchListener(tagOnTouchListener);
        } else {
            Toast.makeText(mContext, "This user is already tagged", Toast.LENGTH_SHORT).show();
        }
    }

    public void addTagViewFromTags(ArrayList<Tag> tags) {
        if (!tagsAreAdded) {
            for (Tag tag : tags) {
                addTag(tag);
            }
            tagsAreAdded = true;
        }
    }

    private void addTag(Tag tag) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        float x = getXCoOrdForTag(tag.getX_co_ord());
        float y = getYCoOrdForTag(tag.getY_co_ord());

        final View tagView = layoutInflater.
                inflate(R.layout.view_for_tag, mRoot, false);
        final TextView tagTextView = tagView.findViewById(R.id.tag_text_view);

        final LinearLayout carrotTopContainer =
                tagView.findViewById(R.id.carrot_top);

        final LinearLayout textContainer =
                tagView.findViewById(R.id.tag_text_container);

        if (mTagTextDrawable != null) {
            ViewCompat.setBackground(textContainer, mTagTextDrawable);
        }
        if (mCarrotTopDrawable != null) {
            ViewCompat.setBackground(carrotTopContainer, mCarrotTopDrawable);
        }

        tagTextView.setText(tag.getUnique_tag_id());
        setColorForTag(tagView);

        tagView.setX(x);
        tagView.setY(y);

        mTagList.add(tagView);
        mRoot.addView(tagView);
    }

    public TagImageView getTagImageView() {
        return mTagImageView;
    }

    public ArrayList<Tag> getTags() {
        ArrayList<Tag> tags = new ArrayList<>();
        if (!mTagList.isEmpty()) {
            for (int i = 0; i < mTagList.size(); i++) {
                View view = mTagList.get(i);
                float x = view.getX();
                x = (x / mRootWidth) * 100;
                float y = view.getY();
                y = (y / mRootHeight) * 100;
                tags.
                        add(new Tag(((TextView) view.
                                findViewById(R.id.tag_text_view)).getText().toString(), x, y));
            }
        }
        return tags;
    }

    public void setTaggedPhotoEvent(PhotoEvent photoEvent) {
        if (this.mPhotoEvent == null) {
            this.mPhotoEvent = photoEvent;
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

    public void animateLike() {
        try {
            mRoot.addView(mLikeImage);
        } catch (Exception ignored) {
            // Illegal Exception is being thrown here while adding mLikeImage
        }
        mLikeImage.setVisibility(View.VISIBLE);
        mLikeImage.setScaleY(0f);
        mLikeImage.setScaleX(0f);

        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator likeScaleUpYAnimator = ObjectAnimator
                .ofFloat(mLikeImage, ImageView.SCALE_Y, 0f, 1f);
        likeScaleUpYAnimator.setDuration(400);
        likeScaleUpYAnimator.setInterpolator(new OvershootInterpolator());

        ObjectAnimator likeScaleUpXAnimator = ObjectAnimator
                .ofFloat(mLikeImage, ImageView.SCALE_X, 0f, 1f);
        likeScaleUpXAnimator.setDuration(400);
        likeScaleUpXAnimator.setInterpolator(new OvershootInterpolator());

        ObjectAnimator likeScaleDownYAnimator = ObjectAnimator
                .ofFloat(mLikeImage, ImageView.SCALE_Y, 1f, 0f);
        likeScaleDownYAnimator.setDuration(100);

        ObjectAnimator likeScaleDownXAnimator = ObjectAnimator
                .ofFloat(mLikeImage, ImageView.SCALE_X, 1f, 0f);
        likeScaleDownXAnimator.setDuration(100);

        animatorSet.playTogether(likeScaleUpXAnimator,
                likeScaleUpYAnimator);

        animatorSet.play(likeScaleDownXAnimator).
                with(likeScaleDownYAnimator).
                after(800);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLikeImage.setVisibility(View.GONE);
                mRoot.removeView(mLikeImage);
            }
        });

        animatorSet.start();
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

    public Drawable getTagTextDrawable() {
        return mTagTextDrawable;
    }

    public Drawable getCarrotTopDrawable() {
        return mCarrotTopDrawable;
    }

    public boolean canWeAddTags() {
        return canWeAddTags;
    }

    public void setCanWeAddTags(boolean mCanWeAddTags) {
        this.canWeAddTags = mCanWeAddTags;
    }

    public void setLikeResource(@DrawableRes int resource) {
        mLikeImage.setImageResource(resource);
    }

    public void setLikeDrawable(Drawable drawable) {
        mLikeImage.setImageDrawable(drawable);
    }

    public void setLikeColor(@ColorInt int color) {
        mLikeImage.setColorFilter(color);
    }

}

