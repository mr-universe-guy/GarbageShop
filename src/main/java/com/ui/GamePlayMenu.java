/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ui;

import com.GarbageShopApp;
import com.TimeListener;
import com.jme3.scene.Node;
import com.mrugames.menumonkey.BaseMenu;
import com.mrugames.menumonkey.MenuDirectorState;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;

/**
 * The game menu is very very simple UI
 * A clock at the top right
 * Your current cash top center
 * The item you are inspecting hovering mid screen
 * @author matt
 */
public class GamePlayMenu extends BaseMenu implements TimeListener{
    private Label cashLabel, clockLabel;

    public GamePlayMenu() {
        Node node = getMenuNode();
        
        //cash label top right
        cashLabel = new Label("$0");
        cashLabel.setMaxWidth(100);
        cashLabel.setTextHAlignment(HAlignment.Right);
        node.attachChild(cashLabel);
        
        clockLabel = new Label("00:00");
        clockLabel.setTextHAlignment(HAlignment.Center);
        node.attachChild(clockLabel);
    }

    @Override
    public void onEnable() {
        MenuDirectorState director = getDirector();
        director.alignPanelToScreen(cashLabel, 1, -1);
        director.alignPanelToScreen(clockLabel, 0, -1);
        GarbageShopApp app = (GarbageShopApp)director.getApplication();
        setHour(app.curHour);
        app.addTimeListener(this);
    }

    @Override
    public void onDisable() {
        
    }

    @Override
    public void setHour(int hour) {
        clockLabel.setText(String.format("%02d:00", hour));
    }
    
}
