package com.example.mjchartdemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mjchartdemo.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/7 0007.
 */

public class ChatBaseAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> list = new ArrayList<>();
    private LayoutInflater inflater = null;

    public ChatBaseAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.activity_chat_item, null);
            holder = new ViewHolder();
            /**得到各个控件的对象*/
            holder.tv_left = (TextView) view.findViewById(R.id.tv_left);
            holder.tv_right = (TextView) view.findViewById(R.id.tv_right);
            holder.ll_left = (LinearLayout) view.findViewById(R.id.ll_left);
            holder.ll_right = (LinearLayout) view.findViewById(R.id.ll_right);
            holder.iv_left = (ImageView) view.findViewById(R.id.iv_left);
            holder.iv_right = (ImageView) view.findViewById(R.id.iv_right);
            holder.tv_time_left = (TextView) view.findViewById(R.id.tv_time_left);
            holder.tv_time_right = (TextView) view.findViewById(R.id.tv_time_right);
            holder.tv_name_left = (TextView) view.findViewById(R.id.tv_name_left);
            holder.tv_name_right = (TextView) view.findViewById(R.id.tv_name_right);

            view.setTag(holder);//绑定ViewHolder对象
        } else {
            holder = (ViewHolder) view.getTag();//取出ViewHolder对象
        }

        Map<String, Object> map = list.get(i);
        if (i + 1 < getCount()) {
            holder.ll_left.setVisibility(View.VISIBLE);
            holder.ll_right.setVisibility(View.GONE);

            if (map.containsKey("AFM_1")) {
                holder.tv_left.setText(map.get("AFM_1").toString());
            }
            if (map.containsKey("AFM_8")) {
                holder.tv_time_left.setText(map.get("AFM_8").toString());
            }
        } else {
            holder.ll_left.setVisibility(View.GONE);
            holder.ll_right.setVisibility(View.VISIBLE);

            if (map.containsKey("AFM_1")) {
                holder.tv_right.setText(map.get("AFM_1").toString());
            }
            if (map.containsKey("AFM_8")) {
                holder.tv_time_right.setText(map.get("AFM_8").toString());
            }
        }
        return view;
    }

    class ViewHolder {
        TextView tv_left, tv_right, tv_time_left, tv_time_right,tv_name_left,tv_name_right;
        LinearLayout ll_left, ll_right;
        ImageView iv_left;
        ImageView iv_right;
    }
}
