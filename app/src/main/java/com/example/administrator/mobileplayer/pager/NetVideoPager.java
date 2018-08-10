package com.example.administrator.mobileplayer.pager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.mobileplayer.base.BasePager;

/*作用：这是一个网络视频的页面*/
public class NetVideoPager extends BasePager {
    private TextView textView;

    /*  用来实例化VideoPager*/
    public NetVideoPager(Context context) {
        super(context);
    }

    /*初始化视图*/
    @Override
    public View initView() {
        textView = new TextView(context);
        return textView;
    }

    /*当孩子需要初始化数据时，重写该方法，用于请求数据，或者显示数据*/
    public void initData(){
        super.initData();
        textView.setText("我是网络视频");
    }
}
