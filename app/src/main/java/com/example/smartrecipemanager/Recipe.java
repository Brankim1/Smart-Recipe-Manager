package com.example.smartrecipemanager;
/**Recipe
 * constructor for store and upload users' favourite recipe to server
 *
 * */
public class Recipe {
    private String id;
    private String title;
    private String pic;

    public Recipe(){}
    public String getId() {
        return id;
    }
    public void setId(String id){
            this.id=id;
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

    public Recipe(String id, String title, String pic) {
        this.id = id;
        this.title = title;
        this.pic = pic;
    }
}
