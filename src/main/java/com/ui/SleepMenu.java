/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ui;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.mrugames.menumonkey.BaseMenu;
import com.mrugames.menumonkey.MenuDirectorState;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.component.BoxLayout;

/**
 * Cover the screen in black, display current funds - cost of living
 * @author matt
 */
public class SleepMenu extends BaseMenu{
    private final Panel fadePanel = new Panel(800,600,ColorRGBA.Black);
    private final Container sleepInfo = new Container(new BoxLayout(Axis.Y, FillMode.Proportional));
    
    public SleepMenu(){
        Node menuNode = getMenuNode();
        menuNode.attachChild(fadePanel);
        sleepInfo.setPreferredSize(new Vector3f(400,400,0));
        menuNode.attachChild(sleepInfo);
    }

    @Override
    public void onEnable() {
        MenuDirectorState director = getDirector();
        Vector2f screenSize = director.getScreenSize();
        fadePanel.setPreferredSize(new Vector3f(screenSize.x, screenSize.x, 0));
        director.centerPanelToScreen(fadePanel);
        director.centerPanelToScreen(sleepInfo);
    }

    @Override
    public void onDisable() {
        sleepInfo.clearChildren();
    }
    
    public Panel getFadePanel(){
        return fadePanel;
    }
    
    public Container getSleepInfoContainer(){
        return sleepInfo;
    }
}
