/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scene;

import com.GarbageShopApp;
import com.entity.DataState;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.unit.PositionComponent;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles simple visuals
 * @author matt
 */
public class VisualState extends BaseAppState{
    private final Map<EntityId, Spatial> assetMap = new HashMap<>();
    private final Node visualNode = new Node("Non-static_Visuals");
    private EntityData ed;
    private AssetManager am;
    private EntitySet assets;

    @Override
    protected void initialize(Application aplctn) {
        ed = getState(DataState.class).getEd();
        am = aplctn.getAssetManager();
    }

    @Override
    protected void cleanup(Application aplctn) {
        
    }

    @Override
    protected void onEnable() {
        GarbageShopApp app = (GarbageShopApp)getApplication();
        assets = ed.getEntities(VisualComponent.class, PositionComponent.class);
        app.getRootNode().attachChild(visualNode);
    }

    @Override
    protected void onDisable() {
        assets.release();
        visualNode.removeFromParent();
    }

    @Override
    public void update(float tpf) {
        if(assets.applyChanges()){
            for(Entity e : assets.getRemovedEntities()){
                removeAsset(e.getId());
            }
            for(Entity e : assets.getAddedEntities()){
                loadAsset(e);
            }
            for(Entity e : assets.getChangedEntities()){
                translateAsset(e.getId(), e.get(PositionComponent.class).getPosition());
            }
        }
    }
    
    private void removeAsset(EntityId id){
        Spatial spat = assetMap.remove(id);
        spat.removeFromParent();
    }
    
    private void loadAsset(Entity e){
        EntityId id = e.getId();
        String asset = e.get(VisualComponent.class).getVisualAsset();
        Vector2f position = e.get(PositionComponent.class).getPosition();
        Spatial spat = am.loadModel(asset);
        assetMap.put(id, spat);
        translateAsset(spat, position);
    }
    
    private void translateAsset(EntityId id, Vector2f position){
        translateAsset(assetMap.get(id), position);
    }
    
    private void translateAsset(Spatial spat, Vector2f position){
        spat.setLocalTranslation(Vectors.vec2ToVec3(position));
    }
}
