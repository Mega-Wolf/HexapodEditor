package fitnesses;

import robots.AWalker;

public class FLateral implements IFitness{

    @Override
    public void calcFitnessValue(AWalker walker) {
        walker.setFitness(Math.abs(walker.getDirection()[0]) - Math.abs(walker.getDirection()[1]));
    }
    
    @Override
    public void calcFitnessValueEveryFrame(AWalker walker) {
        //empty
    }

    @Override
    public String getName() {
        return "Lateral";
    }

    @Override
    public String getDescription() {
        return "Lateral movement - main axis movement";
    }
    
}
