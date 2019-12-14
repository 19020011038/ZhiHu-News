package com.example.zhihuribao.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zhihuribao.Adapter.CommentsAdapter;
import com.example.zhihuribao.Adapter.MainAdapter;
import com.example.zhihuribao.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentsActivity extends AppCompatActivity {
    private String url_1;
    private String url_2;
    private String url_3;
    private String url_4;
    private String url_5;
    private String id_news;
    private String id_user;
    private String title_news;
    private String url_news;
    private String comments;
    private String long_comments;
    private String short_comments;
    private int Flag = 0;
    private List<Map<String, Object>> list = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_comments);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        recyclerView = findViewById(R.id.recyclerView_comments);
        //初始化全局变量
        Bundle bundle = getIntent().getExtras();
        id_user = bundle.getString("id_user");
        id_news = bundle.getString("id_news");
        title_news = bundle.getString("title_news");
        url_news = bundle.getString("url_news");
        comments = bundle.getString("comments");
        long_comments = bundle.getString("long_comments");
        Log.d("yyx1", long_comments);
        short_comments = bundle.getString("short_comments");

        url_1 = "https://news-at.zhihu.com/api/4/story/";
        url_2 = "/long-comments";
        url_4 = "/short-comments";

        //显示出评论总数
        TextView textView = (TextView) findViewById(R.id.title_comments);
        textView.setText(comments + "条评论");
        TextPaint tp = textView.getPaint();
        tp.setFakeBoldText(true);
        //返回新闻页面
        ImageView imageView = (ImageView) findViewById(R.id.back_comments_news);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommentsActivity.this, NewsActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("comments", comments);
                bundle1.putString("long_comments", long_comments);
                bundle1.putString("short_comments", short_comments);
                bundle1.putString("id_user", id_user);
                bundle1.putString("id_news", id_news);
                bundle1.putString("title_news", title_news);
                bundle1.putString("url_news", url_news);
                intent.putExtras(bundle1);
                startActivity(intent);
                finish();
            }
        });
        //如果长评是0
        if (long_comments.equals("0")) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection connection = null;
                    BufferedReader reader = null;
                    try {
                        url_5 = url_1 + id_news + url_4;
                        URL url = new URL(url_5);
                        Log.d("duanpingwangzhi", url_5);
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
                        showResponse2(response.toString());
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
            }).start();
        }
        //如果长评是0
        else {
            //联网获取长评
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection connection = null;
                    BufferedReader reader = null;
                    try {
                        url_3 = url_1 + id_news + url_2;
                        Log.d("yyx2", url_3);
                        URL url = new URL(url_3);
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
            }).start();


        }
    }

    public void showResponse(final String string) {
        Map map1 = new HashMap();
        map1.put("type", 1);
        list.add(map1);
        if (long_comments.equals("0")) {
        } else {
            try {
                JSONObject jsonObject = new JSONObject(string);
                JSONArray jsonArray = jsonObject.getJSONArray("comments");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String author = jsonObject1.getString("author");
                    String content = jsonObject1.getString("content");
                    String avatar = jsonObject1.getString("avatar");
                    String time = jsonObject1.getString("time");
                    String likes = jsonObject1.getString("likes");
//                    JSONObject reply_to = jsonObject1.getJSONObject("reply_to");
//                    String reply_to_content = reply_to.getString("content");
//                    String reply_to_author = reply_to.getString("author");


                    Map map = new HashMap();
                    map.put("author", author);
                    map.put("content", content);
                    map.put("avatar", avatar);
                    map.put("time", time);
                    map.put("likes", likes);
//                    map.put("reply_to_content", reply_to_content);
//                    map.put("reply_to_author", reply_to_author);
                    map.put("type", 2);


                    list.add(map);
                }
                Flag = 1;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //联网获得短评
                        if (Flag == 1) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    HttpURLConnection connection = null;
                                    BufferedReader reader = null;
                                    try {
                                        url_5 = url_1 + id_news + url_4;
                                        URL url = new URL(url_5);
                                        Log.d("duanpingwangzhi", url_5);
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
                                        showResponse2(response.toString());
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
                            }).start();
                        }


                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


    public void showResponse2(final String string) {
        Map map1 = new HashMap();
        map1.put("type", 3);
        list.add(map1);
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("comments");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String author = jsonObject1.getString("author");
                String content = jsonObject1.getString("content");
                String avatar = jsonObject1.getString("avatar");
                String time = jsonObject1.getString("time");
                String likes = jsonObject1.getString("likes");
//                JSONObject reply_to = jsonObject1.getJSONObject("reply_to");
//                String reply_to_content = reply_to.getString("content");
//                String reply_to_author = reply_to.getString("author");


                Map map = new HashMap();
                map.put("author", author);
                map.put("content", content);
                map.put("avatar", avatar);
                map.put("time", time);
                map.put("likes", likes);
//                map.put("reply_to_content", reply_to_content);
//                map.put("reply_to_author", reply_to_author);
                map.put("type", 4);


                list.add(map);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setLayoutManager(new LinearLayoutManager(CommentsActivity.this));//垂直排列 , Ctrl+P
                    recyclerView.setAdapter(new CommentsAdapter(CommentsActivity.this, list, long_comments, short_comments));//绑定适配器
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
