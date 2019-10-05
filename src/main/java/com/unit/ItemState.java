/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unit;

import com.DayListener;
import com.GarbageShopApp;
import com.entity.DataState;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.FastMath;
import com.scene.Collisions;
import com.scene.Coordinate;
import com.scene.Scene;
import com.scene.SceneState;
import com.scene.Vectors;
import com.scene.VisualComponent;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntitySet;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

/**
 * Every morning remove old items and spawn new ones
 * Check if the player can interact with any items
 * @author matt
 */
public class ItemState extends BaseAppState implements DayListener{
    private final Random random = new Random();
    private EntityData ed;
    private EntitySet liveItems;
    private Scene scene;

    @Override
    protected void initialize(Application aplctn) {
        ed = getState(DataState.class).getEd();
        scene = getState(SceneState.class).getMap();
    }

    @Override
    protected void cleanup(Application aplctn) {
        
    }

    @Override
    protected void onEnable() {
        GarbageShopApp app = (GarbageShopApp)getApplication();
        app.addDayListener(this);
        liveItems = ed.getEntities(ItemComponent.class, PositionComponent.class);
    }

    @Override
    protected void onDisable() {
        GarbageShopApp app = (GarbageShopApp)getApplication();
        app.removeDayListener(this);
        liveItems.release();
    }

    @Override
    public void update(float tpf) {
        liveItems.applyChanges();
    }

    @Override
    public void nextDay() {
        liveItems.applyChanges();
        cleanTrash();
        spawnTrash();
    }

    private void cleanTrash() {
        Coordinate min = scene.getAlleyMin();
        Coordinate max = scene.getAlleyMax();
        for(Entity e : liveItems){
            //kill all trash that's hanging around in the open
            if(Collisions.pointInBox(e.get(PositionComponent.class).getPosition(),
            Vectors.fromCoordinate(min), Vectors.fromCoordinate(max))){
                ed.removeEntity(e.getId());
            }
        }
    }

    private void spawnTrash() {
        Coordinate min = scene.getAlleyMin();
        Coordinate max = scene.getAlleyMax();
        int numTrash = 2+random.nextInt(2);
        float rangeX = max.x - min.x;
        float rangeY = max.y - min.y;
        for(int i=0; i<numTrash; i++){
            Set<Entry<String, Item>> items = Items.ITEMMAP.entrySet();
            int r = random.nextInt(items.size());
            int x=0;
            Item item = null;
            for(Entry<String, Item> entry : items){
                if(x != r) {
                    x++;
                    continue;
                }
                item = entry.getValue();
            }
            if(item == null) throw new RuntimeException("Item failed to generate");
            float posX = min.x+(random.nextFloat()*rangeX);
            float posY = min.y+(random.nextFloat()*rangeY);
            float rot = random.nextFloat()*FastMath.TWO_PI;
            ed.setComponents(ed.createEntity(),
                    new PositionComponent(posX, posY, rot),
                    new ItemComponent(item.getItemName()),
                    new VisualComponent(item.getAssetPath())
            );
        }
    }
    
}
