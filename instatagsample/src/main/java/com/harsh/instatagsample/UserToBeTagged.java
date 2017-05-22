package com.harsh.instatagsample;

import java.io.Serializable;

public class UserToBeTagged implements Serializable {
    private String user_unique_id;
    private Double x_co_ord;
    private Double y_co_ord;

    public UserToBeTagged(String user_unique_id, Double x_co_ord, Double y_co_ord) {
        this.user_unique_id = user_unique_id;
        this.x_co_ord = x_co_ord;
        this.y_co_ord = y_co_ord;

    }

    public String getUser_unique_id() {
        return user_unique_id;
    }

    public void setUser_unique_id(String user_unique_id) {
        this.user_unique_id = user_unique_id;
    }

    public Double getX_co_ord() {
        return x_co_ord;
    }

    public void setX_co_ord(Double x_co_ord) {
        this.x_co_ord = x_co_ord;
    }

    public Double getY_co_ord() {
        return y_co_ord;
    }

    public void setY_co_ord(Double y_co_ord) {
        this.y_co_ord = y_co_ord;
    }
}
