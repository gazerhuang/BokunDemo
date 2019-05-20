package cn.sh.bokun.bokundemo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CallActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call("10086");
            }
        });
    }

    private void call(String s){
        //获取输入的电话号码
        String phone = s.trim();
        //创建打电话的意图
        Intent intent = new Intent();
        //设置拨打电话的动作
        intent.setAction(Intent.ACTION_CALL);
        //设置拨打电话的号码
        intent.setData(Uri.parse("tel:" + phone));
        //开启打电话的意图
        startActivity(intent);
    }
}
