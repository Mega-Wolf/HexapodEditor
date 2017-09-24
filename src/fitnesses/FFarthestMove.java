package fitnesses;

import robots.AWalker;

public class FFarthestMove implements IFitness{
    
    @Override
    public void calcFitnessValue(AWalker walker) {
        //walker.setFitness(-walker.getDirection()[0] * walker.getDirection()[0] + walker.getDirection()[1] * walker.getDirection()[1]);
        
        walker.setFitness(walker.getDirection()[1] * walker.getDirection()[1] + (walker.getDirection()[0] * walker.getDirection()[0]));
    }

    @Override
    public void calcFitnessValueEveryFrame(AWalker walker) {
        //empty
    }
    
    @Override
    public String getName() {
        return "Farthest Movement";
    }

    @Override
    public String getDescription() {
        return "Square of distance";
    }
    
}
