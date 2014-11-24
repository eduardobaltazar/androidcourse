package com.example.eduardobaltazar.mypofin;

public interface OnBackgroundTaskCallback {
    public void onTaskCompleted(String response);
    public void onTaskError(String error);
}