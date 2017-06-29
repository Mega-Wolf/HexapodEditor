package mygame;

import math.Vector3d;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class InternalMovement {
	
	/* Static Variables */
	public static final List<InternalMovement> movementList = new ArrayList<InternalMovement>();
	
	/* Consts */
	public static final String RESOURCES = /* "src/main/ */ "resources"; 
	public static final String MOVEMENTS = "movements";
	public static final String MOVEMENT_ENDING = ".move";
	
	private static String slash;
	
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
	final double endHeight;
	final double initialRotation;
	final double endRotation;
	final double[] direction = new double[2];
	
	final int frames;
	
	/**
	 * Constructor for HexapodBattle; loads a movement from a file with the given number
	 * @param movementIndex
	 * @throws IOException 
	 */
	public InternalMovement(int movementIndex) throws IOException {
		DataInputStream input = new DataInputStream(new FileInputStream(RESOURCES + slash + MOVEMENTS + slash + movementIndex + MOVEMENT_ENDING));
		
		frames = input.readInt();
		
		initialHeight = input.readDouble();
		endHeight = input.readDouble();
		initialRotation = input.readDouble();
		endRotation = input.readDouble();
		direction[0] = input.readDouble();
		direction[1] = input.readDouble();
		
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
				jointSolid[i][leg] = new Vector3d(input.readDouble(),input.readDouble(),input.readDouble());
				jointHorizontal[i][leg] = new Vector3d(input.readDouble(),input.readDouble(),input.readDouble());
				jointTop[i][leg] = new Vector3d(input.readDouble(),input.readDouble(),input.readDouble());
				jointBottom[i][leg] = new Vector3d(input.readDouble(),input.readDouble(),input.readDouble());
				
				rotHorizontal[i][leg] = input.readDouble();
				rotTop[i][leg] = input.readDouble();
				rotBottom[i][leg] = input.readDouble();
			}
			jointWeapon[i] = new Vector3d(input.readDouble(),input.readDouble(),input.readDouble());
		}
		
		input.close();		
	}
	
	
	/**
	 * Constructor for HexapodWalk, so Movements can be creates
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
	 * @param initialRotation
	 * @param endRotation
	 * @param frames
	 */
	public InternalMovement(Vector3d[][] jointSolid, Vector3d[][] jointHorizontal, Vector3d[][] jointTop,
			Vector3d[][] jointBottom, Vector3d[] jointWeapon, double[][] rotHorizontal, double[][] rotTop,
			double[][] rotBottom, double initialHeight, double endHeight, double initialRotation, double endRotation,
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
		this.endHeight = endHeight;
		this.initialRotation = initialRotation;
		this.endRotation = endRotation;
		this.frames = frames;
	}

	/**
	 * Used by HexapodWalk; creates a file with this movement
	 * @param movementIndex
	 * @throws IOException
	 */
	public void export(int movementIndex) throws IOException {
		DataOutputStream output = new DataOutputStream(new FileOutputStream(RESOURCES + slash + MOVEMENTS + slash + movementIndex + MOVEMENT_ENDING));
		
		output.writeInt(frames);
		
		output.writeDouble(initialHeight);
		output.writeDouble(endHeight);
		output.writeDouble(initialRotation);
		output.writeDouble(endRotation);
		output.writeDouble(direction[0]);
		output.writeDouble(direction[1]);
				
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
	

	static {
		slash = System.getProperty("file.separator");
		
		int count = 0;
		
		while(true) {
			
			if (new File(RESOURCES + slash + MOVEMENTS + slash + count + MOVEMENT_ENDING).isFile()) {
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

}
