package com.example.backupmanager;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class NotificationChannelClass extends Application {

    public static final String CHANNEL_ID = "BackgroundSync";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "Background Synchronizing", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

}
