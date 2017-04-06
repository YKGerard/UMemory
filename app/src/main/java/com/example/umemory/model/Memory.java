package com.example.umemory.model;

import org.litepal.crud.DataSupport;

/**
 * Created by ${HYK} on 2017/3/26.
 */

public class Memory extends DataSupport{
    private int id;
    private String title;
    private String category;
    private String content;
    private String date;

    public Memory(){
        super();
    }
    public Memory(String title,String content,String category,String date){
        this.title=title;
        this.content=content;
        this.category=category;
        this.date=date;
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
