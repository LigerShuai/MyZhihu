package com.liger.myzhihu.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.liger.myzhihu.R;
import com.liger.myzhihu.db.CacheDbHelper;
import com.liger.myzhihu.fragment.MainFragment;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SwipeRefreshLayout refreshLayout;
    private DrawerLayout drawerLayout;
    private FrameLayout framelayout;

    private String currentFragmentId;
    private CacheDbHelper dbHelper;

    private long firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new CacheDbHelper(this, 1);//指定数据库版本号为1

        initView();
        loadLatest();
    }

    /**
     * 加载最新新闻，在首页显示
     */
    public void loadLatest() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout_content,new MainFragment(),"latest")
                .commit();
        currentFragmentId = "latest";
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        framelayout = (FrameLayout) findViewById(R.id.framelayout_content);

        setSupportActionBar(toolbar);

        refreshLayout.setColorSchemeColors(android.R.color.holo_blue_bright, android.R.color.holo_green_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                replaceMainFragment();
                refreshLayout.setRefreshing(false);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

    }

    /**
     * 刷新,替换为主页面
     */
    private void replaceMainFragment() {
        if (currentFragmentId.equals("latest")){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.framelayout_content, new MainFragment(),"latest")
                    .commit();
        }
    }

    /**
     * 设置当前界面显示的fragment的id
     */
    public void setCurrentFragmentId(String name) {
        currentFragmentId = name;
    }

    //关闭Drawer
    public void closeDrawer(){
        drawerLayout.closeDrawers();
    }

    //设置ToolBar的标题
    public void setToolbarTitle(String text) {
        toolbar.setTitle(text);
    }

    //获取CacheDbHelper对象
    public CacheDbHelper getCacheDbHelper() {
        return dbHelper;
    }

    //设置是否允许下拉刷新
    public void setRefreshEnable(boolean enable) {
        refreshLayout.setEnabled(enable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mode:
                Toast.makeText(this, "mode", Toast.LENGTH_SHORT).show();
                break;
        }
        /*if (id == R.id.action_settings) {
            Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            closeDrawer();
        } else {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Snackbar.make(framelayout, "再按一次退出", Snackbar.LENGTH_SHORT).show();
                firstTime = secondTime;
            } else {
                finish();
            }
        }
    }
}
