/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

/**
 *
 * @author Tobias
 */
public class Movement {
    
    /* Consts */
    public static final int FPS = 30; 
    
    /* final Variables */
    private final double[][] s;
    private final double[][] v;
    private final double[][] a;
    
    private final int lengthFrames;
    private final double lengthTime;
    
    
    /* Constructor */
    public Movement(double[][] s, double[][] v, double[][] a) {
        this.s = s;
        this.v = v;
        this.a = a;
        
        lengthFrames = s.length;
        lengthTime = lengthFrames / (double) FPS;
    }
    
    
}
