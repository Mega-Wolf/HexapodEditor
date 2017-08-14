/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robots;

/**
 *
 * @author Tobia
 */
public class LearningRobot extends AWalker {

    /* Consts */
    
    // 0-5: Leg positions at t = 0
    
    public static final int C_Y_OFFSET = 6;
    public static final int C_ROTATION = 7;
    public static final int C_TRANSLATION = 8;
    
    
    
    public static final int CHROMOSOMES = 9;
    
    /* Variables */
    
    /* Constructors */

    public LearningRobot(boolean mutate) {
        super(mutate);
    }

    public LearningRobot(AWalker g0, AWalker g1) {
        super(g0, g1);
    }
    
    public LearningRobot(double chromosomes[][]) {
        this.chromosomes = chromosomes;
    }
    
    /* Methods */
    
    /* Overrides */
    
    @Override
    protected void mutateCritical(int c, int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void mutateNormal(int c, int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRotation(double t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AWalker newInstance() {
        return new LearningRobot();
    }

    @Override
    public AWalker newInstance(AWalker parent0, AWalker parent1) {
        return new LearningRobot(parent0, parent1);
    }

    @Override
    protected void setupChromosomes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getStartHeight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double[] getDirection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double[] getStartRotation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getRotationAngle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
