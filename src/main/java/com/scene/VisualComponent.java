/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scene;

import com.simsilica.es.EntityComponent;

/**
 *
 * @author matt
 */
public class VisualComponent implements EntityComponent{
    private final String visualAsset;

    public VisualComponent(String visualAsset) {
        this.visualAsset = visualAsset;
    }

    public String getVisualAsset() {
        return visualAsset;
    }
}
