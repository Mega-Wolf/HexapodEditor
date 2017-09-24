package fitnesses;

import robots.AWalker;

public class FRotate implements IFitness{

    @Override
    public void calcFitnessValue(AWalker walker) {
        walker.setFitness(Math.abs(walker.getRotationAngle()));
    }

    @Override
    public void calcFitnessValueEveryFrame(AWalker walker) {
        //empty
    }

    @Override
    public String getName() {
        return "Rotation";
    }

    @Override
    public String getDescription() {
        return "Rotation around the y-axis (rad)";
    }

}
