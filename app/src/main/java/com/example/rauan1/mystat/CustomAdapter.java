package com.example.rauan1.mystat;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
public class CustomAdapter extends ArrayAdapter {
    ImageView img;
    TextView name,day,minutes;
    ArrayList<rest> a;
    public CustomAdapter(Context context, ArrayList<rest> a) {
        super(context, R.layout.rest_items);
        this.a = a;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.rest_items,parent,false);
        img = view.findViewById(R.id.img);
        name = view.findViewById(R.id.name_of_rest);
        day = view.findViewById(R.id.day);
        minutes = view.findViewById(R.id.limit);
        img.setImageResource(R.drawable.ic_launcher_foreground);
        try
        {
            Drawable icon = getContext().getPackageManager().getApplicationIcon(a.get(position).getName());
            img.setImageDrawable(icon);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        day.setText(a.get(position).getDay());
        name.setText(getAppNameFromPackage(a.get(position).getName(),getContext()));
        minutes.setText(a.get(position).getMinutes()+" min");
        return view;
    }

    @Override
    public int getCount() {
        return a.size();
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
