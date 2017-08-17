package com.example.informationget.util;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.informationget.DisplayActivity;
import com.example.informationget.MainActivity;
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
                Intent intent = new Intent(mContext,DisplayActivity.class);
                intent.putExtra(DisplayActivity.INFO_URL,info.getUrl());
                intent.putExtra(DisplayActivity.INFO_TYPE,info.getType());
                mContext.startActivity(intent);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Information info = mInfoList.get(position);

        holder.infoTitle.setText(info.getDesc());
        holder.infoAuthor.setText(info.getWho());
        holder.infoDate.setText(info.getPublishedAt());

        holder.isCollected.setImageResource(info.getCollected().equals("true") ? R.drawable.like : R.drawable.unlike);
        holder.isCollected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info.getCollected().equals("true")) {
                    holder.isCollected.setImageResource(R.drawable.unlike);
                    info.setCollected("false");
                } else {
                    holder.isCollected.setImageResource(R.drawable.like);
                    info.setCollected("true");

                }
                info.save();

            }
        });
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
        ImageButton isCollected;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView;

            infoTitle = (TextView)itemView.findViewById(R.id.item_title);
            infoAuthor = (TextView)itemView.findViewById(R.id.item_author);
            infoDate = (TextView)itemView.findViewById(R.id.item_date);
            isCollected = (ImageButton) itemView.findViewById(R.id.item_collected);

        }
    }


}





