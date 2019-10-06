/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ui;

import com.mrugames.menumonkey.BaseMenu;
import com.simsilica.lemur.Action;
import com.simsilica.lemur.ActionButton;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.BoxLayout;

/**
 *
 * @author matt
 */
public class DialogueMenu extends BaseMenu{
    private final Container dialogueContainer = new Container(new BoxLayout(Axis.Y, FillMode.None));
    private final Label characterLabel = new Label("");
    private final Label responseLabel = new Label("");
    private final Container actionContainer = new Container(new BoxLayout(Axis.X, FillMode.Proportional));
    private Action[] actions;
    
    public DialogueMenu(){
        characterLabel.setMaxWidth(300);
        characterLabel.setTextHAlignment(HAlignment.Center);
        dialogueContainer.addChild(characterLabel);
        responseLabel.setMaxWidth(300);
        dialogueContainer.addChild(responseLabel);
        //actions
        dialogueContainer.addChild(actionContainer);
        getMenuNode().attachChild(dialogueContainer);
    }

    @Override
    public void onEnable() {
        getDirector().centerPanelToScreen(dialogueContainer);
        updateActions();
    }

    @Override
    public void onDisable() {
        
    }
    
    private void updateActions(){
        actionContainer.clearChildren();
        for(Action a : actions){
            ActionButton b = new ActionButton(a);
            actionContainer.addChild(b);
        }
    }

    public void setActions(Action... actions) {
        this.actions = actions;
        updateActions();
    }
    
    public void setCharacterName(String name){
        characterLabel.setText(name);
    }
    
    public void setMessage(String message){
        responseLabel.setText(message);
    }
}
