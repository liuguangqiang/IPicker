package com.liuguangqiang.ipicker.entities;

/**
 * Created by Eric on 16/9/12.
 */

public class Photo {

    public String path;

    public boolean showCamera = false;

    public Photo(String path) {
        this.path = path;
        this.showCamera = false;
    }

    public Photo(boolean showCamera) {
        this.showCamera = showCamera;
    }

}
