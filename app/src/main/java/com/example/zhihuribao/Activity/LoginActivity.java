package com.example.zhihuribao.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zhihuribao.MyDataBaseHelper;
import com.example.zhihuribao.R;

public class LoginActivity extends AppCompatActivity {
    private EditText mGet_tel;
    private EditText mGet_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar !=null){
            actionBar.hide();
        }
        mGet_tel = findViewById(R.id.tel);
        mGet_password = findViewById(R.id.password);

        Button register = (Button)findViewById(R.id.jump_login_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button login = (Button)findViewById(R.id.jump_login_main);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String get_tel = mGet_tel.getText().toString();
                String get_password = mGet_password.getText().toString();
                if("".equals(get_tel))
                    Toast.makeText(LoginActivity.this,"请您输入手机号！",Toast.LENGTH_SHORT).show();
                else{
                    if("".equals(get_password))
                        Toast.makeText(LoginActivity.this,"请您输入密码",Toast.LENGTH_SHORT).show();
                    else{
                        MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(LoginActivity.this);
                        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
                        Cursor cursor = database.query("user", new String[]{"number","id_user","password"}, "number=?", new String[]{get_tel}, null, null, null);
                        if(cursor.moveToFirst()){
                            String data_password = cursor.getString(cursor.getColumnIndex("password"));//Ctrl+P
                            String id = cursor.getString(cursor.getColumnIndex("id_user"));//Ctrl+P
                            if(get_password.equals(data_password))
                            {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("id_user",id);
                                startActivity(intent);
                                finish();
                                Toast.makeText(LoginActivity.this,"登录成功！ 欢迎您~  " + get_tel,Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(LoginActivity.this,"您输入的密码有误！",Toast.LENGTH_SHORT).show();

                        }else
                            Toast.makeText(LoginActivity.this,"请检查您输入的手机号是否正确！",Toast.LENGTH_SHORT).show();
                        cursor.close();
                        database.close();
                    }
                }
            }
        });
    }
}
