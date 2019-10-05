/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ui;

import com.GarbageShopApp;
import com.entity.DataState;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.scene.Vectors;
import com.simsilica.es.EntityData;
import com.simsilica.es.WatchedEntity;
import com.unit.PositionComponent;

/**
 *
 * @author matt
 */
public class CameraState extends BaseAppState{
    private EntityData ed;
    private WatchedEntity player;
    private Camera cam;
    private float zoom = 10f;

    @Override
    protected void initialize(Application aplctn) {
        //first we kill the flycam n such
        GarbageShopApp app = (GarbageShopApp)aplctn;
        app.getFlyByCamera().setEnabled(false);
        //set up ed n stuff
        ed = getState(DataState.class).getEd();
        //and camera
        cam = app.getCamera();
    }

    @Override
    protected void cleanup(Application aplctn) {
        
    }

    @Override
    protected void onEnable() {
        GarbageShopApp app = (GarbageShopApp)getApplication();
        player = ed.watchEntity(app.getPlayerId(), PositionComponent.class);
        player.applyChanges();
        centerCameraOnPosition(player.get(PositionComponent.class).getPosition());
    }

    @Override
    protected void onDisable() {
        player.release();
    }

    @Override
    public void update(float tpf) {
        if(player.applyChanges()){
            centerCameraOnPosition(player.get(PositionComponent.class).getPosition());
        }
    }
    
    private void centerCameraOnPosition(Vector2f worldPosition){
        Vector3f targetPos = Vectors.vec2ToVec3(worldPosition);
        targetPos.z = zoom;
        cam.setLocation(targetPos);
    }
}
