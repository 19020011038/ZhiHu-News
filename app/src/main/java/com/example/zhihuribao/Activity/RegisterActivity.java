package com.example.zhihuribao.Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
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

public class RegisterActivity extends AppCompatActivity {
    private EditText mText_number;
    private EditText mText_name;
    private EditText mText_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        fullScreen(RegisterActivity.this);
        mText_number = findViewById(R.id.get_tel);
        mText_name = findViewById(R.id.get_name);
        mText_password = findViewById(R.id.get_password);

        //设置禁止用户名输入空格
        setEditTextInhibitInputSpace(mText_name);

        ImageView imageView = (ImageView) findViewById(R.id.back_register_login);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        Button button2 = (Button) findViewById(R.id.register);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(mText_number.getText().toString()))
                    Toast.makeText(RegisterActivity.this, "输入手机号不能为空！", Toast.LENGTH_SHORT).show();
                else if (mText_number.length() != 11)
                    Toast.makeText(RegisterActivity.this, "请您输入正确的11位手机号！", Toast.LENGTH_SHORT).show();
                else if (mText_name.length() > 10)
                    Toast.makeText(RegisterActivity.this, "用户名长度不能超过10个字符！", Toast.LENGTH_SHORT).show();
                else if (mText_name.length() < 5)
                    Toast.makeText(RegisterActivity.this, "用户名长度不能小于5个字符！", Toast.LENGTH_SHORT).show();
                else if ("".equals(mText_name.getText().toString()))
                    Toast.makeText(RegisterActivity.this, "输入用户名不能为空！", Toast.LENGTH_SHORT).show();
                else if ("".equals(mText_password.getText().toString()))
                    Toast.makeText(RegisterActivity.this, "输入密码不能为空！", Toast.LENGTH_SHORT).show();
                else if (mText_password.length() < 6)
                    Toast.makeText(RegisterActivity.this, "密码长度不小于6个字符！", Toast.LENGTH_SHORT).show();
                else if (mText_password.length() > 11)
                    Toast.makeText(RegisterActivity.this, "密码长度不超过11个字符！", Toast.LENGTH_SHORT).show();
                else {
                    MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(RegisterActivity.this);
                    SQLiteDatabase database = dataBaseHelper.getReadableDatabase();

                    String number = mText_number.getText().toString();
                    String name = mText_name.getText().toString();
                    String password = mText_password.getText().toString();

                    Cursor cursor = database.query("user", new String[]{"number"}, "number=?", new String[]{number}, null, null, null);
                    Cursor cursor2 = database.query("user", new String[]{"name"}, "name=?", new String[]{name}, null, null, null);

                    if (cursor.moveToFirst())
                        Toast.makeText(RegisterActivity.this, "用户" + number + "已被注册！", Toast.LENGTH_SHORT).show();
                    else {
                        if (cursor2.moveToFirst())
                            Toast.makeText(RegisterActivity.this, "用户名" + name + "已被使用！", Toast.LENGTH_SHORT).show();
                        else {
                            database.execSQL("insert into user(number,name,password) values('" + number + "','" + name + "','" + password + "');");
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    cursor.close();//游标关闭!!!!
                    cursor2.close();//游标关闭!!!!
                    database.close();
                }
            }
        });
    }

    public static void setEditTextInhibitInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") )return "";
    else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
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
