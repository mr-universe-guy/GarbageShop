/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author matt
 */
public class Dialogues {
    private static final Random RANDOM = new Random();
    private static final String[] DISMISSALS = new String[]{
        "Please leave me alone.",
        "I don't have any spare change.",
        "Get out of my way."
    };
    
    public static String getDismissal(){
        return DISMISSALS[RANDOM.nextInt(DISMISSALS.length)];
    }
}
