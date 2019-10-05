/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scene;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;

/**
 * Builds the world map, makes things collide-able, tracks spawn locations, pretty much it.
 * The world is a grid, and the players shop area is a much smaller grid
 * @author matt
 */
public class MapState extends BaseAppState{
    private Map map;

    @Override
    protected void initialize(Application aplctn) {
        map = new Map();
        //build collision objects
        map.addGridObject(new StaticGridObject(new Coordinate(0,0), new Coordinate(6,10)));
        map.addGridObject(new StaticGridObject(new Coordinate(0,10), new Coordinate(1,2)));
        map.addGridObject(new StaticGridObject(new Coordinate(0,12), new Coordinate(24,8)));
        map.addGridObject(new StaticGridObject(new Coordinate(6,0), new Coordinate(10,2)));
        map.addGridObject(new StaticGridObject(new Coordinate(16,0), new Coordinate(8,10)));
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

    public Map getMap() {
        return map;
    }
}
