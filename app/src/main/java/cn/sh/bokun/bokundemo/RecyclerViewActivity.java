package cn.sh.bokun.bokundemo;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cn.sh.bokun.bokundemo.adapter.RvAdapter;
import cn.sh.bokun.bokundemo.entity.RvItem;

public class RecyclerViewActivity extends AppCompatActivity {

    static private String TAG = "RecyclerViewActivity";
    private RecyclerView mRecyclerView;
    private ArrayList<RvItem> mDataList;
    private RvAdapter rvAdapter;
    JSONArray jsonArray = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        initView();
        initData();
        initAdapter();
    }


    private void initView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData() {
        mDataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RvItem item = new RvItem();
            item.setHeader("header");
            item.setTitle("title"+i);
            item.setInfo("info");
            item.setState("state");
            item.setTime("time");
            item.setImageResource(R.mipmap.ic_launcher);
            item.setDetail("detail");
            mDataList.add(item);
        }
    }

    private void initAdapter(){
        rvAdapter = new RvAdapter(R.layout.item_rv, mDataList);
        rvAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        rvAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerViewActivity.this,"position:"+position,Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerView.setAdapter(rvAdapter);
    }

}
