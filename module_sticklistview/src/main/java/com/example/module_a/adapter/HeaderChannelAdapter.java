package com.example.module_a.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.module_a.R;
import com.example.module_a.model.ChannelEntity;

import java.util.List;


/**
 * Created by sunfusheng on 16/4/20.
 */
public class HeaderChannelAdapter extends BaseListAdapter<ChannelEntity> {

    public HeaderChannelAdapter(Context context, List<ChannelEntity> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_channel, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ChannelEntity entity = getItem(position);

        holder.tvTitle.setText(entity.getTitle());
//        holder.givImage.loadImage(entity.getImage_url(), R.color.font_black_6);

        if (TextUtils.isEmpty(entity.getTips())) {
            holder.tvTips.setVisibility(View.INVISIBLE);
        } else {
            holder.tvTips.setVisibility(View.VISIBLE);
            holder.tvTips.setText(entity.getTips());
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView givImage;
        TextView tvTitle;
        TextView tvTips;

        ViewHolder(View view) {
            givImage = view.findViewById(R.id.giv_image);
            tvTitle = view.findViewById(R.id.tv_title);
            tvTips = view.findViewById(R.id.tv_tips);
        }
    }
}
