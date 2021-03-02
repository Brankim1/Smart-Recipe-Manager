package com.example.smartrecipemanager;

/**
 * constructor for store and post users' experience
 *
 * */
public class Post {

        private long postid;
        private String userid;
        private String name;
        private String title;
        private String pic;
        private String content;

        public Post(){}
        public long getPostid() {
            return postid;
        }
        public void setPostid(long postid){
            this.postid=postid;
        }
        public String getUserid() {
        return userid;
    }
        public void setUserid(String userid){
        this.userid=userid;
    }
        public String getName() { return name; }
        public void setName(String name){
        this.name=name;
    }
        public String getTitle() {
            return title;
        }
        public void setTitle(String title){
            this.title=title;
        }
        public String getPic() {
            return pic;
        }
        public void setPic(String pic){
            this.pic=pic;
        }
        public String getContent() {
        return content;
    }
        public void setContent(String content){
        this.content=content;
    }

        public Post(long postid,String userid,String name, String title, String pic, String content ) {
            this.postid = postid;
            this.userid = userid;
            this.name = name;
            this.title = title;
            this.pic = pic;
            this.content = content;
        }

}
