/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import static mygame.GeneticRobot.A_BOTTOM;
import static mygame.GeneticRobot.A_HORIZONTAL;
import static mygame.GeneticRobot.A_TOP;
import static mygame.GeneticRobot.B_BOTTOM;
import static mygame.GeneticRobot.B_HORIZONTAL;
import static mygame.GeneticRobot.B_TOP;
import static mygame.GeneticRobot.PHI_BOTTOM;
import static mygame.GeneticRobot.PHI_HORIZONTAL;
import static mygame.GeneticRobot.PHI_TOP;

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

    public static final int CHROMOSOMES = 3;
    public static final int EXTRA_CHROMOSOME_END = 5;

    public static final int G_X = 0;
    public static final int G_Y = 1;
    public static final int G_Z = 2;

    public static final int GENES = 3;

    public static final int C_TRANSLATION = 3;
    public static final int C_ROTATION = 4;

    /* Variables */
    Vector3d posSolid[] = new Vector3d[LEGS];
    Vector3d posHorizontal[] = new Vector3d[LEGS];
    Vector3d posTop[] = new Vector3d[LEGS];
    Vector3d posBottom[] = new Vector3d[LEGS];
    double[][] chromosomes = new double[EXTRA_CHROMOSOME_END][GENES];

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
        for (int i = 0; i < EXTRA_CHROMOSOME_END; i++) {
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
        posDiff = new Vector3d(-chromosomes[C_TRANSLATION][G_X] / t, -chromosomes[C_TRANSLATION][G_Y] / t, -chromosomes[C_TRANSLATION][G_Z] / t);
    }

    private void setRotation2(double t) {
        if (t < 0.5) {
            inverseKinematics(0, t);
            inverseKinematics(2, t);
            inverseKinematics(4, t);
            moveBack(1, t);
            moveBack(3, t);
            moveBack(5, t);
        } else {
            inverseKinematics(1, t);
            inverseKinematics(3, t);
            inverseKinematics(5, t);
            moveBack(0, t);
            moveBack(2, t);
            moveBack(4, t);
        }
    }

    private void inverseKinematics(int leg, double t) {
        int c = leg < 3 ? leg : LEGS - leg - 1;
        int mirrorFactor = leg < 3 ? 1 : -1;
        Vector3d posAim = new Vector3d(mirrorFactor * chromosomes[c][G_X], chromosomes[c][G_Y], chromosomes[c][G_Z]);

        double sinA = Math.sin(-c * 60 / 180. * Math.PI);
        double cosA = Math.cos(-c * 60 / 180. * Math.PI);

        posAim.addLocal(cosA * posDiff.x + sinA * posDiff.z, posDiff.y, -sinA * posDiff.x + cosA * posDiff.z);
        GeneticIK.posAimDebug = posAim;

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

        posHorizontal[c] = new Vector3d(L2 * (cosB * sinC + sinB * cosC) + L1 * sinB, 0, L2 * (cosB * cosC - sinB * sinC) + L1 * cosB);

        double angleTop = Math.acos((distSquared - L2 * L2 - L3 * L3) / (-2 * L2 * L3));

        //System.out.println(angleBody * 180 / Math.PI);
        //System.out.println(downAngle * 180 / Math.PI);
        rotBottom[leg] = Math.PI - angleTop;
    }

    private void moveBack(int leg, double t) {

    }

    public void setRotation(double t) {
        for (int c = 0; c < CHROMOSOMES; c++) {
            double sinA = Math.sin(c * 60 / 180. * Math.PI);
            double cosA = Math.cos(c * 60 / 180. * Math.PI);

            double sinB = Math.sin(chromosomes[c][A_HORIZONTAL] + chromosomes[c][B_HORIZONTAL] * Math.sin(t * (2 * Math.PI) + chromosomes[c][PHI_HORIZONTAL]));
            double cosB = Math.cos(chromosomes[c][A_HORIZONTAL] + chromosomes[c][B_HORIZONTAL] * Math.sin(t * (2 * Math.PI) + chromosomes[c][PHI_HORIZONTAL]));

            double sinC = Math.sin(chromosomes[c][A_TOP] + chromosomes[c][B_TOP] * Math.sin(t * (2 * Math.PI) + chromosomes[c][PHI_TOP]));
            double cosC = Math.cos(chromosomes[c][A_TOP] + chromosomes[c][B_TOP] * Math.sin(t * (2 * Math.PI) + chromosomes[c][PHI_TOP]));

            double sinD = Math.sin(chromosomes[c][A_BOTTOM] + chromosomes[c][B_BOTTOM] * Math.sin(t * (2 * Math.PI) + chromosomes[c][PHI_BOTTOM]));
            double cosD = Math.cos(chromosomes[c][A_BOTTOM] + chromosomes[c][B_BOTTOM] * Math.sin(t * (2 * Math.PI) + chromosomes[c][PHI_BOTTOM]));

            posSolid[c] = new Vector3d(A * sinA, 0, A * cosA);
            posHorizontal[c] = new Vector3d(L1 * (cosA * sinB + sinA * cosB) + A * sinA, 0, L1 * (cosA * cosB - sinA * sinB) + A * cosA);
            posTop[c] = new Vector3d((cosA * sinB + sinA * cosB) * cosC * L2 + posHorizontal[c].x, -sinC * L2, (cosA * cosB - sinA * sinB) * cosC * L2 + posHorizontal[c].z);
            posBottom[c] = new Vector3d((-(cosA * sinB + sinA * cosB) * sinC * sinD + (cosA * sinB + sinA * cosB) * cosC * cosD) * L3 + posTop[c].x, (-cosC * sinD - sinC * cosD) * L3 - sinC * L2, (-(cosA * cosB - sinA * sinB) * sinC * sinD + (cosA * cosB - sinA * sinB) * cosC * cosD) * L3 + posTop[c].z);

            // other legs
            sinA = Math.sin((LEGS - 1 - c) * 60 / 180. * Math.PI);
            cosA = Math.cos((LEGS - 1 - c) * 60 / 180. * Math.PI);

            sinB = Math.sin(-chromosomes[c][A_HORIZONTAL] + chromosomes[c][B_HORIZONTAL] * Math.sin(t * (2 * Math.PI) + chromosomes[c][PHI_HORIZONTAL]));
            cosB = Math.cos(-chromosomes[c][A_HORIZONTAL] + chromosomes[c][B_HORIZONTAL] * Math.sin(t * (2 * Math.PI) + chromosomes[c][PHI_HORIZONTAL]));

            sinC = Math.sin(chromosomes[c][A_TOP] - chromosomes[c][B_TOP] * Math.sin(t * (2 * Math.PI) + chromosomes[c][PHI_TOP]));
            cosC = Math.cos(chromosomes[c][A_TOP] - chromosomes[c][B_TOP] * Math.sin(t * (2 * Math.PI) + chromosomes[c][PHI_TOP]));

            sinD = Math.sin(chromosomes[c][A_BOTTOM] - chromosomes[c][B_BOTTOM] * Math.sin(t * (2 * Math.PI) + chromosomes[c][PHI_BOTTOM]));
            cosD = Math.cos(chromosomes[c][A_BOTTOM] - chromosomes[c][B_BOTTOM] * Math.sin(t * (2 * Math.PI) + chromosomes[c][PHI_BOTTOM]));

            posSolid[LEGS - 1 - c] = new Vector3d(A * sinA, 0, A * cosA);
            posHorizontal[LEGS - 1 - c] = new Vector3d(L1 * (cosA * sinB + sinA * cosB) + A * sinA, 0, L1 * (cosA * cosB - sinA * sinB) + A * cosA);
            posTop[LEGS - 1 - c] = new Vector3d((cosA * sinB + sinA * cosB) * cosC * L2 + posHorizontal[LEGS - 1 - c].x, -sinC * L2, (cosA * cosB - sinA * sinB) * cosC * L2 + posHorizontal[LEGS - 1 - c].z);
            posBottom[LEGS - 1 - c] = new Vector3d((-(cosA * sinB + sinA * cosB) * sinC * sinD + (cosA * sinB + sinA * cosB) * cosC * cosD) * L3 + posTop[LEGS - 1 - c].x, (-cosC * sinD - sinC * cosD) * L3 - sinC * L2, (-(cosA * cosB - sinA * sinB) * sinC * sinD + (cosA * cosB - sinA * sinB) * cosC * cosD) * L3 + posTop[LEGS - 1 - c].z);
        }
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
