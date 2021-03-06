package com.liger.myzhihu.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liger.myzhihu.R;
import com.liger.myzhihu.adapter.NewsItemAdapter;
import com.liger.myzhihu.model.News;
import com.liger.myzhihu.ui.MainActivity;
import com.liger.myzhihu.utils.Constants;
import com.liger.myzhihu.utils.HttpUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 各新闻类型的主页面
 * Created by Shuai on 2015/12/18.
 */
public class NewsFragment extends BaseFragment {

    @BindView(R.id.newsfragment_listview)
    ListView listView;
    private ImageView imgTitle;
    private TextView textViewTitle;

    private String newsId;   //新闻id
    private String title;//新闻标题

    private ImageLoader imageLoader;

    private News news;
    private NewsItemAdapter adapter;

    public static NewsFragment newInstance(String id, String title) {
        NewsFragment newsFragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("title", title);
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            newsId = bundle.getString("id");
            title = bundle.getString("title");
        }
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) mActivity).setToolbarTitle(title);
        View view = inflater.inflate(R.layout.newsfragment_layout, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        imageLoader = ImageLoader.getInstance();
        addHeader();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    /**
     * 添加ListView的头部
     */
    private void addHeader() {
        View header = LayoutInflater.from(mActivity).inflate(R.layout.news_header, listView, false);
        imgTitle = ButterKnife.findById(header, R.id.image_title);
        textViewTitle = ButterKnife.findById(header, R.id.textview_title);
        listView.addHeaderView(header);
    }

    @Override
    protected void initData() {
        super.initData();
        if (HttpUtils.isNetworkConnected(mActivity)) {
            OkHttpUtils.get()
                    .url(Constants.BASEURL + Constants.THEMENEWS + newsId)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getWritableDatabase();
                            db.execSQL("replace into CacheList(date,json) values(" + (Constants.BASE_COLUMN + Integer.parseInt(newsId))
                                    + ",' " + response + "')");
                            db.close();
                            parseJson(response);
                            setAdapter();
                        }
                    });
        } else {
            SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date = " + (Constants.BASE_COLUMN +
                    Integer.parseInt(newsId)), null);
            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseJson(json);
            }
            cursor.close();
            db.close();
        }
    }

    private void parseJson(String responseString) {
        Gson gson = new Gson();
        news = gson.fromJson(responseString, News.class);
    }

    private void setAdapter() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        textViewTitle.setText(news.getDescription());
        imageLoader.displayImage(news.getImage(), imgTitle, options);
        adapter = new NewsItemAdapter(mActivity, news.getStories());
        listView.setAdapter(adapter);
    }
}
