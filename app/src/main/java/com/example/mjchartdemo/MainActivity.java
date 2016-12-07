package com.example.mjchartdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.mjchartdemo.been.CommonToolbar;
import com.example.mjchartdemo.config.Constant;
import com.example.mjchartdemo.urlconn.EdusStringCallback;
import com.example.mjchartdemo.urlconn.ErrorToast;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView lv_view;
    private List<Map<String, Object>> lists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CommonToolbar mToolbar = (CommonToolbar) findViewById(R.id.common_toolbar);

        mToolbar.setTitle("会话列表");
        mToolbar.setLeftButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mToolbar.setRightButtonIcon(getResources().getDrawable(R.mipmap.ic_launcher));
        mToolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddConvActivity.class);
                startActivity(intent);
            }
        });

        lv_view = (ListView) findViewById(R.id.lv_view);
//        Map<String, Object> gmap = new HashMap<>();
//        gmap.put("title", "dsdfd");
//        lists.add(gmap);
//        gmap = new HashMap<>();
//        gmap.put("title", "dsdfd");
//        lists.add(gmap);
//        gmap = new HashMap<>();
//        gmap.put("title", "dsdfd");
//        lists.add(gmap);
        //        setAdapter();
//        setAdapter();
         requestData();
    }

    public void requestData() {
        String volleyUrl = Constant.sysUrl + Constant.tolist;
        Log.e("TAG", "学员端登陆地址 " + Constant.sysUrl + Constant.tolist);
        //参数
        Map<String, String> map = new HashMap<>();
        map.put(Constant.tableId, "17796");
        map.put(Constant.pageId, "7686");


        Log.e(TAG, "maplogi/" + map.toString());
        //请求
        OkHttpUtils
                .post()
                .params(map)
                .url(volleyUrl)
                .build()
                .execute(new EdusStringCallback(MainActivity.this) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ErrorToast.errorToast(mContext, e);
                        //        dialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "onResponse: " + "  id  " + response);


                    }
                });
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

        if (menuListMap2 != null && menuListMap2.size() > 0) {
            for (int i = 0; i < menuListMap2.size(); i++) {
                Map<String, Object> map = menuListMap2.get(i);
                Map<String, Object> gmap = new HashMap<>();
                gmap.put("title", map.get("AFM_6") + "");
                lists.add(gmap);
            }
        }

        setAdapter();
    }

    private void setAdapter() {
        SimpleAdapter adapter = new SimpleAdapter(this, lists, R.layout.conversation_list_item,
                new String[]{"title"},
                new int[]{R.id.tv_title});
        lv_view.setAdapter(adapter);
    }
}
