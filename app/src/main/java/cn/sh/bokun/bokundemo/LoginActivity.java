package cn.sh.bokun.bokundemo;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import cn.sh.bokun.bokundemo.utils.OkHttpUtils;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    final static private String TAG = "LoginActivity";
    private AutoCompleteTextView acc;
    private EditText pwd;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            Bundle data = message.getData();
            String val = data.getString("value");
            Log.i(TAG, "handle-val-->" + val);

            Toast.makeText(LoginActivity.this,val,Toast.LENGTH_SHORT).show();
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        new Thread(initData).start();
    }

    Runnable initData = new Runnable() {
        @Override
        public void run() {
            OkHttpUtils.postOkHttpRequest("http://192.168.50.15:8888/infraops/login", new okhttp3.Callback() {

                @Override
                public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
                    //得到返回的内容
                    String responseData = null;
                    if (response.body() != null) {
                        responseData = response.body().string();
                    }
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", responseData);
                    msg.setData(data);
                    handler.sendMessage(msg);
                }

            });
        }
    };
}
