/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ui;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import com.scene.Collisions;
import com.scene.Coordinate;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.core.GuiControl;
import com.simsilica.lemur.core.GuiLayout;
import com.simsilica.lemur.event.DefaultMouseListener;
import com.unit.Item;

/**
 * A panel that is used to display a grid
 * @author matt
 */
public class GridPanel extends Panel{
    private GridListener gridListener;
    private float cellSize;
    private int width;
    private int height;
    private Item heldItem;
    private ColorRGBA emptyColor = ColorRGBA.Black;
    private ColorRGBA occupiedColor = ColorRGBA.White;
    private ColorRGBA selectionColor = ColorRGBA.Yellow;
    private ColorRGBA gridColorA = ColorRGBA.Gray;
    private ColorRGBA gridColorB = ColorRGBA.DarkGray;
    
    public GridPanel(float cellSizePixels, int width, int height){
        this.cellSize = cellSizePixels;
        this.width = width;
        this.height = height;
        GuiLayout layout = new SpringGridLayout(Axis.X, Axis.Y);
        getControl(GuiControl.class).setLayout(layout);
        //fill children
        rebuildGrid();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        rebuildGrid();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        rebuildGrid();
    }

    public float getCellSize() {
        return cellSize;
    }

    public void setCellSize(float cellSize) {
        this.cellSize = cellSize;
        rebuildGrid();
    }

    public Item getHeldItem() {
        return heldItem;
    }

    public void setHeldItem(Item heldItem) {
        this.heldItem = heldItem;
    }

    public ColorRGBA getEmptyColor() {
        return emptyColor;
    }

    public void setEmptyColor(ColorRGBA emptyColor) {
        this.emptyColor = emptyColor;
    }

    public ColorRGBA getOccupiedColor() {
        return occupiedColor;
    }

    public void setOccupiedColor(ColorRGBA occupiedColor) {
        this.occupiedColor = occupiedColor;
    }

    public ColorRGBA getSelectionColor() {
        return selectionColor;
    }

    public void setSelectionColor(ColorRGBA selectionColor) {
        this.selectionColor = selectionColor;
    }

    public ColorRGBA getGridColorA() {
        return gridColorA;
    }

    public void setGridColorA(ColorRGBA gridColorA) {
        this.gridColorA = gridColorA;
    }

    public ColorRGBA getGridColorB() {
        return gridColorB;
    }

    public void setGridColorB(ColorRGBA gridColorB) {
        this.gridColorB = gridColorB;
    }

    public void setSelectionAction(GridListener selectionAction) {
        this.gridListener = selectionAction;
    }
    
    private void rebuildGrid(){
        GuiLayout layout = getControl(GuiControl.class).getLayout();
        layout.clearChildren();
        for(int x=0; x<width; x++){
            for(int y=0; y<height; y++){
                Panel cell = new Panel(cellSize, cellSize);
                cell.addMouseListener(new CellListener(new Coordinate(x,y)));
                ColorRGBA color;
                if(y%2 == 0){
                    if(x %2 == 0){
                        color = gridColorA;
                    } else{
                        color = gridColorB;
                    }
                } else{
                    if(x %2 == 1){
                        color = gridColorA;
                    } else{
                        color = gridColorB;
                    }
                }
                ((QuadBackgroundComponent)cell.getBackground()).setColor(color);
                QuadBackgroundComponent border = new QuadBackgroundComponent();
                border.setMargin(1, 1);
                border.setColor(emptyColor);
                cell.setBorder(border);
                layout.addChild(cell, x,y);
            }
        }
    }
    
    public Panel getPanelAtCoordinate(Coordinate coord){
        SpringGridLayout layout = getControl(GuiControl.class).getLayout();
        return (Panel)layout.getChild(coord.x, coord.y);
    }
    
    private void showGridOccupation(Coordinate pos){
        SpringGridLayout layout = getControl(GuiControl.class).getLayout();
        if(pos == null || heldItem == null){
            for(Spatial spat : layout.getChildren()){
                Panel p = (Panel)spat;
                ((QuadBackgroundComponent)p.getBorder()).setColor(emptyColor);
            }
            return;
        }
        Coordinate max = pos.add(heldItem.getWidth(), heldItem.getHeight());
        for(int x=0; x<width; x++){
            for(int y=0; y<height; y++){
                Coordinate cur = new Coordinate(x,y);
                Panel p = (Panel)layout.getChild(x, y);
                ColorRGBA color;
                if(Collisions.pointInBox(cur, pos, max)){
                    //highlight panels
                    color = selectionColor;
                } else{
                    color = emptyColor;
                }
                ((QuadBackgroundComponent)p.getBorder()).setColor(color);
            }
        }
    }
    
    private class CellListener extends DefaultMouseListener{
        private final Coordinate cellCoord;

        public CellListener(Coordinate cellCoord) {
            this.cellCoord = cellCoord;
        }

        @Override
        public void mouseEntered(MouseMotionEvent mme, Spatial sptl, Spatial sptl1) {
            showGridOccupation(cellCoord);
            gridListener.onGridHighlight(cellCoord);
        }

        @Override
        public void mouseExited(MouseMotionEvent event, Spatial target, Spatial capture) {
            showGridOccupation(null);
            gridListener.onGridHighlight(null);
        }

        @Override
        protected void click(MouseButtonEvent event, Spatial target, Spatial capture) {
            gridListener.onGridSelection(cellCoord);
        }
    }
}
