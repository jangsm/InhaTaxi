package com.inhataxi.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.demono.adapter.InfinitePagerAdapter;
import com.inhataxi.R;
import com.inhataxi.model.MainEventBanner;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class AutoBannerAdapter extends InfinitePagerAdapter {

    private List<MainEventBanner> mArrayListEventBanner;
    private Context mContext;

    public AutoBannerAdapter(List<MainEventBanner> data, Context context) {
        this.mArrayListEventBanner = data;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return mArrayListEventBanner.size();
    }

    @Override
    public View getItemView(final int position, View convertView, ViewGroup container) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.fragment_auto_banner, null);
        ImageView image_container = convertView.findViewById(R.id.event_banner_iv_banner);
        ImageView back = convertView.findViewById(R.id.event_banner_iv_backimg);
        TextView title = convertView.findViewById(R.id.event_banner_tv_banner);

        Glide.with(mContext).load(mArrayListEventBanner.get(position).getImgurl()).into(image_container);
        GradientDrawable drawable = (GradientDrawable) mContext.getDrawable(R.drawable.bg_round_trans_trans);
        back.setBackground(drawable);
        back.setClipToOutline(true);
        image_container.setBackground(drawable);
        image_container.setClipToOutline(true);

        title.setText(mArrayListEventBanner.get(position).getTitle());

        image_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("url", mArrayListEventBanner.get(position).getUrl());
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }
}
