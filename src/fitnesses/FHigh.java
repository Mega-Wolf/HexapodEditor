package fitnesses;

import robots.AWalker;

public class FHigh implements IFitness {

    @Override
    public void calcFitnessValue(AWalker walker) {
        walker.setFitness(walker.getStartHeight() + walker.getDirection()[1]);
    }
    
    @Override
    public void calcFitnessValueEveryFrame(AWalker walker) {
        //empty
    }
    
    @Override
    public String getName() {
        return "Height";
    }

    @Override
    public String getDescription() {
        return "Robot body height + movement ahead";
    }
    
}
