package com.novartis.winnovators;

import android.app.Application;

import com.pusher.pushnotifications.PushNotifications;

public class AppClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PushNotifications.start(getApplicationContext(), "452845e8-2506-4150-803d-b1bf738dbde0");
        PushNotifications.addDeviceInterest("hello");
        PushNotifications.addDeviceInterest("debug-notification");
    }
}
