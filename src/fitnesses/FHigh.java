/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fitnesses;

import mygame.AWalker;

/**
 *
 * @author Tobia
 */
public class FHigh implements IFitness {

    @Override
    public void calcFitnessValue(AWalker walker) {
        walker.setFitness(walker.getStartHeight() + walker.getDirection()[1]);
    }
    
    @Override
    public void calcFitnessValueEveryFrame(AWalker walker) {
        //empty
    }
    
}
