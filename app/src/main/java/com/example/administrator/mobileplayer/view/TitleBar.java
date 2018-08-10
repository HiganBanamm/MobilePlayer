package com.example.administrator.mobileplayer.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.mobileplayer.R;

/*自定义类来设置点击事件*/
public class TitleBar extends LinearLayout {
    private View tv_search;
    private View rl_game;
    private View iv_history;
    private  Context context;

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    protected void onFinishInflate(){
        super.onFinishInflate();
        tv_search = getChildAt(1);
        rl_game = getChildAt(2);
        iv_history = getChildAt(3);
MyonClickListener myonClickListener = new MyonClickListener();

        tv_search.setOnClickListener(myonClickListener);
        rl_game.setOnClickListener(myonClickListener);
        iv_history.setOnClickListener(myonClickListener);
    }

    //自定义点击事件
    class MyonClickListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_search:
                    Toast.makeText(context,"搜索",Toast.LENGTH_SHORT).show();
                    break;

                case R.id.rl_game:
                    Toast.makeText(context,"游戏",Toast.LENGTH_SHORT).show();
                    break;

                case R.id.iv_history:
                    Toast.makeText(context,"播放历史",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
