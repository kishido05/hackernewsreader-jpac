package com.jpac.hackernews.data;

import android.view.View;
import android.widget.TextView;

import com.jpac.hackernews.R;

/**
 * Created by syspaulo on 7/26/2015.
 */
public class FooterViewHolder extends NewsViewHolder {

    private TextView loadMore;

    public FooterViewHolder(View itemView) {
        super(itemView);

        itemView.findViewById(R.id.newsContainer).setVisibility(View.GONE);

        loadMore = (TextView) itemView.findViewById(R.id.textFooter);
    }
}
