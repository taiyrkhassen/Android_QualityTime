package com.example.rauan1.mystat;

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
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Objects;

public class Fragment_redacting extends Fragment {
    EditText r_name, r_status;
    Button bt_save;
    ImageView r_img;
    String g_status, g_name;
    Fragment_stat statt;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.redacting, container, false);
        r_img = view.findViewById(R.id.redact_icon);
        r_name = view.findViewById(R.id.redact_name);
        r_status = view.findViewById(R.id.redact_status);
        bt_save = view.findViewById(R.id.r_save);
        statt = new Fragment_stat();
        g_status = "";
        g_name = "";


        bt_save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                System.out.println("DSFSDFSDFSDFDS");
               Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.redact,new Fragment_stat()).commit();
              //    Objects.requireNonNull(getActivity()).setContentView(R.layout.my_stat);
            }
        });


        return view;
    }


}
