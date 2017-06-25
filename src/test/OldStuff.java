/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.FastMath;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Tobias
 */
/*
public class OldStuff {
    private void calcFitness(Robot.DNA dna) {
        double fitness = -10000;
        double[][] aFinal = new double[Robot.DNA.LEGS][3];
        double[][] bFinal = new double[Robot.DNA.LEGS][3];
        int[] cPerLeg = new int[Robot.DNA.LEGS];

        long dummyTime = System.currentTimeMillis();
        
        // calculate final values A and B
        for (int c = 0; c < dna.chromosomes; c++) {
            for (int leg = 0; leg < Robot.DNA.LEGS; leg++) {
                if (((int) (dna.leg[c][Robot.DNA.BITMASK_LEG]) & (1 << leg)) != 0) {
                    cPerLeg[leg]++;
                    for (int i = 0; i < 3; i++) {
                        aFinal[leg][i] += dna.leg[c][Robot.DNA.A_BOTTOM + i];
                        bFinal[leg][i] += dna.leg[c][Robot.DNA.B_BOTTOM + i];
                    }
                }
            }
        }
        for (int leg = 0; leg < Robot.DNA.LEGS; leg++) {
            if (cPerLeg[leg] > 0) {
                for (int i = 0; i < 3; i++) {
                    aFinal[leg][i] /= cPerLeg[leg];
                    bFinal[leg][i] /= cPerLeg[leg];
                }
            }
        }
        timeFinal += System.currentTimeMillis() - dummyTime;

        dummyTime = System.currentTimeMillis();
        double dummy;
        double dummy2;

        for (int leg = 0; leg < Robot.DNA.LEGS; leg++) {
            if (aFinal[leg][0] < 10 || aFinal[leg][0] > 170 || aFinal[leg][0] - 2 * bFinal[leg][0] < 10 || aFinal[leg][0] - 2 * bFinal[leg][0] > 170) {
                dummy = Math.abs(36000 + aFinal[leg][0] - 10) % 360;
                dummy2 = Math.abs(36000 + aFinal[leg][0] - 170) % 360;
                dummy = dummy > 180 ? 360 - dummy : dummy;
                dummy2 = dummy2 > 180 ? 360 - dummy2 : dummy2;
                fitness -= Math.min(dummy, dummy2);

                dummy = Math.abs(36000 + aFinal[leg][0] - 2 * bFinal[leg][0] - 10) % 360;
                dummy2 = Math.abs(36000 + aFinal[leg][0] - 2 * bFinal[leg][0] - 170) % 360;
                dummy = dummy > 180 ? 360 - dummy : dummy;
                dummy2 = dummy2 > 180 ? 360 - dummy2 : dummy2;
                fitness -= Math.min(dummy, dummy2);
            }

            if (aFinal[leg][1] > 360 - 10 || aFinal[leg][1] < 360 - 80 || aFinal[leg][1] - 2 * bFinal[leg][1] > 360 - 10 || aFinal[leg][1] - 2 * bFinal[leg][1] < 360 - 80) {
                dummy = Math.abs(36000 + aFinal[leg][1] - (360 - 10)) % 360;
                dummy2 = Math.abs(36000 + aFinal[leg][1] - (360 - 80)) % 360;
                dummy = dummy > 180 ? 360 - dummy : dummy;
                dummy2 = dummy2 > 180 ? 360 - dummy2 : dummy2;
                fitness -= Math.min(dummy, dummy2);

                dummy = Math.abs(36000 + aFinal[leg][1] - 2 * bFinal[leg][1] - (360 - 10)) % 360;
                dummy2 = Math.abs(36000 + aFinal[leg][1] - 2 * bFinal[leg][1] - (360 - 80)) % 360;
                dummy = dummy > 180 ? 360 - dummy : dummy;
                dummy2 = dummy2 > 180 ? 360 - dummy2 : dummy2;
                fitness -= Math.min(dummy, dummy2);
            }

            if (aFinal[leg][2] < 360 - 45 || aFinal[leg][2] > 45 || aFinal[leg][2] - 2 * bFinal[leg][2] < 360 - 45 || aFinal[leg][2] - 2 * bFinal[leg][2] > 45) {
                dummy = Math.abs(36000 + aFinal[leg][2] - (360 - 45)) % 360;
                dummy2 = Math.abs(36000 + aFinal[leg][2] - 45) % 360;
                dummy = dummy > 180 ? 360 - dummy : dummy;
                dummy2 = dummy2 > 180 ? 360 - dummy2 : dummy2;
                fitness -= Math.min(dummy, dummy2);

                dummy = Math.abs(36000 + aFinal[leg][2] - 2 * bFinal[leg][2] - (360 - 45)) % 360;
                dummy2 = Math.abs(36000 + aFinal[leg][2] - 2 * bFinal[leg][2] - 45) % 360;
                dummy = dummy > 180 ? 360 - dummy : dummy;
                dummy2 = dummy2 > 180 ? 360 - dummy2 : dummy2;
                fitness -= Math.min(dummy, dummy2);
            }

            fitness -= Math.max(Math.abs(bFinal[leg][0]) - 10, 0) * 10;
            fitness -= Math.max(Math.abs(bFinal[leg][1]) - 10, 0) * 10;
            fitness -= Math.max(Math.abs(bFinal[leg][2]) - 10, 0) * 10;
        }
        timeCorrectAngle += System.currentTimeMillis() - dummyTime;
        
        dummyTime = System.currentTimeMillis();
        if (fitness == -10000) {
            fitness = 0;
            for (int i = 0; i < 5; i++) {
                setRotation(dna, 0.1f * i);
                //setRotation(dna, 0);
                if ((Math.abs(jointBottom[0].getWorldTranslation().distanceSquared(jointBottom[1].getWorldTranslation()) - 4) < 0.001f)
                        && (Math.abs(jointBottom[1].getWorldTranslation().distanceSquared(jointBottom[2].getWorldTranslation()) - 4) < 0.001f)
                        && (Math.abs(jointBottom[2].getWorldTranslation().distanceSquared(jointBottom[3].getWorldTranslation()) - 4) < 0.001f)
                        && (Math.abs(jointBottom[3].getWorldTranslation().distanceSquared(jointBottom[4].getWorldTranslation()) - 4) < 0.001f)
                        && (Math.abs(jointBottom[4].getWorldTranslation().distanceSquared(jointBottom[5].getWorldTranslation()) - 4) < 0.001f)
                        && (Math.abs(jointBottom[5].getWorldTranslation().distanceSquared(jointBottom[0].getWorldTranslation()) - 4) < 0.001f)) {
                    fitness += 0;
                } else {
                    double fail = 0;
                    fail += Math.abs(jointBottom[0].getWorldTranslation().distanceSquared(jointBottom[1].getWorldTranslation()) - 4);
                    fail += Math.abs(jointBottom[1].getWorldTranslation().distanceSquared(jointBottom[2].getWorldTranslation()) - 4);
                    fail += Math.abs(jointBottom[2].getWorldTranslation().distanceSquared(jointBottom[3].getWorldTranslation()) - 4);
                    fail += Math.abs(jointBottom[3].getWorldTranslation().distanceSquared(jointBottom[4].getWorldTranslation()) - 4);
                    fail += Math.abs(jointBottom[4].getWorldTranslation().distanceSquared(jointBottom[5].getWorldTranslation()) - 4);
                    fail += Math.abs(jointBottom[5].getWorldTranslation().distanceSquared(jointBottom[0].getWorldTranslation()) - 4);

                    fitness += fail * (-1);
                }
            }
            
            if (fitness == 0) {
                for (int leg = 0; leg < Robot.DNA.LEGS; leg++) {
                    for (int i = 0; i < 3; i++) {
                        fitness += Math.abs(bFinal[leg][i]);
                    }
                }
            }

        }
        timeCalc += System.currentTimeMillis() - dummyTime;
        
        dna.fitness = fitness;
    }
    
    private class DNA {

        public static final int LEGS = 6;

        public static final double MUTATION_RATE = 0.1f;    //TODO

        public static final int A_BOTTOM = 0;
        public static final int A_TOP = 1;
        public static final int A_HORIZONTAL = 2;
        public static final int B_BOTTOM = 3;
        public static final int B_TOP = 4;
        public static final int B_HORIZONTAL = 5;
        public static final int BITMASK_LEG = 6;

        public static final int GENES = 7;

        double[][] leg;

        int chromosomes;

        double fitness;

        public DNA() {
            chromosomes = 1;
            leg = new double[chromosomes][7];
        }

        public DNA(double[][] leg) {
            this.leg = leg;
        }

        public DNA(DNA dna0, DNA dna1) {

            int status = -1;

            chromosomes = Math.max(dna0.chromosomes, dna1.chromosomes);

            if (Math.random() < MUTATION_RATE * MUTATION_RATE) {
                if (chromosomes != 0 && Math.random() < 0.5) {
                    status = (int) (Math.random() * chromosomes);
                    leg = new double[chromosomes - 1][7];
                } else {
                    status = -1000 + (int) (Math.random() * Math.min(dna0.chromosomes, dna1.chromosomes));
                    leg = new double[chromosomes + 1][7];
                }
            } else {
                leg = new double[chromosomes][7];
            }

            int position = 0;

            for (int i = 0; i < chromosomes; i++) {
                if (i != status) {
                    if (i < dna0.leg.length && i < dna1.leg.length) {
                        boolean first = (Math.random() < 0.5);
                        System.arraycopy(first ? dna0.leg[i] : dna1.leg[i], 0, leg[position], 0, 7);
                        if (i == status + 1000 && dna0.chromosomes > 0 && dna1.chromosomes > 0) {
                            System.arraycopy(!first ? dna0.leg[i] : dna1.leg[i], 0, leg[chromosomes], 0, 7);
                        }
                    } else if (i < dna0.leg.length) {
                        System.arraycopy(dna0.leg[i], 0, leg[position], 0, 7);
                    } else {
                        System.arraycopy(dna1.leg[i], 0, leg[position], 0, 7);
                    }
                    position++;
                }
            }

            if (status >= 0) {
                chromosomes--;
            } else if (status < -1) {
                chromosomes++;
            }
            
            int zeros = 0;
            
            for (int c = 0; c < chromosomes; c++) {
                if (leg[c][BITMASK_LEG] == 0) {
                    zeros++;
                }
            }
            
            boolean free = true;
            
            if (zeros > 1) {
                double[][] dummy = new double[chromosomes - zeros + 1][7];
                position = 0;
                for (int c = 0; c < chromosomes; c++) {
                    if (leg[c][BITMASK_LEG] != 0) {
                        System.arraycopy(leg[c], 0, dummy[position], 0, 7);
                        position++;
                    } else {
                        if (free) {
                            System.arraycopy(leg[c], 0, dummy[position], 0, 7);
                            position++;
                            free = false;
                        }
                    }
                }
                chromosomes -= zeros - 1;
            }

            mutate();
        }

        private void mutate() {
            for (int c = 0; c < chromosomes; c++) {
                for (int index = 0; index < GENES; index++) {
                    if (Math.random() < MUTATION_RATE) {
                        if (Math.random() < MUTATION_RATE) {    // critical mutation
                            if (index < 3) {
                                leg[c][index] = (int) (Math.random() * 360 * 32) / 32f;
                            } else if (index < 6) {
                                leg[c][index] = ((int) (Math.random() * 256)) / 32f - 4;
                            } else {
                                leg[c][index] = (int) (Math.random() * 64);
                            }
                        } else // normal mutation
                        {
                            if (index < 3) {
                                leg[c][index] = (leg[c][index] + ((int) (Math.random() * 241) - 160) /32f) % 360;
                            } else if (index < 6) {
                                leg[c][index] += ((int) (Math.random() * 7) - 3) / 32f;
                            } else {
                                int toggle = 0;
                                for (int i = 0; i < LEGS; i++) {
                                    if (Math.random() < MUTATION_RATE) {
                                        toggle += (1 << i);
                                    }
                                }
                                leg[c][index] = (((int) leg[c][index]) ^ toggle);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "DNA:\n " + Arrays.deepToString(leg) + "\n Fitness: " + fitness;
        }

    }

    private static void shuffleArray(DNA[] array) {
        int index;
        DNA temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
    
    public void setRotation(Robot.DNA dna, double t) {
        double[][] aFinal = new double[Robot.DNA.LEGS][3];
        double[][] bFinal = new double[Robot.DNA.LEGS][3];
        int[] cPerLeg = new int[Robot.DNA.LEGS];

        for (int c = 0; c < dna.chromosomes; c++) {
            for (int leg = 0; leg < Robot.DNA.LEGS; leg++) {
                if (((int) (dna.leg[c][Robot.DNA.BITMASK_LEG]) & (1 << leg)) != 0) {
                    cPerLeg[leg]++;
                    for (int i = 0; i < 3; i++) {
                        aFinal[leg][i] += dna.leg[c][Robot.DNA.A_BOTTOM + i];
                        bFinal[leg][i] += dna.leg[c][Robot.DNA.B_BOTTOM + i];
                    }
                }
            }
        }
        for (int leg = 0; leg < Robot.DNA.LEGS; leg++) {
            if (cPerLeg[leg] > 0) {
                for (int i = 0; i < 3; i++) {
                    aFinal[leg][i] /= cPerLeg[leg];
                    bFinal[leg][i] /= cPerLeg[leg];
                }
            }
        }

        for (int leg = 0; leg < Robot.DNA.LEGS; leg++) {
            downAngles[leg] = aFinal[leg][0] - bFinal[leg][0] + bFinal[leg][0] * FastMath.cos(t);
            upAngles[leg] = aFinal[leg][1] - bFinal[leg][1] + bFinal[leg][1] * FastMath.cos(t);
            horizontalAngles[leg] = aFinal[leg][2] - bFinal[leg][2] + bFinal[leg][2] * FastMath.cos(t);
        }
        updateRotations();

    }
}
*/