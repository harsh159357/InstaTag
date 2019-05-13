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
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.harsh.instatagsample.R;
import com.harsh.instatagsample.interfaces.UserClickListener;
import com.harsh.instatagsample.models.User;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final Context context;
    private final List<User> userList;
    private final UserClickListener userClickListener;

    private RequestOptions requestOptions =
            new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_default_avatar)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_default_avatar);

    public UserAdapter(List<User> users, Context context, UserClickListener userClickListener) {
        this.userList = users;
        this.context = context;
        this.userClickListener = userClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);

        holder.user = user;
        holder.txtUserName.setText(user.getUserName());
        holder.txtFullName.setText(user.getFullName());

        Glide
                .with(context)
                .load(user.getUrl())
                .apply(requestOptions.transforms(new CircleCrop()))
                .into(holder.imgProfile);
    }

    @Override
    public int getItemCount() {
        if (userList != null) {
            return userList.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final RelativeLayout relativeLayout;
        private final ImageView imgProfile;
        private final TextView txtUserName;
        private final TextView txtFullName;
        private User user;

        ViewHolder(View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.root_user);
            imgProfile = itemView.findViewById(R.id.img_profile);
            txtUserName = itemView.findViewById(R.id.txt_user_name);
            txtFullName = itemView.findViewById(R.id.txt_full_name);
            relativeLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.root_user) {
                userClickListener.onUserClick(user, getAdapterPosition());
            }
        }
    }
}

