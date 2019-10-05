/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.simsilica.es.EntityData;
import com.simsilica.es.base.DefaultEntityData;

/**
 * Basically only a storage class for entity data
 * @author matt
 */
public class DataState extends BaseAppState{
    private final EntityData ed;
    
    public DataState(){
        ed = new DefaultEntityData();
    }

    public EntityData getEd() {
        return ed;
    }

    @Override
    protected void initialize(Application aplctn) {
        
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
}
