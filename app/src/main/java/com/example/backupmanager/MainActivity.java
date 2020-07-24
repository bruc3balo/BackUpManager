package com.example.backupmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "MainActivity";
    private EditText editText;
    public static final String INPUT_EXTRA = "INPUT_EXTRA";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText= findViewById(R.id.text_tinput);

        textView = findViewById(R.id.textView);
    }

    private void startService(View v) {
        String input = editText.getText().toString();
        Intent serviceIntent = new Intent(this,SynchService.class);
        serviceIntent.putExtra(INPUT_EXTRA,input);

        startService(serviceIntent);
    }

    private void stopService(View v) {
        Intent serviceIntent = new Intent(this,SynchService.class);
        stopService(serviceIntent);
    }

    public void startservice(View view) {
        startService(view);
    }

    public void stopservice(View view) {
        stopService(view);
    }

    public void startIntent(View view) {
        String input = editText.getText().toString();

        Intent serviceIntent = new Intent(this,SynchIntentService.class);
        serviceIntent.putExtra(INPUT_EXTRA,input);

        ContextCompat.startForegroundService(this,serviceIntent);
    }

    public void stopIntent(View view) {

    }

    public void openTimePicker(View view) {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(),"time picker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Toast.makeText(this, "Hour : "+hourOfDay+ " Minute : "+ minute , Toast.LENGTH_SHORT).show();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        c.set(Calendar.SECOND,0);
        updateTimeText(c);
        startAlarm(c);
    }


    private void updateTimeText(Calendar c) {
        String timeText = "Alarm set for : ";
        timeText+= DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        textView.setText(timeText);
    }

    private void startAlarm (Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,intent,0); //todo request code

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE,1);
        }

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
        }

    }

    public void cancelAlarm(View view) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,intent,0); //todo request code

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            textView.setText("Alarm canceled");
        }
    }


    public void cancelJob(View view) {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d(TAG,"Job cancelled");
    }

    public void scheduleJob(View view) {
        ComponentName componentName = new ComponentName(this,SynchJobService.class); //only on wifi
        JobInfo info = new JobInfo.Builder(123,componentName).setRequiresCharging(true).setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED).setPersisted(true).setPeriodic(15*60*1000).build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = 0;
        if (scheduler != null) {
            resultCode = scheduler.schedule(info);
        }

        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG,"Job scheduled");
        } else {
            Log.d(TAG,"Job scheduling failed");
        }
    }
}