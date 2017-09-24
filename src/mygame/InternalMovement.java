package mygame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import math.Vector3d;
import robots.AWalker;

/**
 * Holds all the information of a movement.
 * Additional to the rotations, the positions are already pre-calculated to speed up the simulations.
 * Needed by Hexapod Battle. Not important for Hexapod Walk itself.
 *
 * @author Tobias
 *
 */
public class InternalMovement {

    /* Static Variables */
    public static final List<InternalMovement> movementList = new ArrayList<InternalMovement>();

    /* Consts */
    public static final String MOVEMENTS = "movements";
    public static final String MOVEMENT_ENDING = ".move";

    private static String slash;

    /* Static Constructor */
    /**
     * Creates a list with all possible movements
     */
    static {
        slash = System.getProperty("file.separator");

        int count = 0;

        while (true) {

            if (new File(MOVEMENTS + slash + count + MOVEMENT_ENDING).isFile()) {
                try {
                    movementList.add(new InternalMovement(count));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            } else {
                break;
            }

            count++;
        }
    }

    /* Final Variables */
    final Vector3d[][] jointSolid;
    final Vector3d[][] jointHorizontal;
    final Vector3d[][] jointTop;
    final Vector3d[][] jointBottom;
    final Vector3d[] jointWeapon;

    final double[][] rotHorizontal;
    final double[][] rotTop;
    final double[][] rotBottom;

    final double initialHeight;
    final double changeHeight;
    final double changeRotation;
    final double[] direction;// = new double[2];

    final double[] startRotation;// = new double[2];

    final int frames;

    /**
     * Constructor for HexapodBattle; loads a movement from a file with the
     * given number
     *
     * @param movementIndex
     * @throws IOException
     */
    public InternalMovement(int movementIndex) throws IOException {
        DataInputStream input = new DataInputStream(new FileInputStream(MOVEMENTS + slash + movementIndex + MOVEMENT_ENDING));

        frames = input.readInt();

        initialHeight = input.readDouble();
        changeHeight = input.readDouble();
        changeRotation = input.readDouble();

        direction = new double[2];
        direction[0] = input.readDouble();
        direction[1] = input.readDouble();

        startRotation = new double[2];
        startRotation[0] = input.readDouble();
        startRotation[1] = input.readDouble();

        jointSolid = new Vector3d[frames][6];
        jointHorizontal = new Vector3d[frames][6];
        jointTop = new Vector3d[frames][6];
        jointBottom = new Vector3d[frames][6];

        rotHorizontal = new double[frames][6];
        rotTop = new double[frames][6];
        rotBottom = new double[frames][6];

        jointWeapon = new Vector3d[frames];

        for (int i = 0; i < frames; i++) {
            for (int leg = 0; leg < 6; leg++) {
                jointSolid[i][leg] = new Vector3d(input.readDouble(), input.readDouble(), input.readDouble());
                jointHorizontal[i][leg] = new Vector3d(input.readDouble(), input.readDouble(), input.readDouble());
                jointTop[i][leg] = new Vector3d(input.readDouble(), input.readDouble(), input.readDouble());
                jointBottom[i][leg] = new Vector3d(input.readDouble(), input.readDouble(), input.readDouble());

                rotHorizontal[i][leg] = input.readDouble();
                rotTop[i][leg] = input.readDouble();
                rotBottom[i][leg] = input.readDouble();
            }
            jointWeapon[i] = new Vector3d(input.readDouble(), input.readDouble(), input.readDouble());
        }

        input.close();
    }

    /**
     * Constructor for HexapodWalk, so Movements can be created
     *
     * @param jointSolid
     * @param jointHorizontal
     * @param jointTop
     * @param jointBottom
     * @param jointWeapon
     * @param rotHorizontal
     * @param rotTop
     * @param rotBottom
     * @param initialHeight
     * @param endHeight
     * @param changeRotation
     * @param direction
     * @param startRotation
     * @param frames
     */
    public InternalMovement(Vector3d[][] jointSolid, Vector3d[][] jointHorizontal, Vector3d[][] jointTop,
            Vector3d[][] jointBottom, Vector3d[] jointWeapon, double[][] rotHorizontal, double[][] rotTop,
            double[][] rotBottom, double initialHeight, double endHeight, double changeRotation,
            double[] direction, double[] startRotation,
            int frames) {
        this.jointSolid = jointSolid;
        this.jointHorizontal = jointHorizontal;
        this.jointTop = jointTop;
        this.jointBottom = jointBottom;
        this.jointWeapon = jointWeapon;
        this.rotHorizontal = rotHorizontal;
        this.rotTop = rotTop;
        this.rotBottom = rotBottom;
        this.initialHeight = initialHeight;
        this.changeHeight = endHeight;
        this.changeRotation = changeRotation;
        this.direction = direction;
        this.startRotation = startRotation;
        this.frames = frames;
    }

    public InternalMovement(AWalker walker) {
        frames = 30;

        jointSolid = new Vector3d[frames][6];

        jointHorizontal = new Vector3d[frames][6];
        jointTop = new Vector3d[frames][6];
        jointBottom = new Vector3d[frames][6];
        jointWeapon = new Vector3d[frames];

        rotHorizontal = new double[frames][6];
        rotTop = new double[frames][6];
        rotBottom = new double[frames][6];

        for (int i = 0; i < frames; i++) {
            walker.setRotation(i / 30.0);
            walker.calcPositions();

            for (int leg = 0; leg < 6; leg++) {
                jointSolid[i][leg] = walker.getPosSolid()[leg];
                jointHorizontal[i][leg] = walker.getPosHorizontal()[leg];
                jointTop[i][leg] = walker.getPosTop()[leg];
                jointBottom[i][leg] = walker.getPosBottom()[leg];

                rotHorizontal[i][leg] = walker.getRotHorizontal()[leg];
                rotTop[i][leg] = walker.getRotTop()[leg];
                rotBottom[i][leg] = walker.getRotBottom()[leg];
            }

            jointWeapon[i] = (jointSolid[i][0].add(jointSolid[i][5])).divide(2).normalize().mult(AWalker.A);

        }

        initialHeight = walker.getStartHeight();
        changeHeight = 0;
        changeRotation = walker.getRotationAngle();

        direction = walker.getDirection();
        startRotation = walker.getStartRotation();
    }

    /**
     * Used by HexapodWalk; creates a file with this movement
     *
     * @param movementIndex
     * @throws IOException
     */
    public void export(int movementIndex) throws IOException {
        DataOutputStream output = new DataOutputStream(new FileOutputStream(MOVEMENTS + slash + movementIndex + MOVEMENT_ENDING));

        output.writeInt(frames);

        output.writeDouble(initialHeight);
        output.writeDouble(changeHeight);
        output.writeDouble(changeRotation);
        output.writeDouble(direction[0]);
        output.writeDouble(direction[1]);

        output.writeDouble(startRotation[0]);
        output.writeDouble(startRotation[1]);

        for (int i = 0; i < frames; i++) {
            for (int leg = 0; leg < 6; leg++) {
                output.writeDouble(jointSolid[i][leg].x);
                output.writeDouble(jointSolid[i][leg].y);
                output.writeDouble(jointSolid[i][leg].z);

                output.writeDouble(jointHorizontal[i][leg].x);
                output.writeDouble(jointHorizontal[i][leg].y);
                output.writeDouble(jointHorizontal[i][leg].z);

                output.writeDouble(jointTop[i][leg].x);
                output.writeDouble(jointTop[i][leg].y);
                output.writeDouble(jointTop[i][leg].z);

                output.writeDouble(jointBottom[i][leg].x);
                output.writeDouble(jointBottom[i][leg].y);
                output.writeDouble(jointBottom[i][leg].z);

                output.writeDouble(rotHorizontal[i][leg]);
                output.writeDouble(rotTop[i][leg]);
                output.writeDouble(rotBottom[i][leg]);
            }
            output.writeDouble(jointWeapon[i].x);
            output.writeDouble(jointWeapon[i].y);
            output.writeDouble(jointWeapon[i].z);
        }

        output.close();
    }

    /**
     * Checks if an InternalMovement can be applied after the current one
     *
     * @param nm the new movement
     * @return {@code true} if the new InternalMovement can be started after the
     * current one; {@code false} otherwise
     */
    public boolean compatible(InternalMovement nm) {
        //TODO; Tobi: At the moment only works with loopable movements

        if (initialHeight + changeHeight != nm.initialHeight) {
            return false;
        }

        for (int i = 0; i < 6; i++) {
            if (rotHorizontal[i][0] != nm.rotHorizontal[i][0]) {
                return false;
            }
            if (rotTop[i][0] != nm.rotTop[i][0]) {
                return false;
            }
            if (rotBottom[i][0] != nm.rotBottom[i][0]) {
                return false;
            }
        }

        return true;
    }

}
