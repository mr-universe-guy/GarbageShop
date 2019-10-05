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
import com.simsilica.lemur.anim.AbstractTween;
import com.simsilica.lemur.anim.AnimationState;
import com.simsilica.lemur.anim.Tween;
import com.simsilica.lemur.anim.Tweens;
import com.simsilica.lemur.input.InputMapper;
import com.ui.CameraState;
import com.ui.GamePlayMenu;
import com.ui.Inputs;
import com.ui.PlayerInputState;
import com.unit.DriverComponent;
import com.unit.ItemState;
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
    private final List<TimeListener> timeListeners = new ArrayList<>();
    private final List<DayListener> dayListeners = new ArrayList<>();
    private InputMapper inputMapper;
    private EntityId playerId;
    private final float hourDuration = (24f)/(5f);
    private final int wakeupTime = 6;
    private final int closeTime = 22;
    private float hourProgress = 0f;
    public int curHour = wakeupTime;
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
        AnimationState anim = new AnimationState();
        stateManager.attachAll(
                anim,
                new SceneState(),
                new VisualState(),
                new MobState(),
                new ItemState(),
                new CollisionDebugState(),
                new PlayerInputState(),
                new CameraState(),
                menus
        );
        //finally start a new day!
        Tween checkStates = new AbstractTween(0){
            @Override
            protected void doInterpolate(double d) {
                if(!menus.isEnabled()){
                    anim.add(Tweens.delay(0), this);
                } else{
                    sleep();
                }
            }
        };
        anim.add(Tweens.delay(0), checkStates);
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
                new PositionComponent(3f,11f,0)
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
        if(hour > closeTime) return;//day ends at 10pm, time "stops" and player can dick around
        curHour = hour;
        for(TimeListener timer : timeListeners){
            timer.setHour(curHour);
        }
    }
    
    private void sleep(){
        //TODO: fade to black first
        for(DayListener listener : dayListeners){
            listener.nextDay();
        }
        setHour(wakeupTime);
    }
    
    public void addTimeListener(TimeListener timer){
        timeListeners.add(timer);
    }
    
    public void removeTimeListener(TimeListener timer){
        timeListeners.remove(timer);
    }
    
    public void addDayListener(DayListener listener){
        dayListeners.add(listener);
    }
    
    public void removeDayListener(DayListener listener){
        dayListeners.remove(listener);
    }
}
