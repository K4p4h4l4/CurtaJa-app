package com.example.tolavio.curta;

import java.util.Date;

/**
 * Created by Tolavio on 10-04-2017.
 */

public class BarComment {

    private String barCommentText;
    private String barCommentUser;
    private long barCommentTime;

    public BarComment(String commentText, String commentUser){

        this.barCommentText = commentText;
        this.barCommentUser = commentUser;

        barCommentTime = new Date().getTime();
    }

    public BarComment(){}

    public String getBarCommentText() {
        return barCommentText;
    }

    public void setBarCommentText(String barCommentText) {
        this.barCommentText = barCommentText;
    }

    public String getBarCommentUser() {
        return barCommentUser;
    }

    public void setBarCommentUser(String barCommentUser) {
        this.barCommentUser = barCommentUser;
    }

    public long getBarCommentTime() {
        return barCommentTime;
    }

    public void setBarCommentTime(long barCommentTime) {
        this.barCommentTime = barCommentTime;
    }
}
