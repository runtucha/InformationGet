package com.example.informationget.util;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.informationget.DisplayActivity;
import com.example.informationget.R;
import com.example.informationget.db.Information;

import java.util.List;

/**
 * Created by glenn_cui on 17-8-14.
 */
public class InfoItemAdapter extends RecyclerView.Adapter<InfoItemAdapter.ViewHolder>{

    private Context mContext;
    private List<Information> mInfoList;
    private String TAG = "gtest";

    public InfoItemAdapter (List<Information> InfoList){
        mInfoList = InfoList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.info_item,parent,false);

        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position =holder.getAdapterPosition();
                Information info = mInfoList.get(position);

                Log.d(TAG, "onClick: url is "+info.getUrl());

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Information info = mInfoList.get(position);
        Log.d(TAG, "onBindViewHolder: position "+position);
        Log.d(TAG, "onBindViewHolder: info.getDesc() :" + info.getDesc());
        Log.d(TAG, "onBindViewHolder: hoder.infoTitle is "+holder.infoTitle);

        holder.infoTitle.setText(info.getDesc());
        holder.infoAuthor.setText(info.getWho());
        holder.infoDate.setText(info.getPublishedAt());
    }

    @Override
    public int getItemCount() {
        return mInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;

        TextView infoTitle;
        TextView infoAuthor;
        TextView infoDate;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView;

            infoTitle = (TextView)itemView.findViewById(R.id.item_title);
            infoAuthor = (TextView)itemView.findViewById(R.id.item_author);
            infoDate = (TextView)itemView.findViewById(R.id.item_date);

        }
    }


}





