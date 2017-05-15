package com.bidanet.bdcms.plugin.wechat.entity;

/**
 * Created by Administrator on 2016/9/27.
 */
public class NewsContext {
        private String url;
        private String picUrl;
        private String description;
        private String title;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

}
