package com.example.zhihuribao.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zhihuribao.Activity.MainActivity;
import com.example.zhihuribao.Activity.NewsActivity;
import com.example.zhihuribao.R;

import java.util.List;
import java.util.Map;


public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Map<String, Object>> list;
    private String id_user;
    public Context context;
    public final int CAROUSEL_VIEW = 2;
    public final int ITEM_VIEW = 1;

    public MainAdapter(Context context, List<Map<String, Object>> list, String id_user) {
        this.context = context;
        this.list = list;
        this.id_user = id_user;
    }

    @Override
    public int getItemViewType(int position) {
        if (Integer.valueOf(list.get(position).get("type").toString())== CAROUSEL_VIEW)
            return CAROUSEL_VIEW;
        else
            return ITEM_VIEW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == CAROUSEL_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel, parent, false);
            return new CarouselViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
            return new NewsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CarouselViewHolder) {
//            CarouselViewHolder viewHolder = (CarouselViewHolder) holder;
//            viewHolder.carousel.setImageResource(R.drawable.shoucang2);
//            viewHolder.carousel.invalidate();
        } else if (holder instanceof NewsViewHolder) {
            NewsViewHolder viewHolder = (NewsViewHolder) holder;
            viewHolder.show_title.setText(list.get(position).get("title").toString());
            viewHolder.show_hint.setText(list.get(position).get("hint").toString());
            Glide.with(context).load(list.get(position).get("image").toString()).into(viewHolder.imageView);
            viewHolder.home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id_news = list.get(position).get("id_news").toString();
                    String title_news = list.get(position).get("title").toString();
                    String url_news = list.get(position).get("image").toString();
                    Intent intent = new Intent(MainAdapter.this.context, NewsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id_news", id_news);
                    bundle.putString("id_user", id_user);
                    bundle.putString("title_news", title_news);
                    bundle.putString("url_news", url_news);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    if(MainActivity.class.isInstance(context)){
                        MainActivity activity = (MainActivity)context;
                        activity.finish();
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}

class CarouselViewHolder extends RecyclerView.ViewHolder {
    public ImageView carousel;

    CarouselViewHolder(@NonNull View itemView) {
        super(itemView);
        carousel = itemView.findViewById(R.id.carousel);
    }
}

class NewsViewHolder extends RecyclerView.ViewHolder {
    public TextView show_title;
    public TextView show_hint;
    public ImageView imageView;
    public View home;


    NewsViewHolder(@NonNull View itemView) {
        super(itemView);

        show_title = itemView.findViewById(R.id.title_item);
        TextPaint tp = show_title.getPaint();
        tp.setFakeBoldText(true);
        show_hint = itemView.findViewById(R.id.hint_item);
        home = itemView.findViewById(R.id.LinearLayout_item);
        imageView = itemView.findViewById(R.id.picture_item);
    }
}



