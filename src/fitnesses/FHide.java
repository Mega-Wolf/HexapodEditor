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
public class FHide implements IFitness{

    @Override
    public void calcFitnessValue(AWalker walker) {
        if (walker.getDirection()[0] != 0) {
            walker.setFitness(-5 * walker.getStartHeight() + walker.getDirection()[1]);
        }
    }
    
    @Override
    public void calcFitnessValueEveryFrame(AWalker walker) {
        //empty
    }
    
}
