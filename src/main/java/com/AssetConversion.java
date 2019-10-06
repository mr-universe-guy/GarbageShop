package com;


import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.export.JmeExporter;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.material.Material;
import com.jme3.scene.Spatial;
import com.jme3.system.JmeContext;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author matt
 */
public class AssetConversion extends SimpleApplication{
    private final JFileChooser browser = new JFileChooser();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AssetConversion app = new AssetConversion();
        app.setShowSettings(false);
        app.start(JmeContext.Type.Headless, false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        //use this space to load a gltf, add materials n such then write it to a j3o
        Spatial spat = assetManager.loadModel("models/Road.gltf");
        TextureKey tk = new TextureKey("textures/RoadDiffuse.png");
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture(tk));
        spat.setMaterial(mat);
        JmeExporter ex = BinaryExporter.getInstance();
        browser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        if(browser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
            try {
                ex.save(spat, browser.getSelectedFile());
            } catch (IOException x) {
                Logger.getLogger(AssetConversion.class.getName()).log(Level.SEVERE, null, x);
            }
        }
        this.stop();
    }
    
}
