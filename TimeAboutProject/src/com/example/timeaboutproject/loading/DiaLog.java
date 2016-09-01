package com.example.timeaboutproject.loading;

import com.example.timeaboutproject.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by Administrator on 2016/4/8.
 */
public class DiaLog extends Dialog {
    public DiaLog(Context context, Intent intent) {
        super(context);
        this.context=context;
        this.intent=intent;
        this.msg=msg;
    }
    private Intent intent;
    private Button ipConfirm,ipCancel;
    private Context context;
    private String msg="";
    private TextView diaMsg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_setip);
        diaMsg= (TextView) findViewById(R.id.dia_msg);

        diaMsg.setText(msg);
        ipCancel= (Button) findViewById(R.id.ip_cancel);
        ipConfirm= (Button) findViewById(R.id.ip_confirm);
        ipCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              DiaLog.this.dismiss();
            }
        });
        ipConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(intent);
                DiaLog.this.dismiss();
            }
        });
    }
}
