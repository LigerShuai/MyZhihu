package com.liger.myzhihu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liger.myzhihu.R;
import com.liger.myzhihu.model.StoriesEntity;
import com.liger.myzhihu.utils.Constants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面默认新闻的适配器
 * Created by Shuai on 2015/12/19.
 */
public class MainNewsItemAdapter extends BaseAdapter {

    private List<StoriesEntity> entities;

    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private Context context;

    public MainNewsItemAdapter(Context context) {
        this.context = context;
        this.entities = new ArrayList<>();
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    /**
     * 添加数据源
     */
    public void addList(List<StoriesEntity> items) {
        this.entities.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public Object getItem(int position) {
        return entities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.mainfragment_news_item, null);
            holder = new ViewHolder();
            holder.imgTitle = (ImageView) convertView.findViewById(R.id.mainfragment_iv_title);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.mainfragment_tv_title);
            holder.tvTopic = (TextView) convertView.findViewById(R.id.mainfragment_tv_topic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StoriesEntity entity = entities.get(position);
        // 判断该ListView的item是否有主题标识（即"今日任务字样"）
        if (entity.getType() == Constants.TOPIC) {
            holder.tvTitle.setVisibility(View.GONE);
            holder.imgTitle.setVisibility(View.GONE);
            holder.tvTopic.setVisibility(View.VISIBLE);
            holder.tvTopic.setText(entity.getTitle());
        } else {
            holder.tvTopic.setVisibility(View.GONE);
            holder.tvTitle.setVisibility(View.VISIBLE);
            holder.imgTitle.setVisibility(View.VISIBLE);
            holder.tvTitle.setText(entity.getTitle());
            imageLoader.displayImage(entity.getImages().get(0),holder.imgTitle,options);// 为什么从0开始
        }
        return convertView;
    }

    class ViewHolder{
        TextView tvTopic;
        TextView tvTitle;
        ImageView imgTitle;
    }
}
