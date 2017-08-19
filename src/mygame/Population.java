/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import robots.AWalker;
import fitnesses.IFitness;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Holds GeneticRobots
 * @author Tobia
 */
public class Population {
    
    /* Variables */
    
    private final List<AWalker> bestRobots = Collections.synchronizedList(new ArrayList<>());
    private final IFitness fitness;
    private final AWalker luca;
    
    /* Constructor */
    public Population(AWalker luca, IFitness fitness) {
        this.luca = luca;
        this.fitness = fitness;
    }
    
    public void testGA() {

        System.out.println("Start NEW");

        
        bestRobots.clear();

        int POPULATION_SIZE = 128;

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

        
        
        AWalker population[] = new AWalker[POPULATION_SIZE];
        AWalker populationDummy[] = new AWalker[POPULATION_SIZE];
        for (int i = 0; i < POPULATION_SIZE; i++) {
            populationDummy[i] = luca.newInstance();
        }

        while (running) {
            System.arraycopy(populationDummy, 0, population, 0, POPULATION_SIZE);

            for (int i = 0; i < POPULATION_SIZE; i++) {
                if (population[i].initialFitnessChecks(fitness) == 0) {     // setzt hier intern schon nen neuen Fitness wert
                    fitness.calcFitnessValue(population[i]);                // hier auch XD
                }
            }

            shuffleArray(population);
            Arrays.sort(population, (e1, e2) -> Double.compare(e2.getFitness(), e1.getFitness()));

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
                populationDummy[i] = luca.newInstance(population[i1], population[i2]);
            }

            if (population[0].getFitness() > lowestFail) {
                lowestFail = population[0].getFitness();
            }

            bestRobots.add(population[0]);
            
            /*
            if (loop % 10 == 0) {
                //System.out.println(lowestFail + "," + population[0].getFitness() + "," + population[POPULATION_SIZE - 1].getFitness());
                System.out.println(population[0].getFitness());    
            }
            */
            
            if (loop > 1500) {
                break;
            }

            if (loop > 10_000_000) {
                System.out.println("Finished, round: " + loop);
                //System.out.println("Loop: " + loop + ", Found: " + found + ", best Fail: " + lowestFail);
                System.out.println("Best so far: " + lowestFail);
                System.out.println(population[0]);
                //System.exit(0);

                //running = true;
                //finishedGR = population[0];

                break;

            }

            if (loop % 100 == 0) {
                //AWalker.MUTATION_RATE = Math.sin(2. * Math.PI * loop / 5000.) * 0.2 + 0.2;
                //System.out.println(AWalker.MUTATION_RATE);
                
                
                //System.out.println("Loop: " + loop + ", Found: " + found + ", best Fail: " + lowestFail);
                //System.out.println("Best so far: " + lowestFail);
                //System.out.println(population[0]);
                //System.out.println();
                
            }
            
            loop++;
        }
    }
    
    private static void shuffleArray(Object[] array) {
        int index;
        Object temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
    
    public AWalker getBest() {
        if (bestRobots.isEmpty()) {
            return null;
        }
        return bestRobots.get(bestRobots.size() - 1);
    }
    
    public List<AWalker> getBestRobots() {
        return bestRobots;
    }
}
