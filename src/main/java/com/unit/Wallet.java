/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unit;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple holder for the player funds and wallet listeners
 * @author matt
 */
public class Wallet {
    private List<WalletListener> listeners = new ArrayList<>();
    private long cash;
    
    public Wallet(long startingCash){
        this.cash = startingCash;
    }

    public long getCash() {
        return cash;
    }
    
    public long transaction(long amount){
        cash += amount;
        for(WalletListener listener : listeners){
            listener.onTransaction(amount, cash);
        }
        return cash;
    }
    
    public void addListener(WalletListener listener){
        listeners.add(listener);
    }
    
    public boolean removeListener(WalletListener listener){
        return listeners.remove(listener);
    }
}
