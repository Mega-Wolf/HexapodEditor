package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import fitnesses.FHideExtended;
import fitnesses.IFitness;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    boolean finished;
    GeneticRobot finishedGR;
    
    Population population;

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
         
        Main app = new Main();
        app.start();
    }

    Robot robot;

    @Override
    public void simpleInitApp() {
        
        Thread thread = new Thread(() -> {
            //IFitness fitness = new FFarthestMove();
            //IFitness fitness = new FHide();
            IFitness fitness = new FHideExtended();
            //IFitness fitness = new FLateral();
            //IFitness fitness = new FHigh();
            AWalker luca = new TripodLoopRobot();

            population = new Population(luca, fitness);
            population.testGA();
         });
         
        thread.start();
        
        rootNode.scale(0.2f);
        
        
        Quad q = new Quad(100, 100);
        Geometry groundGeometry = new Geometry("", q);

        Material matSphere = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matSphere.setColor("Color", ColorRGBA.Green);

        groundGeometry.setMaterial(matSphere);

        rootNode.attachChild(groundGeometry);
        groundGeometry.setLocalRotation(new Quaternion(new float[]{-FastMath.PI / 2, 0, 0}));
        groundGeometry.setLocalTranslation(-50, - 0.2f, 50);

        robot = new Robot((SimpleApplication) this);
        rootNode.attachChild(robot.robotNode);
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

    @Override
    public void simpleUpdate(float tpf) {

        /*
        //sum += tpf / 5.;
        sum += tpf ;
        robot.setRotation(g, ((int) sum * 1f / 30f) % 1. );
         */
        
        if (sum == 0) {
            
            
            walker = population.getBest();
            if (walker == null) {
                return;
            }
            
            
            robot.robotNode.rotateUpTo(new Vector3f(0, 1, 0));
            
            System.out.println("Took this: " + walker);
            
            robot.robotNode.rotate(0, (float) -walker.getRotation()[1], 0);
            robot.robotNode.rotate((float) walker.getRotation()[0], 0, 0);
            robot.robotNode.rotate(0, (float) walker.getRotation()[1], 0);
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
        robot.robotNode.setLocalTranslation(sum * (float) walker.getDirection()[0], (float) walker.getHeight(), sum * (float) walker.getDirection()[1]);
        
        sum += tpf /* / 5. */;
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
