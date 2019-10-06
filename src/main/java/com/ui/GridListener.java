/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ui;

import com.scene.Coordinate;

/**
 *
 * @author matt
 */
public interface GridListener {
    public void onGridSelection(Coordinate coordinate);
    public void onGridHighlight(Coordinate coordinate);
}
