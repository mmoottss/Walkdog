package com.example.dog;

public class SampleItem {
    String title, content, name, image;
    SampleItem(String name, String title, String content, String image){
        this.name = name;
        this.title = title;
        this.content = content;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }
}
