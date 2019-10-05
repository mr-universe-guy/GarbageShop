/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scene;

/**
 * An object that is locked to the map grid
 * Objects do NOT support rotation!
 * @author matt
 */
public interface GridObject {
    public Coordinate getPosition();
    public Coordinate getSize();
    public boolean isCollideable();
}
