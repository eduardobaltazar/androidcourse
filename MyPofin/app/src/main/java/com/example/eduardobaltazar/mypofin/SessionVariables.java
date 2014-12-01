package com.example.eduardobaltazar.mypofin;

import android.app.Application;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by eduardo.baltazar on 10/11/2014.
 * General class to store session variables for the application. General Data.
 */
public class SessionVariables extends Application {
    private String user_id;
    private String name;
    private ArrayList<PointModel> pointsList;
    private LatLng markerLatLng;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<PointModel> getPointsList() {
        return pointsList;
    }

    public void setPointsList(ArrayList<PointModel> pointsList) {
        this.pointsList = pointsList;
    }

    public LatLng getMarkerLatLng() {
        return markerLatLng;
    }

    public void setMarkerLatLng(LatLng markerLatLng) {
        this.markerLatLng = markerLatLng;
    }
}
