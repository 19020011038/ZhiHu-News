package com.example.zhihuribao.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
        if (actionBar != null) {
            actionBar.hide();
        }
        fullScreen(ChangeNameActivity.this);
        mGet_change_name = findViewById(R.id.get_new_name);
        //返回
        ImageView back = (ImageView) findViewById(R.id.back_change_name_user);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //修改用户名
        Button button = (Button) findViewById(R.id.change_name);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String id = intent.getStringExtra("id_user");
                String new_name = mGet_change_name.getText().toString();
                if ("".equals(new_name))
                    Toast.makeText(ChangeNameActivity.this, "请您输入新用户名！", Toast.LENGTH_SHORT).show();
                else if (mGet_change_name.length() > 10)
                    Toast.makeText(ChangeNameActivity.this, "用户名长度不能超过10个字符！", Toast.LENGTH_SHORT).show();
                else if (mGet_change_name.length() < 5)
                    Toast.makeText(ChangeNameActivity.this, "用户名长度不能小于5个字符！", Toast.LENGTH_SHORT).show();
                else {
                    MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(ChangeNameActivity.this);
                    SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("name", new_name);//第一个"name" 是字段名字  第二个是对应字段的数据
                    database.update("user", values, "id_user=?", new String[]{id});
                    database.close();
                    Toast.makeText(ChangeNameActivity.this, "用户名修改成功！", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
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
