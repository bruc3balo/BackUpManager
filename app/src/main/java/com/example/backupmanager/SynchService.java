package com.example.backupmanager;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import static com.example.backupmanager.MainActivity.INPUT_EXTRA;
import static com.example.backupmanager.NotificationChannelClass.CHANNEL_ID;

public class SynchService extends Service {
    public SynchService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra(INPUT_EXTRA);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("Background Sync").setContentText("Synchronizing Diaries,Notes,Objectives and Goals").setSmallIcon(R.drawable.backup).setContentIntent(pendingIntent).build();

        Toast.makeText(getApplicationContext(), input, Toast.LENGTH_SHORT).show();
        startForeground(1,notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
