package com.example.backupmanager;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import static com.example.backupmanager.MainActivity.INPUT_EXTRA;
import static com.example.backupmanager.NotificationChannelClass.CHANNEL_ID;

public class SynchIntentService extends IntentService {

    public static final String TAG = "SynchIntentService";
    private PowerManager.WakeLock wakeLock;

    public SynchIntentService() {
        super(TAG);
        setIntentRedelivery(false); // NOT_STICKY
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if (powerManager != null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"BackupSync:Wakelock"); //colon
        }
        wakeLock.acquire();

        Log.d(TAG,"Wakelock acquired");


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Notification  notification = new Notification.Builder(this,CHANNEL_ID).setContentTitle("Background Sync Intent").setContentText("Intent Synchronizing Diaries,Notes,Objectives and Goals").setSmallIcon(android.R.drawable.ic_lock_idle_alarm).build();

            startForeground(1,notification);
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG,"onDestroy");

        wakeLock.release();
        Log.d(TAG,"Wae lock released");

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG,"");
        String input = intent.getStringExtra(INPUT_EXTRA);

        for (int i = 0;i < 10;i++) {
            Log.d(TAG,input + " - " +i);
            SystemClock.sleep(1000);
        }
    }
}
