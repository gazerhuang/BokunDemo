package cn.sh.bokun.bokundemo;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import java.util.Arrays;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    static String TAG = "ScanActivity";
    private ZXingScannerView mScannerView;
    private boolean mFlash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        //隐藏状态栏
        //定义全屏参数
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);

        setContentView(R.layout.activity_scan);


        //if (Build.VERSION.SDK_INT>22){
        if (ContextCompat.checkSelfPermission(ScanActivity.this,
                android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            //先判断有没有权限 ，没有就在这里进行权限的申请
            ActivityCompat.requestPermissions(ScanActivity.this,
                    new String[]{android.Manifest.permission.CAMERA},1);
        }
        //}else {}

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        ViewGroup contentFrame = findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);

        final ImageView img = findViewById(R.id.img_light);
        final TextView textView = findViewById(R.id.img_text);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlash = !mFlash;
                if(mFlash){
                    img.setImageResource(R.drawable.ic_flash_on_white_48px);
                    textView.setText("补光灯开启");
                }else{
                    img.setImageResource(R.drawable.ic_flash_off_white_48px);
                    textView.setText("补光灯关闭");
                }
                mScannerView.setFlash(mFlash);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }
    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v(TAG, rawResult.getText()); // Prints scan results
        Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        Log.d(TAG, "handleResult: " + rawResult.toString());

        Toast.makeText(getApplicationContext(),rawResult.toString(),Toast.LENGTH_SHORT).show();
        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }
}
