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
import com.unit.Item;
import com.unit.ItemComponent;
import com.unit.Items;
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
    private final Geometry itemCollisions = new Geometry("Item_Collision_Debug");
    private SceneState mapState;
    private EntityData ed;
    private EntitySet mobs, items;

    @Override
    protected void initialize(Application aplctn) {
        mapState = getState(SceneState.class);
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
        //item
        Mesh itemMesh = new Mesh();
        itemMesh.setMode(Mesh.Mode.Lines);
        itemCollisions.setMesh(itemMesh);
        itemCollisions.setMaterial(colMat);
    }

    @Override
    protected void cleanup(Application aplctn) {
        
    }

    @Override
    protected void onEnable() {
        mobs = ed.getEntities(MobComponent.class, PositionComponent.class);
        items = ed.getEntities(ItemComponent.class, PositionComponent.class);
        Node rootNode = ((GarbageShopApp)getApplication()).getRootNode();
        rootNode.attachChild(mapCollisions);
        rootNode.attachChild(mobCollisions);
        rootNode.attachChild(itemCollisions);
        getApplication().enqueue(() -> {
            updateMap();
            updateMobs();
            updateItems();
        });
    }

    @Override
    protected void onDisable() {
        mapCollisions.removeFromParent();
        mobCollisions.removeFromParent();
        itemCollisions.removeFromParent();
        mobs.release();
        items.release();
    }

    @Override
    public void update(float tpf) {
        if(mobs.applyChanges()){
            updateMobs();
        }
        if(items.applyChanges()){
            updateItems();
        }
    }
    
    private void updateItems(){
        List<Vector3f> vertList = new ArrayList<>();
        List<Integer> indiceList = new ArrayList<>();
        for(Entity e : items){
            PositionComponent posC = e.get(PositionComponent.class);
            Vector2f pos = posC.getPosition();
            float rot = posC.getRotation();
            Item item = Items.ITEMMAP.get(e.get(ItemComponent.class).getItemKey());
            drawItem(pos, rot, item.getWidth(), item.getHeight(), vertList, indiceList);
        }
        Mesh m = itemCollisions.getMesh();
        m.setBuffer(VertexBuffer.Type.Position, 3,
                BufferUtils.createFloatBuffer(vertList.toArray(new Vector3f[vertList.size()])));
        int[] indices = new int[indiceList.size()];
        int x=0;
        for(Integer i : indiceList){
            indices[x] = i;
            x++;
        }
        m.setBuffer(VertexBuffer.Type.Index, 2, indices);
        itemCollisions.updateModelBound();
    }
    
    private void updateMobs(){
        List<Vector3f> vertList = new ArrayList<>();
        List<Integer> indiceList = new ArrayList<>();
        for(Entity e : mobs){
            Vector3f pos = Vectors.vec2ToVec3(e.get(PositionComponent.class).getPosition());
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
    
    private void drawItem(Vector2f pos, float rot, int sizeX, int sizeY,
            List<Vector3f> vertList, List<Integer> indiceList){
        Vector2f v0 = new Vector2f((-sizeX/2f)*0.1f, (-sizeY/2f)*0.1f);
        Vector2f v3 = new Vector2f((sizeX/2f)*0.1f, (sizeY/2f)*0.1f);
        Vector2f v1 = new Vector2f(v0.x, v3.y);
        Vector2f v2 = new Vector2f(v3.x, v0.y);
        v0.rotateAroundOrigin(rot, false);
        v1.rotateAroundOrigin(rot, false);
        v2.rotateAroundOrigin(rot, false);
        v3.rotateAroundOrigin(rot, false);
        int i=vertList.size();
        vertList.add(Vectors.vec2ToVec3(pos.add(v0)));//0
        vertList.add(Vectors.vec2ToVec3(pos.add(v1)));//1
        vertList.add(Vectors.vec2ToVec3(pos.add(v2)));//2
        vertList.add(Vectors.vec2ToVec3(pos.add(v3)));//3
        int[] indices = new int[]{
            i+0, i+1,
            i+0, i+2,
            i+1, i+3,
            i+2, i+3
        };
        for(int indice : indices){
            indiceList.add(indice);
        }
    }
    
    private void drawCircle(Vector3f pos, float radius, List<Vector3f> vertList, List<Integer> indiceList){
        //TODO: determine resolution from radius, for now we'll just use 16 verts
        Vector2f circlePoint = new Vector2f(radius, 0);//this is the basis of our circle
        int res = 16;
        int c = vertList.size();
        for(int x=0; x<res; x++){
            float percent = (float)x/(float)res;
            Vector3f px = Vectors.vec2ToVec3(Vectors.rotateVector(circlePoint, FastMath.TWO_PI*percent));
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
