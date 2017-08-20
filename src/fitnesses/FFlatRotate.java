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
public class FFlatRotate implements IFitness{

    @Override
    public void calcFitnessValue(AWalker walker) {
        walker.setFitness(Math.abs(walker.getRotationAngle()) - 3 * Math.max(walker.getExtraFitness(), walker.getStartHeight()));
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
    
}
