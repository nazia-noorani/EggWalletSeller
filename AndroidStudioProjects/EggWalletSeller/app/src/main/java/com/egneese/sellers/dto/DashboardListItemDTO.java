package com.egneese.sellers.dto;

/**
 * Created by adityaagrawal on 10/01/16.
 */
public class DashboardListItemDTO {
    private String title;
    private String disc;
    private Integer image;

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }
}
