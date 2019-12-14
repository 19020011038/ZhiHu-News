package com.example.zhihuribao.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zhihuribao.MyDataBaseHelper;
import com.example.zhihuribao.R;

public class UserActivity extends AppCompatActivity {
    private TextView mText_name;
    private ImageView picture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar !=null){
            actionBar.hide();
        }
        //显示用户名
        mText_name = findViewById(R.id.show_name_user);
        ShowName();
        //显示头像
        picture = findViewById(R.id.picture_user);
        Intent intent = getIntent();
        String id_user = intent.getStringExtra("id_user");
        MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(UserActivity.this);
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

        //返回
        ImageView back = (ImageView)findViewById(R.id.back_user_main);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String id = intent.getStringExtra("id_user");
                Intent intent1 = new Intent(UserActivity.this, MainActivity.class);
                intent1.putExtra("id_user",id);
                startActivity(intent1);
                finish();
            }
        });
        //打开收藏夹
        TextView collect = (TextView)findViewById(R.id.jump_user_collect);
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String id = intent.getStringExtra("id_user");
                Intent intent1 = new Intent(UserActivity.this, CollectActivity.class);
                intent1.putExtra("id_user",id);
                startActivity(intent1);
                finish();
            }
        });
        //修改用户名
        TextView edit_name = (TextView)findViewById(R.id.jump_user_change_name);
        edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String id = intent.getStringExtra("id_user");
                Intent intent1 = new Intent(UserActivity.this, ChangeNameActivity.class);
                intent1.putExtra("id_user",id);
                startActivity(intent1);
                finish();
            }
        });
        //修改密码
        TextView edit_password = (TextView)findViewById(R.id.jump_user_change_password);
        edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String id = intent.getStringExtra("id_user");
                Intent intent1 = new Intent(UserActivity.this, ChangePasswordActivity.class);
                intent1.putExtra("id_user",id);
                startActivity(intent1);
                finish();
            }
        });
        //修改头像
        TextView edit_picture = (TextView)findViewById(R.id.jump_user_change_picture);
        edit_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String id = intent.getStringExtra("id_user");
                Intent intent1 = new Intent(UserActivity.this, ChangePictureActivity.class);
                intent1.putExtra("id_user",id);
                startActivity(intent1);
                finish();
            }
        });
    }

    public static Bitmap getBitmapFromByte(byte[] temp){   //将二进制转化为bitmap
        if(temp != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        }else{
            return null;
        }
    }

    private void ShowName() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id_user");

        MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(UserActivity.this);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();

        Cursor cursor = database.query("user", new String[]{"name", "id_user"}, "id_user=?", new String[]{id}, null, null, null);

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));//Ctrl+P
            mText_name.setText(name);
        }

        cursor.close();//游标关闭!!!!
        database.close();
    }


}
