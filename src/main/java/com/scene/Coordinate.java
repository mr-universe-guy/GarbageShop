/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scene;

/**
 *
 * @author matt
 */
public class Coordinate {
    int x, y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public Coordinate add(Coordinate other){
        return new Coordinate(x+other.x, y+other.y);
    }
    
    public Coordinate add(int x, int y){
        return new Coordinate(this.x+x, this.y+y);
    }
}
