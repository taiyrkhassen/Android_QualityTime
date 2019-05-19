package com.example.rauan1.mystat;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotReceiver extends BroadcastReceiver {
    public static final String RECEIVE_ACTION = "com.example.rauan1.mystat.RECEIVE_ACTION";
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent) {
        String name_of_app = intent.getStringExtra(NotService.PACKAGE_NAME);
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        long dayago = time-(1000*60*60*24);
        Map<String,UsageStats> abc = usageStatsManager.queryAndAggregateUsageStats(dayago,time);
        UsageStats current_app = abc.get(name_of_app);
        DatabaseHelper helper = new DatabaseHelper(context);
        ArrayList<rest> c = helper.getReceives();
        Log.d("run:", "onReceive: "+c.size());
        for (int i = 0; i <c.size() ; i++) {
            Log.d("run:", "in map: "+c.get(i).getName());
            Log.d("run:", "current app: " + current_app.getPackageName());
            if (current_app.getPackageName().equals(c.get(i).getName())){
                Log.d("run:", "onReceive: "+"yahoo");
            if (current_app.getTotalTimeInForeground()>c.get(i).getMinutes()*60*1000){
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                String id = "my_channel_01";
                CharSequence name = context.getString(R.string.channel_name);
                String description = context.getString(R.string.channel_description);
                Log.d("run:", "onReceive: "+name+" "+description);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(id, name,importance);
                mChannel.setDescription(description);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mNotificationManager.createNotificationChannel(mChannel);
                mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                int notifyID = 1;
                String CHANNEL_ID = "my_channel_01";
                Notification notification = new Notification.Builder(context)
                        .setContentTitle("your time is out")
                        .setContentText(getAppNameFromPackage(current_app.getPackageName(),context) + " your application is restrited " + c.get(i).getMinutes() + " minutes")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setChannelId(CHANNEL_ID)
                        .build();

                mNotificationManager.notify(notifyID, notification);
            }
        }
    }}

    private String getAppNameFromPackage(String packageName, Context context) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = context.getPackageManager().queryIntentActivities(mainIntent, 0);
        for (ResolveInfo app : pkgAppsList) {
            if (app.activityInfo.packageName.equals(packageName)) {
                return app.activityInfo.loadLabel(context.getPackageManager()).toString();
            }
        }
        return null;
    }
    }
