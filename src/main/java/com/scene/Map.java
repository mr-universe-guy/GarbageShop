/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scene;

import com.jme3.math.Vector2f;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author matt
 */
public class Map {
    private final List<GridObject> gridObjects = new ArrayList<>();
    
    public boolean addGridObject(GridObject object){
        return gridObjects.add(object);
    }
    
    public boolean removeGridObject(GridObject object){
        return gridObjects.remove(object);
    }

    public List<GridObject> getGridObjects() {
        return gridObjects;
    }
    
    public boolean collides(Vector2f circlePos, float circleRadius){
        for(GridObject object : gridObjects){
            Vector2f recMin = Vectors.fromCoordinate(object.getPosition());
            Vector2f recMax = Vectors.fromCoordinate(object.getPosition().add(object.getSize()));
            if(Collisions.circleCollidesRectangle(circlePos, circleRadius, recMin, recMax)){
                return true;
            }
        }
        return false;
    }
}
