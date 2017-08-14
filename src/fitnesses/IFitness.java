/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fitnesses;

import robots.AWalker;

/**
 *
 * @author Tobia
 */
public interface IFitness {
    public void calcFitnessValue(AWalker walker);
    
    public void calcFitnessValueEveryFrame(AWalker walker);
}
