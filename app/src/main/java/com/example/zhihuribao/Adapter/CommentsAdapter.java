package com.example.zhihuribao.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zhihuribao.Activity.NewsActivity;
import com.example.zhihuribao.R;

import java.util.List;
import java.util.Map;

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Map<String, Object>> list;
    private String long_comments;
    private String short_comments;
    public Context context;
    public final int LONG = 1;
    public final int LONG_COMMENTS_VIEW = 2;
    public final int SHORT = 3;
    public final int SHORT_COMMENTS_VIEW = 4;

    public CommentsAdapter(Context context, List<Map<String, Object>> list, String long_comments, String short_comments) {
        this.context = context;
        this.list = list;
        this.long_comments = long_comments;
        this.short_comments = short_comments;
    }

    @Override
    public int getItemViewType(int position) {
        return Integer.valueOf(list.get(position).get("type").toString());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (!long_comments.equals("0")) {
            if (viewType == LONG) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_long_title, parent, false);
                return new LongTitleViewHolder(view);
            } else if (viewType == LONG_COMMENTS_VIEW) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_long_comments, parent, false);
                return new LongCommentsViewHolder(view);
            } else if (viewType == SHORT) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_short_title, parent, false);
                return new ShortTitleViewHolder(view);
            } else {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_short_comments, parent, false);
                return new ShortCommentsViewHolder(view);
            }
        } else {
            if (viewType == SHORT) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_short_title, parent, false);
                return new ShortTitleViewHolder(view);
            } else {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_short_comments, parent, false);
                return new ShortCommentsViewHolder(view);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!long_comments.equals("0")) {
            if (holder instanceof LongTitleViewHolder) {
                LongTitleViewHolder viewHolder = (LongTitleViewHolder) holder;
                viewHolder.show_title_long.setText(long_comments + "条长评");
            } else if (holder instanceof LongCommentsViewHolder) {
                LongCommentsViewHolder viewHolder = (LongCommentsViewHolder) holder;
                Glide.with(context).load(list.get(position).get("avatar").toString()).into(viewHolder.imageView_long);
                Log.d("yyx12345",list.get(position).get("avatar").toString());
                viewHolder.author_long.setText(list.get(position).get("author").toString());
                viewHolder.content_long.setText(list.get(position).get("content").toString());
                viewHolder.likes_long.setText(list.get(position).get("likes").toString());
                viewHolder.time_long.setText(list.get(position).get("time").toString());
            } else if (holder instanceof ShortTitleViewHolder) {
                ShortTitleViewHolder viewHolder = (ShortTitleViewHolder) holder;
                viewHolder.show_title_short.setText(short_comments + "条短评");
            } else {
                ShortCommentsViewHolder viewHolder = (ShortCommentsViewHolder) holder;
                Glide.with(context).load(list.get(position).get("avatar").toString()).into(viewHolder.imageView_short);
                viewHolder.author_short.setText(list.get(position).get("author").toString());
                viewHolder.content_short.setText(list.get(position).get("content").toString());
                viewHolder.likes_short.setText(list.get(position).get("likes").toString());
                viewHolder.time_short.setText(list.get(position).get("time").toString());
            }
        } else {
            if (holder instanceof ShortTitleViewHolder) {
                ShortTitleViewHolder viewHolder = (ShortTitleViewHolder) holder;
                viewHolder.show_title_short.setText(short_comments + "条短评");
            } else {
                ShortCommentsViewHolder viewHolder = (ShortCommentsViewHolder) holder;
                Glide.with(context).load(list.get(position).get("avatar").toString()).into(viewHolder.imageView_short);
                viewHolder.author_short.setText(list.get(position).get("author").toString());
                viewHolder.content_short.setText(list.get(position).get("content").toString());
                viewHolder.likes_short.setText(list.get(position).get("likes").toString());
                viewHolder.time_short.setText(list.get(position).get("time").toString());
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class LongTitleViewHolder extends RecyclerView.ViewHolder {
        public TextView show_title_long;


        LongTitleViewHolder(@NonNull View itemView) {
            super(itemView);

            show_title_long = itemView.findViewById(R.id.long_title);
            TextPaint tp = show_title_long.getPaint();
            tp.setFakeBoldText(true);

        }
    }

    class LongCommentsViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView_long;
        public TextView author_long;
        public TextView content_long;
        public TextView reply_to_author_long;
        public TextView reply_to_content_long;
        public TextView time_long;
        public TextView likes_long;


        LongCommentsViewHolder(@NonNull View itemView) {
            super(itemView);

            author_long = itemView.findViewById(R.id.long_comments_author);
            TextPaint tp = author_long.getPaint();
            tp.setFakeBoldText(true);
            content_long = itemView.findViewById(R.id.long_comments_content);
            likes_long = itemView.findViewById(R.id.long_comments_like);
            imageView_long = itemView.findViewById(R.id.long_comments_picture);
            time_long = itemView.findViewById(R.id.long_comments_time);
        }
    }


    class ShortTitleViewHolder extends RecyclerView.ViewHolder {
        public TextView show_title_short;


        ShortTitleViewHolder(@NonNull View itemView) {
            super(itemView);

            show_title_short = itemView.findViewById(R.id.short_title);
            TextPaint tp = show_title_short.getPaint();
            tp.setFakeBoldText(true);

        }
    }

    class ShortCommentsViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView_short;
        public TextView author_short;
        public TextView content_short;
        public TextView reply_to_author_short;
        public TextView reply_to_content_short;
        public TextView time_short;
        public TextView likes_short;


        ShortCommentsViewHolder(@NonNull View itemView) {
            super(itemView);

            author_short = itemView.findViewById(R.id.short_comments_author);
            TextPaint tp = author_short.getPaint();
            tp.setFakeBoldText(true);
            content_short = itemView.findViewById(R.id.short_comments_content);
            likes_short = itemView.findViewById(R.id.short_comments_like);
            imageView_short = itemView.findViewById(R.id.short_comments_picture);
            time_short = itemView.findViewById(R.id.short_comments_time);
        }
    }

}
