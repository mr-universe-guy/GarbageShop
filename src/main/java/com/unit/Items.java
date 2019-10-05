/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unit;

import java.util.HashMap;
import java.util.Map;

/**
 * A long list of all items in the game
 * @author matt
 */
public class Items {
    public static final Map<String, Item> ITEMMAP = new HashMap<>();
    
    static{
        //fill map here
        ITEMMAP.put("Battery", new Item("Battery", 1,1,"models/battery.gltf","robot,small,power,trash"));
    }
}
