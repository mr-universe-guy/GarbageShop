/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unit;

import com.jme3.math.Vector2f;
import com.simsilica.es.EntityComponent;

/**
 *
 * @author matt
 */
public class DriverComponent implements EntityComponent{
    private final Vector2f movement;

    public DriverComponent(float x, float y){
        this(new Vector2f(x,y));
    }
    
    public DriverComponent(Vector2f movement) {
        this.movement = movement;
    }

    public Vector2f getMovement() {
        return movement;
    }
}
