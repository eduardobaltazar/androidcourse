package com.unosquare.webservicesapp;

public interface OnBackgroundTaskCallback{
    public void onTaskCompleted(String response);
    public void onTaskError(String error);
}