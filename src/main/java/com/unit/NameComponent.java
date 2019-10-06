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
public class NameComponent implements EntityComponent{
    private final String name;

    public NameComponent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
