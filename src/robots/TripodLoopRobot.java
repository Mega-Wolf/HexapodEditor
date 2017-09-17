//Tripod :[[-1.432821159681283, 4.502035344895974], [-0.40891439891937764, -0.6651818060981322], [2.9375258559642132, 1.7361534800301865], [-1.2600145447558977, 4.392344095316288], [-1.576993487322087, 0.8825759780361142], [2.1821855758332025, 2.1825797407084107], [0.9442307092252811], [0.026254282811909353, 2.068521330609688], [6.685559340073908, 8.0]]	Fitness: 14.685559340073908
package robots;

import math.Vector3d;

/**
 * A robot which can loop animations with Inverse Kinematics, the end state must
 * be the beginning state
 * This kind of robot cannot rotate during movement
 *
 * @author Tobias
 */
public class TripodLoopRobot extends AWalker {

    /* Consts */
    //genes
    public static final int G_X = 0;
    public static final int G_Z = 1;

    //chromosomes
    //0 - 5: legs
    public static final int C_Y_OFFSET = 6;
    public static final int C_ROTATION = 7;
    public static final int C_TRANSLATION = 8;

    public static final int CHROMOSOMES = 9;

    /* Variables */
    private Vector3d posDiff;

    /* Constructor */
    
    /**
     * Creates a TipodLoopRobot
     *
     * @param mutate whether the robot is a random one or the default one
     */
    public TripodLoopRobot(boolean mutate) {
        super(mutate);
    }

    /**
     * Constructor for loading already existing walking gaits. Use this for
     * in-game usage
     *
     * @param chromosomes chromosomes 0..5: 2 doubles for x,z per leg chromosome
     * 6: 1 double for the y offset of the robot chromosome 7: 3 doubles for the
     * start rotation around the x,y,z axis chromosome 8: 2 doubles for x,z for
     * translation during walking
     */
    public TripodLoopRobot(double[][] chromosomes) {
        this.chromosomes = chromosomes;
    }
    
    public TripodLoopRobot(double[][] chromosomes, boolean[][] mutate) {
        this.chromosomes = chromosomes;
        this.chromosomeMutations = mutate;
    }

    /**
     * Constructor for genetical usage. Takes to parents and lets mutation and
     * crossover determine the child
     *
     * @param g0 Parent 1
     * @param g1 Parent 2
     */
    public TripodLoopRobot(AWalker g0, AWalker g1) {
        super(g0, g1);
    }

    /* Methods */
    @Override
    protected void mutateNormal(int c, int i) {
        if (c < LEGS) {
            chromosomes[c][i] += Math.random() - 0.5;
            chromosomes[c][i] = clamp(-8, chromosomes[c][i], 8);
        } else if (c == C_Y_OFFSET) {
            chromosomes[c][i] += Math.random() * 0.2 - 0.1;
            chromosomes[c][i] = clamp(0, chromosomes[c][i], 3);
        } else if (c == C_ROTATION) {
            if (i == 0) {
                chromosomes[c][i] += Math.random() * Math.PI / 4 - Math.PI / 8;
                chromosomes[c][i] = clamp(-Math.PI / 2, chromosomes[c][i], Math.PI / 2);
            } else {    // y Wert egal
                chromosomes[c][i] += Math.random() * Math.PI / 4 - Math.PI / 8;
                chromosomes[c][i] = (2 * Math.PI + chromosomes[c][i]) % (2 * Math.PI);
            }

        } else {
            chromosomes[c][i] += Math.random() - 0.5;
            chromosomes[c][i] = clamp(-12, chromosomes[c][i], 12);
        }
    }

    @Override
    protected void mutateCritical(int c, int i) {
        if (c < LEGS) {
            chromosomes[c][i] = Math.random() * 16 - 8;
        } else if (c == C_Y_OFFSET) {
            chromosomes[c][i] = Math.random() * 3;
        } else if (c == C_ROTATION) {
            if (i == 0) {
                chromosomes[c][i] = Math.random() * Math.PI - Math.PI / 2;
            } else {
                chromosomes[c][i] = Math.random() * Math.PI * 2;
            }
        } else {
            chromosomes[c][i] = Math.random() * 24 - 12;
        }
    }

    /**
     * Sets the translation for the given time
     *
     * @param t time
     */
    private void setDistance(double t) {
        posDiff = new Vector3d(chromosomes[C_TRANSLATION][G_X] * t, 0, -chromosomes[C_TRANSLATION][G_Z] * t);
    }

    private void inverseKinematics(int leg) {
        double tx = chromosomes[leg][G_X];
        double tz = chromosomes[leg][G_Z];

        double sinA = Math.sin((leg * 60 + 30) / 180. * Math.PI);
        double cosA = Math.cos((leg * 60 + 30) / 180. * Math.PI);

        double sinAY = Math.sin(((leg * 60 + 30) / 180. * Math.PI) + chromosomes[C_ROTATION][1]);
        double cosAY = Math.cos(((leg * 60 + 30) / 180. * Math.PI) + chromosomes[C_ROTATION][1]);

        double sinX = Math.sin(chromosomes[C_ROTATION][0]);
        double cosX = Math.cos(chromosomes[C_ROTATION][0]);

        double ty = -chromosomes[C_Y_OFFSET][0] + sinX * Math.cos(-((leg * 60 + 30) / 180. * Math.PI) - chromosomes[C_ROTATION][1]) * A;

        tx += cosA * posDiff.x + sinA * posDiff.z;
        tz += -sinA * posDiff.x + cosA * posDiff.z;

        //TODO; mal mit Quaternions rechnen; vllt sind die da genauer?
        //INfo; ist glaube ich eher ein Rechenproblem
        //alternativ: bei der Bestimmung von rotHorizontal schauen, ob x und z nah bei 0 sind, wenn ja, aus den angrenzenden Winkeln interpolieren
        Vector3d posAim = new Vector3d(
                -(tx * cosAY * cosAY + tx * cosX - tx * cosX * cosAY * cosAY - sinAY * sinX * ty - tz * cosAY * sinAY + tz * sinAY * cosX * cosAY),
                (sinAY * sinX * tx + cosX * ty + cosAY * sinX * tz),
                (-tx * cosAY * sinAY + tx * sinAY * cosX * cosAY - cosAY * sinX * ty + tz - tz * cosAY * cosAY + tz * cosAY * cosAY * cosX)
        );

        if (posAim.x < 0.000001 && posAim.x > -0.000001) {
            posAim.x = 0;
        }
        if (posAim.y < 0.000001 && posAim.y > -0.000001) {
            posAim.y = 0;
        }
        if (posAim.z < 0.000001 && posAim.z > -0.000001) {
            posAim.z = 0;
        }

        inverseKinematics(leg, posAim);
    }

    private void inverseKinematics(int leg, Vector3d posAim) {
        //Richtung des Mittelgelenks anpassen
        if (posAim.z == 0) {
            rotHorizontal[leg] = 0;
        } else {
            rotHorizontal[leg] = Math.atan(posAim.x / posAim.z);
        }

        double sinB = Math.sin(rotHorizontal[leg]);
        double cosB = Math.cos(rotHorizontal[leg]);

        Vector3d dummy = new Vector3d(L1 * sinB, 0, L1 * cosB);

        //if (leg == 5) System.out.println("Dummy:" + dummy);
        //Beine anpassen
        double distSquared = dummy.distanceSquared(posAim);
        double dist = Math.sqrt(distSquared);

        double angleBody = Math.acos((L3 * L3 - distSquared - L2 * L2) / (-2 * dist * L2));

        //if (leg == 5) System.out.println("Dist:" + dist);
        //if (leg == 5) System.out.println("Anglebody:" + angleBody * 180 / Math.PI);
        /*
        if (angleBody == Double.NaN) {
            return false;
        }
         */
        double downAngle = Math.asin((posAim.y - dummy.y) / (dist));

        if (dummy.z > posAim.z) {
            downAngle = Math.PI - downAngle;
        }

        //if (leg == 5) System.out.println("Downangle:" + (downAngle * 180 / Math.PI));
        rotTop[leg] = (8 * Math.PI - (angleBody + downAngle)) % (2 * Math.PI);

        double sinC = Math.sin(rotTop[leg]);
        double cosC = Math.cos(rotTop[leg]);

        posHorizontal[leg] = new Vector3d(L2 * (cosB * sinC + sinB * cosC) + L1 * sinB, 0, L2 * (cosB * cosC - sinB * sinC) + L1 * cosB);

        double angleTop = Math.acos((distSquared - L2 * L2 - L3 * L3) / (-2 * L2 * L3));

        //System.out.println(angleBody * 180 / Math.PI);
        //System.out.println(downAngle * 180 / Math.PI);
        rotBottom[leg] = Math.PI - angleTop;
    }

    private void moveBack(int leg, double t) {
        double tx = chromosomes[leg][G_X];
        double tz = chromosomes[leg][G_Z];

        double sinA = Math.sin((leg * 60 + 30) / 180. * Math.PI);
        double cosA = Math.cos((leg * 60 + 30) / 180. * Math.PI);

        double sinAY = Math.sin(((leg * 60 + 30) / 180. * Math.PI) + chromosomes[C_ROTATION][1]);
        double cosAY = Math.cos(((leg * 60 + 30) / 180. * Math.PI) + chromosomes[C_ROTATION][1]);

        double sinX = Math.sin(chromosomes[C_ROTATION][0]);
        double cosX = Math.cos(chromosomes[C_ROTATION][0]);

        double ty = -chromosomes[C_Y_OFFSET][0] + sinX * Math.cos(-((leg * 60 + 30) / 180. * Math.PI) - chromosomes[C_ROTATION][1]) * A;

        tx += cosA * posDiff.x + sinA * posDiff.z;
        ty += 0.5 * Math.sin(t * 2 * Math.PI);
        tz += -sinA * posDiff.x + cosA * posDiff.z;

        Vector3d posAim = new Vector3d(
                -(tx * cosAY * cosAY + tx * cosX - tx * cosX * cosAY * cosAY - sinAY * sinX * ty - tz * cosAY * sinAY + tz * sinAY * cosX * cosAY),
                (sinAY * sinX * tx + cosX * ty + cosAY * sinX * tz),
                (-tx * cosAY * sinAY + tx * sinAY * cosX * cosAY - cosAY * sinX * ty + tz - tz * cosAY * cosAY + tz * cosAY * cosAY * cosX)
        );

        if (posAim.x < 0.000001 && posAim.x > -0.000001) {
            posAim.x = 0;
        }
        if (posAim.y < 0.000001 && posAim.y > -0.000001) {
            posAim.y = 0;
        }
        if (posAim.z < 0.000001 && posAim.z > -0.000001) {
            posAim.z = 0;
        }

        inverseKinematics(leg, posAim);

        /*
        Vector3d posAim = new Vector3d(chromosomes[leg][G_X], -chromosomes[C_Y_OFFSET][0], chromosomes[leg][G_Z]);

        double sinA = Math.sin(-(leg * 60 + 30) / 180. * Math.PI);
        double cosA = Math.cos(-(leg * 60 + 30) / 180. * Math.PI);

        //TODO, set the sin-y according to speed; slower = less factor; therefore you need less space if you are crawling
        posAim.addLocal(cosA * posDiff.x + sinA * posDiff.z, posDiff.y + 0.5 * Math.sin(t * 2 * Math.PI), -sinA * posDiff.x + cosA * posDiff.z);

        inverseKinematics(leg, posAim);
         */
    }

    @Override
    public void setRotation(double t) {
        if (t < 0.5) {
            setDistance(t);
            inverseKinematics(0);
            inverseKinematics(2);
            inverseKinematics(4);

            setDistance(-t);
            moveBack(1, t);
            moveBack(3, t);
            moveBack(5, t);

        } else {
            setDistance(1 - t);
            moveBack(0, t - 0.5);
            moveBack(2, t - 0.5);
            moveBack(4, t - 0.5);

            setDistance(-1 + t);
            inverseKinematics(1);
            inverseKinematics(3);
            inverseKinematics(5);
        }
    }

    @Override
    public double getStartHeight() {
        return chromosomes[C_Y_OFFSET][0];
    }

    @Override
    public double[] getDirection() {
        return chromosomes[C_TRANSLATION];
    }

    @Override
    public double[] getStartRotation() {
        return chromosomes[C_ROTATION];
    }

    @Override
    public double getRotationAngle() {
        return 0;
    }

    @Override
    public AWalker newInstance() {
        return new TripodLoopRobot(true);
    }
    
    @Override
    public AWalker newInstance(double[][] dna) {
        return new TripodLoopRobot(dna);
    }
    
    @Override
    public AWalker newInstance(double[][] dna, boolean[][] mutate) {
        return new TripodLoopRobot(dna, mutate);
    }

    @Override
    public AWalker newInstance(AWalker parent0, AWalker parent1) {
        return new TripodLoopRobot(parent0, parent1);
    }

    @Override
    public String getName() {
        return "Tripod Robot";
    }

    @Override
    public String getDescription() {
        return "A robot which uses a tripod gait for movement. Can't rotate.";
    }
    
    @Override
    public String[][] getDNAInfo() {
        return new String[][] {
            {"Position - Leg 1"},
            {"Position - Leg 2"},
            {"Position - Leg 3"},
            {"Position - Leg 4"},
            {"Position - Leg 5"},
            {"Position - Leg 6"},
            {"Body Height"},
            {"Orientation"},
            {"Movement"},
        };
    }

    @Override
    protected void setupChromosomes() {
        chromosomes = new double[][]{
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0},
            {0, 0},
            {0, 0},};
        
        chromosomeMutations = new boolean[][]{
            {true, true},
            {true, true},
            {true, true},
            {true, true},
            {true, true},
            {true, true},
            {true},
            {true, true},
            {true, true},};
    }

}
