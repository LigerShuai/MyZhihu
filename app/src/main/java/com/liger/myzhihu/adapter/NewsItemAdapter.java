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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Liger on 2015/12/18.
 */
public class NewsItemAdapter extends BaseAdapter {

    private Context context;
    private List<StoriesEntity> entities;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public NewsItemAdapter(Context context, List<StoriesEntity> entities) {
        this.context = context;
        this.entities = entities;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public Object getItem(int i) {
        return entities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.newsfragment_news_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
//            holder.textView = (TextView) view.findViewById(R.id.newsfragment_tv_title);
//            holder.imageView = (ImageView) view.findViewById(R.id.newsfragment_iv_title);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        StoriesEntity entity = entities.get(i);
        holder.textView.setText(entity.getTitle());
        if (entity.getImages() != null) {
            holder.imageView.setVisibility(View.VISIBLE);
            imageLoader.displayImage(entity.getImages().get(0), holder.imageView, options);// 为什么指定为 0
        } else {
            holder.imageView.setVisibility(View.GONE);
        }
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.newsfragment_iv_title)        ImageView imageView;
        @BindView(R.id.newsfragment_tv_title)        TextView textView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

   /* public class ViewHolder{
        TextView textView;
        ImageView imageView;
    }*/
}
