package fitnesses;

import robots.AWalker;

public class FFlatRotate implements IFitness{

    @Override
    public void calcFitnessValue(AWalker walker) {
        walker.setFitness(Math.abs(walker.getRotationAngle()) - Math.max(walker.getExtraFitness(), walker.getStartHeight()));
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
        return "Flat Rotation";
    }

    @Override
    public String getDescription() {
        return "Rotation around y-axis (rad) -  3 * highest point of robot in the cycle";
    }
    
}
