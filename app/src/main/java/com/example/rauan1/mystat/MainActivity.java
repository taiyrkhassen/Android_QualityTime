package com.example.rauan1.mystat;

/*import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static java.util.Collections.sort;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private NotReceiver receiver = new NotReceiver();
    private ArrayList<ElementData> data = new ArrayList<>();
    private IntentFilter filter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TrackingTimeOfUsage();
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Collections.sort(data,Collections.<ElementData>reverseOrder(new Comparator<ElementData>(){
            public int compare(ElementData obj1, ElementData obj2)
            {
                return obj1.UsingTime-(obj2.UsingTime);
            }
        }));
        mAdapter = new MyAdapter(data);
        recyclerView.setAdapter(mAdapter);
        for (int i = 0; i <data.size() ; i++) {
            int s = mAdapter.getItemViewType(i);
        }
        filter = new IntentFilter(NotReceiver.RECEIVE_ACTION);
        registerReceiver(receiver, filter);
        Intent intent = new Intent(MainActivity.this,NotService.class);
        startService(intent);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void TrackingTimeOfUsage() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        long delta = 24*60*60*1000;
        Calendar c = Calendar.getInstance();
        int i = 0;
        List<UsageStats> usageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,time-delta,time);
        for (UsageStats usagestat:usageStats) {
            try {
                if (usagestat.getTotalTimeInForeground() / 1000 > 0) {
                        data.add(new ElementData(getAppNameFromPackage(usagestat.getPackageName(),getApplicationContext()), (int)(usagestat.getTotalTimeInForeground() / 60000), getPackageManager().getApplicationIcon(usagestat.getPackageName())));

                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            i++;
        }
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver,filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}

*/