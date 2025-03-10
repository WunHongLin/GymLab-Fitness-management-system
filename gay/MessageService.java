package com.example.gay;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessageService  extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        System.out.println( "From: " + remoteMessage.getFrom());
        super.onMessageReceived(remoteMessage);
    }
}
