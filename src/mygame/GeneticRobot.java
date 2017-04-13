/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.util.Arrays;

/**
 *
 * @author Tobias
 */
public class GeneticRobot {

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

    public static final double MUTATION_RATE = 0.15;    //TODO

    public static final int A_BOTTOM = 0;
    public static final int A_TOP = 1;
    public static final int A_HORIZONTAL = 2;
    public static final int B_BOTTOM = 3;
    public static final int B_TOP = 4;
    public static final int B_HORIZONTAL = 5;
    public static final int PHI_BOTTOM = 6;
    public static final int PHI_TOP = 7;
    public static final int PHI_HORIZONTAL = 8;

    public static final int GENES = 9;

    public static final int CHROMOSOMES = 3;

    public static double TOLERANCE = 0.00;

    /* Variables */
    Vector3d posSolid[] = new Vector3d[6];
    Vector3d posHorizontal[] = new Vector3d[6];
    Vector3d posTop[] = new Vector3d[6];
    Vector3d posBottom[] = new Vector3d[6];
    double[][] chromosomes = new double[CHROMOSOMES][GENES];

    double fitness;

    /* Constructors */
    public GeneticRobot() {

        /*
        for (int c = 0; c < CHROMOSOMES; c++) {
            chromosomes[c][0] = 120 / 180. * Math.PI;
            chromosomes[c][1] = -60 / 180. * Math.PI;

            //chromosomes[c][5] = 3.6875 + 1 / 128. * Math.PI;
        }
         */
    }

    public GeneticRobot(double[][] chromosomes) {
        this.chromosomes = chromosomes;
    }

    public GeneticRobot(GeneticRobot gr0, GeneticRobot gr1) {
        for (int i = 0; i < CHROMOSOMES; i++) {
            System.arraycopy(Math.random() < 0.5 ? gr0.chromosomes[i] : gr1.chromosomes[i], 0, chromosomes[i], 0, GENES);
        }
        mutate();
    }

    /* Methods */
    private void mutate() {
        for (int c = 0; c < CHROMOSOMES; c++) {
            for (int index = 0; index < 6; index++) {
                //for (int index = 5; index < 6; index++) {
                if (Math.random() < MUTATION_RATE) {
                    if (Math.random() < MUTATION_RATE) {    // critical mutation
                        if (index < 3) {
                            chromosomes[c][index] = (int) (Math.random() * 360 * 32) / 32. / 180. * Math.PI;
                        } else if (index < 6) {
                            chromosomes[c][index] = ((int) (Math.random() * 4 * 64 + 1) - 2 * 64) / 64. * Math.PI;
                        } else {
                            //chromosomes[c][index] = ((int) (Math.random() * 256)) / 128. * Math.PI;
                            chromosomes[c][index] = (int) (Math.random() * 360 * 32) / 32. / 180. * Math.PI;
                        }
                    } else // normal mutation
                     if (index < 3) {
                            //chromosomes[c][index] = (chromosomes[c][index] + ((int) (Math.random() * 257) - 128) / 32.) % 360;
                            chromosomes[c][index] += ((int) (Math.random() * 7 * 32 + 1) - 7 * 16) / 32. / 180. * Math.PI;
                        } else if (index < 6) {
                            chromosomes[c][index] += ((int) (Math.random() * 21) - 10) / 64. * Math.PI;
                        } else {
                            chromosomes[c][index] += ((int) (Math.random() * 7 * 32 + 1) - 7 * 16) / 32. / 180. * Math.PI;
                            //chromosomes[c][index] += ((int) (Math.random() * 11) - 5) / 128. * Math.PI;
                        }
                }
            }
        }

        /*
        if (chromosomes[0][B_HORIZONTAL] != 0 && chromosomes[0][B_HORIZONTAL] == chromosomes[2][B_HORIZONTAL] &&  chromosomes[1][B_HORIZONTAL] ==  chromosomes[2][B_HORIZONTAL] * -1 ) {
            System.out.println(chromosomes[0][B_HORIZONTAL]);
            System.out.println(chromosomes[1][B_HORIZONTAL]);
            System.out.println(chromosomes[2][B_HORIZONTAL]);
            System.out.println();
        }
         */
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

    public void calcFitness() {
        setRotation(0);
        if (touchesEveryone() && correctDistance() && correctAngles()) {     //ORIGINAL
            //if (correctAngles(dna) && touchesEveryone(dna) && correctDistance(dna)) {
            //if (correctAngles(dna) && correctDistance(dna) && touchesEveryone(dna)) {
            //if (touchesEveryone(dna) && correctAngles(dna) && correctDistance(dna)) {
            bestFitnessValue();
        }
    }

    double START_TOUCHES = -100000;
    double START_DISTANCE = -10000;
    double START_ANGLES = -1000;

    private boolean touchesEveryone() {
        setRotation(0);

        double fitness = START_TOUCHES;
        Vector3d normalEven = Vector3d.computeNormal(posBottom[0], posBottom[2], posBottom[4]);
        Vector3d normalUneven = Vector3d.computeNormal(posBottom[1], posBottom[3], posBottom[5]);

        fitness -= normalEven.crossLocal(normalUneven).lengthSquared();
        this.fitness = fitness;

        return fitness >= START_TOUCHES - TOLERANCE;
    }

    private boolean correctDistance() {
        double fitness = START_DISTANCE;

        setRotation(0);

        double distSq02 = Math.abs(posBottom[0].distanceSquared(posBottom[2]));
        double distSq04 = Math.abs(posBottom[0].distanceSquared(posBottom[4]));
        double distSq24 = Math.abs(posBottom[2].distanceSquared(posBottom[4]));

        for (int i = 1; i < 10; i++) {
            setRotation(i / 60.);
            fitness -= Math.max(Math.abs(posBottom[0].distanceSquared(posBottom[2]) - distSq02) - 0.0001, 0);
            fitness -= Math.max(Math.abs(posBottom[0].distanceSquared(posBottom[4]) - distSq04) - 0.0001, 0);
            fitness -= Math.max(Math.abs(posBottom[2].distanceSquared(posBottom[4]) - distSq24) - 0.0001, 0);
        }

        /*
        setRotation(dna, 0.1f);
        fitness -= Math.max(Math.abs(Math.abs(jointBottom[0].getWorldTranslation().distanceSquared(jointBottom[2].getWorldTranslation())) - distSq02) - 0.0001f, 0);
        fitness -= Math.max(Math.abs(Math.abs(jointBottom[0].getWorldTranslation().distanceSquared(jointBottom[4].getWorldTranslation())) - distSq04) - 0.0001f, 0);
        fitness -= Math.max(Math.abs(Math.abs(jointBottom[2].getWorldTranslation().distanceSquared(jointBottom[4].getWorldTranslation())) - distSq24) - 0.0001f, 0);    
         */
        this.fitness = fitness;
        return fitness >= START_DISTANCE - TOLERANCE;

    }

    private boolean correctAngles() {
        double fitness = START_ANGLES;

        double dummy, dummy2;

        for (int c = 0; c < DNA.CHROMOSOMES; c++) {
            if ((chromosomes[c][DNA.A_BOTTOM] + chromosomes[c][DNA.B_BOTTOM] + 2 * Math.PI) % (2 * Math.PI) < BOTTOM_MIN || (chromosomes[c][DNA.A_BOTTOM] + chromosomes[c][DNA.B_BOTTOM] + 2 * Math.PI) % (2 * Math.PI) > BOTTOM_MAX || (chromosomes[c][DNA.A_BOTTOM] - chromosomes[c][DNA.B_BOTTOM] + 2 * Math.PI) % (2 * Math.PI) < BOTTOM_MIN || (chromosomes[c][DNA.A_BOTTOM] - chromosomes[c][DNA.B_BOTTOM] + 2 * Math.PI) % (2 * Math.PI) > BOTTOM_MAX) {
                dummy = Math.abs(10 * Math.PI + chromosomes[c][DNA.A_BOTTOM] + chromosomes[c][DNA.B_BOTTOM] - BOTTOM_MIN) % (2 * Math.PI);
                dummy2 = Math.abs(10 * Math.PI + chromosomes[c][DNA.A_BOTTOM] + chromosomes[c][DNA.B_BOTTOM] - BOTTOM_MAX) % (2 * Math.PI);
                dummy = dummy > Math.PI ? 2 * Math.PI - dummy : dummy;
                dummy2 = dummy2 > Math.PI ? 2 * Math.PI - dummy2 : dummy2;
                fitness -= Math.min(dummy, dummy2);

                dummy = Math.abs(10 * Math.PI + chromosomes[c][DNA.A_BOTTOM] - chromosomes[c][DNA.B_BOTTOM] - BOTTOM_MIN) % (2 * Math.PI);
                dummy2 = Math.abs(10 * Math.PI + chromosomes[c][DNA.A_BOTTOM] - chromosomes[c][DNA.B_BOTTOM] - BOTTOM_MAX) % (2 * Math.PI);
                dummy = dummy > Math.PI ? 2 * Math.PI - dummy : dummy;
                dummy2 = dummy2 > Math.PI ? 2 * Math.PI - dummy2 : dummy2;
                fitness -= Math.min(dummy, dummy2);
            }

            if ((chromosomes[c][DNA.A_TOP] + chromosomes[c][DNA.B_TOP] + 2 * Math.PI) % (2 * Math.PI) > TOP_MAX || (chromosomes[c][DNA.A_TOP] + chromosomes[c][DNA.B_TOP] + 2 * Math.PI) % (2 * Math.PI) < TOP_MIN || (chromosomes[c][DNA.A_TOP] - chromosomes[c][DNA.B_TOP] + 2 * Math.PI) % (2 * Math.PI) > TOP_MAX || (chromosomes[c][DNA.A_TOP] - chromosomes[c][DNA.B_TOP] + 2 * Math.PI) % (2 * Math.PI) < TOP_MIN) {
                dummy = Math.abs(10 * Math.PI + chromosomes[c][DNA.A_TOP] + chromosomes[c][DNA.B_TOP] - (TOP_MAX)) % (2 * Math.PI);
                dummy2 = Math.abs(10 * Math.PI + chromosomes[c][DNA.A_TOP] + chromosomes[c][DNA.B_TOP] - (TOP_MIN)) % (2 * Math.PI);
                dummy = dummy > Math.PI ? 2 * Math.PI - dummy : dummy;
                dummy2 = dummy2 > Math.PI ? 2 * Math.PI - dummy2 : dummy2;
                fitness -= Math.min(dummy, dummy2);

                dummy = Math.abs(10 * Math.PI + chromosomes[c][DNA.A_TOP] - chromosomes[c][DNA.B_TOP] - (TOP_MAX)) % (2 * Math.PI);
                dummy2 = Math.abs(10 * Math.PI + chromosomes[c][DNA.A_TOP] - chromosomes[c][DNA.B_TOP] - (TOP_MIN)) % (2 * Math.PI);
                dummy = dummy > Math.PI ? 2 * Math.PI - dummy : dummy;
                dummy2 = dummy2 > Math.PI ? 2 * Math.PI - dummy2 : dummy2;
                fitness -= Math.min(dummy, dummy2);
            }

            if (!(((chromosomes[c][DNA.A_HORIZONTAL] + chromosomes[c][DNA.B_HORIZONTAL] + 2 * Math.PI) % (2 * Math.PI) >= HORIZONTAL_MIN
                    || (chromosomes[c][DNA.A_HORIZONTAL] + chromosomes[c][DNA.B_HORIZONTAL] + 2 * Math.PI) % (2 * Math.PI) <= HORIZONTAL_MAX)
                    && ((chromosomes[c][DNA.A_HORIZONTAL] - chromosomes[c][DNA.B_HORIZONTAL] + 2 * Math.PI) % (2 * Math.PI) >= HORIZONTAL_MIN
                    || (chromosomes[c][DNA.A_HORIZONTAL] - chromosomes[c][DNA.B_HORIZONTAL] + 2 * Math.PI) % (2 * Math.PI) <= HORIZONTAL_MAX))) {
                dummy = Math.abs(10 * Math.PI + chromosomes[c][DNA.A_HORIZONTAL] + chromosomes[c][DNA.B_HORIZONTAL] - (HORIZONTAL_MIN)) % (2 * Math.PI);
                dummy2 = Math.abs(10 * Math.PI + chromosomes[c][DNA.A_HORIZONTAL] + chromosomes[c][DNA.B_HORIZONTAL] - HORIZONTAL_MAX) % (2 * Math.PI);
                dummy = dummy > Math.PI ? 2 * Math.PI - dummy : dummy;
                dummy2 = dummy2 > Math.PI ? 2 * Math.PI - dummy2 : dummy2;
                fitness -= Math.min(dummy, dummy2);

                dummy = Math.abs(10 * Math.PI + chromosomes[c][DNA.A_HORIZONTAL] - chromosomes[c][DNA.B_HORIZONTAL] - (HORIZONTAL_MIN)) % (2 * Math.PI);
                dummy2 = Math.abs(10 * Math.PI + chromosomes[c][DNA.A_HORIZONTAL] - chromosomes[c][DNA.B_HORIZONTAL] - HORIZONTAL_MAX) % (2 * Math.PI);
                dummy = dummy > Math.PI ? 2 * Math.PI - dummy : dummy;
                dummy2 = dummy2 > Math.PI ? 2 * Math.PI - dummy2 : dummy2;
                fitness -= Math.min(dummy, dummy2);
            }

            fitness -= Math.max(Math.abs(chromosomes[c][DNA.B_BOTTOM]) - 0.3, 0) * 10;
            fitness -= Math.max(Math.abs(chromosomes[c][DNA.B_TOP]) - 0.3, 0) * 10;
            fitness -= Math.max(Math.abs(chromosomes[c][DNA.B_HORIZONTAL]) - 0.3, 0) * 10;

        }
        this.fitness = fitness;

        return fitness >= START_ANGLES - TOLERANCE;
    }

    private void bestFitnessValue() {
        double fitness = 0;
        for (int c = 0; c < DNA.CHROMOSOMES; c++) {
            //fitness += Math.abs(chromosomes[c][DNA.B_BOTTOM]) + Math.abs(chromosomes[c][DNA.B_TOP]) + Math.abs(chromosomes[c][DNA.B_HORIZONTAL]);
            fitness += Math.abs(chromosomes[c][DNA.B_HORIZONTAL]);
            //if (c != 1) {
            //    fitness += Math.abs(chromosomes[c][DNA.B_HORIZONTAL]);
            //}
        }

        //fitness += Math.min(Math.min(Math.abs(chromosomes[0][DNA.B_HORIZONTAL]),Math.abs(chromosomes[1][DNA.B_HORIZONTAL])),Math.abs(chromosomes[2][DNA.B_HORIZONTAL]));
        this.fitness = fitness;
    }

    @Override
    public String toString() {
        return "GR :" + Arrays.deepToString(chromosomes) + "\nFitness: " + fitness;
    }

}
