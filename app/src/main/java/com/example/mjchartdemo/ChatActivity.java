package com.example.mjchartdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.mjchartdemo.adapter.ChatBaseAdapter;
import com.example.mjchartdemo.been.CommonToolbar;
import com.example.mjchartdemo.been.LoginError;
import com.example.mjchartdemo.config.Constant;
import com.example.mjchartdemo.urlconn.EdusStringCallback;
import com.example.mjchartdemo.urlconn.ErrorToast;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.zhy.http.okhttp.log.LoggerInterceptor.TAG;

/**
 * Created by Administrator on 2016/12/7 0007.
 */

public class ChatActivity extends Activity implements View.OnClickListener {
    @Bind(R.id.common_toolbar)
    CommonToolbar mToolbar;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.et_content)
    EditText etContent;
    //    @Bind(R.id.btn_send)
//    Button btnSend;
//    @Bind(R.id.btn_sel)
//    Button btnSel;
    @Bind(R.id.tv_text)
    TextView tvText;
    public ChatBaseAdapter adapter;
    @Bind(R.id.tv_text_id)
    TextView tvTextId;
    private String tableId;//会话对应的tableid

    private List<Map<String, Object>> lists = new ArrayList<>();
    private final String[] msgType = {"文本消息1697", "语音消息1698", "视频消息1699", "穿透消息1700"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        mToolbar.setTitle("聊天中");
        mToolbar.setLeftButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvText.setText(msgType[0].substring(0, 4));
        tvTextId.setText(msgType[0].substring(4));

        tableId = getIntent().getStringExtra("tableId");
        Log.e("chatActivy", "onCreate: "+tableId );
        setAdapter();

        commitData();

    }

    private void commitData() {

        lists.clear();
        String volleyUrl = Constant.sysUrl + Constant.tolist;
        Log.e("TAG", "学员端登陆地址 " + Constant.sysUrl + Constant.tolist);

        //参数
        Map<String, String> map = new HashMap<>();
        map.put(Constant.tableId, "17798");
        map.put(Constant.pageId, "7694");
        map.put(Constant.mainTableId, "17796");
        map.put(Constant.mainPageId, "7686");
        map.put(Constant.mainId, tableId);

        Log.e("AddConvActivity", "maplogi/" + map.toString());
        //请求
        OkHttpUtils
                .post()
                .url(volleyUrl)
                .params(map)
                .build()
                .execute(new EdusStringCallback(ChatActivity.this) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ErrorToast.errorToast(mContext, e);
                        //        dialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "onResponse: " + "  id  " + response);
                        check(response);
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
            if (menuListMap2 != null && menuListMap2.size() > 0) {
                for (int i = 0; i < menuListMap2.size(); i++) {
                    Map<String, Object> map = menuListMap2.get(i);
                    lists.add(map);
                }
                Collections.reverse(lists);   //这行就是将list的内容反转，下面再装进adapter里，就可以倒序显示了
                //setAdapter();
                adapter.notifyDataSetChanged();
                listview.setSelection(listview.getBottom());
            }
        }
    }

    private void setAdapter() {
        adapter = new ChatBaseAdapter(this, lists);
        listview.setAdapter(adapter);
    }

    @OnClick({R.id.btn_send, R.id.btn_sel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                sendMsg();
                break;
            case R.id.btn_sel:
                setSingleDialog();
                break;
        }
    }

    private void sendMsg() {
        String volleyUrl = Constant.sysUrl + Constant.commitAdd;
        Log.e("TAG", "学员端登陆地址 " + Constant.sysUrl + Constant.commitAdd);

        //参数
        Map<String, String> map = new HashMap<>();
        map.put(Constant.tableId, "17798");
        map.put(Constant.pageId, "7691");
        map.put("t0_au_17798_7691_36729", tableId);
        map.put("t0_au_17798_7691_36740", "1703");
        map.put("t0_au_17798_7691_36737", tvTextId.getText().toString());
        map.put("t0_au_17798_7691_36731", etContent.getText().toString());
        map.put("t0_au_17798_7691_36752", "");
        map.put("t0_au_17798_7691_36751", "");

        Log.e("ChatActivity", "maplogi/" + map.toString());
        //请求
        OkHttpUtils
                .post()
                .url(volleyUrl)
                .params(map)
                .build()
                .execute(new EdusStringCallback(ChatActivity.this) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ErrorToast.errorToast(mContext, e);
                        //        dialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("ChatActivity", "onResponse: " + "  id  " + response);
                       // check(response);
                        if (response != null&&response.length()>0) {
                            Toast.makeText(ChatActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                            etContent.setText("");
                            commitData();
                        }
                    }
                });
    }

    private void setSingleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("请选择性别");

        //    设置一个单项选择下拉框
        /**
         * 第一个参数指定我们要显示的一组下拉单选框的数据集合
         * 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'女' 会被勾选上
         * 第三个参数给每一个单选项绑定一个监听器
         */
        builder.setSingleChoiceItems(msgType, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ChatActivity.this, "消息类型：" + msgType[which], Toast.LENGTH_SHORT).show();
                // convStartPsn = psn[which];
                tvText.setText(msgType[which].substring(0, 4));
                tvTextId.setText(msgType[which].substring(4));
                dialog.dismiss();
            }
        });
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        etConvStartPsn.setText(psn[which]);
//                    }
//                });
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
        builder.show();
    }

}
