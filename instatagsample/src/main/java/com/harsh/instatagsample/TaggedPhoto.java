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

import android.os.Parcel;
import android.os.Parcelable;

import com.harsh.instatag.UserToBeTagged;

import java.util.ArrayList;

public class TaggedPhoto implements Parcelable {
    private String id;
    private String imageURI;
    private ArrayList<UserToBeTagged> userToBeTaggeds;

    public TaggedPhoto() {
    }

    public TaggedPhoto(String id, String imageURI, ArrayList<UserToBeTagged> userToBeTaggeds) {
        this.id = id;
        this.imageURI = imageURI;
        this.userToBeTaggeds = userToBeTaggeds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public ArrayList<UserToBeTagged> getUserToBeTaggeds() {
        return userToBeTaggeds;
    }

    public void setUserToBeTaggeds(ArrayList<UserToBeTagged> userToBeTaggeds) {
        this.userToBeTaggeds = userToBeTaggeds;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.imageURI);
        dest.writeTypedList(this.userToBeTaggeds);
    }

    protected TaggedPhoto(Parcel in) {
        this.id = in.readString();
        this.imageURI = in.readString();
        this.userToBeTaggeds = in.createTypedArrayList(UserToBeTagged.CREATOR);
    }

    public static final Creator<TaggedPhoto> CREATOR = new Creator<TaggedPhoto>() {
        @Override
        public TaggedPhoto createFromParcel(Parcel source) {
            return new TaggedPhoto(source);
        }

        @Override
        public TaggedPhoto[] newArray(int size) {
            return new TaggedPhoto[size];
        }
    };
}
