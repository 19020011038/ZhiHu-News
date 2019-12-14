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
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zhihuribao.Adapter.CollectAdapter;
import com.example.zhihuribao.MyDataBaseHelper;
import com.example.zhihuribao.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_collect);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        fullScreen(CollectActivity.this);
        recyclerView = findViewById(R.id.recyclerView_collect);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id_user");
        //返回user
        ImageView imageView = (ImageView)findViewById(R.id.back_collect_user);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String id = intent.getStringExtra("id_user");
                Intent intent1 = new Intent(CollectActivity.this, UserActivity.class);
                intent1.putExtra("id_user",id);
                startActivity(intent1);
                finish();
            }
        });

        //绑定适配器
        MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(CollectActivity.this);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
        Cursor cursor = database.query("collect", new String[]{"id_user", "id_news","title_news","url_news"}, "id_user=?", new String[]{id}, null, null, null);
        List<Map<String, Object>> list = new ArrayList<>();
        while (cursor.moveToNext()){
            String id_news = cursor.getString(cursor.getColumnIndex("id_news"));
            String title_news = cursor.getString(cursor.getColumnIndex("title_news"));
            String url_news = cursor.getString(cursor.getColumnIndex("url_news"));
            Map<String, Object> map = new HashMap<>();
            map.put("id_news",id_news);
            map.put("title_news",title_news);
            map.put("url_news",url_news);
            list.add(map);
        }
        cursor.close();
        database.close();
        recyclerView.setLayoutManager(new LinearLayoutManager(CollectActivity.this));//垂直排列 , Ctrl+P
        recyclerView.setAdapter(new CollectAdapter(CollectActivity.this, list,id));//绑定适配器
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
