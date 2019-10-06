/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unit;

import java.util.Random;

/**
 *
 * @author matt
 */
public class Names {
    private static final Random random = new Random();
    public static final String[] NAMES = new String[]{
        "John","Rebecca","Paige","Matt","Jessica","Cayde","Simon","Denis","Nicole",
        "Sandy","Britanny","Travis","Dan","Pam","Jon","Dirk","Vincent","Steve",
        "Steven","Tyler","Mike","Michael","Alex","Alexa","Samantha","Elissa","Bridget",
        "Kyle","Keith","Borris","Red","Karen","Coraline","Henry","George","Fred"
    };
    
    public static String getRandomName(){
        return NAMES[random.nextInt(NAMES.length)];
    }
}
