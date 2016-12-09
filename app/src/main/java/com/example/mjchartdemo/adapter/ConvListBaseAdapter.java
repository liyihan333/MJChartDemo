package com.example.mjchartdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mjchartdemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/8 0008.
 */

public class ConvListBaseAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> list = new ArrayList<>();
    private List<Map<String, Object>> childlist = new ArrayList<>();

    private LayoutInflater inflater = null;

    public ConvListBaseAdapter(Context context, List<Map<String, Object>> list) {
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
            view = inflater.inflate(R.layout.conversation_list_item, null);
            holder = new ViewHolder();
            /**得到各个控件的对象*/
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_psn_name = (TextView) view.findViewById(R.id.tv_psn_name);

            view.setTag(holder);//绑定ViewHolder对象
        } else {
            holder = (ViewHolder) view.getTag();//取出ViewHolder对象
        }

        Map<String, Object> map = list.get(i);
        holder.tv_title.setText(map.get("AFM_1").toString());
        holder.tv_psn_name.setText(map.get("AFM_6").toString());
        return view;
    }

    class ViewHolder {
        TextView tv_title, tv_psn_name;
    }
}
