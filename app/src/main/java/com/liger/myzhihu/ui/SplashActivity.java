package com.liger.myzhihu.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by Shuai on 2015/12/17.
 */
public class SplashActivity extends Activity {

    @BindView(R.id.start)    ImageView startImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
        ButterKnife.bind(this);
        initImagePath();
    }

    private void initImagePath() {
        final File dir = getFilesDir();
        final File imageFile = new File(dir, "start.jpg");
        if (imageFile.exists()) {
            startImage.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
        } else {
            startImage.setImageResource(R.mipmap.start);
        }

        /**
         *  int pivotXType:    动画在X轴相对于物件位置类型
         *  float pivotXValue: 动画相对于物件的X坐标的开始位置
         */
        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        animation.setDuration(3000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 通过两层请求获取启动图像
                if (HttpUtils.isNetworkConnected(SplashActivity.this)) {
                    OkHttpUtils.get()
                            .url(Constants.BASEURL + Constants.START)
                            .build()
                            .execute(new BitmapCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {

                                }

                                @Override
                                public void onResponse(Bitmap response, int id) {

                                }
                            });
                    HttpUtils.get(Constants.START, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String imgUrl = null;
                            try {
                                JSONObject object = new JSONObject(new String(responseBody));
                                imgUrl = object.getString("img");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //下载数据使用，返回byte数据
                            HttpUtils.getImage(imgUrl, new BinaryHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                                    saveImage(imageFile, binaryData);
                                    startActivity();
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
                                    startActivity();
                                }
                            });
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            startActivity();
                        }
                    });

                } else {
                    Toast.makeText(SplashActivity.this, "没有网络连接!", Toast.LENGTH_LONG).show();
                    startActivity();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startImage.startAnimation(animation);
    }

    private void startActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void saveImage(File file, byte[] data) {
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);//将字节数组写入到此文件的输出流中
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
