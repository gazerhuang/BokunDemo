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
import android.support.annotation.NonNull;
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

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            Bundle data = message.getData();
            String val = data.getString("value");
            Log.i(TAG, "handler-->" + val);
            if(!Objects.requireNonNull(val).isEmpty()){
                try {
                    JSONObject jsonObject = JSONObject.parseObject(val);
                    if(CommonUtils.getVersionCode(UpdateActivity.this)<jsonObject.getIntValue("lastVision")){
                        //Toast.makeText(MainActivity.this,"检测到最新版本："+jsonObject.getIntValue("lastVision"),Toast.LENGTH_SHORT).show();
                        showUpdaloadDialog(jsonObject.getString("msg"),jsonObject.getString("address"));
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

            OkHttpUtils.sendOkHttpRequest("http://104.243.20.19/api/CheckUpdate.php", new okhttp3.Callback() {

                @Override
                public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
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
        // 这里的属性可以一直设置，因为每次设置后返回的是一个builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置提示框的标题
        builder.setTitle("版本升级").
                setIcon(R.mipmap.ic_launcher). // 设置提示框的图标
                setMessage(msg).// 设置要显示的信息
                setPositiveButton("确定", new DialogInterface.OnClickListener() {// 设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startUpload(downloadUrl);//下载最新的版本程序
            }
        }).setNegativeButton("取消", null);//设置取消按钮,null是什么都不做，并关闭对话框
        AlertDialog alertDialog = builder.create();
        // 显示对话框
        alertDialog.show();
    }

    private void startUpload(String downloadUrl){
        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("正在下载新版本");
        progressDialog.setCancelable(false);//不能手动取消下载进度对话框
        progressDialog.show();

        final String fileSavePath=DownloadUtil.getSaveFilePath(downloadUrl);

        DownloadUtil.get().download(downloadUrl, "download", new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess() {
                progressDialog.dismiss();
                openAPK(fileSavePath);
            }
            @Override
            public void onDownloading(int progress) {
                progressDialog.setProgress(progress);
            }
            @Override
            public void onDownloadFailed() {
                progressDialog.dismiss();
            }
        });


    }

    private void openAPK(String fileSavePath){
        File file=new File(fileSavePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//判断版本大于等于7.0
            // "com.ansen.checkupdate.fileprovider"即是在清单文件中配置的authorities
            // 通过FileProvider创建一个content类型的Uri
            data = FileProvider.getUriForFile(this, "cn.sh.bokun.bokundemo.fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);// 给目标应用一个临时授权
        } else {
            data = Uri.fromFile(file);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
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
