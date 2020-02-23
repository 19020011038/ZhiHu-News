package com.example.zhihuribao.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.zhihuribao.Activity.NewsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by TrillGates on 18/4/23.
 * God bless my code!
 */
public class LooperPagerAdapter extends PagerAdapter {

    private List<String> mPics = null;
    private int realPosition;
    public Context context;
    public String id_user;
    public LooperPagerAdapter(Context context,String id_user){
        this.context = context;
        this.id_user = id_user;
    }

    @Override
    public int getCount() {
        if (mPics != null) {
            return Integer.MAX_VALUE;
        }
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int instance = mPics.size();
        if (instance != 0)
            realPosition = position % mPics.size();
        else
            realPosition = 1;
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //imageView.setBackgroundColor(mPics.get(position));
        Glide.with(container).load(mPics.get(realPosition)).into(imageView);
        //设置完数据以后,就添加到容器里
        container.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection connection = null;
                        BufferedReader reader = null;
                        try {
                            URL url = new URL("http://news-at.zhihu.com/api/3/stories/latest");
                            connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(8000);
                            connection.setReadTimeout(8000);
                            InputStream in = connection.getInputStream();
                            //读取刚刚获取的输入流
                            reader = new BufferedReader(new InputStreamReader(in));
                            StringBuilder response = new StringBuilder();
                            String line;
//                    line= reader.readLine();
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
        });
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setData(List<String> colos) {
        this.mPics = colos;
    }

    public int getDataRealSize() {
        if (mPics != null) {
            return mPics.size();
        }
        return 0;
    }

    public void showResponse(final String string){




        try    {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("top_stories");
            JSONObject jsonObject1 = jsonArray.getJSONObject(realPosition-1);
            String id_news = jsonObject1.getString("id");
            Intent intent = new Intent(LooperPagerAdapter.this.context, NewsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id_user",id_user);
            bundle.putString("id_news",id_news);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }    catch (JSONException e)    {
            e.printStackTrace();
        }



    }
}

