package mygame;

import robots.AWalker;
import fitnesses.IFitness;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Holds a group of AWalkers (which must be all of the same subclass) and manages the fitness checks and breeding selection.
 * Started by the Output window on a separate thread. Additionally saves the best robot after every generation
 * so that the fitness value of those can be shown on the Output thread and the main thread can show the best one in action.
 * @author Tobias
 */
public class Population {
    
    /* Variables */
    private final List<AWalker> bestRobots; // = Collections.synchronizedList(new ArrayList<>());
    private final IFitness fitness;
    private final AWalker luca;
    
    private boolean running = true;
    
    private int loop;
    
    /* Constructor */
    
    /**
     * Creates a new population
     * @param luca a blueprint for the type of robot: https://en.wikipedia.org/wiki/Last_universal_common_ancestor
     * @param fitness the fitness function which is used in the specific fitness tests
     * @param bestRobots the list which holds the best robot of every generation; passed as a parameter so that older lists can be continued and thread-safe lists can be used
     */
    public Population(AWalker luca, IFitness fitness, List<AWalker> bestRobots) {
        this.luca = luca;
        this.fitness = fitness;
        this.bestRobots = bestRobots;
    }
    
    /**
     * Runs the genetic algorithm until {@link stopSimulation} is called
     */
    public void testGA() {
        loop = bestRobots.size();
        
        running = true;
        //bestRobots.clear();

        int POPULATION_SIZE = 128;

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
        
        AWalker population[] = new AWalker[POPULATION_SIZE];
        AWalker populationDummy[] = new AWalker[POPULATION_SIZE];
        for (int i = 0; i < POPULATION_SIZE; i++) {
            
            if (loop == 0) {
                populationDummy[i] = luca.newInstance();
            } else {
                populationDummy[i] = luca.newInstance(luca.getDNA(), luca.getShallMutate());
            }
            
        }

        while (running) {
            System.arraycopy(populationDummy, 0, population, 0, POPULATION_SIZE);

            for (int i = 0; i < POPULATION_SIZE; i++) {
                if (population[i].initialFitnessChecks(fitness) == 0) {     // setzt hier intern schon nen neuen Fitness wert
                    fitness.calcFitnessValue(population[i]);                // hier auch XD
                }
            }

            // shuffles the AWalkers in the population so that really no one has an advantage/disadvantage due to Java's stabil sort
            // in fact this is not needed, because the order before is random as well
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
            
            
            //Comment this in for the parameter comparison
            /*
            if (loop == 500) {
                break;
            }
            */
            
            /*
            if (loop % 10 == 0) {
                //System.out.println(lowestFail + "," + population[0].getFitness() + "," + population[POPULATION_SIZE - 1].getFitness());
                System.out.println(population[0].getFitness());    
            }
            */
            
            /*
            
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
            */
            loop++;        
        }
    }
    
    /**
     * Shuffles an array of objects
     * @param array the array which shall be shuffled
     */
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
    
    /**
     * A shortcut to get the best robot of the latest generation
     * @return the best robot of the latest generation; {@code null} if there is none yet
     */
    public AWalker getLast() {
        if (bestRobots.isEmpty()) {
            return null;
        }
        return bestRobots.get(bestRobots.size() - 1);
    }
    
    /**
     * Returns a list with the best robot of every generation
     * @return A list with the best robot of every generation
     */
    public List<AWalker> getBestRobots() {
        return bestRobots;
    }

    /**
     * Tells the population to stop the simulation after the curretn simulation
     */
    public void stopSim() {
        running = false;
    }
}
