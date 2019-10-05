/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scene;

import com.GarbageShopApp;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Draws the collision area of all map objects and all moving collideable objects
 * every frame
 * @author matt
 */
public class CollisionDebugState extends BaseAppState{
    private final Geometry mapCollisions = new Geometry("Map_Collision_Debug");
    private MapState mapState;

    @Override
    protected void initialize(Application aplctn) {
        mapState = getState(MapState.class);
        //create empty mesh, add material
        Mesh m = new Mesh();
        m.setMode(Mesh.Mode.Lines);
        mapCollisions.setMesh(m);
        Material colMat = new Material(aplctn.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mapCollisions.setMaterial(colMat);
    }

    @Override
    protected void cleanup(Application aplctn) {
        
    }

    @Override
    protected void onEnable() {
        ((GarbageShopApp)getApplication()).getRootNode().attachChild(mapCollisions);
        getApplication().enqueue(() -> updateMap());
    }

    @Override
    protected void onDisable() {
        mapCollisions.removeFromParent();
    }

    @Override
    public void update(float tpf) {
        
    }
    
    private void updateMap(){
        List<Float> vertList = new ArrayList<>();
        List<Integer> indiceList = new ArrayList<>();
        List<GridObject> objects = mapState.getMap().getGridObjects();
        for(GridObject obj : objects){
            if(obj.isCollideable()){
                //get points of the collideable object and add them to the mesh
                processGridObject(obj, vertList, indiceList);
            }
        }
        //create mesh buffers
        Mesh m = mapCollisions.getMesh();
        float[] verts = new float[vertList.size()];
        int x=0;
        for(Float f : vertList){
            verts[x] = f;
            x++;
        }
        m.setBuffer(VertexBuffer.Type.Position, 3, verts);
        int[] indices = new int[indiceList.size()];
        x=0;
        for(Integer i : indiceList){
            indices[x] = i;
            x++;
        }
        m.setBuffer(VertexBuffer.Type.Index, 2, BufferUtils.createIntBuffer(indices));
        mapCollisions.updateModelBound();
    }
    
    private void processGridObject(GridObject obj, List<Float> vertList, List<Integer> indiceList){
        System.out.println(vertList.size());
        Coordinate min = obj.getPosition();
        Coordinate max = min.add(obj.getSize());
        int i = vertList.size()/3;
        float[] verts = new float[]{
            min.x, min.y, 0,//0
            min.x, max.y, 0,//1
            max.x, min.y, 0,//2
            max.x, max.y,0//3
        };
        for(float f : verts){
            vertList.add(f);
        }
        int[] indices = new int[]{
            i, i+1, i+0, i+2, i+1, i+3, i+2, i+3
        };
        for(int x : indices){
            indiceList.add(x);
        }
    }
}
