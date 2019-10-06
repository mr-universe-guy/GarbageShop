/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unit;

import com.simsilica.es.EntityComponent;

/**
 * Signifies an entity is looking for a trade, defined by the specific item they
 * are interested in, and a list of tags for items they will accept only if the 
 * alternate item matches all tags. Not sure how to handle prices >.>
 * @author matt
 */
public class TradeComponent implements EntityComponent{
    private final String itemName;
    private final String possibleTags;
    private final boolean selling;

    public TradeComponent(String itemName, String possibleTags, boolean isSelling) {
        this.itemName = itemName;
        this.possibleTags = possibleTags;
        this.selling = isSelling;
    }

    public String getItemName() {
        return itemName;
    }

    public String getPossibleTags() {
        return possibleTags;
    }

    public boolean isSelling() {
        return selling;
    }
}
