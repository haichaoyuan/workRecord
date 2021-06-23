package com.example.module_homepage2.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.module_homepage2.R;
import com.example.module_homepage2.base.AppMenuRes;
import com.example.module_homepage2.base.DataAdapter;

import java.util.List;


/**
 * Des:
 * Author: Luke
 * Data: 17/2/16
 */
public class HomeMenuAdapter extends DataAdapter<AppMenuRes> {

    protected int[] drawables;

    public HomeMenuAdapter(Context ctx, List<AppMenuRes> objects) {
        super(ctx, objects);
        drawables = new int[]{R.drawable.ico_menu_mystock, R.drawable.ico_menu_stockindex, R.drawable.ico_menu_ranking,
                R.drawable.ico_menu_hotblock, R.drawable.ico_menu_ipo, R.drawable.ico_menu_winnerlist,
                R.drawable.ico_menu_oa, R.drawable.ico_menu_trade};
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
            Glide.with((Activity) context).load(drawables[position])
                    .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                    .into(holder.icon);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (AppMenuRes.MENU_TYPE_H5.equals(menuRes.getMenuType())) {
//
//                    context.startActivity(new Intent(context, WebViewActivity.class).putExtra(WebViewActivity.PARAMS_URL, menuRes.getTargetUrl())
//                            .putExtra(WebViewActivity.PARAMS_TITLE, menuRes.getMenuName()));
//
//                } else {
//                    if (!TextUtils.isEmpty(menuRes.getTargetUrl())) {
//                        switch (Integer.valueOf(menuRes.getTargetUrl())) {
//                            case AppMenuRes.TARGET_MYSTOCK:
//
//                                Intent intent1 = new Intent();
//                                intent1.putExtra(QuoteMainFragment.PARAMS_TAB_INDEX, QuoteMainFragment.INDEX_SELF_STOCK);
//                                ((MainActivity) context).replaceContent(intent1, PageConstant.INDEX_QUOTE);
//                                break;
//
//                            case AppMenuRes.TARGET_STOCKINDEX:
//
//                                Intent intent = new Intent();
//                                intent.putExtra(QuoteMainFragment.PARAMS_TAB_INDEX, QuoteMainFragment.INDEX_STOCK_INDEX);
//                                ((MainActivity) context).replaceContent(intent, PageConstant.INDEX_QUOTE);
//                                break;
//
//                            case AppMenuRes.TARGET_CHANGELIST:
//
//                                Intent it = new Intent();
//                                it.putExtra(QuoteMainFragment.PARAMS_TAB_INDEX, QuoteMainFragment.INDEX_MARKETS);
//                                ((MainActivity) context).replaceContent(it, PageConstant.INDEX_QUOTE);
//                                break;
//
//                            case AppMenuRes.TARGET_HOTBLOCK:
//
//                                Intent mIntent = new Intent();
//                                mIntent.putExtra(QuoteMainFragment.PARAMS_TAB_INDEX, QuoteMainFragment.INDEX_MARKETS);
//                                ((MainActivity) context).replaceContent(mIntent, PageConstant.INDEX_QUOTE);
//
//                                break;
//
//                            case AppMenuRes.TARGET_OA:
//
//                                context.startActivity(new Intent(context, OpenAccountHomeActivity.class));
//                                break;
//
//                            case AppMenuRes.TARGET_TRADE:
//
//                                ((MainActivity) context).replaceContent(null, PageConstant.INDEX_TRADE);
//                                break;
//
//                            case AppMenuRes.TARGET_SIMULATE:
//                            case AppMenuRes.TARGET_SIMULATE_CONTEST:
//                                context.startActivity(new Intent(context, SimulateTradeContestActivity.class));
//                                break;
//                            case AppMenuRes.TARGET_ZTINFO:
//                                ((MainActivity) context).replaceContent(null, PageConstant.INDEX_INFO);
//                                break;
//                            case AppMenuRes.TARGET_HKSTOCK:
//                                Intent intent2 = new Intent();
//                                intent2.putExtra(QuoteMainFragment.PARAMS_TAB_INDEX, QuoteMainFragment.INDEX_HKSTOCK);
//                                ((MainActivity) context).replaceContent(intent2, PageConstant.INDEX_QUOTE);
//                                break;
//                            case AppMenuRes.TARGET_MORE:
//                                Intent intent3 = new Intent();
//                                intent3.putExtra(QuoteMainFragment.PARAMS_TAB_INDEX, QuoteMainFragment.INDEX_MORE);
//                                ((MainActivity) context).replaceContent(intent3, PageConstant.INDEX_QUOTE);
//                                break;
//                            case AppMenuRes.TARGET_SD_TG:
//                                SDHelper.INSTANCE.startActivity(context, SDHelper.TOU_GU);
//                                break;
//                            case AppMenuRes.TARGET_LOCK_ACT:
//                                String urlPublic = XJBDataStoreRemote.API_BASE_URL_H5 + "h5/activateLockedAccount/intro.html";
//                                intent = new Intent(context, WebViewActivity.class);
//                                context.startActivity(intent.putExtra(WebViewActivity.PARAMS_TITLE, context.getString(R.string.lock_act_title))
//                                        .putExtra(WebViewActivity.PARAMS_URL, urlPublic).putExtra(WebViewActivity.PARAM_CAN_GOBACK, false));
//                                break;
//                        }
//
//                    }
//                }
            }
        });
        return convertView;
    }

    public class ViewHolder {
        TextView name;
        ImageView icon;
    }
}
