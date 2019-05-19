package com.example.rauan1.mystat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


public class Fragment_apps extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private NotReceiver receiver = new NotReceiver();
    private ArrayList<ElementData> data = new ArrayList<>();
    private IntentFilter filter;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_apps, container, false);
        TrackingTimeOfUsage();
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        Collections.sort(data,Collections.<ElementData>reverseOrder(new Comparator<ElementData>(){
            public int compare(ElementData obj1, ElementData obj2)
            {
                return obj1.UsingTime-(obj2.UsingTime);
            }
        }));
        mAdapter = new MyAdapter(data);
        recyclerView.setAdapter(mAdapter);
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void TrackingTimeOfUsage() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) getContext().getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar beginCal = Calendar.getInstance();
        beginCal.add(Calendar.DAY_OF_MONTH, -1);
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss",Locale.US);
        List<UsageStats> usageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,beginCal.getTimeInMillis(),System.currentTimeMillis());
        for (UsageStats usagestat:usageStats) {
            try {
                if (usagestat.getTotalTimeInForeground() > 0) {
                    data.add(new ElementData(getAppNameFromPackage(usagestat.getPackageName(),getContext()), (int)(usagestat.getTotalTimeInForeground() / 60000), getContext().getPackageManager().getApplicationIcon(usagestat.getPackageName())));
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
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


}
