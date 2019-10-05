/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scene;

import com.GarbageShopApp;
import com.entity.DataState;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntitySet;
import com.unit.MobComponent;
import com.unit.PositionComponent;
import java.util.ArrayList;
import java.util.List;

/**
 * Draws the collision area of all map objects and all moving collideable objects
 * every frame
 * @author matt
 */
public class CollisionDebugState extends BaseAppState{
    private final Geometry mapCollisions = new Geometry("Map_Collision_Debug");
    private final Geometry mobCollisions = new Geometry("Mob_Collision_Debug");
    private MapState mapState;
    private EntityData ed;
    private EntitySet mobs;

    @Override
    protected void initialize(Application aplctn) {
        mapState = getState(MapState.class);
        ed = getState(DataState.class).getEd();
        Material colMat = new Material(aplctn.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        //create empty mesh, add material
        Mesh m = new Mesh();
        m.setMode(Mesh.Mode.Lines);
        mapCollisions.setMesh(m);
        mapCollisions.setMaterial(colMat);
        //mob
        Mesh mobMesh = new Mesh();
        mobMesh.setMode(Mesh.Mode.Lines);
        mobCollisions.setMesh(mobMesh);
        mobCollisions.setMaterial(colMat);
    }

    @Override
    protected void cleanup(Application aplctn) {
        
    }

    @Override
    protected void onEnable() {
        mobs = ed.getEntities(MobComponent.class, PositionComponent.class);
        Node rootNode = ((GarbageShopApp)getApplication()).getRootNode();
        rootNode.attachChild(mapCollisions);
        rootNode.attachChild(mobCollisions);
        getApplication().enqueue(() -> {
            updateMap();
            updateMobs();
        });
    }

    @Override
    protected void onDisable() {
        mapCollisions.removeFromParent();
        mobCollisions.removeFromParent();
        mobs.release();
    }

    @Override
    public void update(float tpf) {
        if(mobs.applyChanges()){
            updateMobs();
        }
    }
    
    private void updateMobs(){
        List<Vector3f> vertList = new ArrayList<>();
        List<Integer> indiceList = new ArrayList<>();
        for(Entity e : mobs){
            Vector3f pos = Vectors.fromVector2f(e.get(PositionComponent.class).getPosition());
            float radius = e.get(MobComponent.class).getRadius();
            drawCircle(pos, radius, vertList, indiceList);
        }
        Mesh m = mobCollisions.getMesh();
        m.setBuffer(VertexBuffer.Type.Position, 3,
                BufferUtils.createFloatBuffer(vertList.toArray(new Vector3f[vertList.size()])));
        int[] indices = new int[indiceList.size()];
        int x=0;
        for(Integer i : indiceList){
            indices[x] = i;
            x++;
        }
        m.setBuffer(VertexBuffer.Type.Index, 2, indices);
        mobCollisions.updateModelBound();
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
    
    private void drawCircle(Vector3f pos, float radius, List<Vector3f> vertList, List<Integer> indiceList){
        //TODO: determine resolution from radius, for now we'll just use 16 verts
        Vector2f circlePoint = new Vector2f(radius, 0);//this is the basis of our circle
        int res = 16;
        int c = vertList.size();
        for(int x=0; x<res; x++){
            float percent = (float)x/(float)res;
            Vector3f px = Vectors.fromVector2f(Vectors.rotateVector(circlePoint, FastMath.TWO_PI*percent));
            vertList.add(pos.add(px));
        }
        indiceList.add(c+res-1);
        indiceList.add(c);
        for(int i=1; i<res; i++){
            indiceList.add(c+i-1);
            indiceList.add(c+i);
        }
    }
    
    private void processGridObject(GridObject obj, List<Float> vertList, List<Integer> indiceList){
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
