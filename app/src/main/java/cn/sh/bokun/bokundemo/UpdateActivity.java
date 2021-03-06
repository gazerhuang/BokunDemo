package cn.sh.bokun.bokundemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.azhon.appupdate.manager.DownloadManager;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import cn.sh.bokun.bokundemo.utils.CommonUtils;
import cn.sh.bokun.bokundemo.utils.DownloadUtil;
import cn.sh.bokun.bokundemo.utils.OkHttpUtils;
import okhttp3.Response;

public class UpdateActivity extends AppCompatActivity {

    private static final String TAG = "UpdateActivity";
    private ProgressDialog progressDialog;
    private Button button;
    private String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final int PERMS_REQUEST_CODE = 200;

    private DownloadManager manager;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            Bundle data = message.getData();
            String val = data.getString("value");
            Log.i(TAG, "handler-->" + val);
            if(!Objects.requireNonNull(val).isEmpty()){
                try {
                    JSONObject jsonObject = JSONObject.parseObject(val);
                    if(CommonUtils.getVersionCode(UpdateActivity.this)<jsonObject.getIntValue("versionCode")){
                        //Toast.makeText(MainActivity.this,"检测到最新版本："+jsonObject.getIntValue("lastVision"),Toast.LENGTH_SHORT).show();
                        showUpdaloadDialog(jsonObject.getString("msg"),jsonObject.getString("url"));
                    }
                    else {
                        Toast.makeText(UpdateActivity.this,"已经是最新版本",Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    });


    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        TextView textView = findViewById(R.id.tv_version);
        button = findViewById(R.id.btn_update);

        String now_ver = "当前版本:"+ CommonUtils.getVersionCode(this);
        textView.setText(now_ver);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1&&
                PackageManager.PERMISSION_GRANTED!=checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            requestPermissions(perms,PERMS_REQUEST_CODE);
        }else {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread(loadScene).start();
                }
            });
        }
    }

    Runnable loadScene = new Runnable() {
        @Override
        public void run() {

            OkHttpUtils.sendOkHttpRequest("http://104.243.20.19/HN/checkUpdate", new okhttp3.Callback() {

                @Override
                public void onFailure(okhttp3.Call call, IOException e) {

                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    //得到返回的内容
                    String responseData = Objects.requireNonNull(response.body()).string();
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", responseData);
                    msg.setData(data);
                    handler.sendMessage(msg);
                }

            });
        }
    };

    private void showUpdaloadDialog(String msg, final String downloadUrl){
        new AlertDialog.Builder(this)
                .setTitle("发现新版本")
                .setMessage(msg)
                .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        manager = DownloadManager.getInstance(UpdateActivity.this);
                        manager.setApkName("HN.apk")
                                .setAuthorities("cn.sh.bokun.bokundemo.fileprovider")
                                .setApkUrl(downloadUrl)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .download();
                    }
                }).create().show();
    }





    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){
        switch(permsRequestCode){
            case PERMS_REQUEST_CODE:
                boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(storageAccepted){
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new Thread(loadScene).start();
                        }
                    });
                }
                break;

        }
    }
}
