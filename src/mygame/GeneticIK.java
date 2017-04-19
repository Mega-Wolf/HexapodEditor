/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

/**
 *
 * @author Tobias
 */
public class GeneticIK {

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
    public static final int G_Y = 1;
    public static final int G_Z = 2;

    public static final int GENES = 3;

    public static final int C_TRANSLATION = 6;
    public static final int C_ROTATION = 7;
    
    public static final int CHROMOSOMES = 8;

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
    public GeneticIK() {
        //TODO; init stuff
    }

    public GeneticIK(double[][] chromosomes) {
        this.chromosomes = chromosomes;
    }

    public GeneticIK(GeneticIK g0, GeneticIK g1) {
        for (int i = 0; i < CHROMOSOMES; i++) {
            System.arraycopy(Math.random() < 0.5 ? g0.chromosomes[i] : g1.chromosomes[i], 0, chromosomes[i], 0, GENES);
        }
        mutate();
    }

    /* Methods */
    private void mutate() {
        //TODO
    }

    /*
        Notizen:
        - Wie bewegt sich Roboter überhaupt?
            - Translation
            - Rotation
        - Wie erkennt der Roboter, dass er sich so bewegt?
        
        Beobachtung:
        - Punkt genau unter dem HJoint nicht genau definiert
            - Festlegen auf 0° horizontal?
     */
    public static Vector3d posAimDebug;

    public boolean setStartRotation() {
        for (int c = 0; c < CHROMOSOMES; c++) {
            Vector3d posAim = new Vector3d(chromosomes[c][G_X], chromosomes[c][G_Y], chromosomes[c][G_Z]);

            double sinA = Math.sin(-c * 60 / 180. * Math.PI);
            double cosA = Math.cos(-c * 60 / 180. * Math.PI);

            //Vector3d diff = new Vector3d(0,0, -ABSTAND);
            //posAim.addLocal(cosA * diff.x + sinA * diff.z, diff.y, - sinA * diff.x + cosA * diff.z);
            GeneticIK.posAimDebug = posAim;

            //Richtung des Mittelgelenks anpassen
            if (posAim.z == 0) {
                rotHorizontal[c] = 0;
            } else {
                rotHorizontal[c] = Math.atan(posAim.x / posAim.z);
            }

            double sinB = Math.sin(rotHorizontal[c]);
            double cosB = Math.cos(rotHorizontal[c]);

            Vector3d dummy = new Vector3d(L1 * sinB, 0, L1 * cosB);

            //Beine anpassen
            double distSquared = dummy.distanceSquared(posAim);
            double dist = Math.sqrt(distSquared);

            double angleBody = Math.acos((L3 * L3 - distSquared - L2 * L2) / (-2 * dist * L2));

            if (angleBody == Double.NaN) {
                return false;
            }

            double downAngle = Math.asin((posAim.y - dummy.y) / (dist));

            rotTop[c] = 2 * Math.PI - (angleBody + downAngle);

            double sinC = Math.sin(rotTop[c]);
            double cosC = Math.cos(rotTop[c]);

            posHorizontal[c] = new Vector3d(L2 * (cosB * sinC + sinB * cosC) + L1 * sinB, 0, L2 * (cosB * cosC - sinB * sinC) + L1 * cosB);

            double angleTop = Math.acos((distSquared - L2 * L2 - L3 * L3) / (-2 * L2 * L3));

            //System.out.println(angleBody * 180 / Math.PI);
            //System.out.println(downAngle * 180 / Math.PI);
            rotBottom[c] = Math.PI - angleTop;
        }
        return true;
    }

    private void setDistance(double t) {
        posDiff = new Vector3d(-chromosomes[C_TRANSLATION][G_X] * t, -chromosomes[C_TRANSLATION][G_Y] * t, -chromosomes[C_TRANSLATION][G_Z] * t);
    }

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
            
            
            //inverseKinematics(0, t);
            //inverseKinematics(2, t);
            //inverseKinematics(4, t);
        }
    }

    private void inverseKinematics(int leg) {
        Vector3d posAim = new Vector3d(chromosomes[leg][G_X], chromosomes[leg][G_Y], chromosomes[leg][G_Z]);

        double sinA = Math.sin(-(leg * 60 + 30) / 180. * Math.PI);
        double cosA = Math.cos(-(leg * 60 + 30) / 180. * Math.PI);

        posAim.addLocal(cosA * posDiff.x + sinA * posDiff.z, posDiff.y, -sinA * posDiff.x + cosA * posDiff.z);
        GeneticIK.posAimDebug = posAim;
        
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

        //Beine anpassen
        double distSquared = dummy.distanceSquared(posAim);
        double dist = Math.sqrt(distSquared);

        double angleBody = Math.acos((L3 * L3 - distSquared - L2 * L2) / (-2 * dist * L2));

        /*
        if (angleBody == Double.NaN) {
            return false;
        }
         */
        double downAngle = Math.asin((posAim.y - dummy.y) / (dist));

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
        Vector3d posAim = new Vector3d(chromosomes[leg][G_X], chromosomes[leg][G_Y], chromosomes[leg][G_Z]);

        double sinA = Math.sin(-(leg * 60 + 30) / 180. * Math.PI);
        double cosA = Math.cos(-(leg * 60 + 30) / 180. * Math.PI);

        posAim.addLocal(cosA * posDiff.x + sinA * posDiff.z, posDiff.y + 0.5 * Math.sin(t * 2 * Math.PI), -sinA * posDiff.x + cosA * posDiff.z);
        GeneticIK.posAimDebug = posAim;
        
        inverseKinematics(leg, posAim);
    }

    /* Fitness Methods */
    public void calcFitness() {

    }

    private boolean correctPosition() {
        return true;
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
