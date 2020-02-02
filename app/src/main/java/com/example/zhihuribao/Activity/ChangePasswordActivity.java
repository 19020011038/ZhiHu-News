package com.example.zhihuribao.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText mGet_origin_password;
    private EditText mGet_new_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_change_password);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        fullScreen(ChangePasswordActivity.this);
        mGet_origin_password = findViewById(R.id.get_origin_password);
        mGet_new_password = findViewById(R.id.get_new_password);
        //返回
        ImageView back = (ImageView) findViewById(R.id.back_change_password_user);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String id = intent.getStringExtra("id_user");
                Intent intent1 = new Intent(ChangePasswordActivity.this, UserActivity.class);
                intent1.putExtra("id_user", id);
                startActivity(intent1);
                finish();
            }
        });
        //修改密码
        Button button = (Button) findViewById(R.id.change_password);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String id = intent.getStringExtra("id_user");
                String origin_password = mGet_origin_password.getText().toString();
                String new_password = mGet_new_password.getText().toString();
                if ("".equals(origin_password))
                    Toast.makeText(ChangePasswordActivity.this, "请您输入旧密码！", Toast.LENGTH_SHORT).show();
                else if ("".equals(new_password))
                    Toast.makeText(ChangePasswordActivity.this, "请您输入新密码！", Toast.LENGTH_SHORT).show();
                else if (mGet_new_password.length() < 6)
                    Toast.makeText(ChangePasswordActivity.this, "密码长度不小于6个字符！", Toast.LENGTH_SHORT).show();
                else if (mGet_new_password.length() > 11)
                    Toast.makeText(ChangePasswordActivity.this, "密码长度不超过11个字符！", Toast.LENGTH_SHORT).show();
                else {
                    MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(ChangePasswordActivity.this);
                    SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
                    Cursor cursor = database.query("user", new String[]{"id_user", "password"}, "id_user=?", new String[]{id}, null, null, null);
                    if (cursor.moveToFirst()) {
                        String password = cursor.getString(cursor.getColumnIndex("password"));//Ctrl+P
                        if (password.equals(origin_password)) {
                            ContentValues values = new ContentValues();
                            values.put("password", new_password);//第一个"name" 是字段名字  第二个是对应字段的数据
                            database.update("user", values, "id_user=?", new String[]{id});
                            Toast.makeText(ChangePasswordActivity.this, "修改成功,请您重新登录！", Toast.LENGTH_SHORT).show();
                            Intent intent2 = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                            intent2.putExtra("id_user", id);
                            startActivity(intent2);
                            finish();
                        } else
                            Toast.makeText(ChangePasswordActivity.this, "您输入的原始密码有误！", Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();//游标关闭!!!!
                    database.close();
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
