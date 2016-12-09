package com.example.mjchartdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.mjchartdemo.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/8 0008.
 */

public class ListBaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private List<Map<String, Object>> mDatas;
    private Context mContext;
    public OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public ListBaseAdapter(List<Map<String, Object>> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 加载数据item的布局，生成VH返回
        View v = LayoutInflater.from(mContext).inflate(R.layout.conversation_list_item, parent, false);
        v.setOnClickListener(this);
        return new ComViewHolder(v);
    }

    private static final String TAG = "ListBaseAdapter";
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder thisholder, int position) {
        if (thisholder instanceof ComViewHolder) {
            final ComViewHolder holder = (ComViewHolder) thisholder;
            Map<String, Object> map = getData(position);
            String AFM_1 = String.valueOf(map.get("AFM_1"));
            holder.tv_title.setText(!AFM_1.equals("null") ? AFM_1 : "无标题");

            String AFM_6 = String.valueOf(map.get("AFM_6"));
            holder.tv_psn_name.setText(!AFM_6.equals("null") ? AFM_6 : "无联系人");

            holder.itemView.setTag(map);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(view, JSON.toJSONString(view.getTag()));
        }
    }

    // 可复用的VH
    class ComViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title;
        public TextView tv_psn_name;

        public ComViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_psn_name = (TextView) itemView.findViewById(R.id.tv_psn_name);
        }
    }


    /**
     * 获取单项数据
     */

    private Map<String, Object> getData(int position) {

        return mDatas.get(position);
    }

    /**
     * 获取全部数据
     */
    public List<Map<String, Object>> getDatas() {

        return mDatas;
    }

    /**
     * 清除数据
     */
    public void clearData() {

        mDatas.clear();
        notifyItemRangeRemoved(0, mDatas.size());
    }

}
