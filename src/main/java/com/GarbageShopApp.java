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
import com.scene.SceneState;
import com.scene.VisualState;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.InputMapper;
import com.ui.CameraState;
import com.ui.GamePlayMenu;
import com.ui.Inputs;
import com.ui.PlayerInputState;
import com.unit.DriverComponent;
import com.unit.MobComponent;
import com.unit.MobState;
import com.unit.PositionComponent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author matt
 */
public class GarbageShopApp extends SimpleApplication{
    public static final String GAME_UI_MENU = "Game_UI";
    private final List<TimeListener> timers = new ArrayList<>();
    private InputMapper inputMapper;
    private EntityId playerId;
    private final float hourDuration = (24f)/(5f);
    private float hourProgress = 0f;
    public int curHour = 6;
    public int curDay = 0;

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
        MenuDirectorState menus = new MenuDirectorState();
        menus.registerMenu(GAME_UI_MENU, new GamePlayMenu());
        menus.setNextMenu(GAME_UI_MENU);
        
        //attach everything else
        stateManager.attachAll(new SceneState(),
                new VisualState(),
                new MobState(),
                new CollisionDebugState(),
                new PlayerInputState(),
                new CameraState(),
                menus
        );
        //temp
        inputManager.setCursorVisible(false);
        //
    }

    @Override
    public void simpleUpdate(float tpf) {
        if((hourProgress+=tpf) > hourDuration){
            setHour(curHour++);
        }
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

    private void setHour(int hour) {
        hourProgress = 0;
        if(curHour > 22) return;//day ends at 10pm, time "stops" and player can dick around
        for(TimeListener timer : timers){
            timer.setHour(curHour);
        }
    }
    
    private void sleep(){
        curHour = 0;
        for(TimeListener timer : timers){
            timer.setHour(0);
        }
    }
    
    public void addTimeListener(TimeListener timer){
        timers.add(timer);
    }
    
    public void removeTimeListener(TimeListener timer){
        timers.remove(timer);
    }
}
