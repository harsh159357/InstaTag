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

    private final ArrayList<TaggedPhoto> taggedPhotos;
    private final Context context;
    private final TaggedPhotoClickListener taggedPhotoClickListener;
    private final ArrayList<String> tagsShowHideHelper;
    private RequestOptions requestOptions =
            new RequestOptions()
                    .placeholder(0)
                    .fallback(0)
                    .centerCrop()
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);


    public TaggedPhotoAdapter(ArrayList<TaggedPhoto> taggedPhotos,
                              Context context,
                              TaggedPhotoClickListener taggedPhotoClickListener) {
        this.taggedPhotos = taggedPhotos;
        this.context = context;
        this.taggedPhotoClickListener = taggedPhotoClickListener;
        this.tagsShowHideHelper = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View defaultView = inflater.inflate(R.layout.item_row_tagged_photo,
                viewGroup, false);
        viewHolder = new TaggedPhotoViewHolder(defaultView, taggedPhotoClickListener);

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return -1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        TaggedPhotoViewHolder defaultViewHolder = (TaggedPhotoViewHolder) viewHolder;
        defaultViewHolder.instaTag.
                measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        defaultViewHolder.instaTag.
                setRootWidth(defaultViewHolder.instaTag.getMeasuredWidth());
        defaultViewHolder.instaTag.
                setRootHeight(defaultViewHolder.instaTag.getMeasuredHeight());
        configureTaggedPhotoViewHolder(defaultViewHolder, position);
    }

    private void configureTaggedPhotoViewHolder(TaggedPhotoViewHolder taggedPhotoViewHolder,
                                                int position) {
        TaggedPhoto taggedPhoto = taggedPhotos.get(position);

        Glide
                .with(context)
                .load(Uri.parse(taggedPhoto.getImageUri()))
                .apply(requestOptions)
                .into(taggedPhotoViewHolder.instaTag.getTagImageView());

        taggedPhotoViewHolder.instaTag.
                addTagViewFromTagsToBeTagged(taggedPhoto.getTags());
        if (tagsShowHideHelper.contains(
                taggedPhotos.get(taggedPhotoViewHolder.getAdapterPosition()).getId())) {
            taggedPhotoViewHolder.instaTag.showTags();
        } else {
            taggedPhotoViewHolder.instaTag.hideTags();
        }
    }

    @Override
    public int getItemCount() {
        return taggedPhotos.size();
    }

    private class TaggedPhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final InstaTag instaTag;
        final ImageView tagHeart;
        final ImageView tagIndicator;
        final TaggedPhotoClickListener taggedPhotoClickListener;

        TaggedPhotoViewHolder(View view, TaggedPhotoClickListener taggedPhotoClickListener) {
            super(view);
            this.taggedPhotoClickListener = taggedPhotoClickListener;
            instaTag = view.findViewById(R.id.insta_tag_tagged_photo);
            tagHeart = view.findViewById(R.id.tag_heart);
            tagIndicator = view.findViewById(R.id.tag_indicator);
            tagHeart.setOnClickListener(this);
            tagIndicator.setOnClickListener(this);
            instaTag.setImageToBeTaggedEvent(taggedImageEvent);
        }

        private InstaTag.TaggedImageEvent taggedImageEvent = new InstaTag.TaggedImageEvent() {
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
            TaggedPhoto taggedPhoto = (TaggedPhoto) taggedPhotos.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.insta_tag_tagged_photo:
                    taggedPhotoClickListener.onTaggedPhotoClick(taggedPhoto, getAdapterPosition());
                    break;
                case R.id.tag_heart:
                    instaTag.animateLike();
                    break;
                case R.id.tag_indicator:
                    if (!tagsShowHideHelper.contains(taggedPhoto.getId())) {
                        instaTag.showTags();
                        tagsShowHideHelper.add(taggedPhoto.getId());
                    } else {
                        instaTag.hideTags();
                        tagsShowHideHelper.remove(taggedPhoto.getId());
                    }
                    break;
            }
        }
    }
}