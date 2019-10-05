/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unit;

import com.entity.DataState;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector2f;
import com.scene.Map;
import com.scene.MapState;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntitySet;

/**
 * Moves mobs based on their driver and speed
 * @author matt
 */
public class MobState extends BaseAppState{
    private MapState mapState;
    private EntityData ed;
    private EntitySet mobs;

    @Override
    protected void initialize(Application aplctn) {
        mapState = getState(MapState.class);
        ed = getState(DataState.class).getEd();
    }

    @Override
    protected void cleanup(Application aplctn) {
        
    }

    @Override
    protected void onEnable() {
        mobs = ed.getEntities(PositionComponent.class, MobComponent.class, DriverComponent.class);
    }

    @Override
    protected void onDisable() {
        mobs.release();
    }

    @Override
    public void update(float tpf) {
        mobs.applyChanges();
        //look at all mobs who's driver is currently > 0
        Map map = mapState.getMap();
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
                    ed.setComponent(e.getId(), new PositionComponent(nextPos));
                }
            }
        }
    }
    
}
