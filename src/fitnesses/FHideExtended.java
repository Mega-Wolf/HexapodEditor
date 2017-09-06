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
public class FHideExtended implements IFitness{

    @Override
    public void calcFitnessValue(AWalker walker) {
        if (walker.getDirection()[1] != 0) {
            //walker.setFitness(-walker.getExtraFitness() + 1000 * 30 * (5 * walker.getHeight() + walker.getDirection()[1]));
            //walker.setFitness(walker.getExtraFitness() + 10 * (- walker.getHeight() + 2.5* Math.abs(walker.getDirection()[1])));
            
            walker.setFitness(3 - (Math.max(walker.getExtraFitness(), walker.getStartHeight() )) /*+ walker.getDirection()[1] / 8*/);
        }
    }
    
    @Override
    public void calcFitnessValueEveryFrame(AWalker walker) {
        for (int leg = 0; leg < 6; leg++) {
            double height = walker.getPosTop()[leg].y;
            
            if (height > walker.getExtraFitness()) {
                walker.setExtraFitness(height);
            }
            
            //walker.setExtraFitness(walker.getExtraFitness() + (3 - walker.getPosTop()[leg].y));
        }
    }
    
    @Override
    public String getName() {
        return "Extended Hide";
    }

    @Override
    public String getDescription() {
        return "3 - highest point in cycle";
    }
}
