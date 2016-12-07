package com.example.mjchartdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.mjchartdemo.config.Constant;
import com.example.mjchartdemo.urlconn.EdusStringCallback;
import com.example.mjchartdemo.urlconn.ErrorToast;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/12/7 0007.
 */

public class SelectPsnActivity extends Activity {
    private static final String TAG ="SelectPsnActivity";
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_psn);

        listview = (ListView) findViewById(R.id.lv_listview);



    }

    public void requestData() {
        String volleyUrl = Constant.sysUrl + Constant.tolist;
        Log.e("TAG", "学员端登陆地址 " + Constant.sysUrl + Constant.tolist);
        //参数
        Map<String, String> map = new HashMap<>();
        map.put(Constant.tableId, "17797");
        map.put(Constant.pageId, "7690");
        map.put(Constant.tableId, "17796");
        map.put(Constant.pageId, "7686");
        map.put(Constant.tableId, "2");



        Log.e(TAG, "maplogi/" + map.toString());
        //请求
        OkHttpUtils
                .post()
                .params(map)
                .url(volleyUrl)
                .build()
                .execute(new EdusStringCallback(SelectPsnActivity.this) {
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
}
