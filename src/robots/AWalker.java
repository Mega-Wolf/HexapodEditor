package robots;

import math.Vector3d;
import math.Transform;
import math.Quaternion;
import fitnesses.IFitness;
import java.util.Arrays;

/**
 * A loopable robot which can be used in a GA
 *
 * @author Tobias
 */
public abstract class AWalker {

    /* Consts */
    //GA stuff
    public static /*final*/ double MUTATION_RATE = 0.15;

    public static final int LEGS = 6;

    //robot measurements
    public static final double A = 1;
    public static final double L1 = 0.5;
    public static final double L2 = 1.5;
    public static final double L3 = 3;

    public static final double SPHERE_RADIUS = 0.2;
    public static final double CHECK_SQUARED = (2 * 0.2) * (2 * 0.2);

    // allowed angles
    public static final double HORIZONTAL_MAX = 60 / 180. * Math.PI;
    public static final double HORIZONTAL_MIN = -60 / 180. * Math.PI;

    public static final double TOP_MAX = 0 / 180. * Math.PI;
    public static final double TOP_MIN = -135 / 180. * Math.PI;

    public static final double BOTTOM_MAX = 165 / 180. * Math.PI;
    public static final double BOTTOM_MIN = 0 / 180. * Math.PI;

    /* Variables */
    // positions of the joints
    protected Vector3d posSolid[] = new Vector3d[LEGS];
    protected Vector3d posHorizontal[] = new Vector3d[LEGS];
    protected Vector3d posTop[] = new Vector3d[LEGS];
    protected Vector3d posBottom[] = new Vector3d[LEGS];

    // angles of the joints
    protected double rotHorizontal[] = new double[LEGS];
    protected double rotTop[] = new double[LEGS];
    protected double rotBottom[] = new double[LEGS];

    // fitness value is used by the genetic algorithm
    private double fitness = 0;

    // extra fitenss value is used for a frame based separated fitness valuein IFitness
    private double extraFitness = 0;

    //two dimensional array, so the genes are located in chromosomes
    protected double[][] chromosomes;
    protected boolean[][] chromosomeMutations;

    /* Constructors */
    /**
     * Creates a new robot (at the start of the simulation)
     */
    public AWalker() {
        setupChromosomes();
    }

    /**
     * Creates a new robot (at the start of the simulation)
     * @param mutate whether the robot is a random one
     */
    public AWalker(boolean mutate) {
        this();

        if (mutate) {
            for (int c = 0; c < chromosomes.length; c++) {
                for (int i = 0; i < chromosomes[c].length; i++) {
                    if (chromosomeMutations[c][i]) {
                        mutateCritical(c, i);
                    }
                }
            }
        }
    }

    /**
     * Creates a new robot out of two parents (they should be of the same type)
     *
     * @param parent0 a parent
     * @param parent1 other parent
     */
    public AWalker(AWalker parent0, AWalker parent1) {
        setupChromosomes();
        chromosomeMutations = parent0.chromosomeMutations;
        for (int c = 0; c < chromosomes.length; c++) {
            System.arraycopy(Math.random() < 0.5 ? parent0.chromosomes[c] : parent1.chromosomes[c], 0, chromosomes[c], 0, parent0.chromosomes[c].length);
        }
        mutate();
    }

    /* Methods */
    /**
     * Mutates the genes of the robot
     */
    protected void mutate() {
        for (int c = 0; c < chromosomes.length; c++) {
            for (int i = 0; i < chromosomes[c].length; i++) {
                if (chromosomeMutations[c][i]) {
                    if (Math.random() < MUTATION_RATE) {
                        if (Math.random() < MUTATION_RATE) {
                            mutateCritical(c, i);
                        } else {
                            mutateNormal(c, i);
                        }
                    }
                }
            }
        }
    }

    public double initialFitnessChecks(IFitness fitnessFunction) {
        for (int i = 0; i < 30; i++) {
            setRotation(i / 30.0);
            calcPositions();
            correctAngles();
            noIntersection();
            checkUgly(i);

            //System.out.println(posBottom[5]);
            
            if (fitness == 0) {
                fitnessFunction.calcFitnessValueEveryFrame(this);
            }
        }
        return fitness;
    }

    /**
     * Calculates the positions of the joints
     */
    public void calcPositions() {
        //TODO; mega ugly und uneffektiv; aber immerhin funktioniert es endlich

        //könnte man eig einmal berechnen
        Transform tRobot = new Transform();
        double[] rots = getStartRotation();
        tRobot.getRotation().multLocal(new Quaternion(new double[]{0, -rots[1], 0}));
        tRobot.getRotation().multLocal(new Quaternion(new double[]{rots[0], 0, 0}));
        tRobot.getRotation().multLocal(new Quaternion(new double[]{0, rots[1], 0}));

        for (int leg = 0; leg < LEGS; leg++) {
            Transform nodeSolid = new Transform();
            nodeSolid.getRotation().multLocal(new Quaternion(new double[]{0, (60 * leg + 30) / 180.0 * Math.PI, 0}));
            nodeSolid.combineWithParent(tRobot);

            Transform jointSolid = new Transform();
            jointSolid.setTranslation(0, 0, 1);
            jointSolid.combineWithParent(nodeSolid);
            posSolid[leg] = jointSolid.getTranslation();

            Transform nodeHorizontal = new Transform();
            nodeHorizontal.setTranslation(0, 0, 1);
            nodeHorizontal.getRotation().multLocal(new Quaternion(new double[]{0, rotHorizontal[leg], 0}));
            nodeHorizontal.combineWithParent(nodeSolid);

            Transform jointHorizontal = new Transform();
            jointHorizontal.setTranslation(0, 0, 0.5);
            jointHorizontal.combineWithParent(nodeHorizontal);
            posHorizontal[leg] = jointHorizontal.getTranslation();

            Transform nodeTop = new Transform();
            nodeTop.setTranslation(0, 0, 0.5);
            nodeTop.getRotation().multLocal(new Quaternion(new double[]{rotTop[leg], 0, 0}));
            nodeTop.combineWithParent(nodeHorizontal);

            Transform jointTop = new Transform();
            jointTop.setTranslation(0, 0, 1.5);
            jointTop.combineWithParent(nodeTop);
            posTop[leg] = jointTop.getTranslation();

            Transform nodeBottom = new Transform();
            nodeBottom.setTranslation(0, 0, 1.5);
            nodeBottom.getRotation().multLocal(new Quaternion(new double[]{rotBottom[leg], 0, 0}));
            nodeBottom.combineWithParent(nodeTop);

            Transform jointBottom = new Transform();
            jointBottom.setTranslation(0, 0, 3);
            jointBottom.combineWithParent(nodeBottom);
            posBottom[leg] = jointBottom.getTranslation();
        }
    }

    {
        /*
    private void calcPositionsOld() {
        
        for (int leg = 0; leg < 1; leg++) {
            double angle = (leg * 60 + 30) / 180. * Math.PI;
            Quaternion rotPoint = new Quaternion(new double[] {0,angle,0});
            
            Quaternion rotS = new Quaternion();
            
            rotS.multLocal(new Quaternion(new double[] {0, -chromosomes[C_ROTATION][1], 0}));
            rotS.multLocal(new Quaternion(new double[] {chromosomes[C_ROTATION][0], 0, 0}));
            rotS.multLocal(new Quaternion(new double[] {0, chromosomes[C_ROTATION][1], 0}));
            
            //posSolid[leg] = rotS.mult(rotPoint.mult(new Vector3d(0,0,A)));
            
            posSolid[leg] = rotS.mult(rotPoint).mult(new Vector3d(0,0,A));
            
            System.out.println("posSolid: " + posSolid[leg]);
            
            //System.out.println(rotHorizontal[leg]);
            
            //Quaternion rotH = rotS.mult(new Quaternion(new double[]  {0, rotHorizontal[leg], 0})).mult(rotS.inverse());
            
            Quaternion rotH = new Quaternion(new double[]  {0, rotHorizontal[leg], 0});
            Quaternion rotT = new Quaternion(new double[]  {rotTop[leg], 0, 0});
            
            //Quaternion rotT = rotH.mult(new Quaternion(new double[]  {rotTop[leg], 0, 0})).mult(rotH.inverse());
            //Quaternion rotT = new Quaternion(1,0,0,Math.PI);
            
            //Vector3d dirHorizontal = rotS.mult(rotH.mult(rotS.inverse().mult(posSolid[leg].mult(L1 / A))));
            //Vector3d dirHorizontal = rotH.mult(posSolid[leg].mult(L1 / A));
            
            Vector3d dirHorizontal = rotS.mult(rotH.mult(rotS.inverse().mult(posSolid[leg].mult(L1 / A))));
            
            System.out.println(rotPoint.mult(rotS.mult(rotH.mult(new Vector3d(0,0,L1)))));
            System.out.println(rotPoint.mult(rotH.mult(rotS.mult(new Vector3d(0,0,L1)))));
            System.out.println(rotS.mult(rotPoint.mult(rotH.mult(new Vector3d(0,0,L1)))));
            System.out.println(rotS.mult(rotH.mult(rotPoint.mult(new Vector3d(0,0,L1)))));
            System.out.println(rotH.mult(rotS.mult(rotPoint.mult(new Vector3d(0,0,L1)))));
            System.out.println(rotH.mult(rotPoint.mult(rotS.mult(new Vector3d(0,0,L1)))));
            
            //Vector3d dirHorizontal = rotH.mult(rotS.mult(rotPoint)).mult(new Vector3d(0,0,L1));
            
            
            //Vector3d dirHorizontal = rotS.mult(rotPoint.mult(rotH.mult(new Vector3d(0,0,L1))));
            
            System.out.println("dirHorizontal: " + dirHorizontal);
            
            posHorizontal[leg] = posSolid[leg].add(dirHorizontal);
            System.out.println("posHorizontal: " + posHorizontal[leg]);
            
            //Vector3d dirTop = rotT.mult();
            
            //Vector3d dirTop = rotT.mult(dirHorizontal.mult(L2 / L1));
            //Vector3d dirTop = rotS.mult(rotH.mult(rotT.mult(rotS.inverse().mult(rotH.inverse().mult(dirHorizontal.mult(L2 / L1))))));77
           
            Vector3d dirTop = rotS.mult(rotPoint.mult(rotH.mult(rotT.mult(new Vector3d(0,0,L2)))));
            
            
            System.out.println(rotS.mult(rotPoint.mult(rotH.mult(rotT.mult(new Vector3d(0,0,L2))))));
            System.out.println(rotS.mult(rotPoint.mult(rotT.mult(rotH.mult(new Vector3d(0,0,L2))))));
            System.out.println(rotS.mult(rotH.mult(rotPoint.mult(rotT.mult(new Vector3d(0,0,L2))))));
            System.out.println(rotS.mult(rotH.mult(rotT.mult(rotPoint.mult(new Vector3d(0,0,L2))))));
            System.out.println(rotS.mult(rotT.mult(rotPoint.mult(rotH.mult(new Vector3d(0,0,L2))))));
            System.out.println(rotS.mult(rotT.mult(rotH.mult(rotPoint.mult(new Vector3d(0,0,L2))))));
            
            System.out.println(rotT.mult(rotPoint.mult(rotH.mult(rotS.mult(new Vector3d(0,0,L2))))));
            System.out.println(rotT.mult(rotPoint.mult(rotS.mult(rotH.mult(new Vector3d(0,0,L2))))));
            System.out.println(rotT.mult(rotH.mult(rotPoint.mult(rotS.mult(new Vector3d(0,0,L2))))));
            System.out.println(rotT.mult(rotH.mult(rotS.mult(rotPoint.mult(new Vector3d(0,0,L2))))));
            System.out.println(rotT.mult(rotS.mult(rotPoint.mult(rotH.mult(new Vector3d(0,0,L2))))));
            System.out.println(rotT.mult(rotS.mult(rotH.mult(rotPoint.mult(new Vector3d(0,0,L2))))));
            
            System.out.println(rotH.mult(rotPoint.mult(rotS.mult(rotT.mult(new Vector3d(0,0,L2))))));
            System.out.println(rotH.mult(rotPoint.mult(rotT.mult(rotS.mult(new Vector3d(0,0,L2))))));
            System.out.println(rotH.mult(rotS.mult(rotPoint.mult(rotT.mult(new Vector3d(0,0,L2))))));
            System.out.println(rotH.mult(rotS.mult(rotT.mult(rotPoint.mult(new Vector3d(0,0,L2))))));
            System.out.println(rotH.mult(rotT.mult(rotPoint.mult(rotS.mult(new Vector3d(0,0,L2))))));
            System.out.println(rotH.mult(rotT.mult(rotS.mult(rotPoint.mult(new Vector3d(0,0,L2))))));
            
            System.out.println(rotPoint.mult(rotS.mult(rotH.mult(rotT.mult(new Vector3d(0,0,L2))))));
            System.out.println(rotPoint.mult(rotS.mult(rotT.mult(rotH.mult(new Vector3d(0,0,L2))))));
            System.out.println(rotPoint.mult(rotH.mult(rotS.mult(rotT.mult(new Vector3d(0,0,L2))))));
            System.out.println(rotPoint.mult(rotH.mult(rotT.mult(rotS.mult(new Vector3d(0,0,L2))))));
            System.out.println(rotPoint.mult(rotT.mult(rotS.mult(rotH.mult(new Vector3d(0,0,L2))))));
            System.out.println(rotPoint.mult(rotT.mult(rotH.mult(rotS.mult(new Vector3d(0,0,L2))))));

            
            
            //Vector3d dirTop = rotH.inverse().mult(rotT.mult(rotH.mult(dirHorizontal.mult(L2 / L1))));
            
            System.out.println(dirTop);
            /*
            //System.out.println(rotS.mult(rotH.mult(rotS.inverse().mult(rotT.mult(rotS.mult(rotH.inverse().mult(rotS.inverse().mult(dirHorizontal))))))));
            //System.out.println(rotH.mult(rotS.mult(rotH.inverse().mult(rotT.mult(rotS.mult(rotH.inverse().mult(rotS.inverse().mult(dirHorizontal))))))));
            
            
            //System.out.println(rotS.mult(rotH.mult(rotS.inverse().mult(rotT.mult(rotH.inverse().mult(rotS.inverse().mult(dirHorizontal)))))));
            //System.out.println(rotH.mult(rotS.mult(rotH.inverse().mult(rotT.mult(rotH.inverse().mult(rotS.inverse().mult(dirHorizontal)))))));
            //System.out.println(rotS.mult(rotH.mult(rotS.inverse().mult(rotT.mult(rotH.inverse().mult(rotS.inverse().mult(dirHorizontal)))))));
            //System.out.println(rotH.mult(rotS.mult(rotH.inverse().mult(rotT.mult(rotH.inverse().mult(rotS.inverse().mult(dirHorizontal)))))));
            
            System.out.println(rotS.mult(rotH.mult(rotT.mult(rotS.inverse().mult(rotH.inverse().mult(dirHorizontal.mult(L2 / L1)))))));
            System.out.println(rotH.mult(rotS.mult(rotT.mult(rotS.inverse().mult(rotH.inverse().mult(dirHorizontal.mult(L2 / L1)))))));
            System.out.println(rotS.mult(rotH.mult(rotT.mult(rotH.inverse().mult(rotS.inverse().mult(dirHorizontal.mult(L2 / L1)))))));
            System.out.println(rotH.mult(rotS.mult(rotT.mult(rotH.inverse().mult(rotS.inverse().mult(dirHorizontal.mult(L2 / L1)))))));
           
            System.out.println(rotS.inverse().mult(rotH.mult(rotT.mult(rotS.mult(rotH.inverse().mult(dirHorizontal.mult(L2 / L1)))))));
            System.out.println(rotH.inverse().mult(rotS.mult(rotT.mult(rotS.mult(rotH.inverse().mult(dirHorizontal.mult(L2 / L1)))))));
            System.out.println(rotS.inverse().mult(rotH.mult(rotT.mult(rotH.mult(rotS.inverse().mult(dirHorizontal.mult(L2 / L1)))))));
            System.out.println(rotH.inverse().mult(rotS.mult(rotT.mult(rotH.mult(rotS.inverse().mult(dirHorizontal.mult(L2 / L1)))))));
            
            System.out.println(rotS.inverse().mult(rotH.inverse().mult(rotT.mult(rotS.mult(rotH.mult(dirHorizontal.mult(L2 / L1)))))));
            System.out.println(rotH.inverse().mult(rotS.inverse().mult(rotT.mult(rotS.mult(rotH.mult(dirHorizontal.mult(L2 / L1)))))));
            System.out.println(rotS.inverse().mult(rotH.inverse().mult(rotT.mult(rotH.mult(rotS.mult(dirHorizontal.mult(L2 / L1)))))));
            System.out.println(rotH.inverse().mult(rotS.inverse().mult(rotT.mult(rotH.mult(rotS.mult(dirHorizontal.mult(L2 / L1)))))));
            
            System.out.println(rotS.inverse().mult(rotH.mult(rotT.mult(rotS.inverse().mult(rotH.mult(dirHorizontal.mult(L2 / L1)))))));
            System.out.println(rotH.inverse().mult(rotS.mult(rotT.mult(rotS.inverse().mult(rotH.mult(dirHorizontal.mult(L2 / L1)))))));
            System.out.println(rotS.inverse().mult(rotH.mult(rotT.mult(rotH.inverse().mult(rotS.mult(dirHorizontal.mult(L2 / L1)))))));
            System.out.println(rotH.inverse().mult(rotS.mult(rotT.mult(rotH.inverse().mult(rotS.mult(dirHorizontal.mult(L2 / L1)))))));
            
            System.out.println(rotS.mult(rotH.inverse().mult(rotT.mult(rotS.inverse().mult(rotH.mult(dirHorizontal.mult(L2 / L1)))))));
            System.out.println(rotH.mult(rotS.inverse().mult(rotT.mult(rotS.inverse().mult(rotH.mult(dirHorizontal.mult(L2 / L1)))))));
            System.out.println(rotS.mult(rotH.inverse().mult(rotT.mult(rotH.inverse().mult(rotS.mult(dirHorizontal.mult(L2 / L1)))))));
            System.out.println(rotH.mult(rotS.inverse().mult(rotT.mult(rotH.inverse().mult(rotS.mult(dirHorizontal.mult(L2 / L1)))))));
            
            System.out.println(rotS.mult(rotH.inverse().mult(rotT.mult(rotS.mult(rotH.inverse().mult(dirHorizontal.mult(L2 / L1)))))));
            System.out.println(rotH.mult(rotS.inverse().mult(rotT.mult(rotS.mult(rotH.inverse().mult(dirHorizontal.mult(L2 / L1)))))));
            System.out.println(rotS.mult(rotH.inverse().mult(rotT.mult(rotH.mult(rotS.inverse().mult(dirHorizontal.mult(L2 / L1)))))));
            System.out.println(rotH.mult(rotS.inverse().mult(rotT.mult(rotH.mult(rotS.inverse().mult(dirHorizontal.mult(L2 / L1)))))));
            
            
//            Vector3d dirTop = (rotS.mult(rotH.mult(rotT))).mult(dirHorizontal.mult(1));
            
            //rotT.
            
            System.out.println("dirTop: " + dirTop);
            
            posTop[leg] = posHorizontal[leg].add(dirTop);
            System.out.println("posTop: " + posTop[leg]);   
            
            
            
            
            
            
        }
    }
         */
    }

    /**
     * Checks if all angles of the robot are in an allowed state
     */
    private void correctAngles() {
        //TODO; evtl. später mal mit genaueren Daten machen; jetzt gilt einfach: falscher Winkel = -1; tut aber schon ganz gut

        for (int leg = 0; leg < LEGS; leg++) {
            if (rotHorizontal[leg] < HORIZONTAL_MIN || rotHorizontal[leg] > HORIZONTAL_MAX) {
                //double d = rotHorizontal[leg] - (HORIZONTAL_MIN + HORIZONTAL_MAX / 2);

                //fitness -= d > d + 2;
                fitness--;
            }

            if (Double.isNaN(rotBottom[leg]) || rotBottom[leg] < BOTTOM_MIN || rotBottom[leg] > BOTTOM_MAX) {
                //fitness -= Math.abs(rotHorizontal[leg] - (HORIZONTAL_MIN + HORIZONTAL_MAX / 2));
                fitness--;
            }

            /*
            if (Double.isNaN(rotTop[leg]) || rotTop[leg] < TOP_MIN){
                fitness--;
            }
             */
            if (Double.isNaN(rotTop[leg]) || (rotTop[leg] >= TOP_MAX && rotTop[leg] <= 2 * Math.PI + TOP_MIN)) {
                fitness--;
            }

        }
    }

    /**
     * Checks, if the robt parts aren't in an unallowed state
     */
    private void noIntersection() {
        // WICHTIG: Später auch die Möglichkeit um auf Hindernisse zu testen

        //Pro Kollision: fitness -= 1
        for (int leg = 0; leg < LEGS; leg++) {

            //body - ground
            if (posSolid[leg].y <= -getStartHeight()) {
                fitness--;
            }

            //horizontal - ground
            if (posHorizontal[leg].y <= -getStartHeight()) {
                fitness--;
            }

            //bottom - bottom; der übernächste
            if (posBottom[leg].distanceSquared(posBottom[(leg + 2) % LEGS]) <= CHECK_SQUARED) {
                fitness--;
            }
            //bottom - bottom; gegenüber
            if (posBottom[leg].distanceSquared(posBottom[(leg + 3) % LEGS]) <= CHECK_SQUARED) {
                fitness--;
            }

            //L2 - L2
            if (intersectCapsules(posHorizontal[leg], posTop[leg], posHorizontal[(leg + 1) % 6], posTop[(leg + 1) % 6])) {
                fitness--;
            }

            //L2 - L3
            if (intersectCapsules(posHorizontal[leg], posTop[leg], posTop[(leg + 1) % 6], posBottom[(leg + 1) % 6])) {
                fitness--;
            }

            //L3 - L2
            if (intersectCapsules(posTop[leg], posBottom[leg], posHorizontal[(leg + 1) % 6], posTop[(leg + 1) % 6])) {
                fitness--;
            }

            //L3 - L3
            if (intersectCapsules(posTop[leg], posBottom[leg], posTop[(leg + 1) % 6], posBottom[(leg + 1) % 6])) {
                fitness--;
            }
        }
    }

    double[] lastRotation = new double[6];
    double[] firstRotation = new double[6];

    private void checkUgly(int frame) {
        switch (frame) {
            case 0:
                for (int i = 0; i < 6; i++) {
                    lastRotation[i] = rotHorizontal[i];
                    firstRotation[i] = rotHorizontal[i];
                }
                break;
            case 29:
                for (int i = 0; i < 6; i++) {
                    if (Math.abs(lastRotation[i] - rotHorizontal[i]) >= 0.1) {
                        fitness--;
                    }
                    if (Math.abs(firstRotation[i] - rotHorizontal[i]) >= 0.1) {
                        fitness--;
                    }
                }
                break;
            case 15:
                //disable chck for the middle, because here are sometimes errors
                break;
            default:
                for (int i = 0; i < 6; i++) {
                    if (Math.abs(lastRotation[i] - rotHorizontal[i]) >= 0.1) {
                        fitness--;
                    }
                    lastRotation[i] = rotHorizontal[i];
                }
                break;
        }
    }

    /**
     * Checks if two capsules collide; r is the sphere-radius
     *
     * @param start1 one joint of the first capsule
     * @param end1 other joint of the first capsule
     * @param start2 one joint of the second capsule
     * @param end2 other joint of the second capsule
     * @return
     */
    private static boolean intersectCapsules(Vector3d start1, Vector3d end1, Vector3d start2, Vector3d end2) {
        Vector3d direction1 = end1.subtract(start1);
        Vector3d direction2 = end2.subtract(start2);

        Vector3d sDiff = start2.subtract(start1);

        double s1r2 = direction2.dot(direction1);   // rs = - s1
        double r1 = -direction1.dot(direction1);
        double equals1 = -sDiff.dot(direction1);

        double s2 = direction2.dot(direction2);
        double equals2 = -sDiff.dot(direction2);

        double finalR;
        double finalS;

        if (s1r2 != 0) {
            finalR = (equals2 - (s2 / s1r2) * equals1) / (-s1r2 - (s2 / s1r2) * r1);
            finalS = finalR * (-r1 / s1r2) + equals1 / s1r2;
        } else {
            finalR = equals1 / r1;
            finalS = equals2 / s2;
        }

        finalR = clamp(0, finalR, 1);
        finalS = clamp(0, finalS, 1);

        return start1.add(direction1.mult(finalR)).distanceSquared(start2.add(direction2.mult(finalS))) <= CHECK_SQUARED;
    }

    /**
     * Clamps a value IMPORTANT: Don't forget to assign this value to the
     * variable
     *
     * @param min the minimum value
     * @param val the value to clamp
     * @param max the maximum value
     * @return
     */
    protected static double clamp(double min, double val, double max) {
        return Math.max(min, Math.min(max, val));
    }

    /* Abstract Methods */
    /**
     * Mutates the genes of the robot critically: sets a value anywhere in the
     * allowed range of values
     *
     * @param c the chromosome to mutate
     * @param i the gene on the chromosome
     */
    protected abstract void mutateCritical(int c, int i);

    /**
     * Mutates the genes of the robot normally: changes the value a bit and
     * clamps it to the allowed range
     *
     * @param c the chromosome to mutate
     * @param i the gene on the chromosome
     */
    protected abstract void mutateNormal(int c, int i);

    /**
     * Sets the feet according to the time
     *
     * @param t time of the cycle; 0 <= t < 1
     */
    public abstract void setRotation(double t);

    public abstract AWalker newInstance();
    
    public abstract AWalker newInstance(double[][] dna);
    
    public abstract AWalker newInstance(double[][] dna, boolean[][] mutate);

    public abstract AWalker newInstance(AWalker parent0, AWalker parent1);

    protected abstract void setupChromosomes();

    /* Setter */
    /**
     * Sets the fitness of this robot
     *
     * @param fitness the fitness of the robot
     */
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    /**
     * Sets the extraFitness of this robot; the extra fitness is a special
     * fitness value, which is used for calculateFitnessEveryFrame
     *
     * @param extraFitness the fitness of the robot
     */
    public void setExtraFitness(double extraFitness) {
        this.extraFitness = extraFitness;
    }

    /* Getter */
    
    /**
     * Gets the chromosomes of the robot to show them to a viewer
     * 
     * @return the chromosomes of the robot with their genes in them
     */
    public double[][] getDNA() {
        return chromosomes;
    }
    
    public boolean[][] getShallMutate() {
        return chromosomeMutations;
    }
    
    /**
     * Gets the horizontal rotation (rotation around body edges)
     *
     * @return the rotations (in radians) per leg
     */
    public double[] getRotHorizontal() {
        return rotHorizontal;
    }

    /**
     * Gets the up rotation (rotation around the second sphere)
     *
     * @return the rotations (in radians) per leg
     */
    public double[] getRotTop() {
        return rotTop;
    }

    /**
     * Gets the down rotation (rotation around third sphere)
     *
     * @return the rotations (in radians) per leg
     */
    public double[] getRotBottom() {
        return rotBottom;
    }

    /**
     * Gets the position of the solid joints
     *
     * @return the positions of the solid joints
     */
    public Vector3d[] getPosSolid() {
        return posSolid;
    }

    /**
     * Gets the position of the horizontal joints
     *
     * @return the positions of the horizontal joints
     */
    public Vector3d[] getPosHorizontal() {
        return posHorizontal;
    }

    /**
     * Gets the position of the top joints
     *
     * @return the positions of the top joints
     */
    public Vector3d[] getPosTop() {
        return posTop;
    }

    /**
     * Gets the position of the bottom joints
     *
     * @return the positions of the bottom joints
     */
    public Vector3d[] getPosBottom() {
        return posBottom;
    }

    /**
     * Gets the fitness value of this robot
     *
     * @return
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * Gets the extraFitness value of this robot
     *
     * @return
     */
    public double getExtraFitness() {
        return extraFitness;
    }

    /* Abstract Getter */
    /**
     * Gets the initial height of the center of the robot body Note: 0 !=
     * ground; 0 = height of the bottom sphere center (change that?); ground = -
     * sphereradius
     *
     * @return the height above the bottom sphere center height
     */
    public abstract double getStartHeight();

    /**
     * Gets the moving vector of the robot
     *
     * @return 0: x; 1: z
     */
    public abstract double[] getDirection();

    /**
     * Gets the initial rotation of the robot
     *
     * @return 0: x; 1: z
     */
    public abstract double[] getStartRotation();

    /**
     * Gets the angle around which a robot has turned during a walk
     *
     * @return the angle according to the current t
     */
    public abstract double getRotationAngle();

    
    /**
     * Gets the name of this robot type
     * 
     * @return the name of the robot type
     */
    public abstract String getName();
    
    
    /**
     * Gets the description of the robot type
     * 
     * @return the description of the robot type
     */
    public abstract String getDescription();
    
    /**
     * Gets Descriptions for all the genes
     * @return 
     */
    public abstract String[][] getDNAInfo();
    
    @Override
    public String toString() {
        return this.getClass() + ":" + Arrays.deepToString(chromosomes) + "\tFitness: " + fitness;
    }
}
