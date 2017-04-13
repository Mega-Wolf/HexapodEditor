/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import mygame.Hexagon;

/**
 *
 * @author Tobias
 */
public class Stick {

    /* final Variables */
    final double MASS = 10;
    final double MASS_SPHERE = 5;
    final double DENSITY = 7;
    final double LENGTH = 2;
    public static final double FPS = 120;
    
    final double staticFriction = 0.35f;
    final double dynamicFriction = 0.2f;
    
    /* Holder Variables */
    public Node stickMiddle = new Node();
    Quaternion rotation = new Quaternion();
    Geometry sphereBottom;
    Geometry sphereTop;
    Geometry stick;
    
    /* Math Variables */
    double rotA = 0;
    double rotV = 0;
    double rotS = 40;
    
    double transA = 0;
    double transV = 0;
    double transS = 0;
    
    double midX = 0;
    double midY = 0;
    
    double footX;
    
    double floorY;
    
    
    SimpleApplication app;
    /*
    public Stick(SimpleApplication app) {
        this.app = app;
        
        Sphere sphereG = new Sphere(16,16,0.2f);
        Material matSphere = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        matSphere.setColor("Color", ColorRGBA.Blue);
        
        Material matStick = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        matStick.setColor("Color", ColorRGBA.Red);
        
        stick = Hexagon.create3DHexagon(0.15f, LENGTH);
        stick.setMaterial(matStick);
        
        sphereTop = new Geometry("Sphere", sphereG);
        sphereTop.setMaterial(matSphere);
        
        sphereBottom = new Geometry("Sphere", sphereG);
        sphereBottom.setMaterial(matSphere);
        
        sphereBottom.setLocalTranslation(0, - LENGTH / 2f, 0);
        sphereTop.setLocalTranslation(0, + LENGTH / 2f, 0);
        
        stickMiddle.attachChild(sphereBottom);
        stickMiddle.attachChild(sphereTop);
        
        stickMiddle.attachChild(stick);
        
        stickMiddle.setLocalRotation(new Quaternion(new double[] {0, 0, rotS / 180f * FastMath.PI}));
        
        footX = rotS * LENGTH / 2;
        
        Quad floor = new Quad(20,20);
        Geometry floorG = new Geometry("floor",floor);
        Material matFloor = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        matFloor.setColor("Color", ColorRGBA.White);
        floorG.setMaterial(matFloor);
        
        floorG.setLocalRotation(new Quaternion(new double[] {-0.5f * FastMath.PI,0,0}));
        floorY = FastMath.cos(rotS/180f*FastMath.PI) * ( -LENGTH / 2f)  - 0.2f;
        floorG.setLocalTranslation(-10, floorY , 10);
        
        footX = FastMath.sin(rotS/180f*FastMath.PI);
        System.out.println(footX);
        
        app.getRootNode().attachChild(floorG);
        
    }
    
    public void tick() {
        //take all foot points; in this example just one
        //try to calculate, so every foot point gets Force
        //add momentum to the midpoint if necessary
        //rotate around according axis
        //restore foot points
        //add translation if necessary
        
        
        double torqueMiddle = FastMath.sin(rotS/180*FastMath.PI) * DENSITY * (MASS + 2 * MASS_SPHERE) * 9.81f;        
        double torqueFoot = FastMath.sin(rotS/180*FastMath.PI) * (LENGTH / 2f) *  DENSITY * MASS * 9.81f + FastMath.sin(rotS/180*FastMath.PI) * LENGTH *  DENSITY * MASS_SPHERE * 9.81f;
        rotS = rotS + rotV * 1 / FPS + rotA * 1 / FPS * 1 / FPS;
        rotV = rotV + rotA * 1 / FPS;
        rotA = torqueMiddle / ((LENGTH / 2f) * (LENGTH / 2f) * DENSITY * MASS_SPHERE * 2 ); // + MIDDLE ??? );
        
        stickMiddle.setLocalRotation(new Quaternion(new double[] {0,0,rotS / 180f * FastMath.PI}));
        
        double offsetX = - LENGTH / 2f * FastMath.sin(rotS / 180f * FastMath.PI);
        double offsetY =  LENGTH / 2f * FastMath.cos(rotS / 180f * FastMath.PI);
        
        stickMiddle.setLocalTranslation(footX + offsetX, floorY + offsetY + 0.2f, 0);
        
        
        
        
        System.out.println(torqueMiddle);
        System.out.println(torqueFoot);
        System.out.println();
        //System.exit(0);
    }
*/

}
