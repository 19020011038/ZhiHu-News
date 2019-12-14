package com.example.zhihuribao.Activity;

import android.content.Intent;
import android.database.Cursor;
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
        mText_number = findViewById(R.id.get_tel);
        mText_name = findViewById(R.id.get_name);
        mText_password = findViewById(R.id.get_password);

        ImageView imageView = (ImageView)findViewById(R.id.back_register_login);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        Button button2 = (Button)findViewById(R.id.register);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(mText_number.getText().toString()))
                    Toast.makeText(RegisterActivity.this,"输入手机号不能为空！",Toast.LENGTH_SHORT).show();
                else
                if("".equals(mText_name.getText().toString()))
                    Toast.makeText(RegisterActivity.this,"输入用户名不能为空！",Toast.LENGTH_SHORT).show();
                else
                if("".equals(mText_password.getText().toString()))
                    Toast.makeText(RegisterActivity.this,"输入密码不能为空！",Toast.LENGTH_SHORT).show();
                else{
                    MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(RegisterActivity.this);
                    SQLiteDatabase database = dataBaseHelper.getReadableDatabase();

                    String number = mText_number.getText().toString();
                    String name = mText_name.getText().toString();
                    String password = mText_password.getText().toString();

                    Cursor cursor = database.query("user", new String[]{"number"}, "number=?", new String[]{number}, null, null, null);
                    Cursor cursor2 = database.query("user", new String[]{"name"}, "name=?", new String[]{name}, null, null, null);

                    if (cursor.moveToFirst())
                        Toast.makeText(RegisterActivity.this, "用户" + number + "已被注册！" , Toast.LENGTH_SHORT).show();
                    else {
                        if (cursor2.moveToFirst())
                            Toast.makeText(RegisterActivity.this, "用户名" + name + "已被使用！" , Toast.LENGTH_SHORT).show();
                        else {
                            database.execSQL("insert into user(number,name,password) values('" +  number + "','" + name + "','" + password + "');");
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            Toast.makeText(RegisterActivity.this,"注册成功！",Toast.LENGTH_SHORT).show();
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
}
