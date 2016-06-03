package com.liger.myzhihu.model;

import java.util.List;

/**
 * 过往的消息
 * Created by Liger on 2015/12/20.
 */
public class Before {

    private List<StoriesEntity> stories;
    private String date;

    public void setStories(List<StoriesEntity> stories) {
        this.stories = stories;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesEntity> getStories() {
        return stories;
    }

    public String getDate() {
        return date;
    }
}
