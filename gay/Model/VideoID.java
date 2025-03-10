package com.example.gay.Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class VideoID {
    @Exclude
    public String VideoID;
    public <T extends VideoID> T withId (@NonNull final String id) {
        this.VideoID = id;
        return (T) this;
    }
}
