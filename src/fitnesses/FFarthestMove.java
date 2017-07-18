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
public class FFarthestMove implements IFitness{

    //Good ones
    //class mygame.TripodLoopRobot:[[-0.24860197565027464, 4.989357901893359], [-2.1250333537249975, 3.0939527435880994], [2.2936719943991077, 1.450331997243784], [-0.25660505728194083, 4.9894728773165875], [-2.0013993210454073, 3.2192395957916613], [2.299338759510491, 1.4601182353575721], [0.08923182276164524], [0.039663366528570165, 4.325698746742811], [9.584423867781133E-6, 8.137821848419764]]	Fitness: 66.2241444365262
    
    @Override
    public void calcFitnessValue(AWalker walker) {
        //walker.setFitness(-walker.getDirection()[0] * walker.getDirection()[0] + walker.getDirection()[1] * walker.getDirection()[1]);
        
        walker.setFitness(Math.abs(walker.getDirection()[1]) * walker.getDirection()[1] - (walker.getDirection()[0] * walker.getDirection()[0]));
    }

    @Override
    public void calcFitnessValueEveryFrame(AWalker walker) {
        //empty
    }
    
    
}
