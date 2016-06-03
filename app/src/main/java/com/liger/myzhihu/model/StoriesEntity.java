package com.liger.myzhihu.model;

import java.io.Serializable;
import java.util.List;

/**
 * 主题日报内容 Stories 实体类
 * 注意与Latest中"当日新闻"的区别，少了ga_prefix字段
 * Created by Shuai on 2015/12/18.
 */
public class StoriesEntity implements Serializable {

    private int id;
    private String title;       // 标题
    private int type;
    private List<String> images;// 图像地址（其类型为数组。请留意在代码中处理无该属性与数组长度为 0 的情况）

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
