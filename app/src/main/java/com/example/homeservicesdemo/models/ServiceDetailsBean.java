package com.example.homeservicesdemo.models;

import java.util.ArrayList;

public class ServiceDetailsBean {
    public String id;
    public String category_id;
    public String category_name;
    public String subcat_id;
    public String subcategory_name;
    public String type;
    public String service_id;
    public String service_name;
    public String service_mrp;
    public String service_price;
    public String service_qty;
    public ArrayList<VarientBean> variant;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getService_mrp() {
        return service_mrp;
    }

    public void setService_mrp(String service_mrp) {
        this.service_mrp = service_mrp;
    }

    public String getService_price() {
        return service_price;
    }

    public void setService_price(String service_price) {
        this.service_price = service_price;
    }

    public String getService_qty() {
        return service_qty;
    }

    public void setService_qty(String service_qty) {
        this.service_qty = service_qty;
    }

    public ArrayList<VarientBean> getVariant() {
        return variant;
    }

    public void setVariant(ArrayList<VarientBean> variant) {
        this.variant = variant;
    }
}
