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
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.harsh.instatag.InstaTag;
import com.harsh.instatagsample.R;
import com.harsh.instatagsample.interfaces.TaggedPhotoClickListener;
import com.harsh.instatagsample.models.TaggedPhoto;

import java.util.ArrayList;

public class TaggedPhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM_DATA_TYPE_1 = 1;
    private final ArrayList<Object> mObjectArrayList;
    private final Context mContext;
    private final TaggedPhotoClickListener mTaggedPhotoClickListener;
    private final ArrayList<String> mTaggedPhotoTagsVisibilityStatusHelper;
    private RequestOptions requestOptions =
            new RequestOptions()
                    .placeholder(0)
                    .fallback(0)
                    .centerCrop()
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);


    public TaggedPhotoAdapter(ArrayList<Object> mObjectArrayList,
                              Context mContext,
                              TaggedPhotoClickListener mTaggedPhotoClickListener) {
        this.mObjectArrayList = mObjectArrayList;
        this.mContext = mContext;
        this.mTaggedPhotoClickListener = mTaggedPhotoClickListener;
        this.mTaggedPhotoTagsVisibilityStatusHelper = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case VIEW_ITEM_DATA_TYPE_1:
                View dataView1 = inflater.inflate(R.layout.item_row_tagged_photo,
                        viewGroup, false);
                viewHolder = new TaggedPhotoViewHolder(dataView1, mTaggedPhotoClickListener);
                break;
            default:
                View defaultView = inflater.inflate(R.layout.item_row_tagged_photo,
                        viewGroup, false);
                viewHolder = new TaggedPhotoViewHolder(defaultView, mTaggedPhotoClickListener);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (mObjectArrayList.get(position) instanceof TaggedPhoto) {
            return VIEW_ITEM_DATA_TYPE_1;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case VIEW_ITEM_DATA_TYPE_1:
                TaggedPhotoViewHolder taggedPhotoViewHolder = (TaggedPhotoViewHolder) viewHolder;
                taggedPhotoViewHolder.instaTagTaggedPhoto.
                        measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                taggedPhotoViewHolder.instaTagTaggedPhoto.
                        setRootWidth(taggedPhotoViewHolder.instaTagTaggedPhoto.getMeasuredWidth());
                taggedPhotoViewHolder.instaTagTaggedPhoto.
                        setRootHeight(taggedPhotoViewHolder.instaTagTaggedPhoto.getMeasuredHeight());
                configureTaggedPhotoViewHolder(taggedPhotoViewHolder, position);
                break;

            default:
                TaggedPhotoViewHolder defaultViewHolder = (TaggedPhotoViewHolder) viewHolder;
                defaultViewHolder.instaTagTaggedPhoto.
                        measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                defaultViewHolder.instaTagTaggedPhoto.
                        setRootWidth(defaultViewHolder.instaTagTaggedPhoto.getMeasuredWidth());
                defaultViewHolder.instaTagTaggedPhoto.
                        setRootHeight(defaultViewHolder.instaTagTaggedPhoto.getMeasuredHeight());
                configureTaggedPhotoViewHolder(defaultViewHolder, position);
                break;
        }
    }

    private void configureTaggedPhotoViewHolder(TaggedPhotoViewHolder taggedPhotoViewHolder,
                                                int position) {
        TaggedPhoto taggedPhoto = (TaggedPhoto) mObjectArrayList.get(position);
        Glide
                .with(mContext)
                .load(Uri.parse(taggedPhoto.getImageUri()))
                .apply(requestOptions)
                .into(taggedPhotoViewHolder.instaTagTaggedPhoto.getTagImageView());

        taggedPhotoViewHolder.instaTagTaggedPhoto.
                addTagViewFromTagsToBeTagged(taggedPhoto.getTagToBeTaggeds());
        if (mTaggedPhotoTagsVisibilityStatusHelper.contains(((TaggedPhoto)
                mObjectArrayList.get(taggedPhotoViewHolder.getAdapterPosition())).getId())) {
            taggedPhotoViewHolder.instaTagTaggedPhoto.showTags();
        } else {
            taggedPhotoViewHolder.instaTagTaggedPhoto.hideTags();
        }
    }

    @Override
    public int getItemCount() {
        return mObjectArrayList.size();
    }

    private class TaggedPhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final InstaTag instaTagTaggedPhoto;
        final ImageView tagHeart;
        final ImageView tagIndicator;
        final TaggedPhotoClickListener taggedPhotoClickListener;

        TaggedPhotoViewHolder(View view, TaggedPhotoClickListener taggedPhotoClickListener) {
            super(view);
            this.taggedPhotoClickListener = taggedPhotoClickListener;
            instaTagTaggedPhoto = view.findViewById(R.id.insta_tag_tagged_photo);
            tagHeart = view.findViewById(R.id.tag_heart);
            tagIndicator = view.findViewById(R.id.tag_indicator);
            tagHeart.setOnClickListener(this);
            tagIndicator.setOnClickListener(this);
            instaTagTaggedPhoto.setImageToBeTaggedEvent(taggedImageEvent);
        }

        private InstaTag.TaggedImageEvent taggedImageEvent = new InstaTag.TaggedImageEvent() {
            @Override
            public void singleTapConfirmedAndRootIsInTouch(int x, int y) {

            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                instaTagTaggedPhoto.animateLike();
                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                instaTagTaggedPhoto.animateLike();
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                instaTagTaggedPhoto.animateLike();
            }
        };

        @Override
        public void onClick(View view) {
            TaggedPhoto taggedPhoto = (TaggedPhoto) mObjectArrayList.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.insta_tag_tagged_photo:
                    taggedPhotoClickListener.onTaggedPhotoClick(taggedPhoto, getAdapterPosition());
                    break;
                case R.id.tag_heart:
                    instaTagTaggedPhoto.animateLike();
                    break;
                case R.id.tag_indicator:
                    if (!mTaggedPhotoTagsVisibilityStatusHelper.contains(taggedPhoto.getId())) {
                        instaTagTaggedPhoto.showTags();
                        mTaggedPhotoTagsVisibilityStatusHelper.add(taggedPhoto.getId());
                    } else {
                        instaTagTaggedPhoto.hideTags();
                        mTaggedPhotoTagsVisibilityStatusHelper.remove(taggedPhoto.getId());
                    }
                    break;
            }
        }
    }
}