/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unit;

import com.simsilica.es.EntityComponent;

/**
 *
 * @author matt
 */
public class ItemComponent implements EntityComponent{
    private final String itemKey;

    public ItemComponent(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getItemKey() {
        return itemKey;
    }
}
