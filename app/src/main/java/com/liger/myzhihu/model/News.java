package com.liger.myzhihu.model;

import java.util.List;

/**
 * 主题日报内容 实体类
 * Created by Shuai on 2015/12/18.
 */
public class News {

    private List<StoriesEntity> stories;// 该主题日报中的文章列表
    private int color;
    private String description;         // 该主题日报的介绍
    private String name;                // 该主题日报的名称
    private String background;          // 该主题日报的背景图片（大图）
    private String image;               // 背景图片的小图版本
    private List<EditorsEntity> editors;// 该主题日报的编辑
    private String image_source;        //  图像的版权信息

    public List<StoriesEntity> getStories() {
        return stories;
    }

    public void setStories(List<StoriesEntity> stories) {
        this.stories = stories;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<EditorsEntity> getEditors() {
        return editors;
    }

    public void setEditors(List<EditorsEntity> editors) {
        this.editors = editors;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    /**
     * 主题日报的编辑 实体类
     */
    public static class EditorsEntity {
        /**
         * id : 70
         * bio : 微在 Wezeit 主编
         * name : 益康糯米
         * avatar : http://pic4.zhimg.com/068311926_m.jpg
         * url : http://www.zhihu.com/people/wezeit
         */
        private int id;        // 数据库中的唯一标识符
        private String bio;    // 主编的个人简介
        private String name;   // 主编姓名
        private String avatar; // 主编头像
        private String url;    // 主编的知乎用户主页

        public void setId(int id) {
            this.id = id;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getId() {
            return id;
        }

        public String getBio() {
            return bio;
        }

        public String getName() {
            return name;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getUrl() {
            return url;
        }
    }
}
