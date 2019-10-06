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
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.simsilica.es.WatchedEntity;
import com.simsilica.lemur.input.AnalogFunctionListener;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;
import com.unit.DriverComponent;
import com.unit.Item;
import com.unit.ItemComponent;
import com.unit.ItemState;
import com.unit.Items;
import com.unit.MobComponent;
import com.unit.NameComponent;
import com.unit.PositionComponent;

/**
 *
 * @author matt
 */
public class PlayerInputState extends BaseAppState{
    private EntityData ed;
    private EntitySet pickableItems, pickableMobs;
    private WatchedEntity player;
    private Vector2f movement = new Vector2f(0,0);
    private boolean needUpdate = false;
    private Camera cam;
    private InputManager inputManager;
    private MenuDirectorState menus;
    private GamePlayMenu gameUI;
    private EntityId highlightedId = null;
    private boolean sprint = false;
    
    private final AnalogFunctionListener ano = (FunctionId fi, double d, double d1) -> {
        if(Inputs.MOVE_X.equals(fi)){
            movement.x = (float)d;
            needUpdate = true;
        }
        if(Inputs.MOVE_Y.equals(fi)){
            movement.y = (float)d;
            needUpdate = true;
        }
    };
    
    private final StateFunctionListener state = (FunctionId fi, InputState is, double d) -> {
        if(Inputs.INTERACT.equals(fi) && InputState.Positive.equals(is)){
            interact();
        }
        if(Inputs.INVENTORY.equals(fi) && InputState.Positive.equals(is)){
            toggleInventory();
        }
        if(Inputs.SPRINT.equals(fi)){
            sprint = InputState.Positive.equals(is);
            needUpdate = true;
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
        InputMapper im = app.getInputMapper();
        im.addAnalogListener(ano, Inputs.MOVE_X, Inputs.MOVE_Y);
        im.addStateListener(state, Inputs.INTERACT, Inputs.INVENTORY, Inputs.SPRINT);
        inputManager = app.getInputManager();
        player = ed.watchEntity(app.getPlayerId(), PositionComponent.class);
        pickableItems = ed.getEntities(ItemComponent.class, NameComponent.class, PositionComponent.class);
        pickableMobs = ed.getEntities(MobComponent.class, NameComponent.class, PositionComponent.class);
        menus = getState(MenuDirectorState.class);
        gameUI = menus.getMenu(Menus.GAME_UI_MENU);
    }

    @Override
    protected void onDisable() {
        GarbageShopApp app = (GarbageShopApp)getApplication();
        InputMapper im = app.getInputMapper();
        im.removeAnalogListener(ano, Inputs.MOVE_X, Inputs.MOVE_Y);
        im.removeStateListener(state, Inputs.INTERACT);
        player.release();
    }

    @Override
    public void update(float tpf) {
        if(needUpdate){
            if(sprint){
                ed.setComponent(player.getId(), new DriverComponent(movement.mult(2)));
            } else{
                ed.setComponent(player.getId(), new DriverComponent(movement.clone()));
            }
        }
        player.applyChanges();
        pickableItems.applyChanges();
        pickableMobs.applyChanges();
        //find any items within reach distance of player and see if our mouse is
        //hovering over them, if so highlight them?
        Vector2f playerPos = player.get(PositionComponent.class).getPosition();
        Vector3f playerScreenPos = cam.getScreenCoordinates(Vectors.vec2ToVec3(playerPos));
        Vector3f cursorWorldPos =
                cam.getWorldCoordinates(inputManager.getCursorPosition(), playerScreenPos.z);
        Vector2f cursor = Vectors.vec3ToVec2(cursorWorldPos);
        //try to collide with an item
        highlightEntity(null);
        for(Entity e : pickableItems){
            PositionComponent pos = e.get(PositionComponent.class);
            Item item = Items.ITEMMAP.get(e.get(ItemComponent.class).getItemKey());
            if(Collisions.pointInRotatedBox(cursor,
                    pos.getPosition(), new Vector2f(item.getWidth()*0.1f,
                            item.getHeight()*0.1f), pos.getRotation())){
                highlightEntity(e);
                break;
            }
        }
        //also try to pick people to talk to
        for(Entity e : pickableMobs){
            if(e.getId().equals(player.getId())){
                continue;
            }
            Vector2f pos = e.get(PositionComponent.class).getPosition();
            float size = e.get(MobComponent.class).getRadius();
            if(Collisions.pointInCircle(cursor, pos, size)){
                highlightEntity(e);
                break;
            }
        }
    }
    
    /**
     * Highlight an item under the cursor such as an item, a piece of furniture 
     * or an npc
     * @param id 
     */
    private void highlightEntity(Entity e){
        if(e == null){
            highlightedId = null;
            gameUI.setCursorText("");
            return;
        }
        highlightedId = e.getId();
        gameUI.setCursorText(e.get(NameComponent.class).getName());
    }
    
    private void interact(){
        GarbageShopApp app = (GarbageShopApp)getApplication();
        Item heldItem = app.getHeldItem();
        if(heldItem != null){
            //we're holding something so this does new stuff
            if(highlightedId == null){
                //set our item down
                Vector2f cPos = getApplication().getInputManager().getCursorPosition();
                Vector3f pos = cam.getWorldCoordinates(cPos, cam.distanceToNearPlane(Vector3f.ZERO));
                getState(ItemState.class).spawnItem(heldItem, Vectors.vec3ToVec2(pos), 0);
                app.setHeldItem(null);
                menus.setNextMenu(Menus.GAME_UI_MENU);
            }
            return;
        }
        Entity e = pickableItems.getEntity(highlightedId);
        if(e != null){
            //e is an item
            Item item = Items.ITEMMAP.get(e.get(ItemComponent.class).getItemKey());
            //this item is going into our hand
            heldItem = item;
            app.setHeldItem(heldItem);
            ed.removeEntity(highlightedId);
            //open the inventory screen
            menus.setNextMenu(Menus.INVENTORY_UI_MENU);
            return;
        }
        e = pickableMobs.getEntity(highlightedId);
        if(e != null){
            //e is a mob
            getState(DialogueState.class).startConvo(highlightedId);
        }
    }
    
    private void toggleInventory(){
        if(isInventoryOpen()){
            menus.setNextMenu(Menus.GAME_UI_MENU);
        } else{
            menus.setNextMenu(Menus.INVENTORY_UI_MENU);
        }
    }
    
    private boolean isInventoryOpen(){
        return Menus.INVENTORY_UI_MENU.equals(menus.getActiveMenuKey());
    }
}
