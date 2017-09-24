package mygame;

import com.jme3.app.DebugKeysAppState;
import robots.AWalker;
import visuals.Robot;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import javafx.application.Application;
import thread.Output;

public class Main extends SimpleApplication {

    /* Variables */
    
    private float sum = 0;
    private Robot robot;
    private AWalker walker;
    private final Node robotOuterNode = new Node();
    private final Node robotStartNode = new Node();

    private float nextReset = 0;
    
    /* Main */
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
    /* Constrcutor */
    
    public Main() {
        super(new StatsAppState(), new DebugKeysAppState());
    }
    
    /* Overrides */

    @Override
    public void simpleInitApp() {
        setPauseOnLostFocus(false);
        inputManager.setCursorVisible(true);

        viewPort.setBackgroundColor(new ColorRGBA(0, 0.6f, 0.9f, 1));
        
        setDisplayStatView(false);
        setDisplayFps(false);
        rootNode.scale(0.2f);
        
        cam.setLocation(new Vector3f(0,2,5));
        System.out.println(cam.getRotation());
        cam.setRotation(cam.getRotation().mult(new Quaternion(new float[] {0.1f,0,0})));
        
        
        Quad q = new Quad(100, 100);
        Geometry groundGeometry = new Geometry("", q);

        Material matGround = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matGround.setColor("Color", new ColorRGBA(0.1f, 0.7f, 0.2f, 1f));

        groundGeometry.setMaterial(matGround);

        rootNode.attachChild(groundGeometry);
        groundGeometry.setLocalRotation(new Quaternion(new float[]{-FastMath.PI / 2, 0, 0}));
        groundGeometry.setLocalTranslation(-50, -0.2f, 50);

        robot = new Robot((SimpleApplication) this);

        robotOuterNode.attachChild(robot.getRobotNode());
        
        robotStartNode.attachChild(robotOuterNode);
        
        rootNode.attachChild(robotStartNode);
        
        new Thread(() -> {
            Application.launch(Output.class, new String[0]);
        }).start();

    }

    @Override
    public void simpleUpdate(float tpf) {
        if (Output.getInstance() == null || Output.getInstance().getPopulation() == null) {
            return;
        }

        if (sum == 0) {
            walker = Output.getInstance().getPopulation().getLast();
            if (walker == null) {
                return;
            }
            
            nextReset = 1;
            robotStartNode.setLocalTransform(Transform.IDENTITY);

            int number = Output.getInstance().getPopulation().getBestRobots().size() - 1;
            Output.getInstance().choose(number);
            robot.getRobotNode().getLocalRotation().set(0, 0, 0, 1);

            System.out.println("Took this: " + walker);

            robot.getRobotNode().rotate(0, (float) -walker.getStartRotation()[1], 0);
            robot.getRobotNode().rotate((float) walker.getStartRotation()[0], 0, 0);
            robot.getRobotNode().rotate(0, (float) walker.getStartRotation()[1], 0);
        }

        robot.setRotation(walker, sum % 1.);
        
        /*
        if (sum > nextReset) {
            nextReset += 1;
            
            robotOuterNode.setLocalTranslation(1 * (float) walker.getDirection()[0], (float) walker.getStartHeight(), 1 * (float) walker.getDirection()[1]);
            robotOuterNode.setLocalRotation(new Quaternion(new float[]{0, 1 * (float) walker.getRotationAngle(), 0}));
            
            //robotStartNode.getLocalRotation().mult(new Quaternion(new float[]{0, 1 * (float) walker.getRotationAngle(), 0}));
        }
        */
        
        robotOuterNode.setLocalTranslation(sum * (float) walker.getDirection()[0], (float) walker.getStartHeight(), sum * (float) walker.getDirection()[1]);
        robotOuterNode.setLocalRotation(new Quaternion(new float[]{0, sum * (float) walker.getRotationAngle(), 0}));
        
        
        

        sum += tpf / 2.;
        if (sum > 2) {
            sum = 0;
        }
    }

}
