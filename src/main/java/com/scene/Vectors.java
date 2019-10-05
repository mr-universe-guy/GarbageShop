/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scene;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 *
 * @author matt
 */
public class Vectors {
    public static Vector3f vec2ToVec3(Vector2f v){
        return new Vector3f(v.x, v.y, 0);
    }
    
    public static Vector2f vec3ToVec2(Vector3f v){
        return new Vector2f(v.x, v.y);
    }
    
    public static Vector2f rotateVector(Vector2f vec, float rads){
        float cos = FastMath.cos(rads);
        float sin = FastMath.sin(rads);
        return new Vector2f(vec.x*cos-vec.y*sin, vec.x*sin+vec.y*cos);
    }
    
    public static Vector2f fromCoordinate(Coordinate other){
        return new Vector2f(other.x, other.y);
    }
}
