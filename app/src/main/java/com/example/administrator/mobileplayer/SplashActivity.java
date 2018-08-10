package com.example.administrator.mobileplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;

public class SplashActivity extends Activity {
    
    private Handler handler = new Handler();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //定义一个Handler,使用postDelayed()方法让activity_splash界面延迟两秒跳转
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //在主线程执行
                startMainActivity();

            }
        }, 2000);
    }

    private boolean isStartMain =false;

//开启主页面方法
    private void startMainActivity() {
        if (!isStartMain){
            isStartMain = true;
            //Intent机制用来协助应用间的交互与通讯
            // 通过Intent，你的程序可以向Android表达某种请求或者意愿
            // Android会根据意愿的内容选择适当的组件来响应
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);

            //关闭当前启动页面
            finish();
        }
    }

    //不用等两秒，可以触屏进入主界面
    public boolean onTouchEvent(MotionEvent event){
        startMainActivity();
        return super.onTouchEvent(event) ;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}

