package com.example.homeservicesdemo.models;

import java.util.ArrayList;
import java.util.List;

public class CategoryBean {
    public String id;
    public String category_name;
    public String image;
    public ArrayList<CategoryBanner> category_banners;
    public ArrayList<Object> banners;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<CategoryBanner> getCategory_banners() {
        return category_banners;
    }

    public void setCategory_banners(ArrayList<CategoryBanner> category_banners) {
        this.category_banners = category_banners;
    }

    public ArrayList<Object> getBanners() {
        return banners;
    }

    public void setBanners(ArrayList<Object> banners) {
        this.banners = banners;
    }
}
