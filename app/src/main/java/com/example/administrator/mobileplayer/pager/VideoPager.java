package com.example.administrator.mobileplayer.pager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.mobileplayer.R;
import com.example.administrator.mobileplayer.SystemVideoPlayer;
import com.example.administrator.mobileplayer.Utils.Utils;
import com.example.administrator.mobileplayer.base.BasePager;
import com.example.administrator.mobileplayer.domain.MediaItem;

import java.util.ArrayList;
import java.util.Formatter;

import android.os.Handler;

import org.w3c.dom.Text;

/*作用：这是一个本地视频的页面*/
public class VideoPager extends BasePager {
    private TextView textView;

    //首先定义lv_video_pager、tv_nomedie、pb_loading三个对象，然后分别实例化
    private ListView lv_video_pager;
    private TextView tv_nomedie;
    private ProgressBar pb_loading;

    private Utils utils;

    //定义集合存放视频
    private ArrayList<MediaItem> mediaItems;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    /*  用来实例化VideoPager*/
    public VideoPager(Context context) {
        super(context);
        utils = new Utils();
    }

    /*初始化视图*/
    @Override
    public View initView() {
        //inflate()方法用于找到相应的.xml文件
        View view = View.inflate(context, R.layout.video_pager, null);
        //然后分别实例化lv_video_pager、tv_nomedie、pb_loading
        lv_video_pager = (ListView) view.findViewById(R.id.lv_video_pager);
        tv_nomedie = (TextView) view.findViewById(R.id.tv_nomedie);
        pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);
        //设置点击事件
        lv_video_pager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //一点击的时候就得到每一条信息
                MediaItem mediaItem = mediaItems.get(position);
                /*//启动系统播放器
                隐示意图，通过匹配调用合适的类
                Intent intent = new Intent();
                intent.setDataAndType(Uri.parse(mediaItem.getData()), "video/*");
                context.startActivity(intent);*/

               /* //使用自己定义的播放器
               //以一个视频的方式
                Intent intent = new Intent(context, SystemVideoPlayer.class);
                intent.setDataAndType(Uri.parse(mediaItem.getData()), "video/*");
                context.startActivity(intent);*/

                //传的是视频的列表，当点击下一个按钮时，可以切换到下一个视频
                //以一个视频列表的方式
                Intent intent = new Intent(context, SystemVideoPlayer.class);
               // intent.setDataAndType(Uri.parse(mediaItem.getData()), "video/*");
                Bundle bundle = new Bundle();
                bundle.putSerializable("videolist",mediaItems);
                intent.putExtras(bundle);
                //传位置
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
        return view;
    }

    /*当孩子需要初始化数据时，重写该方法，用于请求数据，或者显示数据,
      建议view的请求和数据的初始化应该分开来做*/
    public void initData() {
        super.initData();
        getData();
    }

    //为什么定义Handler，因为在子线程不能设置适配器，所以在主线程设置，如何切换过去呢，用Handler
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //主线程

            //如果有数据
            if (mediaItems != null && mediaItems.size() > 0) {
                tv_nomedie.setVisibility(View.GONE);
                pb_loading.setVisibility(View.GONE);
                //设置适配器
                lv_video_pager.setAdapter(new VideoPagerAdapter());

            } else {
                tv_nomedie.setVisibility(View.VISIBLE);
                pb_loading.setVisibility(View.GONE);
            }
        }
    };


    private void getData() {
        //请求视频比较多，所在在子线程中进行
        new Thread() {
            public void run() {
                super.run();
                mediaItems = new ArrayList<MediaItem>();
                //加载本地视频
                //1.拿到上下文
                ContentResolver contentResolver = context.getContentResolver();
                //2.调用query(),第一个参数为扫描的路径uri,第二个参数为表里的列
                //2.1定义参数
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objects = {
                        MediaStore.Video.Media.DISPLAY_NAME,//在SDK显示的名称
                        MediaStore.Video.Media.DURATION,//视频的长度
                        MediaStore.Video.Media.SIZE,//视频文件大小
                        MediaStore.Video.Media.DATA //视频的绝对地址
                };
                //2.2调用query(),返回的是cursor
                Cursor cursor = contentResolver.query(uri, objects, null, null, null);

                //首先判断是否为空
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        MediaItem mediaItem = new MediaItem();
                        String name = cursor.getString(0);
                        mediaItem.setName(name);

                        long duration = cursor.getLong(1);
                        mediaItem.setDuration(duration);

                        long size = cursor.getLong(2);
                        mediaItem.setSize(size);

                        String data = cursor.getString(3);
                        mediaItem.setData(data);

                        //把视频添加到列表中
                        mediaItems.add(mediaItem);
                    }

                    cursor.close();
                }

                handler.sendEmptyMessage(0);

            }
        }.start();
    }


    class VideoPagerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mediaItems.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        //listView里每一条的信息
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_video_pager, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_name = convertView.findViewById(R.id.tv_name);
                viewHolder.tv_duration = convertView.findViewById(R.id.tv_duration);
                viewHolder.tv_size = convertView.findViewById(R.id.tv_size);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();

            }
            //根据集合获取数据
            MediaItem mediaItem = mediaItems.get(position);

            //设置名称
            viewHolder.tv_name.setText(mediaItem.getName());
            viewHolder.tv_size.setText(android.text.format.Formatter.formatFileSize(context, mediaItem.getSize()));
            viewHolder.tv_duration.setText(utils.stringForTime((int) mediaItem.getDuration()));
            return convertView;
        }

        //定义一个容器
        class ViewHolder {
            TextView tv_name;
            TextView tv_duration;
            TextView tv_size;

        }


    }
}
