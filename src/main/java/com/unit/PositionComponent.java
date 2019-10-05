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
public class PositionComponent implements EntityComponent{
    private final Vector2f position;
    
    public PositionComponent(float x, float y){
        this(new Vector2f(x,y));
    }

    public PositionComponent(Vector2f position) {
        this.position = position;
    }

    public Vector2f getPosition() {
        return position;
    }
}
