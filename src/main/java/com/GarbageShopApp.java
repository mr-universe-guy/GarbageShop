/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import com.entity.DataState;
import com.jme3.app.SimpleApplication;
import com.scene.CollisionDebugState;
import com.scene.SceneState;
import com.scene.VisualState;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.InputMapper;
import com.ui.CameraState;
import com.ui.Inputs;
import com.ui.PlayerInputState;
import com.unit.DriverComponent;
import com.unit.MobComponent;
import com.unit.MobState;
import com.unit.PositionComponent;

/**
 *
 * @author matt
 */
public class GarbageShopApp extends SimpleApplication{
    private InputMapper inputMapper;
    private EntityId playerId;

    @Override
    public void simpleInitApp() {
        //very first state is data state
        DataState data = new DataState();
        EntityData ed = data.getEd();
        playerId = ed.createEntity();
        stateManager.attach(data);
        spawnPlayer(ed);
        //prep ui
        GuiGlobals.initialize(this);
        GuiGlobals globals = GuiGlobals.getInstance();
        inputMapper = globals.getInputMapper();
        Inputs.registerDefaultInput(inputMapper);
//        MenuDirectorState menus = new MenuDirectorState();
        
        //attach everything else
        stateManager.attachAll(new SceneState(),
                new VisualState(),
                new MobState(),
                new CollisionDebugState(),
                new PlayerInputState(),
                new CameraState()
        );
        //temp
        inputManager.setCursorVisible(false);
        //
    }
    
    private void spawnPlayer(EntityData ed){
        ed.setComponents(playerId,
                new MobComponent(1f,0.3f),
                new DriverComponent(0f,0f),
                new PositionComponent(3f,11f)
        );
    }

    public InputMapper getInputMapper() {
        return inputMapper;
    }

    public EntityId getPlayerId() {
        return playerId;
    }
    
}
