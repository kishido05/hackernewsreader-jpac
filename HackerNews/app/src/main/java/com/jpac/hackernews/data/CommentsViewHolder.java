package com.jpac.hackernews.data;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jpac.hackernews.R;

/**
 * Created by paulocarabuena on 7/24/15.
 */
public class CommentsViewHolder extends RecyclerView.ViewHolder {

    protected TextView commentContent, commentAuthor, commentTime;
    protected TextView replyTitle, replyAuthor, replyContent;
    protected ViewGroup replyContainer;

    public CommentsViewHolder(View itemView) {
        super(itemView);

        commentAuthor = (TextView) itemView.findViewById(R.id.textAuthor);
        commentTime = (TextView) itemView.findViewById(R.id.textDate);
        commentContent = (TextView) itemView.findViewById(R.id.textContent);

        replyContainer = (ViewGroup) itemView.findViewById(R.id.replyContainer);
        replyAuthor = (TextView) itemView.findViewById(R.id.replyAuthor);
        replyContent = (TextView) itemView.findViewById(R.id.replyContent);
        replyTitle = (TextView) itemView.findViewById(R.id.replyTitle);
    }
}
