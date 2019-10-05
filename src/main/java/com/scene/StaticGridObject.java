/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scene;

/**
 * This grid object is used for background object type collisions and nothing else.
 * @author matt
 */
public class StaticGridObject implements GridObject{
    private final Coordinate position;
    private final Coordinate size;

    public StaticGridObject(Coordinate position, Coordinate size) {
        this.position = position;
        this.size = size;
    }

    @Override
    public Coordinate getPosition() {
        return position;
    }

    @Override
    public Coordinate getSize() {
        return size;
    }

    @Override
    public boolean isCollideable() {
        return true;
    }
    
}
