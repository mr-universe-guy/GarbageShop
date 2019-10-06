/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ui;

import com.GarbageShopApp;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.mrugames.menumonkey.BaseMenu;
import com.scene.Coordinate;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.component.BorderLayout;
import com.unit.HeldItemListener;
import com.unit.Inventory;
import com.unit.Inventory.InventoryItem;
import com.unit.Item;
import java.util.ArrayList;
import java.util.List;

/**
 * The inventory menu needs to display the players inventory as a grid,
 * and need to show the current held item
 * @author matt
 */
public class InventoryMenu extends BaseMenu implements HeldItemListener, GridSelectionAction{
    private final List<Spatial> inventorySpatials = new ArrayList<>();
    private final Container inventoryContainer = new Container(new BorderLayout());
    private float cellSize = 32f;//size of grid cells in pixels
    private final GridPanel grid;
    private final Inventory inventory;
    private Spatial heldSpatial;
    private Item heldItem;
    private AssetManager am;
    private InputManager im;
    private Camera guiCam;
    private Material temp;

    public InventoryMenu(Inventory inventory) {
        this.inventory = inventory;
        Node menuNode = getMenuNode();
        Label title = new Label("Inventory");
        inventoryContainer.addChild(title, BorderLayout.Position.North);
        grid = new GridPanel(32f, Inventory.STARTWIDTH, Inventory.STARTHEIGHT);
        grid.setSelectionAction(this);
        inventoryContainer.addChild(grid, BorderLayout.Position.Center);
        menuNode.attachChild(inventoryContainer);
    }

    @Override
    public void onEnable() {
        GarbageShopApp app = (GarbageShopApp)getDirector().getApplication();
        app.addHeldItemListener(this);
        am = app.getAssetManager();
        temp = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        im = app.getInputManager();
        guiCam = getDirector().getGuiCamera();
        //fill inventory space
        getDirector().centerPanelToScreen(inventoryContainer);
        renderInventoryItems();
        //get held item
        Item item = app.getHeldItem();
        itemHeld(item);
    }

    @Override
    public void onDisable() {
        GarbageShopApp app = (GarbageShopApp)getDirector().getApplication();
        app.removeHeldItemListener(this);
    }

    @Override
    public void update(float tpf) {
        //move the item on screen with player cursor
        if(heldSpatial != null){
            Vector2f cPos = im.getCursorPosition();
            Vector3f screenPos = new Vector3f(cPos.x, cPos.y, 5);
            heldSpatial.setLocalTranslation(screenPos);
        }
    }

    @Override
    public void itemHeld(Item item) {
        this.heldItem = item;
        holdItem(item);
        grid.setHeldItem(item);
    }
    
    private void holdItem(Item item){
        if(heldSpatial != null){
            heldSpatial.removeFromParent();
            heldSpatial = null;
        }
        if(item == null) return;
        Spatial spat = loadSpatial(item.getAssetPath());
        heldSpatial = spat;
        getMenuNode().attachChild(spat);
    }

    @Override
    public void onGridSelection(Coordinate coordinate) {
        if(heldItem == null){
            //pickup item from inventory
            InventoryItem item = inventory.getInventoryItemAtCoordinate(coordinate);
            if(item == null) return;
            inventory.removeItem(item);
            ((GarbageShopApp)getDirector().getApplication()).setHeldItem(item.item);
            renderInventoryItems();
            return;
        }
        //attempt to place item into inventory
        if(inventory.placeItemAtCoordinate(coordinate, heldItem)){
            //success, let app know we are no longer holding an item
            ((GarbageShopApp)getDirector().getApplication()).setHeldItem(null);
            renderInventoryItems();
        } else{
            //failed :(
        }
    }
    
    private void renderInventoryItems(){
        for(Spatial s : inventorySpatials){
            s.removeFromParent();
        }
        Node menuNode = getMenuNode();
        for(InventoryItem invItem : inventory.getItemList()){
            Item item =invItem.item;
            Spatial spat = loadSpatial(item.getAssetPath());
            //get grid cell to position this item over
            Panel cell = grid.getPanelAtCoordinate(invItem.pos);
            //world position of cell
            Vector3f worldPos = cell.getWorldTranslation();
            spat.setLocalTranslation(worldPos.x+(item.getWidth()*cellSize*0.5f)
                    , worldPos.y-(item.getHeight()*cellSize*0.5f), 1);
            inventorySpatials.add(spat);
            menuNode.attachChild(spat);
        }
    }
    
    private Spatial loadSpatial(String asset){
        Spatial spat = am.loadModel(asset);
        //temp add a default material, gotta fix this soon!
        spat.setMaterial(temp);
        spat.setLocalScale(cellSize*10);
        return spat;
    }
}
