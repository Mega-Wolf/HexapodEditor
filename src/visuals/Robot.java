package visuals;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import robots.AWalker;

/**
 * 3D representation of the robot in the scene
 * @author Tobias
 */
public class Robot {

    private final SimpleApplication app;

    //Nodes
    private final Node robotNode;
    private final Node[] nodeSolid = new Node[6];
    private final Node[] nodeHorizontal = new Node[6];
    private final Node[] nodeTop = new Node[6];
    private final Node[] nodeBottom = new Node[6];

    //Geometries
    private final Geometry body;

    private final Geometry[] horizontalHex = new Geometry[6];
    private final Geometry[] upHex = new Geometry[6];
    private final Geometry[] downHex = new Geometry[6];

    private final Geometry[] jointSolid = new Geometry[6];
    private final Geometry[] jointHorizontal = new Geometry[6];
    private final Geometry[] jointTop = new Geometry[6];
    private final Geometry[] jointBottom = new Geometry[6];

    private final Geometry sphereWeapon;
    
    //angles
    private final float[] horizontalAngles = {0, 0, 0, 0, 0, 0};
    private final float[] upAngles = {-60 / 180f * FastMath.PI, -60/ 180f * FastMath.PI, -60/ 180f * FastMath.PI, -60/ 180f * FastMath.PI, -60/ 180f * FastMath.PI, -60/ 180f * FastMath.PI};
    private final float[] downAngles = {120/ 180f * FastMath.PI, 120/ 180f * FastMath.PI, 120/ 180f * FastMath.PI, 120/ 180f * FastMath.PI, 120/ 180f * FastMath.PI, 120/ 180f * FastMath.PI};
    
    /* Constructor */
    
    /**
     * Creates a 3d model of the robot
     * @param app 
     */
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

        body.rotate(0, 30 / 180f * FastMath.PI,0);
        
        for (int i = 0; i < AWalker.LEGS; i++) {
            nodeSolid[i] = new Node();
            robotNode.attachChild(nodeSolid[i]);
            
            jointSolid[i] = sphere.clone();
            nodeSolid[i].attachChild(jointSolid[i]);

            nodeSolid[i].rotate(0, (i * 60 + 30) / 180f * FastMath.PI, 0);
            jointSolid[i].setLocalTranslation(0, 0, 1);
        }    
        
        Geometry gHorHex = Hexagon.create3DHexagon(0.15f, 0.5f);

        for (int i = 0; i < AWalker.LEGS; i++) {
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

        for (int i = 0; i < AWalker.LEGS; i++) {
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

        for (int i = 0; i < AWalker.LEGS; i++) {
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

        for (int i = 0; i < AWalker.LEGS; i++) {
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
        
        sphereWeapon = sphere.clone(false);
        sphereWeapon.setMaterial(matSpecial);
        
        robotNode.attachChild(sphereWeapon);
        
        sphereWeapon.setLocalTranslation(0, 0, 1);  
        updateRotations();

        //center();
    }

    /**
     * Sets the rotations of the robot according to the internally saved rotation values
     */
    private void updateRotations() {
        for (int i = 0; i < AWalker.LEGS; i++) {        
            // [0, 2 * PI)
            nodeHorizontal[i].setLocalRotation(new Quaternion(new float[]{0, horizontalAngles[i], 0}));
            nodeTop[i].setLocalRotation(new Quaternion(new float[]{upAngles[i], 0, 0}));
            nodeBottom[i].setLocalRotation(new Quaternion(new float[]{downAngles[i], 0, 0}));
        }    
        
        //System.out.println(":");
        //System.out.println(jointBottom[0].getWorldTransform());
    }

    /**
     * Calculates the center of mass of the robot
     * @return The center of mass of the robot
     */
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

        //Hence this function is never used; the values here are quiet old and should be updated to consts instead of hardcoded values
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
    
    /* Getter */

    /**
     * Returns a node which contains the robot
     * @return The node of the robot
     */
    public Node getRobotNode(){
        return robotNode;
    }

    /* Setter */
    
    /**
     * Sets the rotations according to the associated AWalker
     * @param walker The associated robot
     * @param t The time in the cycle (0 <= t < 1)
     */
    public void setRotation(AWalker walker, double t) {
        walker.setRotation(t);
        for (int c = 0; c < AWalker.LEGS; c++) {
            horizontalAngles[c] = (float) walker.getRotHorizontal()[c];
            upAngles[c] = (float) walker.getRotTop()[c];
            downAngles[c] = (float) walker.getRotBottom()[c];
        }
        updateRotations();
    }
}
