package com.liger.myzhihu.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.liger.myzhihu.R;
import com.liger.myzhihu.model.MenuListItem;
import com.liger.myzhihu.model.Themes;
import com.liger.myzhihu.ui.MainActivity;
import com.liger.myzhihu.utils.Constants;
import com.liger.myzhihu.utils.HttpUtils;
import com.liger.myzhihu.utils.PreferUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by Shuai on 2015/12/17.
 */
public class MenuFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.tv_login)    TextView login;
    @BindView(R.id.tv_backup)    TextView backup;
    @BindView(R.id.tv_download)    TextView download;
    @BindView(R.id.top_linear_menu)    LinearLayout topMenu;
    @BindView(R.id.tv_main)    TextView main;//首页
    @BindView(R.id.listview)    ListView listView;

    private Themes themes;

    private List<MenuListItem> items;// 新闻条目 的数据源
    private NewsTypeAdapter adapter;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu, null);
        ButterKnife.bind(this, view);
        init(view);
        return view;
    }

    private void init(View view) {
        /*透明度动画*/
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(1000);
        view.startAnimation(alphaAnimation);
        /*旋转动画*/
        //pivotX pivotY 为旋转中心点的坐标
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, 100, 100);
        /*缩放动画*/
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 2, 0, 2);
        /*位移动画*/
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 200, 0, 300);

        /*动画集合*/
        AnimationSet animationSet = new AnimationSet(true);

        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(1000);
        animationSet.addAnimation(aa);

        TranslateAnimation ta = new TranslateAnimation(0, 100, 0, 200);
        ta.setDuration(1000);
        animationSet.addAnimation(ta);

        view.startAnimation(animationSet);

        /* ObjectAnimator */
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translateX", 300);
        objectAnimator.setDuration(300).start();

        /*  PropertyValuesHolder */
        PropertyValuesHolder pvh1 = PropertyValuesHolder.ofFloat("translationX", 300f);
        PropertyValuesHolder pvh2 = PropertyValuesHolder.ofFloat("scaleX", 1f, 0, 1f);
        PropertyValuesHolder pvh3 = PropertyValuesHolder.ofFloat("scaleY", 1f, 0, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvh1, pvh2, pvh3).setDuration(1000).start();

        /* ValueAnimator */
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 100);
        valueAnimator.setTarget(view);
        valueAnimator.setDuration(1000).start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                //TO DO use the value
            }
        });

        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0.5f);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "translationX", 300f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        animatorSet.playTogether(animator1, animator2, animator3);
        animatorSet.start();


        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) mActivity).loadLatest();
                ((MainActivity) mActivity).closeDrawer();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
                        .replace(R.id.framelayout_content,
                                NewsFragment.newInstance(items.get(position).getId(), items.get(position).getTitle()), "news")
                        .commit();
                ((MainActivity) mActivity).setCurrentFragmentId(items.get(position).getId());
                ((MainActivity) mActivity).closeDrawer();
            }
        });
    }

    //进入侧滑菜单时，加载数据。若有网络，从网络获取数据并存入Pref文件；若无网络则从Pref文件获取
    @Override
    protected void initData() {
        items = new ArrayList<>();
        if (HttpUtils.isNetworkConnected(mActivity)) {
            OkHttpUtils.get()
                    .url(Constants.BASEURL + Constants.THEMES)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            // 把数据存放在 SharedPreferences 里
                            PreferUtil.putStringToDefault(mActivity, Constants.THEMES, response);
                            parseJson(response);
                            listView.setAdapter(new NewsTypeAdapter());
                        }
                    });
        } else {
            String cache = PreferUtil.getStringFromDefault(mActivity, Constants.THEMES, "");
            parseJson(cache);
           /* try {
                JSONObject object = new JSONObject(json);
                parseJson(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        }
    }

    private void parseJson(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("others");
            for (int i = 0; i < jsonArray.length(); i++) {
                MenuListItem listItem = new MenuListItem();
                JSONObject object = jsonArray.getJSONObject(i);
                // 把解析出的数据存放在实体类中
                listItem.setId(object.getString("id"));
                listItem.setTitle(object.getString("name"));
                items.add(listItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_main:
                ((MainActivity) mActivity).loadLatest();
                ((MainActivity) mActivity).closeDrawer();
                break;
        }
    }

    /**
     * 侧滑菜单里 新闻类型的适配器
     */
    public class NewsTypeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.menu_listview_item, null);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_item);
                convertView.setTag(viewHolder);
            }
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.textView.setText(items.get(position).getTitle());
            return convertView;
        }

        class ViewHolder {
            TextView textView;
        }
    }
}
