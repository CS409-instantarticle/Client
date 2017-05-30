package com.example.rho_eojin1.a409_prototype13;

/**
 * Created by Rho-Eojin1 on 2017. 5. 8..
 */

public class MainListElement {
    private String article_id;
    private String title;
    private String thumbnail;
    private String press;


    public MainListElement(String article_id, String title, String thumbnail, String press){
        this.article_id = article_id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.press = press;
    }

    public String getArticleID() {return this.article_id;}

    public String getTitle(){
        return this.title;
    }

    public String getThumbnail(){
        return this.thumbnail;
    }

    public String getPress(){
        return this.press;
    }
}
