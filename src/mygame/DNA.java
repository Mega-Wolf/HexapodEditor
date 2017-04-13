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
public class DNA {

        public static final int LEGS = 6;

        public static final double MUTATION_RATE = 0.05;    //TODO

        public static final int A_BOTTOM = 0;
        public static final int A_TOP = 1;
        public static final int A_HORIZONTAL = 2;
        public static final int B_BOTTOM = 3;
        public static final int B_TOP = 4;
        public static final int B_HORIZONTAL = 5;
        public static final int PHI = 6;

        public static final int GENES = 7;

        public static final int CHROMOSOMES = 3;

        double[][] leg = new double[CHROMOSOMES][GENES];

        double fitness;

        public DNA() {
            
            for (int c = 0; c < 3; c++) {
                leg[c][0] = 120;
                leg[c][1] = -60;
             
                leg[c][5] = 3.6875 + 1 / 128. * Math.PI;
            }
            
            //for (int c = 0; c < 3; c++) {
            /*
                for (int i = 0; i < 3; i++) {
                    double f = (int) (Math.random() * 360 * 32) / 32f;
                    leg[0][i] = f;
                    leg[1][i] = f;
                    leg[2][i] = f;
                }
                for (int i = 3; i < 6; i++) {
                    double f = ((int) (Math.random() * 256)) / 32f - 4;
                    leg[0][i] = f;
                    leg[1][i] = f;
                    leg[2][i] = f;
                }
                double f = ((int) (Math.random() * 256)) / 128f * FastMath.PI;
                leg[0][6] = f;
                leg[1][6] = f;
                leg[2][6] = f;
             */
            //}
        }

        public DNA(double[][] leg) {
            this.leg = leg;
        }

        public DNA(DNA dna0, DNA dna1) {
            for (int i = 0; i < CHROMOSOMES; i++) {
                System.arraycopy(Math.random() < 0.5 ? dna0.leg[i] : dna1.leg[i], 0, leg[i], 0, GENES);
            }
            mutate();
        }

        private void mutate() {
            for (int c = 0; c < CHROMOSOMES; c++) {
                for (int index = 0; index < GENES; index++) {
                //for (int index = 5; index < 6; index++) {
                    if (Math.random() < MUTATION_RATE) {
                        if (Math.random() < MUTATION_RATE) {    // critical mutation
                            if (index < 3) {
                                leg[c][index] = (int) (Math.random() * 360 * 32) / 32.;
                            } else if (index < 6) {
                                leg[c][index] = ((int) (Math.random() * 256)) / 32. - 4;
                            } else {
                                leg[c][index] = ((int) (Math.random() * 256)) / 128. * Math.PI;
                            }
                        } else // normal mutation
                        if (index < 3) {
                            leg[c][index] = (leg[c][index] + ((int) (Math.random() * 257) - 128) / 32.) % 360;
                        } else if (index < 6) {
                            leg[c][index] += ((int) (Math.random() * 11) - 5) / 32.;
                        } else {
                            leg[c][index] += ((int) (Math.random() * 11) - 5) / 128. * Math.PI;
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
