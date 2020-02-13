package com.example.zhihuribao.Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zhihuribao.MyDataBaseHelper;
import com.example.zhihuribao.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewsActivity extends AppCompatActivity {
    private WebView webView;
    private String id_user;
    private String id_news;
    private String title_news;
    private String url_news;
    private String url_1;
    private String url_2;
    private String url3;
    private String url4;
    private View comments_all;
    private View collect;
    private TextView comments_number;
    private TextView popular_number;
    private String comments;
//    private String popularity;
    private String long_comments;
    private String short_comments;
    private ImageView collect_picture;
    private ImageView zan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_news);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        fullScreen(NewsActivity.this);
        Bundle bundle = getIntent().getExtras();
        id_user = bundle.getString("id_user");
        id_news = bundle.getString("id_news");
        title_news = bundle.getString("title_news");
        url_news = bundle.getString("url_news");
        comments_number = findViewById(R.id.comments_number);
//        popular_number = findViewById(R.id.popular_number);
        comments_all = findViewById(R.id.comments);
        collect = findViewById(R.id.collect);
        collect_picture = findViewById(R.id.collect_picture);
        zan = findViewById(R.id.zan);
        url3 = "https://news-at.zhihu.com/api/3/story-extra/";
        //加载新闻内容
        NewsActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = getIntent().getExtras();
                String id_news = bundle.getString("id_news");
                url_1 = "https://daily.zhihu.com/story/";
                url_2 = url_1 + id_news;
                webView = findViewById(R.id.news);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl(url_2);
            }
        });
        //返回主页面
        ImageView imageView = (ImageView) findViewById(R.id.back_news_main);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        //初始化点赞图标
        MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(NewsActivity.this);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
        Cursor cursor = database.query("zan", new String[]{"id_user", "id_news", "id_zan"}, "id_user=?and id_news=?", new String[]{id_user, id_news}, null, null, null);
        if (cursor.moveToFirst()) {
            zan.setImageResource(R.drawable.zan);
            zan.invalidate();
        } else {
            zan.setImageResource(R.drawable.dianzan);
            zan.invalidate();
        }
        cursor.close();
        //初始化收藏图标
        Cursor cursor2 = database.query("collect", new String[]{"id_user", "id_news", "id_collect"}, "id_user=?and id_news=?", new String[]{id_user, id_news}, null, null, null);
        if (cursor2.moveToFirst()) {
            collect_picture.setImageResource(R.drawable.shoucang2);
            collect_picture.invalidate();
        } else {
            collect_picture.setImageResource(R.drawable.shoucang);
            collect_picture.invalidate();
        }
        cursor2.close();
        database.close();
        //设置点赞图标的点击事件
        zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(NewsActivity.this);
                SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
                Cursor cursor = database.query("zan", new String[]{"id_user", "id_news", "id_zan"}, "id_user=?and id_news=?", new String[]{id_user, id_news}, null, null, null);
                if (cursor.moveToFirst()) {
                    String id_zan = cursor.getString(cursor.getColumnIndex("id_zan"));
                    database.delete("zan", "id_zan=?", new String[]{id_zan});
                    zan.setImageResource(R.drawable.dianzan);
                    zan.invalidate();
                    Toast.makeText(NewsActivity.this, "取消点赞", Toast.LENGTH_SHORT).show();
                } else {
                    database.execSQL("insert into zan(id_user,id_news,title_news,url_news) values('" + id_user + "','" + id_news + "','" + title_news + "','" + url_news + "');");
                    zan.setImageResource(R.drawable.zan);
                    zan.invalidate();
                    Toast.makeText(NewsActivity.this, "点赞~", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
                database.close();
            }
        });
        //设置收藏图标的点击事件
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(NewsActivity.this);
                SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
                Cursor cursor = database.query("collect", new String[]{"id_user", "id_news", "id_collect"}, "id_user=?and id_news=?", new String[]{id_user, id_news}, null, null, null);
                if (cursor.moveToFirst()) {
                    String id_collect = cursor.getString(cursor.getColumnIndex("id_collect"));
                    database.delete("collect", "id_collect=?", new String[]{id_collect});
                    collect_picture.setImageResource(R.drawable.shoucang);
                    collect_picture.invalidate();
                    Toast.makeText(NewsActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                } else {
                    database.execSQL("insert into collect(id_user,id_news,title_news,url_news) values('" + id_user + "','" + id_news + "','" + title_news + "','" + url_news + "');");
                    collect_picture.setImageResource(R.drawable.shoucang2);
                    collect_picture.invalidate();
                    Toast.makeText(NewsActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
                database.close();
            }
        });

        //下面是联网部分
        //开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    url4 = url3 + id_news;
                    URL url = new URL(url4);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    //读取刚刚获取的输入流
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    showResponse(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }).

                start();

    }

    public void showResponse(final String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            comments = jsonObject.getString("comments");
            long_comments = jsonObject.getString("long_comments");
            short_comments = jsonObject.getString("short_comments");
//            popularity = jsonObject.getString("popularity");


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    comments_number.setText(comments);
//                    popular_number.setText(popularity);
                    //跳转到评论
                    comments_all.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(NewsActivity.this, CommentsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("comments", comments);
                            bundle.putString("long_comments", long_comments);
                            bundle.putString("short_comments", short_comments);
                            bundle.putString("id_user", id_user);
                            bundle.putString("id_news", id_news);
                            bundle.putString("title_news", title_news);
                            bundle.putString("url_news", url_news);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //设置隐藏状态栏
    public void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //设置状态栏为透明，否则在部分手机上会呈现系统默认的浅灰色
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以考虑设置为透明色
                //window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }
}
