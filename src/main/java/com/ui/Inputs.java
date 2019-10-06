/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ui;

import com.jme3.input.KeyInput;
import com.simsilica.lemur.input.Button;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;

/**
 * Here be player inputs
 * @author matt
 */
public class Inputs {
    public static final String PLAYERGROUP = "Player";
    public static final FunctionId MOVE_Y = new FunctionId(PLAYERGROUP, "Y_MOVEMENT");
    public static final FunctionId MOVE_X = new FunctionId(PLAYERGROUP, "X_MOVEMENT");
    public static final FunctionId INTERACT = new FunctionId(PLAYERGROUP, "INTERACT");
    public static final FunctionId INVENTORY = new FunctionId(PLAYERGROUP, "OPEN_INVENTORY");
    
    public static void registerDefaultInput(InputMapper im){
        im.activateGroup(PLAYERGROUP);
        im.map(MOVE_Y, InputState.Positive, KeyInput.KEY_W);
        im.map(MOVE_Y, InputState.Negative, KeyInput.KEY_S);
        im.map(MOVE_X, InputState.Positive, KeyInput.KEY_D);
        im.map(MOVE_X, InputState.Negative, KeyInput.KEY_A);
        im.map(INTERACT, Button.MOUSE_BUTTON1);
        im.map(INVENTORY, KeyInput.KEY_TAB);
    }
}
