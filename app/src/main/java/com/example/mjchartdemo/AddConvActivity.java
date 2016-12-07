package com.example.mjchartdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/12/6 0006.
 */

public class AddConvActivity extends Activity {
    private EditText etConvName;
    private TextView etConvStartPsn, etConvPsn;
    private String convStartPsn;
    final String[] psn = {"学员", "人员", "系统"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_conv);

        etConvName = (EditText) findViewById(R.id.et_conv_name);
        etConvPsn = (TextView) findViewById(R.id.et_conv_psn);
        etConvStartPsn = (TextView) findViewById(R.id.et_conv_start_psn);

        etConvStartPsn.setText(psn[0]);
        etConvStartPsn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSingleDialog();
            }
        });
        etConvPsn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddConvActivity.this, SelectPsnActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setSingleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddConvActivity.this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("请选择性别");

        //    设置一个单项选择下拉框
        /**
         * 第一个参数指定我们要显示的一组下拉单选框的数据集合
         * 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'女' 会被勾选上
         * 第三个参数给每一个单选项绑定一个监听器
         */
        builder.setSingleChoiceItems(psn, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AddConvActivity.this, "性别为：" + psn[which], Toast.LENGTH_SHORT).show();
                // convStartPsn = psn[which];
                etConvStartPsn.setText(psn[which]);
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
