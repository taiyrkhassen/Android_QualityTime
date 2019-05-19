package com.example.rauan1.mystat;

import android.app.TimePickerDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Fragment_stat extends Fragment {
    @Nullable

    PieChart pieChart;
    ArrayList<PieEntry> yValues;
    Button bt3;
    public TextView name, status;
    int sum = 0;
    Fragment_redacting redd;
    ArrayList<ElementData> data = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.my_stat, container, false);
        redd = new Fragment_redacting();
        TrackingTimeOfUsage();
        Collections.sort(data,Collections.<ElementData>reverseOrder(new Comparator<ElementData>(){
            public int compare(ElementData obj1, ElementData obj2)
            {
                return obj1.UsingTime-(obj2.UsingTime);
            }
        }));
        redd.r_name = view.findViewById(R.id.redact_name);
        redd.r_status = view.findViewById(R.id.redact_status);
        addApp(data);
        pieChart = view.findViewById(R.id.pie_chart);
        bt3 = view.findViewById(R.id.button3);
        bt3.setText(String.format("%02d:%02d", sum/60, sum%60).toString());
        bt3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                pieChart.setVisibility(View.VISIBLE);
                bt3.setVisibility(View.INVISIBLE);
                pieChart.setUsePercentValues(true);
                pieChart.getDescription().setEnabled(false);
                pieChart.setExtraOffsets(5, 10, 5, 5);

                pieChart.setDragDecelerationFrictionCoef(0.99f);
                pieChart.setDrawHoleEnabled(true);
                pieChart.setHoleColor(Color.WHITE);
                pieChart.setTransparentCircleRadius(60f);


                Description description = new Description();
                description.setText("Our FinalProject....\n im exhausted");
                description.setTextSize(9);
                pieChart.setDescription(description);
                pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
                PieDataSet pieDataSet = new PieDataSet(yValues, "Applications");
                pieDataSet.setSliceSpace(3f);
                pieDataSet.setSelectionShift(5f);
                pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

                PieData pieData = new PieData(pieDataSet);
                pieData.setValueTextColor(Color.BLACK);
                pieData.setValueTextSize(10f);

                pieChart.setData(pieData);
                pieChart.setCenterText(String.format("%02d:%02d", sum/60, sum%60).toString());

                pieChart.setCenterTextColor(Color.RED);
                pieChart.setCenterTextSize(20);
                pieChart.setRotationEnabled(false);
                pieChart.setHoleRadius(40);
                pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                    @Override
                    public void onValueSelected(Entry e, Highlight h) {

                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNothingSelected() {

                    }
                });

            }
        });
        return view;
    }
    public void addApp(ArrayList<ElementData> a){
        yValues = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            sum+=a.get(i).getUsingTime();
        }
        for (int i = 0; i < 6 ; i++) {
            yValues.add(new PieEntry(a.get(i).getUsingTime()*100/sum,a.get(i).getName()));
        }
    }
    public String getName(){
        return name.getText().toString();
    }
    public String getStatus(){
        return status.getText().toString();
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
