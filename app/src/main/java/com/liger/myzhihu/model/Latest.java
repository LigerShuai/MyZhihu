package com.liger.myzhihu.model;

import java.util.List;

/**
 * 最新新闻 实体类
 * Created by Shuai on 2015/12/19.
 */
public class Latest {

    /**
     * date : 20151219
     * stories : [{"images":["http://pic4.zhimg.com/9a79d6be3a24861e586c6eee89144d43.jpg"],"type":0,"id":7619784,
     * "ga_prefix":"121909","title":"官员、银行家、高管\u2026\u2026总觉得这些「精英」肯定信不过"},{"title":"《魂斗罗》中的角色，趴下的时候总是要「萝莉式屈腿俯卧」",
     * "ga_prefix":"121908","images":["http://pic1.zhimg.com/343db59b88d6754c3d6988f40f7d456c.jpg"],"multipic":true,
     * "type":0,"id":7611185},{"images":["http://pic2.zhimg.com/c51610b16bc8974da7a110f56a37bad9.jpg"],"type":0,
     * "id":7615718,"ga_prefix":"121907","title":"相比较其它地区，北京考生上清北有多大优势？"},{"images":["http://pic1.zhimg
     * .com/d39ae3c757b0524f351c0874ad9f7fc0.jpg"],"type":0,"id":7531212,"ga_prefix":"121907","title":"买哪台电脑好，我真的是靠算出来的"},
     * {"images":["http://pic2.zhimg.com/3b32ab44de832e50b6c4f119e0c07509.jpg"],"type":0,"id":7567902,"ga_prefix":"121907",
     * "title":"想在楼顶种一大片花花草草，需要慢慢来"},{"images":["http://pic1.zhimg.com/08c1e536dede1ba73d414b8eed628030.jpg"],"type":0,
     * "id":7621777,"ga_prefix":"121906","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"http://pic1.zhimg.com/202cf9520ddf666c19200009581814c0.jpg","type":0,"id":7611185,
     * "ga_prefix":"121908","title":"《魂斗罗》中的角色，趴下的时候总是要「萝莉式屈腿俯卧」"},{"image":"http://pic3.zhimg
     * .com/6c4ec13d0d48c0fabc06dd5ecbd9131a.jpg","type":0,"id":7615718,"ga_prefix":"121907",
     * "title":"相比较其它地区，北京考生上清北有多大优势？"},{"image":"http://pic1.zhimg.com/33fe5a45ff8845089e714634da19790c.jpg","type":0,
     * "id":7442302,"ga_prefix":"121821","title":"他的动画电影，走在幻想与现实的边界，好看得无话可说"},{"image":"http://pic3.zhimg
     * .com/f14d5a1e088105123378951cf58dde76.jpg","type":0,"id":7620938,"ga_prefix":"121819",
     * "title":"周末到了，推荐读读日报里的好文章，各位可要读一读"},{"image":"http://pic2.zhimg.com/5f56ecaaac226107710a0d15571b95f9.jpg","type":0,
     * "id":7608078,"ga_prefix":"121817","title":"在黄河漂流是我干过最疯狂的事，有人上了船再也没能回来"}]
     */

    private String date;
    /**
     * images : ["http://pic4.zhimg.com/9a79d6be3a24861e586c6eee89144d43.jpg"]
     * type : 0
     * id : 7619784
     * ga_prefix : 121909
     * title : 官员、银行家、高管……总觉得这些「精英」肯定信不过
     */
    private List<StoriesEntity> stories;
    private List<TopStoriesEntity> top_stories;

    public void setDate(String date) {
        this.date = date;
    }

    public void setStories(List<StoriesEntity> stories) {
        this.stories = stories;
    }

    public void setTop_stories(List<TopStoriesEntity> top_stories) {
        this.top_stories = top_stories;
    }

    public String getDate() {
        return date;
    }

    public List<StoriesEntity> getStories() {
        return stories;
    }

    public List<TopStoriesEntity> getTop_stories() {
        return top_stories;
    }

    /**
     * 界面顶部 ViewPager 滚动显示的内容
     */
    public static class TopStoriesEntity {
        private String image;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;

        public void setImage(String image) {
            this.image = image;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public int getType() {
            return type;
        }

        public int getId() {
            return id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public String getTitle() {
            return title;
        }
    }
}
