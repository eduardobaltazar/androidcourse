package com.unosquare.finalapp;

public interface OnBackgroundTaskCallback {
    public void onTaskCompleted(String response);
    public void onTaskError(String error);
}