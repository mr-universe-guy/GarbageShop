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
import com.jme3.input.InputManager;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.mrugames.menumonkey.MenuDirectorState;
import com.scene.Collisions;
import com.scene.Vectors;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntitySet;
import com.simsilica.es.WatchedEntity;
import com.simsilica.lemur.input.AnalogFunctionListener;
import com.simsilica.lemur.input.FunctionId;
import com.unit.DriverComponent;
import com.unit.Item;
import com.unit.ItemComponent;
import com.unit.Items;
import com.unit.PositionComponent;

/**
 *
 * @author matt
 */
public class PlayerInputState extends BaseAppState{
    private EntityData ed;
    private EntitySet pickableItems;
    private WatchedEntity player;
    private Vector2f movement = new Vector2f(0,0);
    private boolean needUpdate = false;
    private Camera cam;
    private InputManager inputManager;
    private Item heldItem = null;
    private GamePlayMenu gameUI;
    
    private final AnalogFunctionListener ano = (FunctionId fi, double d, double d1) -> {
        if(Inputs.MOVE_X.equals(fi)){
            movement.x = (float)d;
            needUpdate = true;
        }
        if(Inputs.MOVE_Y.equals(fi)){
            movement.y = (float)d;
            needUpdate =true;
        }
    };

    @Override
    protected void initialize(Application aplctn) {
        ed = getState(DataState.class).getEd();
    }

    @Override
    protected void cleanup(Application aplctn) {
        
    }

    @Override
    protected void onEnable() {
        GarbageShopApp app = (GarbageShopApp)getApplication();
        cam = app.getCamera();
        app.getInputMapper().addAnalogListener(ano, Inputs.MOVE_X, Inputs.MOVE_Y);
        inputManager = app.getInputManager();
        player = ed.watchEntity(app.getPlayerId(), PositionComponent.class);
        pickableItems = ed.getEntities(ItemComponent.class, PositionComponent.class);
        gameUI = getState(MenuDirectorState.class).getMenu(GarbageShopApp.GAME_UI_MENU);
    }

    @Override
    protected void onDisable() {
        GarbageShopApp app = (GarbageShopApp)getApplication();
        app.getInputMapper().removeAnalogListener(ano, Inputs.MOVE_X, Inputs.MOVE_Y);
        player.release();
    }

    @Override
    public void update(float tpf) {
        if(needUpdate){
            ed.setComponent(player.getId(), new DriverComponent(movement.clone()));
        }
        player.applyChanges();
        pickableItems.applyChanges();
        //find any items within reach distance of player and see if our mouse is
        //hovering over them, if so highlight them?
        Vector2f playerPos = player.get(PositionComponent.class).getPosition();
        Vector3f playerScreenPos = cam.getScreenCoordinates(Vectors.vec2ToVec3(playerPos));
        Vector3f cursorWorldPos =
                cam.getWorldCoordinates(inputManager.getCursorPosition(), playerScreenPos.z);
        //try to collide with an item
        gameUI.setCursorText("");
        for(Entity e : pickableItems){
            PositionComponent pos = e.get(PositionComponent.class);
            Item item = Items.ITEMMAP.get(e.get(ItemComponent.class).getItemKey());
            if(Collisions.pointInRotatedBox(Vectors.vec3ToVec2(cursorWorldPos),
                    pos.getPosition(), new Vector2f(item.getWidth()*0.1f, item.getHeight()*0.1f), pos.getRotation())){
                gameUI.setCursorText(item.getItemName());
                break;
            }
        }
    }
    
}
