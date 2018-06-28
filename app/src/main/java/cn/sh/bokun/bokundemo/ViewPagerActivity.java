package cn.sh.bokun.bokundemo;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ViewPagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        //找到viewpager
        ViewPager viewpager = findViewById(R.id.viewPager);

        //获取两个页面
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View page1 = inflater.inflate(R.layout.view_page_1, null);
        @SuppressLint("InflateParams") View page2 = inflater.inflate(R.layout.view_page_2, null);

        //加入到集合里
        ArrayList<View> pageList = new ArrayList<>();
        pageList.add(page1);
        pageList.add(page2);

        //建一个适配器
        MyPagerAdater pagerAdater = new MyPagerAdater(pageList);

        //设置到viewpager里，到此完成了。
        viewpager.setAdapter(pagerAdater);

    }

    class MyPagerAdater extends PagerAdapter {
        //view集合
        ArrayList<View> pageList;

        MyPagerAdater(ArrayList<View> pageList) {
            this.pageList = pageList;
        }

        //返回页面
        @NonNull
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(pageList.get(position),position);
            return pageList.get(position);
        }

        //这里是返回页面的个数，如当返回0时，则无页面，我们这里返回2个
        public int getCount() {
            return pageList.size();
        }

        //这里要返回true
        @Override
        public boolean isViewFromObject(@NonNull View arg0, @NonNull Object arg1) {
            return arg0==arg1;
        }
    }
}
