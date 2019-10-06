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
    private static final String[] SPECIFICBUY = new String[]{
        "Do you have a %1$S for sale?",
        "I'm looking to buy a %1$S.",
        "You wouldn't happen to have a %1$S?"
    };
    private static final String[] REFUSEPURCHASE = new String[]{
        "I'm not going to buy that.",
        "That's not at all what I asked for.",
        "No, thanks."
    };
    private static final String[] CUSTOMEROFFER = new String[]{
        "I'll buy that for $%1$S.",
        "Would you take $%1$S?",
        "How does $%1$S sound?"
    };
    
    public static String getDismissal(){
        return getRandomFromArray(DISMISSALS);
    }
    
    public static String getSpecificBuy(){
        return getRandomFromArray(SPECIFICBUY);
    }
    
    public static String getRefusePurchase(){
        return getRandomFromArray(REFUSEPURCHASE);
    }
    
    public static String getCustomerOffer(){
        return getRandomFromArray(CUSTOMEROFFER);
    }
    
    private static String getRandomFromArray(String[] array){
        return array[RANDOM.nextInt(array.length)];
    }
}
