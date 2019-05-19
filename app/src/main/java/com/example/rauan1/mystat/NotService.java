package com.example.rauan1.mystat;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotService extends Service {
    public static final String TAG = NotService.class.getSimpleName();
    public static final String PACKAGE_NAME = "PACKAGE_NAME";
    public ScheduledExecutorService scheduledExecutorService;
    private NotificationManager manager;
    public NotificationCompat.Builder builder;

    public NotService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("ServiceCast")
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
         builder = getNotificationBuilder();
        builder.setContentTitle("MyStat Service Notification")
                .setSmallIcon(R.drawable.ic_launcher_foreground);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        startForeground(123, getStart_notification("start notification"));
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(NotReceiver.RECEIVE_ACTION);
                manager.notify(123,getStart_notification("Your app is out"));
                intent.putExtra(PACKAGE_NAME,RunningAppName());
                sendBroadcast(intent);
            }
        },1000,10000,TimeUnit.MILLISECONDS);
        return START_STICKY;
    }

    private Notification getStart_notification(String contentText) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this,2343,new Intent(this,MainActivity1.class),PendingIntent.FLAG_UPDATE_CURRENT);
        return builder.setContentText(contentText)
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scheduledExecutorService.shutdownNow();
    }
    public String RunningAppName(){
        String topPackageName = null;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
            topPackageName = foregroundTaskInfo.topActivity.getPackageName();
        }else{
            UsageStatsManager usage = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> stats = usage.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000*1000, time);
            if (stats != null) {
                SortedMap<Long, UsageStats> runningTask = new TreeMap<Long,UsageStats>();
                for (UsageStats usageStats : stats) {
                    runningTask.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (runningTask.isEmpty()) {
                    topPackageName="None";
                }
                topPackageName =  runningTask.get(runningTask.lastKey()).getPackageName();
            }
        }
        return topPackageName;
    }
    private NotificationCompat.Builder getNotificationBuilder() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return new NotificationCompat.Builder(this);
        }
        else{
            String my_channel = "my_channel_id";

            if (manager.getNotificationChannel(my_channel)==null){
            NotificationChannel channel = new NotificationChannel(my_channel,"For Notification of limitation",NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);}

            return new NotificationCompat.Builder(this,"my_channel_id");
        }
    }

}
