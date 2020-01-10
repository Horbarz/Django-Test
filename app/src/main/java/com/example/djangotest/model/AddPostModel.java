package com.example.djangotest.model;

public class AddPostModel {
    private int category;
    private String title;
    private String text;

    public AddPostModel(int category, String title, String text) {
        this.category = category;
        this.title = title;
        this.text = text;
    }

    public int getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
