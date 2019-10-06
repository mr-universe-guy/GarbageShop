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
import com.mrugames.menumonkey.MenuDirectorState;
import com.scene.Coordinate;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.simsilica.lemur.Action;
import com.simsilica.lemur.Button;
import com.unit.Inventory;
import com.unit.Inventory.InventoryItem;
import com.unit.Item;
import com.unit.Items;
import com.unit.NameComponent;
import com.unit.TradeComponent;
import com.unit.Wallet;
import java.util.Random;

/**
 * Tracks mobs who are interested in having a conversation
 * @author matt
 */
public class DialogueState extends BaseAppState{
    private final Random random = new Random();
    private EntityData ed;
    private EntitySet conversations;
    private MenuDirectorState menus;
    private Inventory inventory;
    private Wallet wallet;
    private final Action closeAction = new Action("leave"){
        @Override
        public void execute(Button button) {
            closeConvo();
        }
    };

    @Override
    protected void initialize(Application aplctn) {
        ed = getState(DataState.class).getEd();
        menus = getState(MenuDirectorState.class);
        GarbageShopApp app = (GarbageShopApp)aplctn;
        inventory = app.getPlayerInventory();
        wallet = app.getPlayerWallet();
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
        conversations.release();
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
        menus.setNextMenu(Menus.DIALOGUE_UI_MENU);
        menu.setCharacterName(e.get(NameComponent.class).getName());
        TradeComponent trade = e.get(TradeComponent.class);
        menu.setMessage(String.format(Dialogues.getSpecificBuy(), trade.getItemName()));
        menu.setActions(
                new Action("Here you go"){
                @Override
                public void execute(Button button) {
                    //add a grid intercept to the inventory menu to capture the grid
                    //selection and bring it back here
                    InventoryMenu inven = menus.getMenu(Menus.INVENTORY_UI_MENU);
                    CustomerBuyListener buy = new CustomerBuyListener(e);
                    inven.setGridIntercept(buy);
                    menus.setNextMenu(Menus.INVENTORY_UI_MENU);
                }
            },
                closeAction
        );
    }
    
    public void finalizeSaleConvo(String customerName, TradeComponent trade,
            InventoryItem invItem, Entity customer){
        Item item = invItem.item;
        int customerOffer = item.getMinValue()+random.nextInt(item.getMaxValue()-item.getMinValue());
        DialogueMenu menu = menus.getMenu(Menus.DIALOGUE_UI_MENU);
        menu.setCharacterName(customerName);
        menu.setMessage(String.format(Dialogues.getCustomerOffer(), customerOffer));
        menu.setActions(new AcceptSale(invItem, customerOffer, customer), closeAction);
    }
    
    public void customerRefuseConvo(String name){
        DialogueMenu menu = menus.getMenu(Menus.DIALOGUE_UI_MENU);
        menu.setMessage(Dialogues.getRefusePurchase());
        menu.setCharacterName(name);
        menu.setActions(closeAction);
    }
    
    private void sellTo(Entity buyer, InventoryItem item){
        TradeComponent trade = buyer.get(TradeComponent.class);
        String name = buyer.get(NameComponent.class).getName();
        if(Items.isAcceptable(item.item, Items.ITEMMAP.get(trade.getItemName()))){
            //remove item from inventory, get cash
            finalizeSaleConvo(name, trade, item, buyer);
        } else{
            customerRefuseConvo(name);
        }
    }
    
    private class CustomerBuyListener implements GridListener{
        private Entity customer;
        
        public CustomerBuyListener(Entity customer){
            this.customer = customer;
        }
        
        @Override
        public void onGridSelection(Coordinate coordinate) {
            InventoryItem item = inventory.getInventoryItemAtCoordinate(coordinate);
            if(item == null) return;
            InventoryMenu inven = menus.getMenu(Menus.INVENTORY_UI_MENU);
            inven.setGridIntercept(null);
            menus.setNextMenu(Menus.DIALOGUE_UI_MENU);
            sellTo(customer, item);
        }

        @Override
        public void onGridHighlight(Coordinate coordinate) {
            
        }
    }
    
    private class AcceptSale extends Action{
        private final InventoryItem item;
        private final int amount;
        private final EntityId customerId;
        
        public AcceptSale(InventoryItem item, int amount, Entity customer) {
            super("Accept");
            this.item = item;
            this.amount = amount;
            this.customerId = customer.getId();
        }

        @Override
        public void execute(Button button) {
            inventory.removeItem(item);
            wallet.transaction(amount);
            ed.removeComponent(customerId, TradeComponent.class);
            menus.setNextMenu(Menus.GAME_UI_MENU);
        }
        
    }
}
