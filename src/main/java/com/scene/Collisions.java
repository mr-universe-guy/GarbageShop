/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scene;

/**
 *
 * @author matt
 */
public class Collisions {
    public static boolean collidesWith(GridObject a, GridObject b){
        Coordinate aMin = a.getPosition();
        Coordinate aMax = aMin.add(a.getSize());
        
        Coordinate bMin = b.getPosition();
        Coordinate bMax = bMin.add(b.getSize());
        
        //check collisions
        return isOverlap(aMin.x, aMax.x, bMin.x, bMax.x) &&
                isOverlap(aMin.y, aMax.y, bMin.y, bMax.y);
    }
    
    /**
     * Check if a single dimensional line intersects another single dimensional line
     * @param aMin
     * @param aMax
     * @param bMin
     * @param bMax
     * @return 
     */
    public static boolean isOverlap(float aMin, float aMax, float bMin, float bMax){
        return bMin <= aMax && aMin <= bMax;
    }
}
