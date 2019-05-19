package com.example.rauan1.mystat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fragment_rest extends Fragment implements View.OnClickListener {
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    LinearLayout rest_apps;
    ArrayList<String> days;
    ArrayList<String> pkgname;
    EditText r1,r2;
    Button b1,b2,b3,b4,b5,b6,b7;
    boolean a1,a2,a3,a4,a5,a6,a7;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.restrictions, container, false);
        days = new ArrayList<>();
        pkgname = new ArrayList<>();
        rest_apps = view.findViewById(R.id.rest_apps);
        r1 = view.findViewById(R.id.rest_time);
        r2 = view.findViewById(R.id.rest_time2);
        b1 = view.findViewById(R.id.day_1);
        b2 = view.findViewById(R.id.day_2);
        b3 = view.findViewById(R.id.day_3);
        b4 = view.findViewById(R.id.day_4);
        b5 = view.findViewById(R.id.day_5);
        b6 = view.findViewById(R.id.day_6);
        b7 = view.findViewById(R.id.day_7);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        rest_apps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final List<ApplicationInfo> pkgAppsList = getContext().getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
                String[] colors = new String[pkgAppsList.size()];
                int i = 0;
                for(ApplicationInfo app : pkgAppsList)
                {
                    colors[i] = app.loadLabel(getContext().getPackageManager()).toString();
                    i++;
                }
                final boolean[] checkedColors = new boolean[pkgAppsList.size()];
                builder.setMultiChoiceItems(colors, checkedColors, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedColors[which] = isChecked;
                        String currentItem = pkgAppsList.get(which).packageName;
                    }
                });
                builder.setCancelable(false);
                builder.setTitle("app to make a limit?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i<checkedColors.length; i++){
                            boolean checked = checkedColors[i];
                            if (checked) {
                                pkgname.add(pkgAppsList.get(i).packageName);
                                ImageView m = new ImageView(getContext());
                                m.setMaxWidth(16);
                                m.setMaxHeight(16);
                                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                m.setLayoutParams(layoutParams);
                                m.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                                m.setImageDrawable(pkgAppsList.get(i).loadIcon(getContext().getPackageManager()));
                                rest_apps.addView(m);
                            }
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        Button b1 = view.findViewById(R.id.save);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!r1.getText().toString().isEmpty() || !r2.getText().toString().isEmpty())&&numbers(r1.getText().toString(),r2.getText().toString())){
                for (int i = 0; i < days.size(); i++) {
                    for (int j = 0; j < pkgname.size(); j++) {
                        DatabaseHelper helper = new DatabaseHelper(getContext());
                        int hours_to_minutes = 0;
                        int minutes = 0;
                        if (!r1.getText().toString().isEmpty())
                            hours_to_minutes = (Integer.parseInt(r1.getText().toString())*60);
                        if (!r2.getText().toString().isEmpty()){
                            minutes = Integer.parseInt(r2.getText().toString());
                        }
                        helper.insertNote(pkgname.get(j),days.get(i),minutes+hours_to_minutes);
                    }
                }
                    Fragment newFragment = new Fragment_restriction();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else {
                    Toast.makeText(getContext(), "your time is empty or smthing went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    @SuppressLint("NewApi")
    public void onClick(View v){

        if ( !getboolean((Button)v)){
            v.setBackgroundResource(R.drawable.circle_but_1);
            String a = ((Button)v).getText().toString();
            days.add(a);

        }
        else{
            v.setBackgroundResource(R.drawable.circle_but);
            String a = ((Button)v).getText().toString();
            days.remove(a);
        }
        setboolean((Button)v);
        Log.d("SIZE", "onClick: "+days.toString());

    }
    boolean getboolean(Button b){
        switch (b.getId()){
            case R.id.day_1:
                return a1;
            case R.id.day_2:
                return a2;
            case R.id.day_3:
                return a3;
            case R.id.day_4:
                return a4;
            case R.id.day_5:
                return a5;
            case R.id.day_6:
                return a6;
            case R.id.day_7:
                return a7;
        }
        return false;
    }
    void setboolean(Button b){
        switch (b.getId()){
            case R.id.day_1:
                a1 = !a1;
                break;
            case R.id.day_2:
                a2 = !a2;
                break;
            case R.id.day_3:
                a3 = !a3;
                break;
            case R.id.day_4:
                a4 = !a4;
                break;
            case R.id.day_5:
                a5 = !a5;
                break;
            case R.id.day_6:
                a6 = !a6;
                break;
            case R.id.day_7:
                a7 = !a7;
                break;
        }
    }
    boolean numbers(String a,String b){
        for (int i = 0; i <a.length() ; i++) {
            if (!Character.isDigit(a.charAt(i))){
                return false;
            }
        }
        for (int i = 0; i < b.length(); i++) {
            if (!Character.isDigit(b.charAt(i))){
                return false;
            }
        }
        return true;
    }

}
