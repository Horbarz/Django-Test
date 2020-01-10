package com.example.djangotest.model;



public class PostModel {
    private Integer id;
    private String title;
    private String text;
    private Integer category;

    public PostModel(Integer id, String title, String text, Integer category) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public Integer getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "PostModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", category=" + category +
                '}';
    }
}
