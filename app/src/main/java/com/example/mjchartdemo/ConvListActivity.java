package com.example.mjchartdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mjchartdemo.adapter.ConvListBaseAdapter;
import com.example.mjchartdemo.adapter.ListBaseAdapter;
import com.example.mjchartdemo.base.BaseActivity;
import com.example.mjchartdemo.been.CommonToolbar;
import com.example.mjchartdemo.config.Constant;
import com.example.mjchartdemo.urlconn.EdusStringCallback;
import com.example.mjchartdemo.urlconn.ErrorToast;
import com.example.mjchartdemo.view.DividerItemDecoration;
import com.example.mjchartdemo.view.RecycleViewDivider;
import com.example.mjchartdemo.view.WrapContentLinearLayoutManager;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/12/8 0008.
 */

public class ConvListActivity extends BaseActivity {
    @Bind(R.id.common_toolbar)
    CommonToolbar mToolbar;
    @Bind(R.id.lv)
    RecyclerView mRecyclerView;
    @Bind(R.id.refresh_layout)
    MaterialRefreshLayout mRefreshLayout;


    private String tableId, pageId;


    private int totalNum = 0;
    private int start = 0;
    private final int limit = 20;
    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;


    private List<Map<String, Object>> datas = new ArrayList<>();
    private ListBaseAdapter adapter;
    private List<Map<String, Object>> childDatas = new ArrayList<>();
    private final static String TAG = "ConvListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conv_list);
        ButterKnife.bind(this);
        tableId = "17796";
        initView();
        initRefreshLayout();

    }

    @Override
    public void initView() {

        mToolbar.setTitle("会话列表");
        mToolbar.setLeftButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mToolbar.setRightButtonIcon(getResources().getDrawable(R.mipmap.ic_add_pic));
        mToolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConvListActivity.this, AddConvActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    protected void onResume() {
        super.onResume();
        datas.clear();
        childDatas.clear();
        requestData();
    }

    private void initRefreshLayout() {
        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                datas.clear();
                childDatas.clear();
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (adapter != null && adapter.getItemCount() < totalNum) {
                    Log.e(TAG, "onRefreshLoadMore: " + adapter.getItemCount());
                    loadMoreData();
                } else {
                    Snackbar.make(mRecyclerView, "没有更多了", Snackbar.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    /**
     * 下拉刷新方法
     */
    private void refreshData() {
        start = 0;
        state = STATE_REFREH;
        requestData();
    }

    /**
     * 上拉加载方法
     */
    private void loadMoreData() {
        start += limit;
        state = STATE_MORE;
        requestData();
    }

    /**
     * 分动作展示数据
     */
    private void showData() {
        Log.e(TAG, "showData: " + state);
        switch (state) {
            case STATE_NORMAL:
                normalRequest();
                break;
            case STATE_REFREH:

                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(0);
                    mRefreshLayout.finishRefresh();
                    if (datas.size() == 0) {
                        Snackbar.make(mRecyclerView, "本页无数据", Snackbar.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "showData: 执行了共x条");
                        Snackbar.make(mRecyclerView, "共" + totalNum + "条", Snackbar.LENGTH_SHORT).show();
                    }

                }

                break;
            case STATE_MORE:
                if (adapter != null) {
                    //  adapter.addData(adapter.getDatas().size(), datas, childTab);
                    datas.addAll(childDatas);
                    adapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(adapter.getDatas().size());
                    mRefreshLayout.finishRefreshLoadMore();
                    Snackbar.make(mRecyclerView, "更新了" + childDatas.size() + "条数据", Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public void requestData() {
        if (hasInternetConnected()) {
            //地址
            String volleyUrl = Constant.sysUrl + Constant.tolist;
            Log.e("TAG", "学员端登陆地址 " + Constant.sysUrl + Constant.tolist);
            //参数
            Map<String, String> map = new HashMap<>();
            map.put(Constant.tableId, tableId);
            map.put(Constant.pageId, "7686");
            map.put(Constant.start, start + "");
            map.put(Constant.limit, limit + "");
            Log.e("TAG", "学员端登陆map " + map.toString());
            //请求
            OkHttpUtils
                    .post()
                    .params(map)
                    .url(volleyUrl)
                    .build()
                    .execute(new EdusStringCallback(ConvListActivity.this) {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ErrorToast.errorToast(mContext, e);
                            mRefreshLayout.finishRefresh();
//                        ((BaseActivity) getActivity()).dialog.dismiss();
                            backStart();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.e("", "onResponse: " + "  id  " + response);
                            check(response);
                        }
                    });
        } else {
//            ((BaseActivity) getActivity()).dialog.dismiss();
            mRefreshLayout.finishRefresh();
            Toast.makeText(ConvListActivity.this, "请连接网络", Toast.LENGTH_SHORT).show();
            backStart();
        }
    }

    public void backStart() {

        //下拉失败后需要将加上limit的strat返还给原来的start，否则会获取不到数据
        if (state == STATE_MORE) {
            //start只能是limit的整数倍
            if (start > limit) {
                start -= limit;
            }
            mRefreshLayout.finishRefreshLoadMore();
        }
    }

    public void check(String menuData) {
        Map<String, Object> menuMap = JSON.parseObject(menuData,
                new TypeReference<Map<String, Object>>() {
                });
        List<Map<String, Object>> menuListMap2 = null;
        if (menuMap.containsKey("dataList")) {
            menuListMap2 = (List<Map<String, Object>>) menuMap.get("dataList");
            Log.e("menuListMap2", JSON.toJSONString(menuListMap2));
        }
        childDatas.clear();
        totalNum = Integer.parseInt(menuMap.get("dataCount") + "");
        Log.e("menuListMap2num", menuMap.get("dataCount") + "");
        if (menuListMap2 != null && menuListMap2.size() > 0) {
            for (int i = 0; i < menuListMap2.size(); i++) {
                Map<String, Object> map = menuListMap2.get(i);
                if (start > 0) {
                    childDatas.add(map);
                } else {
                    datas.add(map);
                }
            }
        }
        showData();
    }

    private void normalRequest() {
        adapter = new ListBaseAdapter(datas, this);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
       // mRecyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL));
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//                mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        adapter.setOnItemClickListener(new ListBaseAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                Log.e("TAG", "data " + data);
               // Toast.makeText(ConvListActivity.this, data, Toast.LENGTH_SHORT).show();
                toItem(data);
            }
        });
        //((BaseActivity) getActivity()).dialog.dismiss();
    }

    private void toItem(String data) {
        Map<String, Object> menuMap = JSON.parseObject(data,
                new TypeReference<Map<String, Object>>() {
                });
//        Log.e(TAG, "onItemClick: " + menuMap.get("T_17796_0").toString());
        Intent intent = new Intent(ConvListActivity.this, ChatActivity.class);
        intent.putExtra("tableId", menuMap.get("T_17796_0").toString());
        startActivity(intent);
    }
}
