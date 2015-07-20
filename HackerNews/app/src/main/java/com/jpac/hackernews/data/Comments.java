package com.jpac.hackernews.data;

import java.io.Serializable;

public class Comments implements Serializable {

    private News comment;
    private News reply;

    public News getComment() {
        return comment;
    }

    public void setComment(News comment) {
        this.comment = comment;
    }

    public News getReply() {
        return reply;
    }

    public void setReply(News reply) {
        this.reply = reply;
    }
}
