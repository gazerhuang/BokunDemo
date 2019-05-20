package cn.sh.bokun.bokundemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.sh.bokun.bokundemo.utils.NotificationUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private long mPressedTime = 0;
    private final int mRequestCode = 100;//3.权限请求码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermission();

    }

    private void init(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            双击退出APP,不起效注意是不是有Activity没有finish掉
            //super.onBackPressed();

            long mNowTime = System.currentTimeMillis();//获取第一次按键时间
            if ((mNowTime - mPressedTime) > 2000) {//比较两次按键时间差
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mPressedTime = mNowTime;
            } else {//退出程序
                //销毁
//            this.finish();
//            System.exit(0);

                //不销毁
                moveTaskToBack(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update) {
            StartActivity(UpdateActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //Maybe...useless
            return true;
        } else if (id == R.id.nav_login) {
            StartActivity(LoginActivity.class);
        } else if (id == R.id.nav_qr) {
            StartActivity(ScanActivity.class);
        } else if (id == R.id.nav_list) {
            StartActivity(RecyclerViewActivity.class);
        } else if (id == R.id.nav_viewpager) {
            StartActivity(ViewPagerActivity.class);
        } else if (id == R.id.nav_chart) {
            StartActivity(ChartActivity.class);
        } else if (id == R.id.nav_unity) {
            StartActivity(UnityActivity.class);
        } else if (id == R.id.nav_webview) {
            StartActivity(WebViewActivity.class);
        } else if (id == R.id.nav_notification) {
            NotificationUtils.showNotification(MainActivity.this,WebViewActivity.class,"通知","Hello World");
        } else if (id == R.id.nav_call) {
            StartActivity(CallActivity.class);
        } else if (id == R.id.nav_share) {
            Intent textIntent = new Intent(Intent.ACTION_SEND);
            textIntent.setType("text/plain");
            textIntent.putExtra(Intent.EXTRA_TEXT, "https://github.com/gazerhuang/BokunDemo");
            startActivity(Intent.createChooser(textIntent, "分享"));
        } else if (id == R.id.nav_code) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse("https://github.com/gazerhuang/BokunDemo");
            intent.setData(content_url);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void StartActivity(Class<?> cls){
        Intent intent = new Intent(MainActivity.this,cls);
        startActivity(intent);
    }

    private void initPermission(){
        //1.首先声明一个数组permissions，将所有需要申请的权限都放在里面
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.CAMERA};
        //2.创建一个mPermissionList，逐个判断哪些权限未授权，将未授权的权限存储到mPermissionList中
        List<String> mPermissionList = new ArrayList<>();
        //4.权限判断和申请
        mPermissionList.clear();//清空已经允许的没有通过的权限
        //逐个判断是否还有未通过的权限
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permission);//添加还未授予的权限到mPermissionList中
            }
        }
        //申请权限
        if (mPermissionList.size()>0){//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this,permissions,mRequestCode);
        }else {
            //权限已经都通过了，可以将程序继续打开了
            init();
        }
    }

    /**
     * 5.请求权限后回调的方法
     * @param requestCode 是我们自己定义的权限请求码
     * @param permissions 是我们请求的权限名称数组
     * @param grantResults 是我们在弹出页面后是否允许权限的标识数组，数组的长度对应的是权限
     *                     名称数组的长度，数组的数据0表示允许权限，-1表示我们点击了禁止权限
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;//有权限没有通过
        if (mRequestCode==requestCode){
            for (int grantResult : grantResults) {
                if (grantResult == -1) {
                    hasPermissionDismiss = true;
                    break;
                }
            }
        }
        if (hasPermissionDismiss){//如果有没有被允许的权限
            showPermissionDialog();
        }else {
            //权限已经都通过了，可以将程序继续打开了
            init();
        }
    }

    /**
     *  6.不再提示权限时的展示对话框
     */
    AlertDialog mPermissionDialog;
    String mPackName = "cn.sh.bokun.bokundemo";

    private void showPermissionDialog() {
        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(this)
                    .setMessage("已禁用权限，请手动授予")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();

                            Uri packageURI = Uri.parse("package:" + mPackName);
                            Intent intent = new Intent(Settings.
                                    ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭页面或者做其他操作
                            cancelPermissionDialog();
                            MainActivity.this.finish();
                        }
                    })
                    .create();
        }
        mPermissionDialog.show();
    }

    private void cancelPermissionDialog() {
        mPermissionDialog.cancel();
    }
}
