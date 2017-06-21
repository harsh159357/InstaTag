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

package com.harsh.instatagsample;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.harsh.instatag.InstaTag;

import java.util.ArrayList;

public class TaggedPhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM_DATA_TYPE_1 = 1;
    private ArrayList<Object> objectArrayList;
    private Context context;
    private TaggedPhotoAdapterClickListener taggedPhotoAdapterClickListener;
    private ArrayList<String> taggedPhotoTagsVisibilityStatusHelper;

    public TaggedPhotoAdapter(ArrayList<Object> objectArrayList, Context context, TaggedPhotoAdapterClickListener taggedPhotoAdapterClickListener) {
        this.objectArrayList = objectArrayList;
        this.context = context;
        this.taggedPhotoAdapterClickListener = taggedPhotoAdapterClickListener;
        this.taggedPhotoTagsVisibilityStatusHelper = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case VIEW_ITEM_DATA_TYPE_1:
                View dataView1 = inflater.inflate(R.layout.item_row_tagged_photo, viewGroup, false);
                viewHolder = new TaggedPhotoViewHolder(dataView1, taggedPhotoAdapterClickListener);
                break;
            default:
                View defaultView = inflater.inflate(R.layout.item_row_tagged_photo, viewGroup, false);
                viewHolder = new TaggedPhotoViewHolder(defaultView, taggedPhotoAdapterClickListener);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (objectArrayList.get(position) instanceof TaggedPhoto) {
            return VIEW_ITEM_DATA_TYPE_1;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case VIEW_ITEM_DATA_TYPE_1:
                TaggedPhotoViewHolder taggedPhotoViewHolder = (TaggedPhotoViewHolder) viewHolder;
                taggedPhotoViewHolder.instaTagTaggedPhoto.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                taggedPhotoViewHolder.instaTagTaggedPhoto.setInstaRootWidth(taggedPhotoViewHolder.instaTagTaggedPhoto.getMeasuredWidth());
                taggedPhotoViewHolder.instaTagTaggedPhoto.setInstaRootHeight(taggedPhotoViewHolder.instaTagTaggedPhoto.getMeasuredHeight());
                configureTaggedPhotoViewHolder(taggedPhotoViewHolder, position);
                if (taggedPhotoTagsVisibilityStatusHelper.contains(((TaggedPhoto)
                        objectArrayList.get(taggedPhotoViewHolder.getAdapterPosition())).getId())) {
                    taggedPhotoViewHolder.instaTagTaggedPhoto.showTags();
                } else {
                    taggedPhotoViewHolder.instaTagTaggedPhoto.hideTags();
                }
                break;
            default:
                TaggedPhotoViewHolder defaultViewHolder = (TaggedPhotoViewHolder) viewHolder;
                defaultViewHolder.instaTagTaggedPhoto.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                defaultViewHolder.instaTagTaggedPhoto.setInstaRootWidth(defaultViewHolder.instaTagTaggedPhoto.getMeasuredWidth());
                defaultViewHolder.instaTagTaggedPhoto.setInstaRootHeight(defaultViewHolder.instaTagTaggedPhoto.getMeasuredHeight());
                configureTaggedPhotoViewHolder(defaultViewHolder, position);
                break;
        }
    }

    private void configureTaggedPhotoViewHolder(TaggedPhotoViewHolder taggedPhotoViewHolder, int position) {
        TaggedPhoto taggedPhoto = (TaggedPhoto) objectArrayList.get(position);
        Glide.with(context).load(Uri.parse(taggedPhoto.getImageURI()))
                .centerCrop()
                .placeholder(0)
                .fallback(0)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(taggedPhotoViewHolder.instaTagTaggedPhoto.getInstaTagImageView());
        taggedPhotoViewHolder.instaTagTaggedPhoto.addTagViewFromUserToBeTaggedList(taggedPhoto.getUserToBeTaggeds());
    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }

    private class TaggedPhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        InstaTag instaTagTaggedPhoto;
        ImageView tagIndicator;
        TaggedPhotoAdapterClickListener taggedPhotoAdapterClickListener;

        TaggedPhotoViewHolder(View view, TaggedPhotoAdapterClickListener taggedPhotoAdapterClickListener) {
            super(view);
            this.taggedPhotoAdapterClickListener = taggedPhotoAdapterClickListener;
            instaTagTaggedPhoto = (InstaTag) view.findViewById(R.id.insta_tag_tagged_photo);
            tagIndicator = (ImageView) view.findViewById(R.id.tag_indicator);
            tagIndicator.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            TaggedPhoto taggedPhoto = (TaggedPhoto) objectArrayList.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.insta_tag_tagged_photo:
                    taggedPhotoAdapterClickListener.onTaggedPhotoClick(taggedPhoto, getAdapterPosition());
                    break;
                case R.id.tag_indicator:
                    if (!taggedPhotoTagsVisibilityStatusHelper.contains(taggedPhoto.getId())) {
                        instaTagTaggedPhoto.showTags();
                        taggedPhotoTagsVisibilityStatusHelper.add(taggedPhoto.getId());
                    } else {
                        instaTagTaggedPhoto.hideTags();
                        taggedPhotoTagsVisibilityStatusHelper.remove(taggedPhoto.getId());
                    }
                    break;
            }
        }
    }
}