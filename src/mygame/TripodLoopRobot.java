package mygame;

/**
 * A robot which can loop animations with Inverse Kinematics, the end state must
 * be the beginning state
 *
 * @author Tobias
 */
public class TripodLoopRobot {

    /* TODOS */
    // maybe add speed later on

    /* Consts */
    public static final int LEGS = 6;

    public static final double A = 1;
    public static final double L1 = 0.5;
    public static final double L2 = 1.5;
    public static final double L3 = 3;

    public static final double HORIZONTAL_MAX = 45 / 180. * Math.PI;
    public static final double HORIZONTAL_MIN = (360 - 45) / 180. * Math.PI;

    public static final double TOP_MAX = (360 - 10) / 180. * Math.PI;
    public static final double TOP_MIN = (360 - 80) / 180. * Math.PI;

    public static final double BOTTOM_MAX = 170 / 180. * Math.PI;
    public static final double BOTTOM_MIN = 25 / 180. * Math.PI;

    public static final int G_X = 0;
    public static final int G_Z = 1;

    public static final int GENES = 2;

    public static final int C_Y_OFFSET = 6;
    public static final int C_ROTATION = 7;
    public static final int C_TRANSLATION = 8;

    public static final int CHROMOSOMES = 9;

    /* Variables */
    Vector3d posSolid[] = new Vector3d[LEGS];
    Vector3d posHorizontal[] = new Vector3d[LEGS];
    Vector3d posTop[] = new Vector3d[LEGS];
    Vector3d posBottom[] = new Vector3d[LEGS];
    double[][] chromosomes = new double[CHROMOSOMES][GENES];

    double rotHorizontal[] = new double[LEGS];
    double rotTop[] = new double[LEGS];
    double rotBottom[] = new double[LEGS];

    double fitness;

    Vector3d posDiff;

    /* Constructor */
    public TripodLoopRobot() {
        //TODO; init stuff
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

    /**
     * Constructor for genetical usage. Takes to parents and lets mutation and
     * crossover determine the child
     *
     * @param g0 Parent 1
     * @param g1 Parent 2
     */
    public TripodLoopRobot(TripodLoopRobot g0, TripodLoopRobot g1) {
        for (int i = 0; i < CHROMOSOMES; i++) {
            System.arraycopy(Math.random() < 0.5 ? g0.chromosomes[i] : g1.chromosomes[i], 0, chromosomes[i], 0, GENES);
        }
        mutate();
    }

    /* Methods */
    /**
     * Mutates the values of the robot
     */
    private void mutate() {
        //TODO
    }

    /**
     * Sets the translation for the given time
     *
     * @param t time
     */
    private void setDistance(double t) {
        posDiff = new Vector3d(-chromosomes[C_TRANSLATION][G_X] * t, 0, -chromosomes[C_TRANSLATION][G_Z] * t);
    }

    public void setRotation(double t) {
        if (t < 0.5) {
            setDistance(t);
            inverseKinematics(0);
            inverseKinematics(2);
            inverseKinematics(4);

            inverseKinematics(1);
            inverseKinematics(3);
            inverseKinematics(5);
            
            /*
            setDistance(-t);
            moveBack(1, t);
            moveBack(3, t);
            moveBack(5, t);
            */
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
    
    public static Vector3d posAimDebug;

    private void inverseKinematics(int leg) {
        

        double tx = chromosomes[leg][G_X];
        double tz = chromosomes[leg][G_Z];

        double x = chromosomes[C_ROTATION][0];
        double y = chromosomes[C_ROTATION][1];

        double sinA = Math.sin((leg * 60 + 30) / 180. * Math.PI);
        double cosA = Math.cos((leg * 60 + 30) / 180. * Math.PI);
        
        double sinAY = Math.sin( ( (leg * 60 + 30) / 180. * Math.PI) + chromosomes[C_ROTATION][1]);
        double cosAY = Math.cos( ( (leg * 60 + 30) / 180. * Math.PI) + chromosomes[C_ROTATION][1]);
        
        double sinX = Math.sin(chromosomes[C_ROTATION][0]);
        double cosX = Math.cos(chromosomes[C_ROTATION][0]);
        
        double ty = -1 + sinX * Math.cos(-( (leg * 60 + 30) / 180. * Math.PI) - chromosomes[C_ROTATION][1]) * A;
        
        
        
        Vector3d posAim = new Vector3d(
            -(tx * cosAY * cosAY + tx * cosX - tx * cosX * cosAY * cosAY - sinAY * sinX * ty - tz * cosAY * sinAY + tz * sinAY * cosX * cosAY),
            (sinAY * sinX * tx + cosX * ty + cosAY * sinX * tz),
            (-tx * cosAY * sinAY + tx * sinAY * cosX * cosAY - cosAY * sinX * ty + tz - tz * cosAY * cosAY + tz * cosAY * cosAY * cosX)
        );
        
        
        
        if (posAim.x < 0.000001 && posAim.x > -0.000001) posAim.x = 0;
        if (posAim.y < 0.000001 && posAim.y > -0.000001) posAim.y = 0;
        if (posAim.z < 0.000001 && posAim.z > -0.000001) posAim.z = 0;
        
        //inverseKinematics(leg, posAim);
        inverseKinematics(leg, posAim);
        System.out.println(posAim);
        //System.out.println(posAim3);
        System.out.println(x);
        System.out.println(y);
        System.out.println(tx);
        System.out.println(ty);
        System.out.println(tz);
        System.out.println();
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
        
        if (leg == 5) System.out.println("Dummy:" + dummy);

        //Beine anpassen
        double distSquared = dummy.distanceSquared(posAim);
        double dist = Math.sqrt(distSquared);

        double angleBody = Math.acos((L3 * L3 - distSquared - L2 * L2) / (-2 * dist * L2));

        if (leg == 5) System.out.println("Dist:" + dist);
        
        if (leg == 5) System.out.println("Anglebody:" + angleBody * 180 / Math.PI);
        
        /*
        if (angleBody == Double.NaN) {
            return false;
        }
         */
        
        
        double downAngle = Math.asin((posAim.y - dummy.y) / (dist));

        if (dummy.z > posAim.z) {
            downAngle = Math.PI - downAngle;
        }
        
        if (leg == 5) System.out.println("Downangle:" + (downAngle * 180 / Math.PI));
        
        rotTop[leg] = 2 * Math.PI - (angleBody + downAngle);

        double sinC = Math.sin(rotTop[leg]);
        double cosC = Math.cos(rotTop[leg]);

        posHorizontal[leg] = new Vector3d(L2 * (cosB * sinC + sinB * cosC) + L1 * sinB, 0, L2 * (cosB * cosC - sinB * sinC) + L1 * cosB);

        double angleTop = Math.acos((distSquared - L2 * L2 - L3 * L3) / (-2 * L2 * L3));

        //System.out.println(angleBody * 180 / Math.PI);
        //System.out.println(downAngle * 180 / Math.PI);
        rotBottom[leg] = Math.PI - angleTop;
    }

    private void moveBack(int leg, double t) {
        Vector3d posAim = new Vector3d(chromosomes[leg][G_X], -chromosomes[C_Y_OFFSET][0], chromosomes[leg][G_Z]);

        double sinA = Math.sin(-(leg * 60 + 30) / 180. * Math.PI);
        double cosA = Math.cos(-(leg * 60 + 30) / 180. * Math.PI);

        //TODO, set the sin-y according to speed; slower = less factor; therefore you need less space if you are crawling
        posAim.addLocal(cosA * posDiff.x + sinA * posDiff.z, posDiff.y + 0.5 * Math.sin(t * 2 * Math.PI), -sinA * posDiff.x + cosA * posDiff.z);

        inverseKinematics(leg, posAim);
    }

    /* Fitness Methods */
    public void calcFitness() {

    }

    private boolean correctAngles() {
        return true;
    }

    private boolean noIntersection() {
        return true;
    }

    private void bestValue() {

    }
}
