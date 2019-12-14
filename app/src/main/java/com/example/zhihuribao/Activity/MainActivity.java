package com.example.zhihuribao.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.zhihuribao.Adapter.LooperPagerAdapter;
import com.example.zhihuribao.Adapter.MainAdapter;
import com.example.zhihuribao.MyDataBaseHelper;
import com.example.zhihuribao.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MyViewPager.OnViewPagerTouchListener, ViewPager.OnPageChangeListener {
    private ImageView picture;
    private List<Map<String, Object>> list = new ArrayList<>();
    private List<Integer> mData = new ArrayList<>();
    private ViewPager mViewPager;
    private RecyclerView recyclerView;
    private String Today;
    private String Yesterday;
    private int Flag = 0;
    private String url_1;
    private String url_2;


    private static final String TAG = "MainActivity";
    private MyViewPager mLoopPager;
    private LooperPagerAdapter mLooperPagerAdapter;

    private static List<String> sPics = new ArrayList<>();


    private Handler mHandler;
    private boolean mIsTouch = false;
    private LinearLayout mPointContainer;
    private RefreshLayout refreshLayout;
    private int t;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        url_1 = "http://news.at.zhihu.com/api/1.2/stories/before/";
        t = 0;
        Today = getOldDate(t);
        Log.d("yyxnb",Today);
        mHandler = new Handler();
//        initView();
        //就是找到这个viewPager控件
        mLoopPager = (MyViewPager) this.findViewById(R.id.looper_pager);
        //设置适配器
        mLooperPagerAdapter = new LooperPagerAdapter();
//        mLooperPagerAdapter.setData(sPics);
//        mLoopPager.setAdapter(mLooperPagerAdapter);
        mLoopPager.setOnViewPagerTouchListener(this);
        mLoopPager.addOnPageChangeListener(this);
        mPointContainer = (LinearLayout) this.findViewById(R.id.points_container);
        //根据图片的个数,去添加点的个数
//        insertPoint();



        recyclerView = findViewById(R.id.recyclerView_main);
        refreshLayout = findViewById(R.id.refreshLayout);
        //显示头像
        picture = findViewById(R.id.picture_main);
        Intent intent = getIntent();
        String id_user = intent.getStringExtra("id_user");
        MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(MainActivity.this);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
        Cursor cursor = database.query("user", new String[]{"picture", "id_user"}, "id_user=?", new String[]{id_user}, null, null, null);
        if (cursor.moveToFirst()) {
            byte[] blob = cursor.getBlob(cursor.getColumnIndex("picture"));//Ctrl+P
            Bitmap bitmap = getBitmapFromByte(blob);
            if (bitmap != null)
                picture.setImageBitmap(bitmap);
        }
        cursor.close();//游标关闭!!!!
        database.close();


        //字体加粗
        TextView tv = (TextView) findViewById(R.id.hello);
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
        TextView tv1 = (TextView) findViewById(R.id.day);
        TextPaint tp1 = tv1.getPaint();
        tp1.setFakeBoldText(true);
        TextView tv2 = (TextView) findViewById(R.id.month);
        TextPaint tp2 = tv2.getPaint();
        tp2.setFakeBoldText(true);

        //点击头像的跳转
        ImageView imageView = (ImageView) findViewById(R.id.picture_main);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = getIntent();
                String id = intent1.getStringExtra("id_user");
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                intent.putExtra("id_user", id);
                startActivity(intent);
                finish();
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        sPics.clear();
        //下面是联网部分
        //开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("https://news-at.zhihu.com/api/3/stories/latest");
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

    //OnCreate结束
    @Override
    public void onAttachedToWindow() {
        //if (Flag.equals("1")) {
            super.onAttachedToWindow();
            //当我这个界面绑定到窗口的时候
//        if (sPics.size() != 0)
            mHandler.post(mLooperTask);
        //}
    }

    @Override
    public void onDetachedFromWindow() {
        //if (Flag.equals("1")) {
            super.onDetachedFromWindow();
            Log.d(TAG, "onDetachedFromWindow");
//            if (sPics.size()!=0)
            mHandler.removeCallbacks(mLooperTask);
       // }
    }

    private Runnable mLooperTask = new Runnable() {
        @Override
        public void run() {
            //if (Flag.equals("1")) {
                if (!mIsTouch) {
                    //切换viewPager里的图片到下一个
                    int currentItem = mLoopPager.getCurrentItem();
                    mLoopPager.setCurrentItem(++currentItem, true);
                }
                mHandler.postDelayed(this, 3000);
           // }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
//        if (Flag.equals("1")) {
            //就是找到这个viewPager控件
            mLoopPager = (MyViewPager) this.findViewById(R.id.looper_pager);
            //设置适配器
            mLooperPagerAdapter = new LooperPagerAdapter();
            mLooperPagerAdapter.setData(sPics);
            mLoopPager.setAdapter(mLooperPagerAdapter);
            mLoopPager.setOnViewPagerTouchListener(this);
            mLoopPager.addOnPageChangeListener(this);
            mPointContainer = (LinearLayout) this.findViewById(R.id.points_container);
            //根据图片的个数,去添加点的个数
            insertPoint();
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void insertPoint() {
//        if (Flag.equals("1")) {
            for (int i = 0; i < sPics.size(); i++) {
                View point = new View(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(40, 40);
                point.setBackground(getResources().getDrawable(R.drawable.shape_point_normal));
                layoutParams.leftMargin = 20;
                point.setLayoutParams(layoutParams);
                mPointContainer.addView(point);
//            }
        }
    }

    @Override
    public void onPagerTouch(boolean isTouch) {
//        if (Flag.equals("1"))
            mIsTouch = isTouch;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        if (Flag.equals("1")) {
//        }

    }

    @Override
    public void onPageSelected(int position) {
//        if (Flag.equals("1")) {
            //这个方法的调用,其实是viewPager停下来以后选中的位置
            int realPosition;
            if (mLooperPagerAdapter.getDataRealSize() != 0) {
                realPosition = position % mLooperPagerAdapter.getDataRealSize();
            } else {
                realPosition = 0;
            }
            setSelectPoint(realPosition);
//        }
    }

    private void setSelectPoint(int realPosition) {
//        if (Flag.equals("1")) {
            for (int i = 0; i < mPointContainer.getChildCount(); i++) {
                View point = mPointContainer.getChildAt(i);
                if (i != realPosition) {
                    //那就是白色
                    point.setBackgroundResource(R.drawable.shape_point_normal);
                } else {
                    //选中的颜色
                    point.setBackgroundResource(R.drawable.shape_point_selected);
                }
//            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
//        if(Flag.equals("1")){}
    }


    public static Bitmap getBitmapFromByte(byte[] temp) {   //将二进制转化为bitmap
        if (temp != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        } else {
            return null;
        }
    }

    public void showResponse(final String string) {
        Map map1 = new HashMap();
        map1.put("type", 2);
        list.add(map1);
        try {
            //接口1获取轮播图资源
            JSONObject jsonObject2 = new JSONObject(string);
            JSONArray jsonArray2 = jsonObject2.getJSONArray("top_stories");
            for (int i = 0; i < jsonArray2.length(); i++) {
                JSONObject jsonObject = jsonArray2.getJSONObject(i);
                String image = jsonObject.getString("image");
                sPics.add(image);
            }
//            Flag = "1";
            //接口1获取今日新闻
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("stories");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String title = jsonObject1.getString("title");
                String hint = jsonObject1.getString("hint");
                JSONArray images = jsonObject1.getJSONArray("images");
                String image = images.get(0).toString();
                String id_news = jsonObject1.getString("id");


                Map map = new HashMap();

                map.put("title", title);
                map.put("hint", hint);
                map.put("image", image);
                map.put("id_news", id_news);
                map.put("type", 1);


                list.add(map);
            }
            Flag = 1;

            runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void run() {
                    //绑定轮播适配器
                    Log.e("size",String.valueOf(sPics.size()));
                    mLooperPagerAdapter.setData(sPics);
                    mLoopPager.setAdapter(mLooperPagerAdapter);
                    insertPoint();
                    //开启线程获得下一天的数据
                    if(Flag == 1){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                HttpURLConnection connection = null;
                                BufferedReader reader = null;
                                try {
                                    url_2 = url_1 + Today;
                                    URL url = new URL(url_2);
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
//                    //接受数据
//                    Intent intent1 = getIntent();
//                    String id = intent1.getStringExtra("id_user");
//                    //绑定适配器
//                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));//垂直排列 , Ctrl+P
//                    recyclerView.setAdapter(new MainAdapter(MainActivity.this, list, id));//绑定适配器

//                    //下拉加载
//                    refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//                        @Override
//                        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//
//                            refreshLayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
//                        }
//                    });
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showResponse2(final String string) {
        Map map1 = new HashMap();
        map1.put("type", 3);
        list.add(map1);
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("news");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String title = jsonObject1.getString("title");
                String hint = jsonObject1.getString("hint");
                String image = jsonObject1.getString("image");
                String id_news = jsonObject1.getString("id");


                Map map = new HashMap();

                map.put("title", title);
                map.put("hint", hint);
                map.put("image", image);
                map.put("id_news", id_news);
                map.put("type", 1);


                list.add(map);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //接受数据
                    Intent intent1 = getIntent();
                    String id = intent1.getStringExtra("id_user");
                    //绑定适配器
                    t = t - 1;
                    //t是-1，代表昨天的日期
                    Yesterday = getOldDate2(t);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));//垂直排列 , Ctrl+P
                    recyclerView.setAdapter(new MainAdapter(MainActivity.this, list, id,Yesterday));//绑定适配器
                    //下拉加载
                    refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                        @Override
                        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                            
                            refreshLayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                        }
                    });
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public static String getOldDate(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }
    public static String getOldDate2(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy年MM月dd日");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }

}
