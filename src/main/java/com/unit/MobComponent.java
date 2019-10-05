/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unit;

import com.simsilica.es.EntityComponent;

/**
 *
 * @author matt
 */
public class MobComponent implements EntityComponent{
    private final float speed;
    private final float radius;

    public MobComponent(float speed, float radius) {
        this.speed = speed;
        this.radius = radius;
    }

    public float getSpeed() {
        return speed;
    }

    public float getRadius() {
        return radius;
    }
}
