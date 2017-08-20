/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visuals;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

/**
 *
 * @author Tobias
 */
public class Hexagon {
/**
     * Creates a hexagon geometry
     *
     * @param a side of the hexagon
     * @param h height of the mesh
     * @return hexagon Geometry with already set material
     */
    public static Geometry create3DHexagon(float a, float h) {
        //TODO; lots of improvments possible; use less triangles

        //IMP: Do this once and not every time
        Mesh hexagon = new Mesh();
        Vector3f[] vertices = new Vector3f[14];
        vertices[0] = new Vector3f(0, 0.5f * h, 0);
        vertices[1] = new Vector3f(0, 0.5f * h, a * 1);
        vertices[2] = new Vector3f(a * FastMath.sqrt(3) / 2f, 0.5f * h, a * 0.5f);
        vertices[3] = new Vector3f(a * FastMath.sqrt(3) / 2f, 0.5f * h, a * -0.5f);
        vertices[4] = new Vector3f(0, 0.5f * h,a * -1);
        vertices[5] = new Vector3f(a * -FastMath.sqrt(3) / 2f, 0.5f * h, a * -0.5f);
        vertices[6] = new Vector3f(a * -FastMath.sqrt(3) / 2f, 0.5f * h, a * 0.5f);
        vertices[7] = new Vector3f(0, -0.5f * h, 0);
        vertices[8] = new Vector3f(0, -0.5f * h, a * 1);
        vertices[9] = new Vector3f(a * FastMath.sqrt(3) / 2f, -0.5f * h, a * 0.5f);
        vertices[10] = new Vector3f(a * FastMath.sqrt(3) / 2f, -0.5f * h, a * -0.5f);
        vertices[11] = new Vector3f(0, -0.5f * h,a * -1);
        vertices[12] = new Vector3f(a * -FastMath.sqrt(3) / 2f, -0.5f * h, a * -0.5f);
        vertices[13] = new Vector3f(a * -FastMath.sqrt(3) / 2f, -0.5f * h, a * 0.5f);

        /*
        Vector2f[] texCoord = new Vector2f[7];
        texCoord[0] = new Vector2f(0.5f, 0.5f);
        texCoord[1] = new Vector2f(0.5f, 0);
        texCoord[2] = new Vector2f((2 - FastMath.sqrt(3)) / 2f, 0.25f);
        texCoord[3] = new Vector2f((2 - FastMath.sqrt(3)) / 2f, 0.75f);
        texCoord[4] = new Vector2f(0.5f, 1);
        texCoord[5] = new Vector2f(1 - (2 - FastMath.sqrt(3)) / 2f, 0.75f);
        texCoord[6] = new Vector2f(1 - (2 - FastMath.sqrt(3)) / 2f, 0.25f);
        */

        //IMP: 4 triangles instead of 6
        int[] indexes = {0, 1, 2, 0, 2, 3, 0, 3, 4, 0, 4, 5, 0, 5, 6, 0, 6, 1,
                         7, 9, 8, 7, 10,9, 7,11,10, 7,12,11, 7,13,12, 7, 8,13,
                         1, 8, 2, 9, 2, 8, 2, 9, 3,10, 3, 9, 3,10, 4,11, 4,10,
                         4,11, 5,12, 5,11, 5,12, 6,13, 6,12, 6,13, 1, 8, 1,13};

        hexagon.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        //hexagon.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
        hexagon.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indexes));
        hexagon.updateBound();

        Geometry hexGeo = new Geometry("Hex", hexagon);

        //IMP: Is this neccessary
        //IMP: hardcoded path; CONST would be better
        //Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //mat.setTexture("ColorMap", assetManager.loadTexture(Loader.loadTexture("Ground", "Forrest", Color.GREEN, 0)));
        //hexGeo.setMaterial(mat);

        return hexGeo;
    }    
}
