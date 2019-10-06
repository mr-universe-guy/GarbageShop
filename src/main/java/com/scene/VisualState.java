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
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.unit.ColorComponent;
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
    private Material mat;

    @Override
    protected void initialize(Application aplctn) {
        ed = getState(DataState.class).getEd();
        am = aplctn.getAssetManager();
        mat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
    }

    @Override
    protected void cleanup(Application aplctn) {
        
    }

    @Override
    protected void onEnable() {
        GarbageShopApp app = (GarbageShopApp)getApplication();
        assets = ed.getEntities(VisualComponent.class, PositionComponent.class);
        app.getRootNode().attachChild(visualNode);
        assets.applyChanges();
        for(Entity e : assets){
            loadAsset(e);
        }
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
                PositionComponent posC = e.get(PositionComponent.class);
                translateAsset(e.getId(), posC.getPosition(), posC.getRotation());
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
        PositionComponent posC = e.get(PositionComponent.class);
        Vector2f position = posC.getPosition();
        float rot = posC.getRotation();
        Spatial spat = am.loadModel(asset);
        assetMap.put(id, spat);
        //for now we're applying a temp material
        ColorComponent col = ed.getComponent(id, ColorComponent.class);
        if(col != null){
            ColorRGBA color = col.getColor();
            if(spat instanceof Geometry){
                colorGeometry((Geometry)spat, color);
            } else if(spat instanceof Node){
                for(Geometry geo : ((Node)spat).descendantMatches(Geometry.class)){
                    colorGeometry(geo, color);
                }
            }
        }
        if(!asset.endsWith("j3o")){
            spat.setMaterial(mat);
        }
        visualNode.attachChild(spat);
        translateAsset(spat, position, rot);
    }
    
    private void colorGeometry(Geometry geo, ColorRGBA color){
        Material mat = geo.getMaterial();
        mat.setColor("Color", color);
    }
    
    private void translateAsset(EntityId id, Vector2f position, float rot){
        translateAsset(assetMap.get(id), position, rot);
    }
    
    private void translateAsset(Spatial spat, Vector2f position, float rotation){
        spat.setLocalTranslation(Vectors.vec2ToVec3(position));
        Quaternion rot = new Quaternion().fromAngleAxis(rotation, Vector3f.UNIT_Z);
        spat.setLocalRotation(rot);
    }
}
