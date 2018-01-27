package com.lions.app.sportsstore.structs;

import java.io.Serializable;

/**
 * Created by Panwar on 27/12/17.
 */

public class Products implements Serializable {

    String product_id;
    String product_name;
    String product_description;
    String product_price;
    String product_rating;
    String product_quantity;
    String product_url;
    String product_no_images;
    String product_sizes;
    String product_color;
    String product_cat_id;


    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }


    public String getProduct_rating() {
        return product_rating;
    }

    public void setProduct_rating(String product_rating) {
        this.product_rating = product_rating;
    }

    public String getProduct_url() {
        return product_url;
    }

    public void setProduct_url(String product_url) {
        this.product_url = product_url;
    }

    public String getProduct_no_images() {
        return product_no_images;
    }

    public void setProduct_no_images(String product_no_images) {
        this.product_no_images = product_no_images;
    }



    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_sizes() {
        return product_sizes;
    }

    public void setProduct_sizes(String product_sizes) {
        this.product_sizes = product_sizes;
    }

    public String getProduct_color() {
        return product_color;
    }

    public void setProduct_color(String product_color) {
        this.product_color = product_color;
    }

    public String getProduct_cat_id() {
        return product_cat_id;
    }

    public void setProduct_cat_id(String product_cat_id) {
        this.product_cat_id = product_cat_id;
    }



}
