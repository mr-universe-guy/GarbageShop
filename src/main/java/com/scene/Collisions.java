/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scene;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

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
    
    public static Vector2f clampOnRectangle(Vector2f point, Vector2f rMin, Vector2f rMax){
        Vector2f clamp = new Vector2f();
        clamp.x = FastMath.clamp(point.x, rMin.x, rMax.x);
        clamp.y = FastMath.clamp(point.y, rMin.y, rMax.y);
        return clamp;
    }
    
    public static boolean circleCollidesRectangle(Vector2f circlePos, float radius, Vector2f rMin, Vector2f rMax){
        Vector2f clamped = clampOnRectangle(circlePos, rMin, rMax);
        return (radius*radius > clamped.distanceSquared(circlePos));
    }
}
