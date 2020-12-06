package com.example.simpleblogapp;

public class Blog {
    private String Title,Description,DownloadUrl;


    public Blog(){}

    public Blog(String title, String description, String downloadUrl) {
        Title = title;
        Description = description;
        DownloadUrl = downloadUrl;
    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDownloadUrl() {
        return DownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        DownloadUrl = downloadUrl;
    }
}
