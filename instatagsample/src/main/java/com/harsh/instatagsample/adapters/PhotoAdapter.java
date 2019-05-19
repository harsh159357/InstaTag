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

package com.harsh.instatagsample.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.harsh.instatag.InstaTag;
import com.harsh.instatagsample.R;
import com.harsh.instatagsample.interfaces.PhotoClickListener;
import com.harsh.instatagsample.models.Photo;

import java.util.ArrayList;
import java.util.HashSet;

public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final ArrayList<Photo> photos;
    private final HashSet<String> tagsShowHideHelper;
    private final PhotoClickListener photoClickListener;
    private Animation tagShowAnimation, tagHideAnimation;
    private int carrotTopColor, tagBackgroundColor, tagTextColor, likeColor;

    private RequestOptions requestOptions =
            new RequestOptions()
                    .placeholder(0)
                    .fallback(0)
                    .centerCrop()
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);


    public PhotoAdapter(ArrayList<Photo> photos, Context context, PhotoClickListener photoClickListener) {
        this.photos = photos;
        this.context = context;
        this.photoClickListener = photoClickListener;
        this.tagsShowHideHelper = new HashSet<>();
    }

    public PhotoAdapter(ArrayList<Photo> photos, Context context, PhotoClickListener photoClickListener,
                        int showAnim, int hideAnim,
                        int carrotTopColor, int tagBackgroundColor, int tagTextColor, int likeColor) {
        this.photos = photos;
        this.context = context;
        this.photoClickListener = photoClickListener;
        this.tagsShowHideHelper = new HashSet<>();
        this.tagShowAnimation = AnimationUtils.loadAnimation(context, showAnim);
        this.tagHideAnimation = AnimationUtils.loadAnimation(context, hideAnim);
        this.carrotTopColor = carrotTopColor;
        this.tagBackgroundColor = tagBackgroundColor;
        this.tagTextColor = tagTextColor;
        this.likeColor = likeColor;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View defaultView = inflater.inflate(R.layout.item_photo, viewGroup, false);
        viewHolder = new TaggedPhotoViewHolder(defaultView);

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return -1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        TaggedPhotoViewHolder defaultViewHolder = (TaggedPhotoViewHolder) viewHolder;

        defaultViewHolder.instaTag.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        defaultViewHolder.instaTag.setRootWidth(defaultViewHolder.instaTag.getMeasuredWidth());
        defaultViewHolder.instaTag.setRootHeight(defaultViewHolder.instaTag.getMeasuredHeight());

        configureTaggedPhotoViewHolder(defaultViewHolder, position);
    }

    private void configureTaggedPhotoViewHolder(TaggedPhotoViewHolder taggedPhotoViewHolder,
                                                int position) {
        Photo photo = photos.get(position);

        Glide
                .with(context)
                .load(Uri.parse(photo.getImageUri()))
                .apply(requestOptions)
                .into(taggedPhotoViewHolder.instaTag.getTagImageView());

        if (tagShowAnimation != null) {
            taggedPhotoViewHolder.instaTag.setTagShowAnimation(tagShowAnimation);
        }

        if (tagHideAnimation != null) {
            taggedPhotoViewHolder.instaTag.setTagHideAnimation(tagHideAnimation);
        }

        if (carrotTopColor != 0) {
            taggedPhotoViewHolder.instaTag.setCarrotTopBackGroundColor(carrotTopColor);
        }

        if (tagBackgroundColor != 0) {
            taggedPhotoViewHolder.instaTag.setTagBackgroundColor(tagBackgroundColor);
        }

        if (tagTextColor != 0) {
            taggedPhotoViewHolder.instaTag.setTagTextColor(tagTextColor);
        }

        if (likeColor != 0) {
            taggedPhotoViewHolder.instaTag.setLikeColor(likeColor);
        }

        taggedPhotoViewHolder.instaTag.
                addTagViewFromTags(photo.getTags());
        if (tagsShowHideHelper.contains(photos.get(taggedPhotoViewHolder.getAdapterPosition()).getId())) {
            taggedPhotoViewHolder.instaTag.showTags();
        } else {
            taggedPhotoViewHolder.instaTag.hideTags();
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    private class TaggedPhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final InstaTag instaTag;
        final ImageView tagHeart;
        final ImageView tagIndicator;

        TaggedPhotoViewHolder(View view) {
            super(view);
            instaTag = view.findViewById(R.id.insta_tag_photo);
            tagHeart = view.findViewById(R.id.tag_heart);
            tagIndicator = view.findViewById(R.id.tag_indicator);
            tagHeart.setOnClickListener(this);
            tagIndicator.setOnClickListener(this);
            instaTag.setTaggedPhotoEvent(photoEvent);
        }

        private InstaTag.PhotoEvent photoEvent = new InstaTag.PhotoEvent() {
            @Override
            public void singleTapConfirmedAndRootIsInTouch(int x, int y) {

            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                instaTag.animateLike();
                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                instaTag.animateLike();
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                instaTag.animateLike();
            }
        };

        @Override
        public void onClick(View view) {
            Photo photo = photos.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.insta_tag_photo:
                    photoClickListener.onPhotoClick(photo, getAdapterPosition());
                    break;
                case R.id.tag_heart:
                    instaTag.animateLike();
                    break;
                case R.id.tag_indicator:
                    if (!tagsShowHideHelper.contains(photo.getId())) {
                        instaTag.showTags();
                        tagsShowHideHelper.add(photo.getId());
                    } else {
                        instaTag.hideTags();
                        tagsShowHideHelper.remove(photo.getId());
                    }
                    break;
            }
        }
    }
}