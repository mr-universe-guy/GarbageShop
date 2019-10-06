/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scene;

import com.GarbageShopApp;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * Builds the world map, makes things collide-able, tracks spawn locations, pretty much it.
 * The world is a grid, and the players shop area is a much smaller grid
 * @author matt
 */
public class SceneState extends BaseAppState{
    private Scene scene;
    private final Node mapNode = new Node("Map_Objects");

    @Override
    protected void initialize(Application aplctn) {
        GarbageShopApp app = (GarbageShopApp)aplctn;
        scene = new Scene(new Vector2f(13,22), new Vector2f(13,-2));
        //build collision objects
        scene.addCollisionObject(new StaticGridObject(new Coordinate(0,0), new Coordinate(10,10)));
        scene.addCollisionObject(new StaticGridObject(new Coordinate(0,12), new Coordinate(10,8)));
        scene.addCollisionObject(new StaticGridObject(new Coordinate(16,0), new Coordinate(7,4)));
        scene.addCollisionObject(new StaticGridObject(new Coordinate(16,6), new Coordinate(7,14)));
        scene.addCollisionObject(new StaticGridObject(new Coordinate(-1,10), new Coordinate(1,2)));
        scene.addCollisionObject(new StaticGridObject(new Coordinate(23,4), new Coordinate(1,2)));
        //set some item spawners
        scene.addItemSpawnArea(new StaticGridObject(new Coordinate(3,10), new Coordinate(6,2)));
        scene.addItemSpawnArea(new StaticGridObject(new Coordinate(17,4), new Coordinate(4,2)));
        //add some sleep zones
        scene.addSleepZone(new StaticGridObject(new Coordinate(0,10), new Coordinate(1,2)));
        
        //build map visuals
        AssetManager am = app.getAssetManager();
//        Spatial dumpster = am.loadModel("models/dumpster.gltf");
//        Material mat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
//        dumpster.setMaterial(mat);
//        dumpster.setLocalTranslation(1, 11, 0);
//        mapNode.attachChild(dumpster);
        
        Spatial buildingA = am.loadModel("object/BuildingAObject.j3o");
        mapNode.attachChild(buildingA);
        Spatial buildingB = am.loadModel("object/BuildingBObject.j3o");
        mapNode.attachChild(buildingB);
        Spatial buildingC = am.loadModel("object/BuildingCObject.j3o");
        mapNode.attachChild(buildingC);
        Spatial buildingD = am.loadModel("object/BuildingDObject.j3o");
        mapNode.attachChild(buildingD);
        Spatial road = am.loadModel("object/RoadObject.j3o");
        mapNode.attachChild(road);
        
        app.getRootNode().attachChild(mapNode);
    }

    @Override
    protected void cleanup(Application aplctn) {
        
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {
        
    }

    public Scene getMap() {
        return scene;
    }
}
