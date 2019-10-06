/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unit;

import com.scene.Collisions;
import com.scene.Coordinate;
import com.scene.Vectors;
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
    
    public Item getItemAtCoordinate(Coordinate coord){
        for(InventoryItem item : itemList){
            Coordinate itemPos = item.pos;
            Coordinate itemExtent = itemPos.add(item.item.getWidth(), item.item.getHeight());
            if(Collisions.pointInBox(Vectors.fromCoordinate(coord),
                    Vectors.fromCoordinate(itemPos), Vectors.fromCoordinate(itemExtent))){
                return item.item;
            }
        }
        return null;
    }
    
    public boolean placeItemAtCoordinate(Coordinate coord, Item item){
        Coordinate itemMax = coord.add(item.getWidth(), item.getHeight());
        for(InventoryItem b : itemList){
            Coordinate bMin = b.pos;
            Coordinate bMax = bMin.add(b.item.getWidth(), b.item.getHeight());
            if(Collisions.boxBoxCollision(coord, itemMax, bMin, bMax)){
                return false;
            }
        }
        return itemList.add(new InventoryItem(item, coord));
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
