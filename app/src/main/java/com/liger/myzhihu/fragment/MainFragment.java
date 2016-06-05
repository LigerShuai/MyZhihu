package com.liger.myzhihu.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.liger.myzhihu.R;
import com.liger.myzhihu.adapter.MainNewsItemAdapter;
import com.liger.myzhihu.model.Before;
import com.liger.myzhihu.model.Latest;
import com.liger.myzhihu.model.StoriesEntity;
import com.liger.myzhihu.ui.MainActivity;
import com.liger.myzhihu.utils.Constants;
import com.liger.myzhihu.utils.HttpUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 默认的主页面 （今日热闻界面）
 * Created by Shuai on 2015/12/18.
 */
public class MainFragment extends BaseFragment {

    @BindView(R.id.mainfragment_listview)    ListView listView;
    private boolean isLoading = false;//是否从网络加载 ？
    private Latest latest;
    private String date;//  最新消息的时间
    private Handler handler = new Handler();
    private MainNewsItemAdapter adapter;
    private Before before;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) mActivity).setToolbarTitle("今日热闻");
        View view = inflater.inflate(R.layout.mainfragment, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        adapter = new MainNewsItemAdapter(mActivity);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            // 防止ListView的滑动与下拉刷新冲突
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (listView != null && listView.getChildCount() > 0) {
                    boolean enableRefresh = (firstVisibleItem == 0) && (view.getChildAt(firstVisibleItem).getTop() == 0);
                    ((MainActivity) mActivity).setRefreshEnable(enableRefresh);

                    //滑到底部时，加载过去的消息
                    if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading) {
                        loadMore(Constants.BASEURL + Constants.BEFORE + date);
                    }
                }
            }
        });
    }

    /**
     * 加载过去的消息。若有网络，从网络加载数据并存入数据库，再从数据库获取；若没有网络，直接从数据库获取
     */
    private void loadMore(String url) {
        isLoading = true;
        if (HttpUtils.isNetworkConnected(mActivity)) {
            OkHttpUtils.get()
                    .url(url)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            //将数据存入数据库
                            SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getWritableDatabase();
                            db.execSQL("replace into CacheList(date,json) values(" + date + ",' " + response + "')");
                            db.close();
                            parseBeforeJson(response);
                            Log.d("shuai", "loadmore");
                        }
                    });
        } else {
            SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date = " + date, null);
            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseBeforeJson(json);
            } else {
                db.delete("CacheList", "date < " + date, null);//删除比当前时间小的表
                isLoading = false;
                Snackbar.make(listView, "每有更多的离线内容了...", Snackbar.LENGTH_LONG).show();
            }
            cursor.close();
            db.close();
        }
    }

    /**
     * 解析过去的消息
     */
    private void parseBeforeJson(String responseString) {
        Gson gson = new Gson();
        before = gson.fromJson(responseString, Before.class);
        if (before == null) {
            isLoading = false;
            return;
        }
        date = before.getDate();
        //添加数据源
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<StoriesEntity> list = before.getStories();
                StoriesEntity topic = new StoriesEntity();
                topic.setType(Constants.TOPIC);
                topic.setTitle(convertDate(date));
                list.add(0, topic);
                adapter.addList(list);
                isLoading = false;
            }
        });
    }

    /**
     * 将时间转换为正常的年月日
     */
    private String convertDate(String date) { //date: 20151218
        StringBuilder time = new StringBuilder();
        time.append(date.substring(0, 4)).append("年")
                .append(date.substring(5, 6)).append("月")
                .append(date.substring(7, 8)).append("日");
        /*String timeStr = "";
        timeStr = timeStr.concat(date.substring(0, 4)).concat("年")
                .concat(date.substring(5, 6)).concat("月")
                .concat(date.substring(7, 8)).concat("日");*/
        return time.toString();
    }

    @Override
    protected void initData() {
        super.initData();
        loadFirst();
    }

    /**
     * 进入app时第一次加载数据
     */
    private void loadFirst() {
        isLoading = true;
        if (HttpUtils.isNetworkConnected(mActivity)) {
            OkHttpUtils.get()
                    .url(Constants.BASEURL + Constants.LATESTNEWS)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            //将数据存入数据库
                            SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getWritableDatabase();
                            db.execSQL("replace into CacheList(date,json) values(" + Constants.LATEST_COLUMN + ",' " + response + "')");
                            db.close();
                            parseLatestJson(response);
                            Log.d("shuai", "loadfirst");
                        }
                    });
           /* Request request = new Request.Builder().url(Constants.BASEURL+Constants.LATESTNEWS).build();
            OkHttpUtil.enqueue(request, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseString =  response.body().string();
                    //将数据存入数据库
                    SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getWritableDatabase();
                    db.execSQL("replace into CacheList(date,json) values(" + Constants.LATEST_COLUMN + ",' " + responseString + "')");
                    db.close();
                    parseLatestJson(responseString);
                }
            });*/
        } else {
            SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date = " + Constants.LATEST_COLUMN, null);
            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseLatestJson(json);
            } else {
                isLoading = false;
            }
            cursor.close();
            db.close();
        }
    }

    private void parseLatestJson(String responseString) {
        Gson gson = new Gson();
        latest = gson.fromJson(responseString, Latest.class); //按照Latest的格式解析数据
        date = latest.getDate();

        // 添加数据源
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<StoriesEntity> list = latest.getStories();
                StoriesEntity topic = new StoriesEntity();
                topic.setType(Constants.TOPIC);
                topic.setTitle("今日热闻");
                list.add(0, topic);  // 将topic插入到List的第0个位置，List中原来的值依次后移
                adapter.addList(list);
                isLoading = false;
            }
        });
    }
}