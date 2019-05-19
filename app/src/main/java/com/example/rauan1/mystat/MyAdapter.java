package com.example.rauan1.mystat;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<ElementData> mDataset;
    int initial_value;
    int lastPosition = -1;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView times;
        public TextView count;
        public ImageView icon;
        public ProgressBar pbar;
        public MyViewHolder(View view) {
            super(view);
            icon = view.findViewById(R.id.icon_img);
            name = view.findViewById(R.id.icon_name);
            times = view.findViewById(R.id.icon_times);
            count = view.findViewById(R.id.icon_count);
            pbar = view.findViewById(R.id.progressBar);
        }
    }

    public MyAdapter(ArrayList<ElementData> myDataset) {
        mDataset = myDataset;
        notifyDataSetChanged();
    }
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
       holder.count.setText(String.valueOf(position+1));
       holder.name.setText(mDataset.get(position).getName());
       holder.times.setText(mDataset.get(position).getUsingTime()+ " m");
       holder.icon.setImageDrawable(mDataset.get(position).getIcon());
       holder.pbar.setMax(1440);
       initial_value = 0;
        holder.pbar.setProgress(mDataset.get(position).getUsingTime());


        if (position > lastPosition) {
            ObjectAnimator animator = ObjectAnimator.ofInt(holder.pbar, "progress", 0, mDataset.get(position).getUsingTime());
            animator.setInterpolator(new DecelerateInterpolator());
            animator.setDuration(2000);
            animator.start();
            lastPosition = position;
        }
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}