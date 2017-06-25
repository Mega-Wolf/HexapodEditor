/*
 * Copyright (c) 2009-2012 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package mygame;

import com.jme3.export.*;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.logging.Logger;

/**
 * <code>Quaternion</code> defines a single example of a more general class of
 * hypercomplex numbers. Quaternions extends a rotation in three dimensions to a
 * rotation in four dimensions. This avoids "gimbal lock" and allows for smooth
 * continuous rotation.
 * 
 * <code>Quaternion</code> is defined by four doubleing point numbers: {x y z
 * w}.
 * 
 * @author Mark Powell
 * @author Joshua Slack
 */
public final class Quaternion implements Savable, Cloneable, java.io.Serializable {

    static final long serialVersionUID = 1;

    private static final Logger logger = Logger.getLogger(Quaternion.class.getName());
    /**
     * Represents the identity quaternion rotation (0, 0, 0, 1).
     */
    public static final Quaternion IDENTITY = new Quaternion();
    public static final Quaternion DIRECTION_Z = new Quaternion();
    public static final Quaternion ZERO = new Quaternion(0, 0, 0, 0);
    
    static {
        DIRECTION_Z.fromAxes(Vector3d.UNIT_X, Vector3d.UNIT_Y, Vector3d.UNIT_Z);
    }
    protected double x, y, z, w;

    /**
     * Constructor instantiates a new <code>Quaternion</code> object
     * initializing all values to zero, except w which is initialized to 1.
     *
     */
    public Quaternion() {
        x = 0;
        y = 0;
        z = 0;
        w = 1;
    }

    /**
     * Constructor instantiates a new <code>Quaternion</code> object from the
     * given list of parameters.
     *
     * @param x
     *            the x value of the quaternion.
     * @param y
     *            the y value of the quaternion.
     * @param z
     *            the z value of the quaternion.
     * @param w
     *            the w value of the quaternion.
     */
    public Quaternion(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getW() {
        return w;
    }

    /**
     * sets the data in a <code>Quaternion</code> object from the given list
     * of parameters.
     *
     * @param x
     *            the x value of the quaternion.
     * @param y
     *            the y value of the quaternion.
     * @param z
     *            the z value of the quaternion.
     * @param w
     *            the w value of the quaternion.
     * @return this
     */
    public Quaternion set(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    /**
     * Sets the data in this <code>Quaternion</code> object to be equal to the
     * passed <code>Quaternion</code> object. The values are copied producing
     * a new object.
     *
     * @param q
     *            The Quaternion to copy values from.
     * @return this
     */
    public Quaternion set(Quaternion q) {
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
        this.w = q.w;
        return this;
    }

    /**
     * Constructor instantiates a new <code>Quaternion</code> object from a
     * collection of rotation angles.
     *
     * @param angles
     *            the angles of rotation (x, y, z) that will define the
     *            <code>Quaternion</code>.
     */
    public Quaternion(double[] angles) {
        fromAngles(angles);
    }

    /**
     * Constructor instantiates a new <code>Quaternion</code> object from an
     * interpolation between two other quaternions.
     *
     * @param q1
     *            the first quaternion.
     * @param q2
     *            the second quaternion.
     * @param interp
     *            the amount to interpolate between the two quaternions.
     */
    public Quaternion(Quaternion q1, Quaternion q2, double interp) {
        slerp(q1, q2, interp);
    }

    /**
     * Constructor instantiates a new <code>Quaternion</code> object from an
     * existing quaternion, creating a copy.
     *
     * @param q
     *            the quaternion to copy.
     */
    public Quaternion(Quaternion q) {
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
        this.w = q.w;
    }

    /**
     * Sets this Quaternion to {0, 0, 0, 1}.  Same as calling set(0,0,0,1).
     */
    public void loadIdentity() {
        x = y = z = 0;
        w = 1;
    }

    /**
     * @return true if this Quaternion is {0,0,0,1}
     */
    public boolean isIdentity() {
        if (x == 0 && y == 0 && z == 0 && w == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <code>fromAngles</code> builds a quaternion from the Euler rotation
     * angles (y,r,p).
     *
     * @param angles
     *            the Euler angles of rotation (in radians).
     */
    public Quaternion fromAngles(double[] angles) {
        if (angles.length != 3) {
            throw new IllegalArgumentException(
                    "Angles array must have three elements");
        }

        return fromAngles(angles[0], angles[1], angles[2]);
    }

    /**
     * <code>fromAngles</code> builds a Quaternion from the Euler rotation
     * angles (x,y,z) aka (pitch, yaw, rall)). Note that we are applying in order: (y, z, x) aka (yaw, roll, pitch) but
     * we've ordered them in x, y, and z for convenience.
     * @see <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm">http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm</a>
     * 
     * @param xAngle
     *            the Euler pitch of rotation (in radians). (aka Attitude, often rot
     *            around x)
     * @param yAngle
     *            the Euler yaw of rotation (in radians). (aka Heading, often
     *            rot around y)
     * @param zAngle
     *            the Euler roll of rotation (in radians). (aka Bank, often
     *            rot around z)
     */
    public Quaternion fromAngles(double xAngle, double yAngle, double zAngle) {
        double angle;
        double sinY, sinZ, sinX, cosY, cosZ, cosX;
        angle = zAngle * 0.5;
        sinZ = Math.sin(angle);
        cosZ = Math.cos(angle);
        angle = yAngle * 0.5;
        sinY = Math.sin(angle);
        cosY = Math.cos(angle);
        angle = xAngle * 0.5;
        sinX = Math.sin(angle);
        cosX = Math.cos(angle);

        // variables used to reduce multiplication calls.
        double cosYXcosZ = cosY * cosZ;
        double sinYXsinZ = sinY * sinZ;
        double cosYXsinZ = cosY * sinZ;
        double sinYXcosZ = sinY * cosZ;

        w = (cosYXcosZ * cosX - sinYXsinZ * sinX);
        x = (cosYXcosZ * sinX + sinYXsinZ * cosX);
        y = (sinYXcosZ * cosX + cosYXsinZ * sinX);
        z = (cosYXsinZ * cosX - sinYXcosZ * sinX);

        normalizeLocal();
        return this;
    }

    /**
     * <code>toAngles</code> returns this quaternion converted to Euler
     * rotation angles (yaw,roll,pitch).<br/>
     * Note that the result is not always 100% accurate due to the implications of euler angles.
     * @see <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm">http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm</a>
     * 
     * @param angles
     *            the double[] in which the angles should be stored, or null if
     *            you want a new double[] to be created
     * @return the double[] in which the angles are stored.
     */
    public double[] toAngles(double[] angles) {
        if (angles == null) {
            angles = new double[3];
        } else if (angles.length != 3) {
            throw new IllegalArgumentException("Angles array must have three elements");
        }

        double sqw = w * w;
        double sqx = x * x;
        double sqy = y * y;
        double sqz = z * z;
        double unit = sqx + sqy + sqz + sqw; // if normalized is one, otherwise
        // is correction factor
        double test = x * y + z * w;
        if (test > 0.499 * unit) { // singularity at north pole
            angles[1] = 2 * Math.atan2(x, w);
            angles[2] = Math.PI / 2;
            angles[0] = 0;
        } else if (test < -0.499 * unit) { // singularity at south pole
            angles[1] = -2 * Math.atan2(x, w);
            angles[2] = -Math.PI / 2;
            angles[0] = 0;
        } else {
            angles[1] = Math.atan2(2 * y * w - 2 * x * z, sqx - sqy - sqz + sqw); // roll or heading 
            angles[2] = Math.asin(2 * test / unit); // pitch or attitude
            angles[0] = Math.atan2(2 * x * w - 2 * y * z, -sqx + sqy - sqz + sqw); // yaw or bank
        }
        return angles;
    }

    public Quaternion fromRotationMatrix(double m00, double m01, double m02,
            double m10, double m11, double m12, double m20, double m21, double m22) {
        
        System.out.println("FromRotationmatrix");
        
        // first normalize the forward (F), up (U) and side (S) vectors of the rotation matrix
        // so that the scale does not affect the rotation
        double lengthSquared = m00 * m00 + m10 * m10 + m20 * m20;
        if (lengthSquared != 1.0 && lengthSquared != 0.0) {
            lengthSquared = 1.0 / Math.sqrt(lengthSquared);
            m00 *= lengthSquared;
            m10 *= lengthSquared;
            m20 *= lengthSquared;
        }
        lengthSquared = m01 * m01 + m11 * m11 + m21 * m21;
        if (lengthSquared != 1.0 && lengthSquared != 0.0) {
            lengthSquared = 1.0 / Math.sqrt(lengthSquared);
            m01 *= lengthSquared;
            m11 *= lengthSquared;
            m21 *= lengthSquared;
        }
        lengthSquared = m02 * m02 + m12 * m12 + m22 * m22;
        if (lengthSquared != 1.0 && lengthSquared != 0.0) {
            lengthSquared = 1.0 / Math.sqrt(lengthSquared);
            m02 *= lengthSquared;
            m12 *= lengthSquared;
            m22 *= lengthSquared;
        }

        // Use the Graphics Gems code, from 
        // ftp://ftp.cis.upenn.edu/pub/graphics/shoemake/quatut.ps.Z
        // *NOT* the "Matrix and Quaternions FAQ", which has errors!

        // the trace is the sum of the diagonal elements; see
        // http://mathworld.wolfram.com/MatrixTrace.html
        double t = m00 + m11 + m22;

        // we protect the division by s by ensuring that s>=1
        if (t >= 0) { // |w| >= .5
            double s = Math.sqrt(t + 1); // |s|>=1 ...
            w = 0.5 * s;
            s = 0.5 / s;                 // so this division isn't bad
            x = (m21 - m12) * s;
            y = (m02 - m20) * s;
            z = (m10 - m01) * s;
        } else if ((m00 > m11) && (m00 > m22)) {
            double s = Math.sqrt(1.0 + m00 - m11 - m22); // |s|>=1
            x = s * 0.5; // |x| >= .5
            s = 0.5 / s;
            y = (m10 + m01) * s;
            z = (m02 + m20) * s;
            w = (m21 - m12) * s;
        } else if (m11 > m22) {
            double s = Math.sqrt(1.0 + m11 - m00 - m22); // |s|>=1
            y = s * 0.5; // |y| >= .5
            s = 0.5 / s;
            x = (m10 + m01) * s;
            z = (m21 + m12) * s;
            w = (m02 - m20) * s;
        } else {
            double s = Math.sqrt(1.0 + m22 - m00 - m11); // |s|>=1
            z = s * 0.5; // |z| >= .5
            s = 0.5 / s;
            x = (m02 + m20) * s;
            y = (m21 + m12) * s;
            w = (m10 - m01) * s;
        }

        return this;
    }

    /**
     * <code>getRotationColumn</code> returns one of three columns specified
     * by the parameter. This column is returned as a <code>Vector3d</code>
     * object.
     *
     * @param i
     *            the column to retrieve. Must be between 0 and 2.
     * @return the column specified by the index.
     */
    public Vector3d getRotationColumn(int i) {
        return getRotationColumn(i, null);
    }

    /**
     * <code>getRotationColumn</code> returns one of three columns specified
     * by the parameter. This column is returned as a <code>Vector3d</code>
     * object. The value is retrieved as if this quaternion was first normalized.
     *
     * @param i
     *            the column to retrieve. Must be between 0 and 2.
     * @param store
     *            the vector object to store the result in. if null, a new one
     *            is created.
     * @return the column specified by the index.
     */
    public Vector3d getRotationColumn(int i, Vector3d store) {
        if (store == null) {
            store = new Vector3d();
        }

        double norm = norm();
        if (norm != 1.0) {
            norm = (1.0 / Math.sqrt(norm));
        }

        double xx = x * x * norm;
        double xy = x * y * norm;
        double xz = x * z * norm;
        double xw = x * w * norm;
        double yy = y * y * norm;
        double yz = y * z * norm;
        double yw = y * w * norm;
        double zz = z * z * norm;
        double zw = z * w * norm;

        switch (i) {
            case 0:
                store.x = 1 - 2 * (yy + zz);
                store.y = 2 * (xy + zw);
                store.z = 2 * (xz - yw);
                break;
            case 1:
                store.x = 2 * (xy - zw);
                store.y = 1 - 2 * (xx + zz);
                store.z = 2 * (yz + xw);
                break;
            case 2:
                store.x = 2 * (xz + yw);
                store.y = 2 * (yz - xw);
                store.z = 1 - 2 * (xx + yy);
                break;
            default:
                logger.warning("Invalid column index.");
                throw new IllegalArgumentException("Invalid column index. " + i);
        }

        return store;
    }

    /**
     * <code>fromAngleAxis</code> sets this quaternion to the values specified
     * by an angle and an axis of rotation. This method creates an object, so
     * use fromAngleNormalAxis if your axis is already normalized.
     *
     * @param angle
     *            the angle to rotate (in radians).
     * @param axis
     *            the axis of rotation.
     * @return this quaternion
     */
    public Quaternion fromAngleAxis(double angle, Vector3d axis) {
        Vector3d normAxis = axis.normalize();
        fromAngleNormalAxis(angle, normAxis);
        return this;
    }

    /**
     * <code>fromAngleNormalAxis</code> sets this quaternion to the values
     * specified by an angle and a normalized axis of rotation.
     *
     * @param angle
     *            the angle to rotate (in radians).
     * @param axis
     *            the axis of rotation (already normalized).
     */
    public Quaternion fromAngleNormalAxis(double angle, Vector3d axis) {
        if (axis.x == 0 && axis.y == 0 && axis.z == 0) {
            loadIdentity();
        } else {
            double halfAngle = 0.5 * angle;
            double sin = Math.sin(halfAngle);
            w = Math.cos(halfAngle);
            x = sin * axis.x;
            y = sin * axis.y;
            z = sin * axis.z;
        }
        return this;
    }

    /**
     * <code>toAngleAxis</code> sets a given angle and axis to that
     * represented by the current quaternion. The values are stored as
     * follows: The axis is provided as a parameter and built by the method,
     * the angle is returned as a double.
     *
     * @param axisStore
     *            the object we'll store the computed axis in.
     * @return the angle of rotation in radians.
     */
    public double toAngleAxis(Vector3d axisStore) {
        double sqrLength = x * x + y * y + z * z;
        double angle;
        if (sqrLength == 0.0) {
            angle = 0.0;
            if (axisStore != null) {
                axisStore.x = 1.0;
                axisStore.y = 0.0;
                axisStore.z = 0.0;
            }
        } else {
            angle = (2.0 * Math.acos(w));
            if (axisStore != null) {
                double invLength = (1.0 / Math.sqrt(sqrLength));
                axisStore.x = x * invLength;
                axisStore.y = y * invLength;
                axisStore.z = z * invLength;
            }
        }

        return angle;
    }

    /**
     * <code>slerp</code> sets this quaternion's value as an interpolation
     * between two other quaternions.
     *
     * @param q1
     *            the first quaternion.
     * @param q2
     *            the second quaternion.
     * @param t
     *            the amount to interpolate between the two quaternions.
     */
    public Quaternion slerp(Quaternion q1, Quaternion q2, double t) {
        // Create a local quaternion to store the interpolated quaternion
        if (q1.x == q2.x && q1.y == q2.y && q1.z == q2.z && q1.w == q2.w) {
            this.set(q1);
            return this;
        }

        double result = (q1.x * q2.x) + (q1.y * q2.y) + (q1.z * q2.z)
                + (q1.w * q2.w);

        if (result < 0.0) {
            // Negate the second quaternion and the result of the dot product
            q2.x = -q2.x;
            q2.y = -q2.y;
            q2.z = -q2.z;
            q2.w = -q2.w;
            result = -result;
        }

        // Set the first and second scale for the interpolation
        double scale0 = 1 - t;
        double scale1 = t;

        // Check if the angle between the 2 quaternions was big enough to
        // warrant such calculations
        if ((1 - result) > 0.1) {// Get the angle between the 2 quaternions,
            // and then store the sin() of that angle
            double theta = Math.acos(result);
            double invSinTheta = 1.0 / Math.sin(theta);

            // Calculate the scale for q1 and q2, according to the angle and
            // it's sine value
            scale0 = Math.sin((1 - t) * theta) * invSinTheta;
            scale1 = Math.sin((t * theta)) * invSinTheta;
        }

        // Calculate the x, y, z and w values for the quaternion by using a
        // special
        // form of linear interpolation for quaternions.
        this.x = (scale0 * q1.x) + (scale1 * q2.x);
        this.y = (scale0 * q1.y) + (scale1 * q2.y);
        this.z = (scale0 * q1.z) + (scale1 * q2.z);
        this.w = (scale0 * q1.w) + (scale1 * q2.w);

        // Return the interpolated quaternion
        return this;
    }

    /**
     * Sets the values of this quaternion to the slerp from itself to q2 by
     * changeAmnt
     *
     * @param q2
     *            Final interpolation value
     * @param changeAmnt
     *            The amount diffrence
     */
    public void slerp(Quaternion q2, double changeAmnt) {
        if (this.x == q2.x && this.y == q2.y && this.z == q2.z
                && this.w == q2.w) {
            return;
        }

        double result = (this.x * q2.x) + (this.y * q2.y) + (this.z * q2.z)
                + (this.w * q2.w);

        if (result < 0.0) {
            // Negate the second quaternion and the result of the dot product
            q2.x = -q2.x;
            q2.y = -q2.y;
            q2.z = -q2.z;
            q2.w = -q2.w;
            result = -result;
        }

        // Set the first and second scale for the interpolation
        double scale0 = 1 - changeAmnt;
        double scale1 = changeAmnt;

        // Check if the angle between the 2 quaternions was big enough to
        // warrant such calculations
        if ((1 - result) > 0.1) {
            // Get the angle between the 2 quaternions, and then store the sin()
            // of that angle
            double theta = Math.acos(result);
            double invSinTheta = 1.0 / Math.sin(theta);

            // Calculate the scale for q1 and q2, according to the angle and
            // it's sine value
            scale0 = Math.sin((1 - changeAmnt) * theta) * invSinTheta;
            scale1 = Math.sin((changeAmnt * theta)) * invSinTheta;
        }

        // Calculate the x, y, z and w values for the quaternion by using a
        // special
        // form of linear interpolation for quaternions.
        this.x = (scale0 * this.x) + (scale1 * q2.x);
        this.y = (scale0 * this.y) + (scale1 * q2.y);
        this.z = (scale0 * this.z) + (scale1 * q2.z);
        this.w = (scale0 * this.w) + (scale1 * q2.w);
    }

    /**
     * Sets the values of this quaternion to the nlerp from itself to q2 by blend.
     * @param q2
     * @param blend
     */
    public void nlerp(Quaternion q2, double blend) {
        double dot = dot(q2);
        double blendI = 1.0 - blend;
        if (dot < 0.0) {
            x = blendI * x - blend * q2.x;
            y = blendI * y - blend * q2.y;
            z = blendI * z - blend * q2.z;
            w = blendI * w - blend * q2.w;
        } else {
            x = blendI * x + blend * q2.x;
            y = blendI * y + blend * q2.y;
            z = blendI * z + blend * q2.z;
            w = blendI * w + blend * q2.w;
        }
        normalizeLocal();
    }

    /**
     * <code>add</code> adds the values of this quaternion to those of the
     * parameter quaternion. The result is returned as a new quaternion.
     *
     * @param q
     *            the quaternion to add to this.
     * @return the new quaternion.
     */
    public Quaternion add(Quaternion q) {
        return new Quaternion(x + q.x, y + q.y, z + q.z, w + q.w);
    }

    /**
     * <code>add</code> adds the values of this quaternion to those of the
     * parameter quaternion. The result is stored in this Quaternion.
     *
     * @param q
     *            the quaternion to add to this.
     * @return This Quaternion after addition.
     */
    public Quaternion addLocal(Quaternion q) {
        this.x += q.x;
        this.y += q.y;
        this.z += q.z;
        this.w += q.w;
        return this;
    }

    /**
     * <code>subtract</code> subtracts the values of the parameter quaternion
     * from those of this quaternion. The result is returned as a new
     * quaternion.
     *
     * @param q
     *            the quaternion to subtract from this.
     * @return the new quaternion.
     */
    public Quaternion subtract(Quaternion q) {
        return new Quaternion(x - q.x, y - q.y, z - q.z, w - q.w);
    }

    /**
     * <code>subtract</code> subtracts the values of the parameter quaternion
     * from those of this quaternion. The result is stored in this Quaternion.
     *
     * @param q
     *            the quaternion to subtract from this.
     * @return This Quaternion after subtraction.
     */
    public Quaternion subtractLocal(Quaternion q) {
        this.x -= q.x;
        this.y -= q.y;
        this.z -= q.z;
        this.w -= q.w;
        return this;
    }

    /**
     * <code>mult</code> multiplies this quaternion by a parameter quaternion.
     * The result is returned as a new quaternion. It should be noted that
     * quaternion multiplication is not commutative so q * p != p * q.
     *
     * @param q
     *            the quaternion to multiply this quaternion by.
     * @return the new quaternion.
     */
    public Quaternion mult(Quaternion q) {
        return mult(q, null);
    }

    /**
     * <code>mult</code> multiplies this quaternion by a parameter quaternion.
     * The result is returned as a new quaternion. It should be noted that
     * quaternion multiplication is not commutative so q * p != p * q.
     *
     * It IS safe for q and res to be the same object.
     * It IS NOT safe for this and res to be the same object.
     *
     * @param q
     *            the quaternion to multiply this quaternion by.
     * @param res
     *            the quaternion to store the result in.
     * @return the new quaternion.
     */
    public Quaternion mult(Quaternion q, Quaternion res) {
        if (res == null) {
            res = new Quaternion();
        }
        double qw = q.w, qx = q.x, qy = q.y, qz = q.z;
        res.x = x * qw + y * qz - z * qy + w * qx;
        res.y = -x * qz + y * qw + z * qx + w * qy;
        res.z = x * qy - y * qx + z * qw + w * qz;
        res.w = -x * qx - y * qy - z * qz + w * qw;
        return res;
    }

    /**
     *
     * <code>fromAxes</code> creates a <code>Quaternion</code> that
     * represents the coordinate system defined by three axes. These axes are
     * assumed to be orthogonal and no error checking is applied. Thus, the user
     * must insure that the three axes being provided indeed represents a proper
     * right handed coordinate system.
     *
     * @param axis
     *            the array containing the three vectors representing the
     *            coordinate system.
     */
    public Quaternion fromAxes(Vector3d[] axis) {
        if (axis.length != 3) {
            throw new IllegalArgumentException(
                    "Axis array must have three elements");
        }
        return fromAxes(axis[0], axis[1], axis[2]);
    }

    /**
     *
     * <code>fromAxes</code> creates a <code>Quaternion</code> that
     * represents the coordinate system defined by three axes. These axes are
     * assumed to be orthogonal and no error checking is applied. Thus, the user
     * must insure that the three axes being provided indeed represents a proper
     * right handed coordinate system.
     *
     * @param xAxis vector representing the x-axis of the coordinate system.
     * @param yAxis vector representing the y-axis of the coordinate system.
     * @param zAxis vector representing the z-axis of the coordinate system.
     */
    public Quaternion fromAxes(Vector3d xAxis, Vector3d yAxis, Vector3d zAxis) {
        return fromRotationMatrix(xAxis.x, yAxis.x, zAxis.x, xAxis.y, yAxis.y,
                zAxis.y, xAxis.z, yAxis.z, zAxis.z);
    }

    /**
     * <code>mult</code> multiplies this quaternion by a parameter vector. The
     * result is returned as a new vector.
     *
     * @param v
     *            the vector to multiply this quaternion by.
     * @return the new vector.
     */
    public Vector3d mult(Vector3d v) {
        return mult(v, null);
    }

    /**
     * <code>mult</code> multiplies this quaternion by a parameter vector. The
     * result is stored in the supplied vector
     *
     * @param v
     *            the vector to multiply this quaternion by.
     * @return v
     */
    public Vector3d multLocal(Vector3d v) {
        double tempX, tempY;
        tempX = w * w * v.x + 2 * y * w * v.z - 2 * z * w * v.y + x * x * v.x
                + 2 * y * x * v.y + 2 * z * x * v.z - z * z * v.x - y * y * v.x;
        tempY = 2 * x * y * v.x + y * y * v.y + 2 * z * y * v.z + 2 * w * z
                * v.x - z * z * v.y + w * w * v.y - 2 * x * w * v.z - x * x
                * v.y;
        v.z = 2 * x * z * v.x + 2 * y * z * v.y + z * z * v.z - 2 * w * y * v.x
                - y * y * v.z + 2 * w * x * v.y - x * x * v.z + w * w * v.z;
        v.x = tempX;
        v.y = tempY;
        return v;
    }

    /**
     * Multiplies this Quaternion by the supplied quaternion. The result is
     * stored in this Quaternion, which is also returned for chaining. Similar
     * to this *= q.
     *
     * @param q
     *            The Quaternion to multiply this one by.
     * @return This Quaternion, after multiplication.
     */
    public Quaternion multLocal(Quaternion q) {
        double x1 = x * q.w + y * q.z - z * q.y + w * q.x;
        double y1 = -x * q.z + y * q.w + z * q.x + w * q.y;
        double z1 = x * q.y - y * q.x + z * q.w + w * q.z;
        w = -x * q.x - y * q.y - z * q.z + w * q.w;
        x = x1;
        y = y1;
        z = z1;
        return this;
    }

    /**
     * Multiplies this Quaternion by the supplied quaternion. The result is
     * stored in this Quaternion, which is also returned for chaining. Similar
     * to this *= q.
     *
     * @param qx -
     *            quat x value
     * @param qy -
     *            quat y value
     * @param qz -
     *            quat z value
     * @param qw -
     *            quat w value
     *
     * @return This Quaternion, after multiplication.
     */
    public Quaternion multLocal(double qx, double qy, double qz, double qw) {
        double x1 = x * qw + y * qz - z * qy + w * qx;
        double y1 = -x * qz + y * qw + z * qx + w * qy;
        double z1 = x * qy - y * qx + z * qw + w * qz;
        w = -x * qx - y * qy - z * qz + w * qw;
        x = x1;
        y = y1;
        z = z1;
        return this;
    }

    /**
     * <code>mult</code> multiplies this quaternion by a parameter vector. The
     * result is returned as a new vector.
     * 
     * @param v
     *            the vector to multiply this quaternion by.
     * @param store
     *            the vector to store the result in. It IS safe for v and store
     *            to be the same object.
     * @return the result vector.
     */
    public Vector3d mult(Vector3d v, Vector3d store) {
        if (store == null) {
            store = new Vector3d();
        }
        if (v.x == 0 && v.y == 0 && v.z == 0) {
            store.set(0, 0, 0);
        } else {
            double vx = v.x, vy = v.y, vz = v.z;
            store.x = w * w * vx + 2 * y * w * vz - 2 * z * w * vy + x * x
                    * vx + 2 * y * x * vy + 2 * z * x * vz - z * z * vx - y
                    * y * vx;
            store.y = 2 * x * y * vx + y * y * vy + 2 * z * y * vz + 2 * w
                    * z * vx - z * z * vy + w * w * vy - 2 * x * w * vz - x
                    * x * vy;
            store.z = 2 * x * z * vx + 2 * y * z * vy + z * z * vz - 2 * w
                    * y * vx - y * y * vz + 2 * w * x * vy - x * x * vz + w
                    * w * vz;
        }
        return store;
    }

    /**
     * <code>mult</code> multiplies this quaternion by a parameter scalar. The
     * result is returned as a new quaternion.
     *
     * @param scalar
     *            the quaternion to multiply this quaternion by.
     * @return the new quaternion.
     */
    public Quaternion mult(double scalar) {
        return new Quaternion(scalar * x, scalar * y, scalar * z, scalar * w);
    }

    /**
     * <code>mult</code> multiplies this quaternion by a parameter scalar. The
     * result is stored locally.
     *
     * @param scalar
     *            the quaternion to multiply this quaternion by.
     * @return this.
     */
    public Quaternion multLocal(double scalar) {
        w *= scalar;
        x *= scalar;
        y *= scalar;
        z *= scalar;
        return this;
    }

    /**
     * <code>dot</code> calculates and returns the dot product of this
     * quaternion with that of the parameter quaternion.
     *
     * @param q
     *            the quaternion to calculate the dot product of.
     * @return the dot product of this and the parameter quaternion.
     */
    public double dot(Quaternion q) {
        return w * q.w + x * q.x + y * q.y + z * q.z;
    }

    /**
     * <code>norm</code> returns the norm of this quaternion. This is the dot
     * product of this quaternion with itself.
     *
     * @return the norm of the quaternion.
     */
    public double norm() {
        return w * w + x * x + y * y + z * z;
    }

//    /**
//     * <code>normalize</code> normalizes the current <code>Quaternion</code>
//     * @deprecated The naming of this method doesn't follow convention.
//     * Please use {@link Quaternion#normalizeLocal() } instead.
//     */
//    @Deprecated
//    public void normalize() {
//        double n = Math.invSqrt(norm());
//        x *= n;
//        y *= n;
//        z *= n;
//        w *= n;
//    }

    /**
     * <code>normalize</code> normalizes the current <code>Quaternion</code>.
     * The result is stored internally.
     */
    public Quaternion normalizeLocal() {
        double n = 1.0 / Math.sqrt(norm());
        x *= n;
        y *= n;
        z *= n;
        w *= n;
        return this;
    }

    /**
     * <code>inverse</code> returns the inverse of this quaternion as a new
     * quaternion. If this quaternion does not have an inverse (if its normal is
     * 0 or less), then null is returned.
     *
     * @return the inverse of this quaternion or null if the inverse does not
     *         exist.
     */
    public Quaternion inverse() {
        double norm = norm();
        if (norm > 0.0) {
            double invNorm = 1.0 / norm;
            return new Quaternion(-x * invNorm, -y * invNorm, -z * invNorm, w
                    * invNorm);
        }
        // return an invalid result to flag the error
        return null;
    }

    /**
     * <code>inverse</code> calculates the inverse of this quaternion and
     * returns this quaternion after it is calculated. If this quaternion does
     * not have an inverse (if it's normal is 0 or less), nothing happens
     *
     * @return the inverse of this quaternion
     */
    public Quaternion inverseLocal() {
        double norm = norm();
        if (norm > 0.0) {
            double invNorm = 1.0 / norm;
            x *= -invNorm;
            y *= -invNorm;
            z *= -invNorm;
            w *= invNorm;
        }
        return this;
    }

    /**
     * <code>negate</code> inverts the values of the quaternion.
     *
     */
    public void negate() {
        x *= -1;
        y *= -1;
        z *= -1;
        w *= -1;
    }

    /**
     *
     * <code>toString</code> creates the string representation of this
     * <code>Quaternion</code>. The values of the quaternion are displaced (x,
     * y, z, w), in the following manner: <br>
     * (x, y, z, w)
     *
     * @return the string representation of this object.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ", " + w + ")";
    }

    /**
     * <code>equals</code> determines if two quaternions are logically equal,
     * that is, if the values of (x, y, z, w) are the same for both quaternions.
     *
     * @param o
     *            the object to compare for equality
     * @return true if they are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Quaternion)) {
            return false;
        }

        if (this == o) {
            return true;
        }

        Quaternion comp = (Quaternion) o;
        if (Double.compare(x, comp.x) != 0) {
            return false;
        }
        if (Double.compare(y, comp.y) != 0) {
            return false;
        }
        if (Double.compare(z, comp.z) != 0) {
            return false;
        }
        if (Double.compare(w, comp.w) != 0) {
            return false;
        }
        return true;
    }

    /**
     * 
     * <code>hashCode</code> returns the hash code value as an integer and is
     * supported for the benefit of hashing based collection classes such as
     * Hashtable, HashMap, HashSet etc.
     * 
     * @return the hashcode for this instance of Quaternion.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        long hash = 37;
        hash = 37 * hash + Double.doubleToLongBits(x);
        hash = 37 * hash + Double.doubleToLongBits(y);
        hash = 37 * hash + Double.doubleToLongBits(z);
        hash = 37 * hash + Double.doubleToLongBits(w);
        return (int) hash;

    }

    /**
     * <code>readExternal</code> builds a quaternion from an
     * <code>ObjectInput</code> object. <br>
     * NOTE: Used with serialization. Not to be called manually.
     * 
     * @param in
     *            the ObjectInput value to read from.
     * @throws IOException
     *             if the ObjectInput value has problems reading a double.
     * @see java.io.Externalizable
     */
    public void readExternal(ObjectInput in) throws IOException {
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
        w = in.readDouble();
    }

    /**
     * <code>writeExternal</code> writes this quaternion out to a
     * <code>ObjectOutput</code> object. NOTE: Used with serialization. Not to
     * be called manually.
     * 
     * @param out
     *            the object to write to.
     * @throws IOException
     *             if writing to the ObjectOutput fails.
     * @see java.io.Externalizable
     */
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
        out.writeDouble(w);
    }

    public void write(JmeExporter e) throws IOException {
        OutputCapsule cap = e.getCapsule(this);
        cap.write(x, "x", 0);
        cap.write(y, "y", 0);
        cap.write(z, "z", 0);
        cap.write(w, "w", 1);
    }

    public void read(JmeImporter e) throws IOException {
        InputCapsule cap = e.getCapsule(this);
        x = cap.readDouble("x", 0);
        y = cap.readDouble("y", 0);
        z = cap.readDouble("z", 0);
        w = cap.readDouble("w", 1);
    }

    /**
     * @return A new quaternion that describes a rotation that would point you
     *         in the exact opposite direction of this Quaternion.
     */
    public Quaternion opposite() {
        return opposite(null);
    }

    /**
     * FIXME: This seems to have singularity type issues with angle == 0, possibly others such as PI.
     * @param store
     *            A Quaternion to store our result in. If null, a new one is
     *            created.
     * @return The store quaternion (or a new Quaterion, if store is null) that
     *         describes a rotation that would point you in the exact opposite
     *         direction of this Quaternion.
     */
    public Quaternion opposite(Quaternion store) {
        if (store == null) {
            store = new Quaternion();
        }

        Vector3d axis = new Vector3d();
        double angle = toAngleAxis(axis);

        store.fromAngleAxis(Math.PI + angle, axis);
        return store;
    }

    /**
     * @return This Quaternion, altered to describe a rotation that would point
     *         you in the exact opposite direction of where it is pointing
     *         currently.
     */
    public Quaternion oppositeLocal() {
        return opposite(this);
    }

    @Override
    public Quaternion clone() {
        try {
            return (Quaternion) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // can not happen
        }
    }
}

