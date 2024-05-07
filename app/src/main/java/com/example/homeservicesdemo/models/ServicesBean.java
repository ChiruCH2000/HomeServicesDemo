package com.example.homeservicesdemo.models;

import android.widget.TextView;

import java.util.ArrayList;

public class ServicesBean{
    public String service_id;
    public String name;
    public String mrp;
    public String price;
    public Object description;
    public String image;
    public String category_name;
    public String category_id;
    public String subcat_id;
    public String subcategory_name;
    public String warranty;
    public String duration;
    public String note1;
    public String note2;
    public String type;
    public ArrayList<VarientBean> variants;
    public ArrayList<ServiceIncluded> service_included;
    public ArrayList<ServiceExcluded> service_excluded;
    public ArrayList<Object> about_service;
    public ArrayList<HowItWork> how_it_works;
    public ArrayList<Object> company_cover;
    public ArrayList<Object> please_note;
    public int service_rating;
    public ArrayList<Object> customer_reviews;

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSubcat_id() {
        return subcat_id;
    }

    public void setSubcat_id(String subcat_id) {
        this.subcat_id = subcat_id;
    }

    public String getSubcategory_name() {
        return subcategory_name;
    }

    public void setSubcategory_name(String subcategory_name) {
        this.subcategory_name = subcategory_name;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getNote1() {
        return note1;
    }

    public void setNote1(String note1) {
        this.note1 = note1;
    }

    public String getNote2() {
        return note2;
    }

    public void setNote2(String note2) {
        this.note2 = note2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<VarientBean> getVariants() {
        return variants;
    }

    public void setVariants(ArrayList<VarientBean> variants) {
        this.variants = variants;
    }

    public ArrayList<ServiceIncluded> getService_included() {
        return service_included;
    }

    public void setService_included(ArrayList<ServiceIncluded> service_included) {
        this.service_included = service_included;
    }

    public ArrayList<ServiceExcluded> getService_excluded() {
        return service_excluded;
    }

    public void setService_excluded(ArrayList<ServiceExcluded> service_excluded) {
        this.service_excluded = service_excluded;
    }

    public ArrayList<Object> getAbout_service() {
        return about_service;
    }

    public void setAbout_service(ArrayList<Object> about_service) {
        this.about_service = about_service;
    }

    public ArrayList<HowItWork> getHow_it_works() {
        return how_it_works;
    }

    public void setHow_it_works(ArrayList<HowItWork> how_it_works) {
        this.how_it_works = how_it_works;
    }

    public ArrayList<Object> getCompany_cover() {
        return company_cover;
    }

    public void setCompany_cover(ArrayList<Object> company_cover) {
        this.company_cover = company_cover;
    }

    public ArrayList<Object> getPlease_note() {
        return please_note;
    }

    public void setPlease_note(ArrayList<Object> please_note) {
        this.please_note = please_note;
    }

    public int getService_rating() {
        return service_rating;
    }

    public void setService_rating(int service_rating) {
        this.service_rating = service_rating;
    }

    public ArrayList<Object> getCustomer_reviews() {
        return customer_reviews;
    }

    public void setCustomer_reviews(ArrayList<Object> customer_reviews) {
        this.customer_reviews = customer_reviews;
    }
}
