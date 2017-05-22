package com.harsh.instatagsample;

import android.os.Parcel;
import android.os.Parcelable;

public class SomeOne implements Parcelable {
    private String userName;
    private String fullName;
    private String url;

    public SomeOne() {
    }

    public SomeOne(String userName, String fullName, String url) {
        this.userName = userName;
        this.fullName = fullName;
        this.url = url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userName);
        dest.writeString(this.fullName);
        dest.writeString(this.url);
    }

    protected SomeOne(Parcel in) {
        this.userName = in.readString();
        this.fullName = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<SomeOne> CREATOR = new Parcelable.Creator<SomeOne>() {
        @Override
        public SomeOne createFromParcel(Parcel source) {
            return new SomeOne(source);
        }

        @Override
        public SomeOne[] newArray(int size) {
            return new SomeOne[size];
        }
    };

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static Creator<SomeOne> getCREATOR() {
        return CREATOR;
    }
}

