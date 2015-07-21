package com.jpac.hackernews.data;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jpac.hackernews.R;

/**
 * Created by syspaulo on 7/21/2015.
 */
public class NewsViewHolder extends RecyclerView.ViewHolder {

    protected View parent;
    protected TextView title, author, date, points;
    protected ImageButton url;

    public NewsViewHolder(View itemView) {
        super(itemView);

        parent = itemView;
        title = (TextView) itemView.findViewById(R.id.textTitle);
        author = (TextView) itemView.findViewById(R.id.textAuthor);
        date = (TextView) itemView.findViewById(R.id.textDate);
        points = (TextView) itemView.findViewById(R.id.textPoints);
        url = (ImageButton) itemView.findViewById(R.id.buttonOpenLink);
    }
}
