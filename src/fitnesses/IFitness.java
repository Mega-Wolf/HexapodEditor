package fitnesses;

import robots.AWalker;

/**
 * Responsible for the specific fitness checks for an AWalker
 * @author Tobias
 */
public interface IFitness {
    
    /* Methods */
    
    /**
     * Called once at the end of a fitness check. Write to walker.setFitness
     * @param walker The robot to check
     */
    public void calcFitnessValue(AWalker walker);
    
    /**
     * Called every frame during the fitness test. Write to Walker.setExtraFitness
     * @param walker  The robot to check
     */
    public void calcFitnessValueEveryFrame(AWalker walker);
    
    /* Getter */
    
    /**
     * Gets the name of the fitness function
     * @return the name of this fitness function
     */
    public String getName();
    
    /**
     * Gets a desciption of the fitness function
     * @return a description of this fitness function
     */
    public String getDescription();
}
