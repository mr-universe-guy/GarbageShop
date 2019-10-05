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
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.lemur.input.AnalogFunctionListener;
import com.simsilica.lemur.input.FunctionId;
import com.unit.DriverComponent;

/**
 *
 * @author matt
 */
public class PlayerInputState extends BaseAppState{
    private EntityData ed;
    private EntityId playerId;
    private Vector2f movement = new Vector2f(0,0);
    private boolean needUpdate = false;
    
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
        playerId = ((GarbageShopApp)aplctn).getPlayerId();
    }

    @Override
    protected void cleanup(Application aplctn) {
        
    }

    @Override
    protected void onEnable() {
        GarbageShopApp app = (GarbageShopApp)getApplication();
        app.getInputMapper().addAnalogListener(ano, Inputs.MOVE_X, Inputs.MOVE_Y);
    }

    @Override
    protected void onDisable() {
        GarbageShopApp app = (GarbageShopApp)getApplication();
        app.getInputMapper().removeAnalogListener(ano, Inputs.MOVE_X, Inputs.MOVE_Y);
    }

    @Override
    public void update(float tpf) {
        if(needUpdate){
            ed.setComponent(playerId, new DriverComponent(movement.clone()));
        }
    }
    
}
