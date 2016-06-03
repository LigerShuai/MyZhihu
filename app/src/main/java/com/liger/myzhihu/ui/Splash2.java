package com.liger.myzhihu.ui;

/**
 * Created by Liger on 2016/4/25.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.liger.myzhihu.R;
import com.liger.myzhihu.utils.Constants;
import com.liger.myzhihu.utils.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class Splash2 extends Activity {
    private ImageView iv_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
        iv_start = (ImageView) findViewById(R.id.start);
        initImage();
    }

    private void initImage() {
        File dir = getFilesDir(); // dir = /data/data/krelve.app.kuaihu/files
        final File imgFile = new File(dir, "start.jpg");// imgFile = /data/data/krelve.app.kuaihu/files/start.jpg
        if (imgFile.exists()) {
            iv_start.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));// imgFile.getAbsolutePath() = /data/data/krelve.app.kuaihu/files/start.jpg
        } else {
            iv_start.setImageResource(R.mipmap.start);
        }

        /**
         *  int pivotXType:    动画在X轴相对于物件位置类型
         *  float pivotXValue: 动画相对于物件的X坐标的开始位置
         */
        final ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(3000);
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (HttpUtils.isNetworkConnected(Splash2.this)) {
                    /**
                     *  AsyncHttpResponseHandler：获取 String 对象
                     *  JsonHttpResponseHandler： 获取 Json 对象
                     */
                    HttpUtils.get(Constants.START, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            try {
                                JSONObject jsonObject = new JSONObject(new String(bytes));
                                String url = jsonObject.getString("img");
                                HttpUtils.getImage(url, new BinaryHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                        saveImage(imgFile, bytes);
                                        startActivity();
                                    }

                                    @Override
                                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                        startActivity();
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            startActivity();
                        }
                    });
                } else {
                    Toast.makeText(Splash2.this, "没有网络连接!", Toast.LENGTH_LONG).show();
                    startActivity();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv_start.startAnimation(scaleAnim);

    }

    private void startActivity() {
        Intent intent = new Intent(Splash2.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    public void saveImage(File file, byte[] bytes) {
        try {
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
