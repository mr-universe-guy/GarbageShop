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
public class Scene {
    private final List<GridObject> collisionObjects = new ArrayList<>();
    private final List<GridObject> itemSpawners = new ArrayList<>();
    private final List<GridObject> sleepZones = new ArrayList<>();
    private final Vector2f northSpawn, southSpawn;
    private final float spawnRadius = 2.5f;

    public Scene(Vector2f northSpawn, Vector2f southSpawn) {
        this.northSpawn = northSpawn;
        this.southSpawn = southSpawn;
    }
    
    public boolean addCollisionObject(GridObject object){
        return collisionObjects.add(object);
    }
    
    public boolean removeGridObject(GridObject object){
        return collisionObjects.remove(object);
    }

    public List<GridObject> getGridObjects() {
        return collisionObjects;
    }
    
    public boolean addItemSpawnArea(GridObject spawnArea){
        return itemSpawners.add(spawnArea);
    }
    
    public boolean removeItemSpawnArea(GridObject spawnArea){
        return itemSpawners.remove(spawnArea);
    }
    
    public List<GridObject> getItemSpawners(){
        return itemSpawners;
    }
    
    public boolean addSleepZone(GridObject sleepZone){
        return sleepZones.add(sleepZone);
    }
    
    public boolean removeSleepZone(GridObject sleepZone){
        return sleepZones.remove(sleepZone);
    }
    
    public List<GridObject> getSleepZones(){
        return sleepZones;
    }

    public Vector2f getNorthSpawn() {
        return northSpawn;
    }

    public Vector2f getSouthSpawn() {
        return southSpawn;
    }

    public float getSpawnRadius() {
        return spawnRadius;
    }
    
    public boolean collides(Vector2f circlePos, float circleRadius){
        for(GridObject object : collisionObjects){
            Vector2f recMin = Vectors.fromCoordinate(object.getPosition());
            Vector2f recMax = Vectors.fromCoordinate(object.getPosition().add(object.getSize()));
            if(Collisions.circleCollidesRectangle(circlePos, circleRadius, recMin, recMax)){
                return true;
            }
        }
        return false;
    }
}
