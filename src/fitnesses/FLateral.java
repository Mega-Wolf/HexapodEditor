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
public class FLateral implements IFitness{

    @Override
    public void calcFitnessValue(AWalker walker) {
        walker.setFitness(Math.abs(walker.getDirection()[0]) - Math.abs(walker.getDirection()[1]));
    }
    
    @Override
    public void calcFitnessValueEveryFrame(AWalker walker) {
        //empty
    }
    
    
}
