package com.example.module_homepage2.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.module_homepage2.R;
import com.example.module_homepage2.base.AppMenuRes;
import com.example.module_homepage2.base.NumberUtil;

import java.util.List;

/**
 * Des:
 * Author: Luke
 * Data: 17/2/16
 */
public class HomeMenu2Adapter extends HomeMenuAdapter {
    public HomeMenu2Adapter(Context ctx, List<AppMenuRes> objects) {
        super(ctx, objects);
        drawables = new int[10];
    }

    @Override
    public int getCount() {
        return dataArray == null ? 0 : (dataArray.size() > drawables.length ? drawables.length : dataArray.size());
    }

    @Override
    public View getTheView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.item_grid_home_menu, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.menu_tv);
            holder.icon = (ImageView) convertView.findViewById(R.id.menu_img);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        final AppMenuRes menuRes = getItem(position);

//        ((GALinearLayout) convertView).setGAString("homemenu" + position + ":" + menuRes.getId());

        holder.name.setText(menuRes.getMenuName());

        DrawableCrossFadeFactory drawableCrossFadeFactory =
                new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
        if (!menuRes.isSimulated())
            Glide.with((Activity) context)
                    .load(menuRes.getMenuImageUrl()).placeholder(context.getResources().
                    getDrawable(drawables[position]))
                    .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                    .into(holder.icon);
        else
            Glide.with((Activity) context).load(NumberUtil.toInt(menuRes.getMenuImageUrl()))
                    .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                    .into(holder.icon);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return convertView;
    }
}
