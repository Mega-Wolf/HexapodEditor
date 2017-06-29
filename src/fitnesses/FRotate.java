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
public class FRotate implements IFitness{

    @Override
    public void calcFitnessValue(AWalker walker) {
        walker.setFitness(Math.abs(walker.getRotationAngle()));
    }

    @Override
    public void calcFitnessValueEveryFrame(AWalker walker) {
        //empty
    }
    
}
