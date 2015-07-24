package com.jpac.hackernews.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jpac.hackernews.R;
import com.jpac.hackernews.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsViewHolder> {

    private List<Comments> commentsList;
    private Context context;

    public CommentsAdapter(Context context) {

        this.context = context;
        this.commentsList = new ArrayList<Comments>();
    }

    public void add(List<Comments> comments) {
        commentsList.addAll(comments);
    }

    public void clear() {
        commentsList.clear();
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.list_item_comments, viewGroup, false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentsViewHolder holder, int i) {
        Comments comment = getItem(i);

        News main = comment.getComment();
        News reply = comment.getReply();

        holder.commentAuthor.setText(main.getBy());
        holder.commentTime.setText(Utils.getTimeAgo(main.getTime()));
        holder.commentContent.setText(Html.fromHtml(main.getText()));

        if (reply != null) {
            holder.replyContainer.setVisibility(View.VISIBLE);
            holder.replyAuthor.setText(reply.getBy());

            if (reply.getTitle() == null) {
                holder.replyTitle.setVisibility(View.GONE);
            } else {
                holder.replyTitle.setVisibility(View.VISIBLE);
                holder.replyTitle.setText(reply.getTitle());
            }

            holder.replyContent.setText(Html.fromHtml(reply.getText()));
        } else {
            holder.replyContainer.setVisibility(View.GONE);
        }
    }

    public Comments getItem(int i) {
        return commentsList.get(i);
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }
}
