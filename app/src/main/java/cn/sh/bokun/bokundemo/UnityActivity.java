package cn.sh.bokun.bokundemo;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.unity3d.player.UnityPlayer;

import cn.sh.bokun.bokundemo.utils.UnityPlayerActivity;


public class UnityActivity extends UnityPlayerActivity {
    private final static String TAG = "UnityActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unity);

        LinearLayout ll_unity = findViewById(R.id.unityViewLayout);
        View mView = mUnityPlayer.getView();
        ll_unity.addView(mView);

        Button button = findViewById(R.id.btn_rotate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnityPlayer.UnitySendMessage("Cube", "RotateCube", "80");
                Log.d(TAG, "onClick: 111");
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("确认退出Unity吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // nothing
                    }
                }).show();
        super.onBackPressed();
    }
}
