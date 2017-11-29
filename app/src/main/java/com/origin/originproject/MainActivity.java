package com.origin.originproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.origin.simple_socket.service.DefaultCallback;
import com.origin.simple_socket.service.Rst;
import com.origin.simple_socket.utils.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onTestClick(View view) {
        APICtrl.init(getApplicationContext());
    }

    public void onTest2Click(View view) {
        APICtrl.testQuery(new DefaultCallback() {
            @Override
            public boolean callbackOnUIThread() {
                return true;
            }

            @Override
            public void onRst(String reqId, int action, Rst rst) {
                String msg = "";
                if (rst != null) {
                    if (rst.isSuccess()) {
                        msg = rst.getMsg() + ":" + Utils.bytesToHexString((byte[]) rst.getOtherRst());
                    } else {
                        msg = rst.getMsg();
                    }
                } else {
                    msg = "rst is null";
                }
                showToast(msg);
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
