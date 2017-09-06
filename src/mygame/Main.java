package mygame;

import com.jme3.app.DebugKeysAppState;
import robots.AWalker;
import visuals.Robot;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import fitnesses.FRotate;
import fitnesses.IFitness;
import javafx.application.Application;
import robots.BetterRobot;
import thread.Output;

public class Main extends SimpleApplication {

    boolean finished;

    public Main() {
        super( new StatsAppState(), new DebugKeysAppState() );
    }
    
    public static void main(String[] args) {
        
        //    System.out.println(Math.sin(45/180f*Math.PI) * Math.sin(45/180f*Math.PI));
        //    System.exit(0);
        
        {
        /*
        long start = System.currentTimeMillis();
        float f = 0;
        for (int j = 0; j < 512; j++) {
            for (int i = 0; i < 10000; i++) {
            f += FastMath.sin(i / 30f);
            f += FastMath.cos(i / 30f);
            f += FastMath.tan(i / 30f);
        }
        }
        System.out.println(f);
        System.out.println(System.currentTimeMillis() - start);
        System.exit(0);
         */
 /*  
        List<float> list = new ArrayList<float>(512);
        for (int j = 0; j < 512; j++) {
            list.add((float)Math.random());
        }
        long start = System.currentTimeMillis();
        list.sort(null);    
        System.out.println(System.currentTimeMillis() - start);
        System.out.println(list.get(0));
        System.out.println(list.get(1));
        System.out.println(list.get(2));
        System.out.println(list.get(3));
        System.out.println(list.get(4));
        System.exit(0);
        
         */
 /*
        float aStart = 0;
        float vStart = -1;
        float sStart = 0;
        
        float aa = 1;
        float a;
        float oldA = aStart;
        float v;
        float oldV= vStart;
        float s;
        float oldS = sStart;
        
        float delta = 30;
        
        for (int i = 1; i <= delta; i++) {
            
            a = oldA + Math.sin(i / delta) - Math.sin((i - 1) / delta);
            v = oldV + oldA * 1 / delta + (-1) * (Math.cos(i / delta) - Math.cos((i - 1) / delta));
            s = oldS + /*oldV * 1 / delta +*//* 1 / 2. * oldA * 1 / delta * 1 / delta + (-1) * (Math.sin(i / delta) - Math.sin((i - 1) / delta));
            
            
            //a = oldA + aa * 1 / delta;
            //v = oldV + oldA * 1 / delta + 1 / 2. * aa * 1 / delta * 1 / delta;
            //s = oldS + oldV * 1 / delta + 1 / 2. * oldA * 1 / delta * 1 / delta + 1 / 6. * aa * 1 / delta * 1 / delta * 1 / delta;
            
            //a = oldA + aa * 1 / delta;
            //v = oldV + oldA * 1 / delta;
            //s = oldS + oldV * 1 / delta + 1 / 2. * oldA * 1 / delta * 1 / delta;
            
            oldV = v;
            oldS = s;
            oldA = a;
            
            System.out.println("t: " + i + " / " + delta);
            System.out.println("s: " + s);
            System.out.println("Should: " + (sStart /*+ vStart * i / delta*/ /* + 1 / 2. * aStart * i/ delta * i/ delta + (-1) * Math.sin(aa * i / delta)));
            System.out.println("v: " + v);
            System.out.println("Should: " + (/*vStart +*/ /* aStart * i/ delta + (-1) * Math.cos(aa * i / delta)));
            System.out.println("a: " + a);
            System.out.println("Should: " + (aStart + Math.sin(aa * i / delta)));
            System.out.println();
            
            /*
            System.out.println("t: " + i + " / " + delta);
            System.out.println("s: " + s);
            System.out.println("Should: " + (sStart + vStart * i / delta + 1 / 2. * aStart * i/ delta * i/ delta + 1 / 6. * aa * i / delta * i / delta * i / delta));
            System.out.println("v: " + v);
            System.out.println("Should: " + (vStart + aStart * i/ delta + 1 / 2. * aa * i / delta * i / delta));
            System.out.println("a: " + a);
            System.out.println("Should: " + (aStart + aa * i / delta));
            System.out.println();
         */ /*
        }
        
        
        System.exit(0);
            */
        }
        
        
        /*
        
        double[][] chroms = {
            {0, 3},
            {0, 3},
            {0, 3},
            {0, 3},
            {0, 3},
            {0, 3},
            {1},
            {0, 0},
            {0, 3},
        };
        
       
        t = new TripodLoopRobot(chroms);
        
        
        Vector3d[][] jointSolid = new Vector3d[30][6];
        Vector3d[][] jointHorizontal = new Vector3d[30][6];
        Vector3d[][] jointTop = new Vector3d[30][6];
        Vector3d[][] jointBottom = new Vector3d[30][6];
        
        Vector3d[] jointWeapon = new Vector3d[30];
        
        double[][] rotHorizontal = new double[30][6];
        double[][] rotTop = new double[30][6];
	double[][] rotBottom = new double[30][6];
        
        double initialHeight = 1;
        double endHeight = 1;
        double initialRotation = 0;
        double endRotation = 0;
	int frames = 30;
        
        for (int i = 0; i < 30; i++) {
            t.setRotation(i / 30.0);
            
            jointSolid[i] = t.posSolid;
            jointHorizontal[i] = t.posHorizontal;
            jointTop[i] = t.posTop;
            jointBottom[i] = t.posBottom;
            
            //jointWeapon = t.
            
            
            
        }
        
        */
        
        
         
        Main app = new Main();
        app.start();
    }

    Robot robot;
    
    BitmapText fitnessText;

    @Override
    public void simpleInitApp() {
        
        setPauseOnLostFocus(false);
        inputManager.setCursorVisible(true);
        
        
        new Thread(() -> {
            //Application.launch(Output.class, new String[0]);
            Application.launch(Output.class, new String[0]);
        }).start();
        
        
        viewPort.setBackgroundColor(new ColorRGBA(0, 0.6f, 0.9f , 1));
        
        //setDisplayStatView(false); setDisplayFps(false);
        
        fitnessText = new BitmapText(guiFont, false);
        fitnessText.setQueueBucket(Bucket.Gui);
        
        fitnessText.setSize(guiFont.getCharSet().getRenderedSize());
        fitnessText.setText("Fitness: ");
        fitnessText.setLocalTranslation(300, fitnessText.getLineHeight(), 0);
        fitnessText.setColor(new ColorRGBA(0.5f,0,0,1));
        
        guiNode.attachChild(fitnessText);
        
        rootNode.scale(0.2f);
        
        Quad q = new Quad(100, 100);
        Geometry groundGeometry = new Geometry("", q);

        Material matSphere = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matSphere.setColor("Color", new ColorRGBA(0.1f,0.7f,0.2f,1f));

        groundGeometry.setMaterial(matSphere);

        rootNode.attachChild(groundGeometry);
        groundGeometry.setLocalRotation(new Quaternion(new float[]{-FastMath.PI / 2, 0, 0}));
        groundGeometry.setLocalTranslation(-50, - 0.2f, 50);

        robot = new Robot((SimpleApplication) this);
        
        robotOuterNode.attachChild(robot.getRobotNode());
        
        rootNode.attachChild(robotOuterNode);
        //testGA();

        //double x = Math.PI / 6;
        //double y = Math.PI / 6;

        /*
        walker = new TripodLoopRobot(new double[][] {
            {1.6029845816266275, 3.8782624074324796},
            {-1.3177250565539262, 1.9310578477958016},
            {0.10909108724059657, -0.17897369039648914},
            {1.6117042552357883, 3.8678671527501587},
            {-2.5209010830038876, 2.628952956790129},
            {0.15843137767931514, -0.1334882752926867},
            {2.017518427871427},
            {-0.011466739897852962, 1.1312599238230865},
            {1.0962633061456208, 8.742270477740323}
        });
        */
        
        /*
        g = new TripodLoopRobot(new double[][] {
            {0,3},
            {0,3},
            {0,3},
            {0,3},
            {0,3},
            {0,3},
            {1},
            {x,y},
            {0,3}
        });
         */
        //robot.setRotation(walker, 0);
        //g.calcFitness(0);
        //System.out.println(walker);

        // System.exit(0);
        //robot.robotNode.rotateUpTo(new Vector3f((float) (Math.sin(-y) * Math.sin(x)) , (float) Math.cos(x), (float) (Math.cos(-y) * Math.sin(x)) ));     
        //robot.robotNode.rotate((float) (Math.sin(-y) * Math.sin(x)) , (float) Math.cos(x), (float) (Math.cos(-y) * Math.sin(x)) ); 
        //robot.robotNode.rotate((float) x, (float) -y,0);
        

//        g.calcFitness();
        //      System.out.println("Fitenss: " + g.getFitness());
    }

    float sum = 0;
    AWalker walker;
    
    Node robotOuterNode = new Node();

    @Override
    public void simpleUpdate(float tpf) {

        /*
        //sum += tpf / 5.;
        sum += tpf ;
        robot.setRotation(g, ((int) sum * 1f / 30f) % 1. );
         */
        
        if (Output.getInsatnce() == null || Output.getInsatnce().getPopulation() == null) {
            return;
        }
        
        if (sum == 0) {
            
            walker = Output.getInsatnce().getPopulation().getLast();                    

            int number = Output.getInsatnce().getPopulation().getBestRobots().size() - 1;
            
            //walker = t;
            if (walker == null) {
                return;
            }
            
            Output.getInsatnce().choose(number);
            
            /*
            fitnessText.setText("Fitness: " + walker.getFitness() +
                    "          Generation: " + (number) + 
                    "          Movement total: " + Math.sqrt(walker.getDirection()[0] * walker.getDirection()[0] + walker.getDirection()[1] * walker.getDirection()[1]) + 
                    "          (Movement ahead: " + walker.getDirection()[1] + 
                    "          Movement lateral: " + walker.getDirection()[0] + ")");
            */
            
            robot.getRobotNode().rotateUpTo(new Vector3f(0, 1, 0));
            
            System.out.println("Took this: " + walker);
            
            robot.getRobotNode().rotate(0, (float) -walker.getStartRotation()[1], 0);
            robot.getRobotNode().rotate((float) walker.getStartRotation()[0], 0, 0);
            robot.getRobotNode().rotate(0, (float) walker.getStartRotation()[1], 0);
        }
        
        /*
        for (int i = 0; i < 30; i++) {
            robot.setRotation(walker, i / 30f);
        }
        System.exit(0);
        */
        
        //robot.setRotation(walker, ((int) (10 * sum) * 1f / 30f) % 1.);
        //robot.robotNode.setLocalTranslation(((int) (10 * sum) * 1f / 30f) % 1f * (float) walker.getDirection()[0], (float) walker.getHeight(), ((int) (10 * sum) * 1f / 30f) % 1f * (float) walker.getDirection()[1]);
        
        robot.setRotation(walker, sum % 1.);
        robotOuterNode.setLocalTranslation(sum * (float) walker.getDirection()[0], (float) walker.getStartHeight(), sum * (float) walker.getDirection()[1]);
        robotOuterNode.setLocalRotation(new Quaternion(new float[] {0, sum * (float) walker.getRotationAngle(), 0}));
        
        
        sum += tpf  / 2.  ;
        if (sum > 2) {
        //if (sum > 20) {
            sum = 0;
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

}
