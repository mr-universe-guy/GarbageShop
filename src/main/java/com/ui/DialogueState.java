/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ui;

import com.entity.DataState;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.mrugames.menumonkey.MenuDirectorState;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.simsilica.lemur.Action;
import com.simsilica.lemur.Button;
import com.unit.NameComponent;
import com.unit.TradeComponent;

/**
 * Tracks mobs who are interested in having a conversation
 * @author matt
 */
public class DialogueState extends BaseAppState{
    private EntityData ed;
    private EntitySet conversations;
    private MenuDirectorState menus;
    private final Action closeAction = new Action("leave"){
        @Override
        public void execute(Button button) {
            closeConvo();ddd
        }
    };

    @Override
    protected void initialize(Application aplctn) {
        ed = getState(DataState.class).getEd();
        menus = getState(MenuDirectorState.class);
    }

    @Override
    protected void cleanup(Application aplctn) {
        
    }

    @Override
    protected void onEnable() {
        conversations = ed.getEntities(NameComponent.class, TradeComponent.class);
    }

    @Override
    protected void onDisable() {
        
    }

    @Override
    public void update(float tpf) {
        conversations.applyChanges();
    }
    
    public void startConvo(EntityId id){
        //parse who we're talkin to
        Entity e = conversations.getEntity(id);
        if(e == null){
            //they don't want to talk to us
            startIgnoreConvo(id);
            return;
        }
        TradeComponent trade = e.get(TradeComponent.class);
        if(trade.isSelling()){
            //they want us to buy from them
        } else{
            //we're trying to sell to them
            startBuyerConvo(id);
        }
    }
    
    private void closeConvo(){
        menus.setNextMenu(Menus.GAME_UI_MENU);
    }
    
    private void startIgnoreConvo(EntityId id){
        //TODO: in the future maybe have different personalities with different dialogues
        Entity e = ed.getEntity(id, NameComponent.class);
        DialogueMenu menu = menus.getMenu(Menus.DIALOGUE_UI_MENU);
        menu.setCharacterName(e.get(NameComponent.class).getName());
        menu.setMessage(Dialogues.getDismissal());
        menu.setActions(closeAction);
        menus.setNextMenu(Menus.DIALOGUE_UI_MENU);
    }

    private void startBuyerConvo(EntityId id) {
        Entity e = conversations.getEntity(id);
        DialogueMenu menu = menus.getMenu(Menus.DIALOGUE_UI_MENU);
        menu.setCharacterName(e.get(NameComponent.class).getName());
        TradeComponent trade = e.get(TradeComponent.class);
        menu.setMessage(String.format(Dialogues.getSpecificBuy(), trade.getItemName()));
        menu.setActions(new Action("Follow Me"){
                @Override
                public void execute(Button button) {

                }
            },
                new Action("Here you go"){
                @Override
                public void execute(Button button) {

                }
            },
                closeAction
        );
    }
}
