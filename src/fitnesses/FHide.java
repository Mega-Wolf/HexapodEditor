package fitnesses;

import robots.AWalker;

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
    
    @Override
    public String getName() {
        return "Hiding";
    }

    @Override
    public String getDescription() {
        return "-5 * body height + movement ahead";
    }
}
