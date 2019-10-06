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
    public static boolean gridObjectCollision(GridObject a, GridObject b){
        Coordinate aMin = a.getPosition();
        Coordinate aMax = aMin.add(a.getSize());
        
        Coordinate bMin = b.getPosition();
        Coordinate bMax = bMin.add(b.getSize());
        
        //check collisions
        return isOverlap(aMin.x, aMax.x, bMin.x, bMax.x) &&
                isOverlap(aMin.y, aMax.y, bMin.y, bMax.y);
    }
    
    public static boolean boxBoxCollision(Coordinate aMin, Coordinate aMax,
            Coordinate bMin, Coordinate bMax){
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
    
    public static boolean pointInBox(Vector2f point, Vector2f boxMin, Vector2f boxMax){
        return point.x > boxMin.x && point.x < boxMax.x && point.y > boxMin.y && point.y < boxMax.y;
    }
    
    public static boolean pointInBox(Coordinate point, Coordinate boxMin, Coordinate boxMax){
        return point.x >= boxMin.x && point.x < boxMax.x && point.y >= boxMin.y && point.y < boxMax.y;
    }
    
    public static boolean pointInRotatedBox(Vector2f point, Vector2f boxMin, Vector2f boxSize, float rotation){
        //first get our point into local box-space
        Vector2f boxExtents = boxSize.mult(0.5f);
        Vector2f localPoint = point.subtract(boxMin);
        localPoint.rotateAroundOrigin(rotation, true);
        //now do a regular box check
        return pointInBox(localPoint, boxExtents.negate(), boxExtents);
    }
    
    public static boolean pointInCircle(Vector2f point, Vector2f circlePos, float circleRadius){
        return point.distanceSquared(circlePos) < circleRadius*circleRadius;
    }
}
