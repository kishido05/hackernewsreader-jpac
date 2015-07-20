package com.jpac.hackernews.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jpac.hackernews.R;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends BaseAdapter {

    private List<Comments> commentsList;
    private Context context;

    public CommentsAdapter(Context context) {

        this.context = context;
        this.commentsList = new ArrayList<Comments>();
    }

    @Override
    public int getCount() {
        return commentsList.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return commentsList.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_comments, null);

            holder = new ViewHolder();

            holder.commentAuthor = (TextView) view.findViewById(R.id.textAuthor);
            holder.commentTime = (TextView) view.findViewById(R.id.textDate);
            holder.commentContent = (TextView) view.findViewById(R.id.textContent);

            holder.replyContainer = (ViewGroup) view.findViewById(R.id.replyContainer);
            holder.replyAuthor = (TextView) view.findViewById(R.id.replyAuthor);
            holder.replyContent = (TextView) view.findViewById(R.id.replyContent);
            holder.replyTitle = (TextView) view.findViewById(R.id.replyTitle);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Comments comment = (Comments) getItem(i);

        holder.commentAuthor.setText(comment.getComment().getBy());
        holder.commentTime.setText(comment.getComment().getTime());
        holder.commentContent.setText(Html.fromHtml(comment.getComment().getText()));

        if (comment.getReply() != null) {
            holder.replyContainer.setVisibility(View.VISIBLE);
            holder.replyAuthor.setText(comment.getReply().getBy());

            if (comment.getReply().getTitle() == null) {
                holder.replyTitle.setVisibility(View.GONE);
            } else {
                holder.replyTitle.setVisibility(View.VISIBLE);
                holder.replyTitle.setText(comment.getReply().getTitle());
            }

            holder.replyContent.setText(Html.fromHtml(comment.getReply().getText()));
        } else {
            holder.replyContainer.setVisibility(View.GONE);
        }

        return view;
    }

    public void add(List<Comments> comments) {
        commentsList.addAll(comments);
        notifyDataSetChanged();
    }

    public void clear() {
        commentsList.clear();
        notifyDataSetChanged();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    public static class ViewHolder {

        TextView commentContent, commentAuthor, commentTime;
        TextView replyTitle, replyAuthor, replyContent;
        ViewGroup replyContainer;

    }
}
