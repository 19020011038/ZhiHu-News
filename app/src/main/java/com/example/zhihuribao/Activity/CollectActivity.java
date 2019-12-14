package com.example.zhihuribao.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zhihuribao.Adapter.CollectAdapter;
import com.example.zhihuribao.Adapter.MainAdapter;
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
}
