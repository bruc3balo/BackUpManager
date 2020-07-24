package com.example.backupmanager;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class SynchJobService extends JobService {

    public static final String TAG = "SynchJobService";
    private boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG,"Job Started");
        doBackgroundWork(params);
        return false;
    }

    private void doBackgroundWork(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0;i < 10;i++) {
                    Log.d(TAG,"run: "+i);

                    if (jobCancelled) {
                        return;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

                Log.d(TAG,"Job finished");
                jobFinished(params,false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG,"Job cancelled before completion");
        jobCancelled = true;
        return false;
    }
}
