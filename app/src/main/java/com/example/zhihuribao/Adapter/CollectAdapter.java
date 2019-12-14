package com.example.zhihuribao.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zhihuribao.Activity.CollectActivity;
import com.example.zhihuribao.Activity.NewsActivity;
import com.example.zhihuribao.Activity.UserActivity;
import com.example.zhihuribao.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;


public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.ViewHolder> {

    private List<Map<String, Object>> list;
    private String id_user;
    public Context context;

    public CollectAdapter(Context context, List<Map<String, Object>> list,String id_user) {
        this.context = context;
        this.list = list;
        this.id_user = id_user;
    }

    @NonNull
    @Override
    public CollectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_collect, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CollectAdapter.ViewHolder holder, final int position) {
        holder.show_title.setText(list.get(position).get("title_news").toString());
        Glide.with(context).load(list.get(position).get("url_news").toString()).into(holder.imageView);
        holder.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id_news = list.get(position).get("id_news").toString();
                Intent intent = new Intent(CollectAdapter.this.context, NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id_user",id_user);
                bundle.putString("id_news",id_news);
                bundle.putString("title_news",list.get(position).get("title_news").toString());
                bundle.putString("url_news",list.get(position).get("url_news").toString());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView show_title;
        private ImageView imageView;
        private View home;


        ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_title = itemView.findViewById(R.id.title_item_collect);
            TextPaint tp = show_title.getPaint();
            tp.setFakeBoldText(true);
            home = itemView.findViewById(R.id.item_collect);
            imageView = itemView.findViewById(R.id.picture_item_collect);
        }
    }
}
