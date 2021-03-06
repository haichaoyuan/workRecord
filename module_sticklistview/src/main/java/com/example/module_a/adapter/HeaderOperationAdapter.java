package com.example.module_a.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.module_a.R;
import com.example.module_a.model.OperationEntity;

import java.util.List;

/**
 * Created by sunfusheng on 16/4/20.
 */
public class HeaderOperationAdapter extends BaseListAdapter<OperationEntity> {

    public HeaderOperationAdapter(Context context) {
        super(context);
    }

    public HeaderOperationAdapter(Context context, List<OperationEntity> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_operation, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OperationEntity entity = getItem(position);

        holder.tvTitle.setText(entity.getTitle());
//        holder.givImage.loadImage(entity.getImage_url(), R.color.font_black_6);

        if (TextUtils.isEmpty(entity.getSubtitle())) {
            holder.tvSubtitle.setVisibility(View.INVISIBLE);
        } else {
            holder.tvSubtitle.setVisibility(View.VISIBLE);
            holder.tvSubtitle.setText(entity.getSubtitle());
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView givImage;
        TextView tvTitle;
        TextView tvSubtitle;

        ViewHolder(View view) {
            givImage = view.findViewById(R.id.giv_image);
            tvTitle = view.findViewById(R.id.tv_title);
            tvSubtitle = view.findViewById(R.id.tv_subtitle);
        }
    }
}
