// [[21.21875, 310.6875, 34.25, -10.0, -10.0, 10.0], [20.03125, 338.40625, 335.90625, -10.0, 10.0, -8.03125], [20.28125, 339.8125, 317.5, 10.0, 10.0, -2.46875]]
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author Tobias
 */
public strictfp class Robot {

    SimpleApplication app;

    //Params
    // Vector3f position = new Vector3f(0,0,0);
    // Quaternion rotation = new Quaternion();
    //Nodes
    Node robotNode;
    Node[] nodeSolid = new Node[6];
    Node[] nodeHorizontal = new Node[6];
    Node[] nodeTop = new Node[6];
    Node[] nodeBottom = new Node[6];

    //Geometries
    Geometry body;

    Geometry[] horizontalHex = new Geometry[6];
    Geometry[] upHex = new Geometry[6];
    Geometry[] downHex = new Geometry[6];

    Geometry[] jointSolid = new Geometry[6];
    Geometry[] jointHorizontal = new Geometry[6];
    Geometry[] jointTop = new Geometry[6];
    Geometry[] jointBottom = new Geometry[6];

    Geometry sphereSpecial;
    
    //angles
    float[] horizontalAngles = {0, 0, 0, 0, 0, 0};
    float[] upAngles = {-60 / 180f * FastMath.PI, -60/ 180f * FastMath.PI, -60/ 180f * FastMath.PI, -60/ 180f * FastMath.PI, -60/ 180f * FastMath.PI, -60/ 180f * FastMath.PI};
    float[] downAngles = {120/ 180f * FastMath.PI, 120/ 180f * FastMath.PI, 120/ 180f * FastMath.PI, 120/ 180f * FastMath.PI, 120/ 180f * FastMath.PI, 120/ 180f * FastMath.PI};
    
    //touching stuff
    public Robot(SimpleApplication app) {
        this.app = app;

        Node rn = app.getRootNode();

        Material matSphere = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        matSphere.setColor("Color", ColorRGBA.Blue);

        Material matHex = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        matHex.setColor("Color", ColorRGBA.Red);

        robotNode = new Node();
        rn.attachChild(robotNode);

        body = Hexagon.create3DHexagon(1, 0.3f);
        robotNode.attachChild(body);

        Sphere sphereG = new Sphere(16, 16, 0.2f);
        Geometry sphere = new Geometry("Sphere", sphereG);

        for (int i = 0; i < 6; i++) {
            nodeSolid[i] = new Node();
            robotNode.attachChild(nodeSolid[i]);
            jointSolid[i] = sphere.clone();
            nodeSolid[i].attachChild(jointSolid[i]);

            nodeSolid[i].rotate(0, i * 60 / 180f * FastMath.PI, 0);
            jointSolid[i].setLocalTranslation(0, 0, 1);
        }
        
        robotNode.detachChild(nodeSolid[3]);
        robotNode.detachChild(nodeSolid[4]);
        robotNode.detachChild(nodeSolid[5]);
        
        
        Geometry gHorHex = Hexagon.create3DHexagon(0.15f, 0.5f);

        for (int i = 0; i < 6; i++) {
            nodeHorizontal[i] = new Node();
            nodeHorizontal[i].setLocalTranslation(0, 0, 1);
            nodeSolid[i].attachChild(nodeHorizontal[i]);

            horizontalHex[i] = gHorHex.clone();
            horizontalHex[i].rotate(0.5f * FastMath.PI, 0, 0);
            horizontalHex[i].setLocalTranslation(0, 0, 0.25f);
            nodeHorizontal[i].attachChild(horizontalHex[i]);

            jointHorizontal[i] = sphere.clone();
            nodeHorizontal[i].attachChild(jointHorizontal[i]);
            jointHorizontal[i].setLocalTranslation(0, 0, 0.5f);

        }

        Geometry gUpHex = Hexagon.create3DHexagon(0.15f, 1.5f);

        for (int i = 0; i < 6; i++) {
            nodeTop[i] = new Node();
            nodeTop[i].setLocalTranslation(0, 0, 0.5f);
            nodeHorizontal[i].attachChild(nodeTop[i]);

            upHex[i] = gUpHex.clone();
            upHex[i].rotate(0.5f * FastMath.PI, 0, 0);
            upHex[i].setLocalTranslation(0, 0, 0.75f);
            nodeTop[i].attachChild(upHex[i]);

            jointTop[i] = sphere.clone();
            nodeTop[i].attachChild(jointTop[i]);
            jointTop[i].setLocalTranslation(0, 0, 1.5f);
        }

        Geometry gDownHex = Hexagon.create3DHexagon(0.15f, 3f);

        for (int i = 0; i < 6; i++) {
            nodeBottom[i] = new Node();
            nodeBottom[i].setLocalTranslation(0, 0, 1.5f);
            nodeTop[i].attachChild(nodeBottom[i]);

            downHex[i] = gDownHex.clone();
            downHex[i].rotate(0.5f * FastMath.PI, 0, 0);
            downHex[i].setLocalTranslation(0, 0, 1.5f);
            nodeBottom[i].attachChild(downHex[i]);

            jointBottom[i] = sphere.clone();
            nodeBottom[i].attachChild(jointBottom[i]);
            jointBottom[i].setLocalTranslation(0, 0, 3f);
        }

        body.setMaterial(matHex);

        for (int i = 0; i < 6; i++) {
            jointSolid[i].setMaterial(matSphere);
            jointHorizontal[i].setMaterial(matSphere);
            jointTop[i].setMaterial(matSphere);
            jointBottom[i].setMaterial(matSphere);

            horizontalHex[i].setMaterial(matHex);
            upHex[i].setMaterial(matHex);
            downHex[i].setMaterial(matHex);
        }
        
        Material matSpecial = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        matSpecial.setColor("Color", ColorRGBA.Orange);
        
        
        
        //jointBottom[0].removeFromParent();
        
        sphereSpecial = sphere.clone(false);
        sphereSpecial.setMaterial(matSpecial);
        
        nodeSolid[2].attachChild(sphereSpecial);
        
        updateRotations();

        //center();
    }

    public void updateRotations() {
        for (int i = 0; i < DNA.LEGS; i++) {
            
            // [0, 360)
            //nodeHorizontal[i].setLocalRotation(new Quaternion(new float[]{0, horizontalAngles[i] / 180f * FastMath.PI, 0}));
            //nodeTop[i].setLocalRotation(new Quaternion(new float[]{upAngles[i] / 180f * FastMath.PI, 0, 0}));
            //nodeBottom[i].setLocalRotation(new Quaternion(new float[]{downAngles[i] / 180f * FastMath.PI, 0, 0}));
            
            // [0, 2 * PI)
            nodeHorizontal[i].setLocalRotation(new Quaternion(new float[]{0, horizontalAngles[i], 0}));
            nodeTop[i].setLocalRotation(new Quaternion(new float[]{upAngles[i], 0, 0}));
            nodeBottom[i].setLocalRotation(new Quaternion(new float[]{downAngles[i], 0, 0}));
        }
    }

    //Geometry centerGeometry;
    public Vector3f getCenter() {
        /*
        if (centerGeometry == null) {
            Sphere sphereG = new Sphere(16,16,0.21f);
            centerGeometry = new Geometry("Sphere", sphereG);
        
            Material matSphere = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            matSphere.setColor("Color", ColorRGBA.Green);
        
            centerGeometry.setMaterial(matSphere);
            
            app.getRootNode().attachChild(centerGeometry);
        }
         */

        Vector3f center = body.getWorldTranslation().mult(3 / 2f * FastMath.sqrt(3) * 1 * 1 * 0.3f);

        for (int i = 0; i < 6; i++) {
            center.addLocal(horizontalHex[i].getWorldTranslation().mult(3 / 2f * FastMath.sqrt(3) * 0.15f * 0.15f * 0.5f));
            center.addLocal(upHex[i].getWorldTranslation().mult(3 / 2f * FastMath.sqrt(3) * 0.15f * 0.15f * 1.5f));
            center.addLocal(downHex[i].getWorldTranslation().mult(3 / 2f * FastMath.sqrt(3) * 0.15f * 0.15f * 3.5f));

            center.addLocal(jointSolid[i].getWorldTranslation().mult(4 / 3f * FastMath.PI * 0.2f * 0.2f * 0.2f));
            center.addLocal(jointHorizontal[i].getWorldTranslation().mult(4 / 3f * FastMath.PI * 0.2f * 0.2f * 0.2f));
            center.addLocal(jointTop[i].getWorldTranslation().mult(4 / 3f * FastMath.PI * 0.2f * 0.2f * 0.2f));
            center.addLocal(jointBottom[i].getWorldTranslation().mult(4 / 3f * FastMath.PI * 0.2f * 0.2f * 0.2f));
        }

        center.divideLocal(3 / 2f * FastMath.sqrt(3) * 1 * 1 * 0.3f + 6 * 3 / 2f * FastMath.sqrt(3) * 0.15f * 0.15f * 0.5f + 6 * 3 / 2f * FastMath.sqrt(3) * 0.15f * 0.15f * 1.5f + 6 * 3 / 2f * FastMath.sqrt(3) * 0.15f * 0.15f * 3.5f + 24 * 4 / 3f * FastMath.PI * 0.2f * 0.2f * 0.2f);
        return center;
        //centerGeometry.setLocalTranslation(center);
    }

    int naturalMutation;

    public void setRotation(GeneticIK g, double t) {
        
        //sphereSpecial.setLocalTranslation((float)GeneticIK.posAim.x + FastMath.sin(2/3f * FastMath.PI), (float)GeneticIK.posAim.y, (float)GeneticIK.posAim.z + FastMath.cos(2/3f * FastMath.PI));
        sphereSpecial.setLocalTranslation((float)GeneticIK.posAimDebug.x, (float)GeneticIK.posAimDebug.y, (float)GeneticIK.posAimDebug.z + 1);
        
        
        //TODO
        g.setRotation(t);
        
        for (int c = 0; c < DNA.LEGS; c++) {
            horizontalAngles[c] = (float) g.rotHorizontal[c];
            upAngles[c] = (float) g.rotTop[c];
            downAngles[c] = (float) g.rotBottom[c];
            
            
            
            /*
            horizontalAngles[c] = (float) (gr.chromosomes[c][A_HORIZONTAL] + gr.chromosomes[c][B_HORIZONTAL] * Math.sin(t * (2 * Math.PI) + gr.chromosomes[c][GeneticRobot.PHI_HORIZONTAL]));
            upAngles[c] = (float) (gr.chromosomes[c][A_TOP] + gr.chromosomes[c][B_TOP] * Math.sin(t * (2 * Math.PI) + gr.chromosomes[c][GeneticRobot.PHI_TOP]));
            downAngles[c] = (float) (gr.chromosomes[c][A_BOTTOM] + gr.chromosomes[c][B_BOTTOM] * Math.sin(t * (2 * Math.PI) + gr.chromosomes[c][GeneticRobot.PHI_BOTTOM]));

            horizontalAngles[LEGS - 1 - c] = (float) (-gr.chromosomes[c][A_HORIZONTAL] + gr.chromosomes[c][B_HORIZONTAL] * Math.sin(t * (2 * Math.PI) + gr.chromosomes[c][GeneticRobot.PHI_HORIZONTAL]));
            upAngles[LEGS - 1 - c] = (float) (gr.chromosomes[c][A_TOP] - gr.chromosomes[c][B_TOP] * Math.sin(t * (2 * Math.PI) + gr.chromosomes[c][GeneticRobot.PHI_TOP]));
            downAngles[LEGS - 1 - c] = (float) (gr.chromosomes[c][A_BOTTOM] - gr.chromosomes[c][B_BOTTOM] * Math.sin(t * (2 * Math.PI) + gr.chromosomes[c][GeneticRobot.PHI_BOTTOM]));
            */
        }
        updateRotations();
    }
}
