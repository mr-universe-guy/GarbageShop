/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unit;

/**
 * The data for in game items the player can trade.
 * Width/height are in approx 1/10th meter increments (round up)
 * @author matt
 */
public class Item {
    private String itemName;
    private int width;
    private int height;
    private String assetPath;
    private String tags;

    public Item(String itemName, int width, int height, String assetPath, String tags) {
        this.itemName = itemName;
        this.width = width;
        this.height = height;
        this.assetPath = assetPath;
        this.tags = tags;
    }

    public String getItemName() {
        return itemName;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getAssetPath() {
        return assetPath;
    }

    public String getTags() {
        return tags;
    }
    
}
