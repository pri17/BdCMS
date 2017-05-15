package com.bidanet.bdcms.plugin.wechat.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public class NewMessage {

    private String toUser;
    private List<NewsContext> newsContext;

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public List<NewsContext> getNewsContext() {
        return newsContext;
    }

    public void setNewsContext(List<NewsContext> newsContext) {
        this.newsContext = newsContext;
    }


}
