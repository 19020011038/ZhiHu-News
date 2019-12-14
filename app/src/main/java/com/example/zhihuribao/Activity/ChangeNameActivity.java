package com.example.zhihuribao.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zhihuribao.MyDataBaseHelper;
import com.example.zhihuribao.R;

public class ChangeNameActivity extends AppCompatActivity {
    private EditText mGet_change_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_change_name);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar !=null){
            actionBar.hide();
        }
        mGet_change_name = findViewById(R.id.get_new_name);
        //返回
        ImageView back = (ImageView)findViewById(R.id.back_change_name_user);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String id = intent.getStringExtra("id_user");
                Intent intent1 = new Intent(ChangeNameActivity.this, UserActivity.class);
                intent1.putExtra("id_user",id);
                startActivity(intent1);
                finish();
            }
        });
        //修改用户名
        Button button = (Button)findViewById(R.id.change_name);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String id = intent.getStringExtra("id_user");
                String new_name = mGet_change_name.getText().toString();
                if("".equals(new_name))
                    Toast.makeText(ChangeNameActivity.this,"请您输入新用户名！",Toast.LENGTH_SHORT).show();
                else {
                    MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(ChangeNameActivity.this);
                    SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("name", new_name);//第一个"name" 是字段名字  第二个是对应字段的数据
                    database.update("user", values, "id_user=?", new String[]{id});
                    database.close();
                    Intent intent1 = new Intent(ChangeNameActivity.this, UserActivity.class);
                    intent1.putExtra("id_user",id);
                    startActivity(intent1);
                    Toast.makeText(ChangeNameActivity.this,"用户名修改成功！",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
