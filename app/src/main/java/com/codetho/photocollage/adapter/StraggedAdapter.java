package com.codetho.photocollage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.codetho.photocollage.R;


import java.util.ArrayList;



public class StraggedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mData;

    public StraggedAdapter(Context mContext, ArrayList<String> data) {
        this.mContext = mContext;
        this.mData = data;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stragged_row, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        Glide.with(this.mContext)
                .load(mData.get(position))
                .into(myViewHolder.ivImage);
    }

    @Override
    public int getItemCount() {
        return (null != mData) ? mData.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivImage = (ImageView) itemView.findViewById(R.id.gridImg);
        }
    }

}
