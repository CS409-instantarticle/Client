package com.example.rho_eojin1.a409_prototype13;

/**
 * Created by Rho-Eojin1 on 2017. 5. 8..
 */

public class ContentListElement {
    private String type;
    private String content;

    public ContentListElement(String type, String content){
        this.type = type;
        this.content = content;
    }

    public String getType(){
        return this.type;
    }

    public String getContent(){
        return this.content;
    }
}
