package com.pokemongostats.model.bean;

/**
 * Created by Zapagon on 04/04/2017.
 */
public class DrawerItem {

    private String itemName;
    private int imgResID = -1;

    public DrawerItem(String itemName) {
        this.itemName = itemName;
    }

    public DrawerItem(String itemName, int imgResID) {
        this.itemName = itemName;
        this.imgResID = imgResID;
    }

    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public int getImgResID() {
        return imgResID;
    }
    public void setImgResID(int imgResID) {
        this.imgResID = imgResID;
    }

}