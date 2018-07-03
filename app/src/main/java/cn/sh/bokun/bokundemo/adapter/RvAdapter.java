package cn.sh.bokun.bokundemo.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sh.bokun.bokundemo.R;
import cn.sh.bokun.bokundemo.entity.RvItem;

public class RvAdapter extends BaseQuickAdapter<RvItem, BaseViewHolder> {

    @SuppressWarnings("unchecked")
    public RvAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RvItem item) {
        helper.setText(R.id.tv_header, item.getHeader());
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_info, item.getInfo());
        helper.setText(R.id.tv_state, item.getState());
        helper.setImageResource(R.id.avatar, item.getImageResource());
        helper.setText(R.id.tv_time, item.getTime());
        if(item.getState().equals("已完成")){
            helper.setTextColor(R.id.tv_state, Color.parseColor("#009688"));
        }
        if(item.getState().equals("待执行")){
            helper.setTextColor(R.id.tv_state, Color.parseColor("#EF5350"));
        }
        if(item.getState().equals("进行中")){
            helper.setTextColor(R.id.tv_state, Color.parseColor("#3F51B5"));
        }
    }
}
