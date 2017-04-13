package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import java.util.Arrays;
import java.util.Random;
import test.Stick;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public strictfp class Main extends SimpleApplication {

    boolean finished;
    GeneticRobot finishedGR;
    
    public static void main(String[] args) {

        //    System.out.println(Math.sin(45/180f*Math.PI) * Math.sin(45/180f*Math.PI));
        //    System.exit(0);
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

        Main app = new Main();

        app.start();
    }

    Robot robot;
    Stick stick;

    @Override
    public void simpleInitApp() {
        /*
        Geometry geom = Hexagon.create3DHexagon(1, 0.3f);

        robot = new Robot((SimpleApplication) this);
        rootNode.attachChild(robot.robotNode);

        // stick = new Stick((SimpleApplication) this);
        //  rootNode.attachChild(stick.stickMiddle);
        robot.testGA();
        
        */
        
        Quad q = new Quad(100, 100);
        Geometry ggg = new Geometry("", q);
        
        Material matSphere = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matSphere.setColor("Color", ColorRGBA.Green);
        
        ggg.setMaterial(matSphere);
        
        rootNode.attachChild(ggg);
        ggg.setLocalRotation(new Quaternion(new float[]{-FastMath.PI / 2,0,0}));
        ggg.setLocalTranslation(-50, -1 - 0.2f, 50);
        
        robot = new Robot((SimpleApplication) this);
        rootNode.attachChild(robot.robotNode);
        //testGA();
        //GeneticIK.ABSTAND = 1.5;
        g = new GeneticIK(new double[][] {{0,-1,3},{0,-1,3},{0,-1,3}});
        
        g.setStartRotation();
        robot.setRotation(g, 0);
        
    }

    float sum = 0;
    GeneticIK g;
    
    @Override
    public void simpleUpdate(float tpf) {
        
        sum += tpf / 5.;
        GeneticIK.ABSTAND = sum;
        g.setStartRotation();
        robot.setRotation(g,0);
        //robot.robotNode.setLocalTranslation(sum, -sum / 2, -sum / 3);
        robot.robotNode.setLocalTranslation(0, 0,sum);
        
        //robot.robotNode.move(tpf / 5f, 0, 0);
        
        
        
        /*if (finished) {
            sum += tpf;
            //robot.setRotation(finishedGR, sum);
        }*/

        /*
        sum += tpf;
        
        if (sum > 1/Stick.FPS) {
            sum -= 1/Stick.FPS;
        //    robot.downAngles[4] -= 1;
        //    robot.updateRotations();
        stick.tick();
        }
         */
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    public void testGA() {

        int POPULATION_SIZE = 128;

        int found = 0;
        int loop = 0;
        double lowestFail = -9999999999f;

        /*
        int index = 0;
        int[] lookUpPop = new int[(POPULATION_SIZE / 2) * (POPULATION_SIZE + 1)];
        for (int i = 0; i < POPULATION_SIZE; i++) {
            for (int j = 0; j < (POPULATION_SIZE - i); j++) {
                lookUpPop[index] = i;
                index++;
            }
        }
         */
        boolean running = true;

        GeneticRobot population[] = new GeneticRobot[POPULATION_SIZE];
        GeneticRobot populationDummy[] = new GeneticRobot[POPULATION_SIZE];
        for (int i = 0; i < POPULATION_SIZE; i++) {
            populationDummy[i] = new GeneticRobot();
        }

        while (running) {
            //GeneticRobot.TOLERANCE = Math.max(0.5 - loop / 10000.,0);
            
            loop++;

            System.arraycopy(populationDummy, 0, population, 0, POPULATION_SIZE);

            for (int i = 0; i < POPULATION_SIZE; i++) {
                population[i].calcFitness();
            }

            shuffleArray(population);
            Arrays.sort(population, (e1, e2) -> Double.compare(e2.fitness, e1.fitness));

            for (int i = 0; i < POPULATION_SIZE; i++) {
                int i1 = 0;
                int i2 = 0;

                while (Math.random() < 0.5) {
                    i1++;
                    if (i1 == POPULATION_SIZE - 1) {
                        break;
                    }
                }

                while (Math.random() < 0.5) {
                    i2++;
                    if (i2 == POPULATION_SIZE - 1) {
                        break;
                    }
                }
                //int i1 = lookUpPop[(int) (Math.random() * 513*256)];
                //int i2 = lookUpPop[(int) (Math.random() * 513*256)];
                populationDummy[i] = new GeneticRobot(population[i1], population[i2]);
            }

            if (population[0].fitness > lowestFail) {
                lowestFail = population[0].fitness;
            }

            if (loop % 100 == 0) {
                System.out.println(lowestFail + "," + population[0].fitness + "," + population[POPULATION_SIZE - 1].fitness);
            }

            if (loop > 10_000) {
                System.out.println("Finished, round: " + loop);
                System.out.println("Loop: " + loop + ", Found: " + found + ", best Fail: " + lowestFail);
                System.out.println(population[0]);
                //System.exit(0);

                finished = true;
                finishedGR = population[0];

                break;

            }

            if (loop % 1000 == 0) {
                System.out.println("Loop: " + loop + ", Found: " + found + ", best Fail: " + lowestFail);
                System.out.println(population[0]);
            }
        }
    }
    
    private static void shuffleArray(GeneticRobot[] array) {
        int index;
        GeneticRobot temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
}
