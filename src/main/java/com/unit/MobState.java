/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unit;

import com.GarbageShopApp;
import com.TimeListener;
import com.entity.DataState;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector2f;
import com.scene.Scene;
import com.scene.SceneState;
import com.scene.Vectors;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import java.util.Random;

/**
 * Moves mobs based on their driver and speed
 * @author matt
 */
public class MobState extends BaseAppState implements TimeListener{
    private final Random random = new Random();
    private SceneState mapState;
    private EntityData ed;
    private EntitySet mobs;
    private Vector2f northSpawn = new Vector2f(27,20);
    private Vector2f southSpawn = new Vector2f(27,0);
    private float spawnRadius = 3;
    private float minSpeed = 0.7f;
    private float maxSpeed = 1.f;

    @Override
    protected void initialize(Application aplctn) {
        mapState = getState(SceneState.class);
        ed = getState(DataState.class).getEd();
    }

    @Override
    protected void cleanup(Application aplctn) {
        
    }

    @Override
    protected void onEnable() {
        mobs = ed.getEntities(PositionComponent.class, MobComponent.class, DriverComponent.class);
        GarbageShopApp app = (GarbageShopApp)getApplication();
        app.addTimeListener(this);
    }

    @Override
    protected void onDisable() {
        mobs.release();
        GarbageShopApp app = (GarbageShopApp)getApplication();
        app.removeTimeListener(this);
    }

    @Override
    public void update(float tpf) {
        mobs.applyChanges();
        //look at all mobs who's driver is currently > 0
        Scene map = mapState.getMap();
        for(Entity e : mobs){
            Vector2f driver = e.get(DriverComponent.class).getMovement();
            if(driver.lengthSquared() > 0.01f){
                MobComponent mob = e.get(MobComponent.class);
                float radius = mob.getRadius();
                float speed = mob.getSpeed()*tpf;
                Vector2f position = e.get(PositionComponent.class).getPosition();
                Vector2f nextPos = position.add(driver.mult(speed));
                //check this position against the map to make sure we can walk here
                if(!map.collides(nextPos, radius)){
                    ed.setComponent(e.getId(), new PositionComponent(nextPos,0));
                }
                if(nextPos.y > 30 || nextPos.y < -10){
                    ed.removeEntity(e.getId());
                }
            }
        }
    }

    @Override
    public void setHour(int hour) {
        int min, max;
        if(hour < 8){
            //early morning rush hour
            min = 7;
            max = 15;
        } else if(hour >= 8 && hour < 12){
            //mostly dead
            min = 2;
            max = 5;
        } else if(hour >= 12 && hour < 2){
            //slightly picks up for lunch
            min = 6;
            max = 12;
        } else if(hour >= 2 && hour < 5){
            //dead again
            min = 2;
            max = 5;
        } else if(hour >= 5 && hour < 8){
            //evening rush hour
            min = 7;
            max = 15;
        } else{
            //things quiet down alot at night
            min = 3;
            max = 7;
        }
        spawnWave(random.nextInt(min+(max-min)));
    }
    
    public void spawnWave(int numSpawned){
        int half = numSpawned/2;
        for(int i=0; i<half; i++){
            spawnPedestrian(northSpawn);
        }
        for(int i=0; i<numSpawned-half; i++){
            spawnPedestrian(southSpawn);
        }
    }
    
    public EntityId spawnPedestrian(Vector2f spawnPos){
        PositionComponent pos = new PositionComponent(
                spawnPos.add(Vectors.randomFromRadius(spawnRadius)), 0);
        float speed = minSpeed+(random.nextFloat()*(maxSpeed-minSpeed));
        MobComponent mob = new MobComponent(speed, 0.3f);
        EntityId id = ed.createEntity();
        Vector2f dir;
        if(spawnPos.y < 10){
            dir = new Vector2f(0,1);
        } else{
            dir = new Vector2f(0,-1);
        }
        DriverComponent driver = new DriverComponent(dir);
        ed.setComponents(id, pos, mob, driver);
        return id;
    }
}
