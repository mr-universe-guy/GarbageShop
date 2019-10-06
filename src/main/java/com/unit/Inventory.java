/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unit;

import com.scene.Collisions;
import com.scene.Coordinate;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds items in a grid, items cannot overlap!
 * @author matt
 */
public class Inventory {
    private List<InventoryItem> itemList = new ArrayList<>();
    public static int STARTWIDTH = 2;
    public static int STARTHEIGHT = 5;
    private int width;
    private int height;
    
    public Inventory(){
        width = STARTWIDTH;
        height = STARTHEIGHT;
    }
    
    public InventoryItem getInventoryItemAtCoordinate(Coordinate coord){
        for(InventoryItem item : itemList){
            Coordinate itemPos = item.pos;
            if(item.item.getWidth() == 1 && item.item.getHeight() == 1){
                if(coord.equals(item.pos)){
                    return item;
                }
            } else{
                Coordinate itemExtent = itemPos.add(item.item.getWidth(), item.item.getHeight());
                if(Collisions.pointInBox(coord,itemPos, itemExtent)){
                    return item;
                }
            }
        }
        return null;
    }
    
    public boolean placeItemAtCoordinate(Coordinate coord, Item item){
        Coordinate itemMax = coord.add(item.getWidth()-1, item.getHeight()-1);
        for(InventoryItem b : itemList){
            Coordinate bMin = b.pos;
            Coordinate bMax = bMin.add(b.item.getWidth()-1, b.item.getHeight()-1);
            if(Collisions.boxBoxCollision(coord, itemMax, bMin, bMax)){
                return false;
            }
        }
        return itemList.add(new InventoryItem(item, coord));
    }
    
    public boolean removeItem(InventoryItem item){
        return itemList.remove(item);
    }

    public List<InventoryItem> getItemList() {
        return itemList;
    }
    
    public class InventoryItem{
        public Item item;
        public Coordinate pos;
        
        public InventoryItem(Item item, Coordinate pos){
            this.item = item;
            this.pos = pos;
        }
    }
}
