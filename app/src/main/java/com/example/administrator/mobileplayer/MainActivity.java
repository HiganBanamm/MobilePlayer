package com.example.administrator.mobileplayer;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.mobileplayer.base.BasePager;
import com.example.administrator.mobileplayer.base.ReplaceFragment;
import com.example.administrator.mobileplayer.pager.AudioPager;
import com.example.administrator.mobileplayer.pager.NetAudioPager;
import com.example.administrator.mobileplayer.pager.NetVideoPager;
import com.example.administrator.mobileplayer.pager.VideoPager;

import java.util.ArrayList;
//作用：主界面

public class MainActivity extends FragmentActivity {

    private RadioGroup rg_main;

    /*用BasePager做泛型*/
    private ArrayList<BasePager> basePagers;

    //4个页面分别对应的位置
    private int position ;
    @Override
    //生命周期方法之一
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //主界面
        setContentView(R.layout.activity_main);

        rg_main = (RadioGroup) findViewById(R.id.rg_main);
        /*把AudioPager、NetAudioPager、NetVideoPager、VideoPager实例化出来*/
        /*将这几个页面放到一个集合里面，在此之前将RadioGroup实例化（定义出来），以及将那4个放在集合中*/
        basePagers = new ArrayList<>();
        basePagers.add(new VideoPager(this));//将本地视频放入集合中,序号为0
        basePagers.add(new AudioPager(this));//将本地音乐放入集合中
        basePagers.add(new NetVideoPager(this));//将网络视频放入集合中
        basePagers.add(new NetAudioPager(this));//将网络音乐放入集合中

        rg_main.setOnCheckedChangeListener(new MyOnCheckChangedListener());
        rg_main.check(R.id.rb_video);//默认是本地视频
    }

    class MyOnCheckChangedListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                default://本地视频
                    position=0;
                    break;
                case R.id.rb_audio: //本地音乐
                    position=1;
                    break;

                case R.id.rb_net_video:
                    position=2;
                    break;
                case R.id.rb_net_audio:
                    position=3;
                    break;
            }

            setFragment();
        }
    }

    private void setFragment() {
        //1.获取到FragmentManager
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //2.开启一个事务，通过调用beginTransaction()方法开启
        FragmentTransaction ft = fm.beginTransaction();

        //3.向容器内加入碎片，一般使用replace()方法实现，需要传入容器的id和待添加的碎片实例
        ft.replace(R.id.fl_main,new ReplaceFragment(getBasePager()));

        //4.提交事务，调用commit()方法来完成
        ft.commit();

    }

    private BasePager getBasePager() {
        //根据位置获取Pager
        BasePager basePager = basePagers.get(position);
        if (basePager != null &&!basePager.isInitData ){
            basePager.isInitData = true;
            basePager.initData();
        }
        return basePager;
    }
}
