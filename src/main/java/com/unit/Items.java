/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unit;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A long list of all items in the game
 * @author matt
 */
public class Items {
    private static final Random RANDOM = new Random();
    public static final Map<String, Item> ITEMMAP = new HashMap<>();
    
    static{
        //fill map here
        ITEMMAP.put("Battery", new Item("Battery", 1,1,1,3,"object/BatteryObject.j3o",
                "robot,small,power,trash"));
    }
    
    public static Item getRandomItem(){
        Collection<Item> items = ITEMMAP.values();
        int r = RANDOM.nextInt(items.size());
        Item v = null;
        for(Item item : items){
            v = item;
            if(r-- == 0){
                break;
            }
        }
        return v;
    }
    
    public static boolean isAcceptable(Item offer, Item target){
        if(target.getItemName().equals(offer.getItemName())){
            return true;
        }
        String[] targetTags = target.getTags().split(",");
        String offerTags = offer.getTags();
        for(String tag : targetTags){
            if(!offerTags.contains(tag)){
                return false;
            }
        }
        return true;
    }
}
