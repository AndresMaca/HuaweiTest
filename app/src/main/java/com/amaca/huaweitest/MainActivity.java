package com.amaca.huaweitest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getToken().start();


    }


    private Thread getToken() {
       return new Thread() {
            @Override
            public void run() {
                try {
                    // read from agconnect-services.json
                    String appId = AGConnectServicesConfig.fromContext(getApplicationContext()).getString("client/app_id");
                    final String token = HmsInstanceId.getInstance(getApplicationContext()).getToken(appId, "HCM");
                    Log.i(TAG, "get token:" + token);
                    if(!TextUtils.isEmpty(token)) {
                        sendRegTokenToServer(token);
                    }
                    runOnUiThread(new Thread(){
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Token: " +token, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (final ApiException e) {
                    Log.e(TAG, "get token failed, " + e);
                    runOnUiThread(new Thread(){
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Error" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };
    }
    private void sendRegTokenToServer(String token) {
        Log.i(TAG, "sending token to server. token:" + token);
        Toast.makeText(this, "Token: "+token, Toast.LENGTH_SHORT).show();
    }



}
