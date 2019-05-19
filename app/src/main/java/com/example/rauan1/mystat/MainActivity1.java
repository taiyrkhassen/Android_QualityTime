package com.example.rauan1.mystat;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity1 extends AppCompatActivity {
    BottomNavigationView btm;
    private NotReceiver receiver = new NotReceiver();
    private IntentFilter filter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        btm = findViewById(R.id.bottom_nav);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, new Fragment_restriction()).commit();
        btm.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment current_fragment = null;
                switch (menuItem.getItemId()){
                    case R.id.my_stat:
                        current_fragment = new Fragment_stat();


                        break;
                    case R.id.my_time_applications:
                        current_fragment = new Fragment_apps();

                        break;
                    case R.id.my_list_time:
                        current_fragment = new Fragment_restriction();

                        break;
                }
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container, current_fragment).commit();

                return true;
            }
        });
        filter = new IntentFilter(NotReceiver.RECEIVE_ACTION);
        registerReceiver(receiver, filter);
        Intent intent = new Intent(MainActivity1.this,NotService.class);
        startService(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(receiver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(receiver);
    }
}

