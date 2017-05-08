package com.example.rho_eojin1.a409_prototype13;

/**
 * Created by Rho-Eojin1 on 2017. 5. 8..
 */

public class ContentListElement {
    private int index;
    private String type;
    private String content;

    public ContentListElement(int index, String type, String content){
        this.index = index;
        this.type = type;
        this.content = content;
    }

    public int getIndex() { return this.index;}

    public String getType(){
        return this.type;
    }

    public String getContent(){
        return this.content;
    }
}
