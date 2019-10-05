/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import com.entity.DataState;
import com.jme3.app.SimpleApplication;
import com.mrugames.menumonkey.MenuDirectorState;
import com.scene.CollisionDebugState;
import com.scene.MapState;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.InputMapper;

/**
 *
 * @author matt
 */
public class GarbageShopApp extends SimpleApplication{
    private InputMapper inputMapper;

    @Override
    public void simpleInitApp() {
        //very first state is data state
        stateManager.attach(new DataState());
        //prep ui
        GuiGlobals.initialize(this);
        GuiGlobals globals = GuiGlobals.getInstance();
        inputMapper = globals.getInputMapper();
//        MenuDirectorState menus = new MenuDirectorState();
        
        //attach everything else
        stateManager.attachAll(
                new MapState(),
                new CollisionDebugState()
        );
        //temp
        inputManager.setCursorVisible(false);
        //
    }
    
}
