package se.liu.ida.noahe116.tddd78.swarm.common;

import java.awt.geom.Point2D;

/**
 * 2-dimensional vector.
 * all angles are in radians
 **/
public class Vector2D {
    /**
     * x coordinate of vector
     */
    public double x;
    /**
     * y coordinate of vector
     */
    public double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Point2D p) {
        this(p.getX(), p.getY());
    }

    public Vector2D() {
        this(0, 0);
    }

    /**
     * Set this vector to the values of a different vector.
     * @param v vector to copy values from.
     **/
    public void set(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
    }
    
    /**
     * Add a vector to this vector.
     * @param v vector to add
     **/
    public void add(Vector2D v) {
        this.x += v.x;
        this.y += v.y;
    }

    /**
     * Subtract a vector from this vector.
     * @param v vector to subtract
     **/
    public void subtract(Vector2D v) {
        this.x -= v.x;
        this.y -= v.y;
    }

    /**
     * Multiply this vector with a scalar.
     * @param scalar scalar to multiply with
     **/
    public void multiply(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    /**
     * Magnitude/norm/length of this vector.
     * @return magnitude of this vector
     **/
    public double magnitude() {
        return Math.sqrt(this.x*this.x + this.y*this.y);
    }
    
    /**
     * Rotation of this vector (angle from the x axis).
     * @return rotation of this vector
     **/
    public double rotation() {
        return Math.atan2(this.y, this.x);
    }

    /**
     * A flipped/inversed version of this vector.
     * @return flipped vector
     **/
    public Vector2D flipped() {
        return Vector2D.multiply(this, -1);
    }

    /**
     * The unit vector of this vector (same direction, with length 1).
     * @return unit vector
     **/
    public Vector2D unit() {
        return Vector2D.fromLengthRotation(1, this.rotation());
    }

    /**
     * A copy of this vector.
     * @return vector copy
     **/
    public Vector2D copy() {
        return new Vector2D(this.x, this.y);
    }

    /**
     * String representation of this vector.
     * @return string representation
     **/
    @Override
    public String toString() {
        return String.format("(%f, %f)", this.x, this.y);
    }


    /**
     * Create a vector from a length and direction.
     * @param length length of vector
     * @param rotation rotation of vector
     * @return vector with given length and rotation
     **/
    public static Vector2D fromLengthRotation(double length, double rotation) {
        return new Vector2D(Math.cos(rotation)*length, Math.sin(rotation)*length);
    }
    
    /**
     * Add two vectors.
     * @param v1 term vector 1
     * @param v2 term vector 2
     * @return sum of vectors
     **/
    public static Vector2D add(Vector2D v1, Vector2D v2) {
        return new Vector2D(v1.x+v2.x, v1.y+v2.y);
    }

    /**
     * Subtract a vector with another vector.
     * @param v1 vector to subtract from
     * @param v2 vector to subtract with
     * @return difference of vectors
     **/
    public static Vector2D subtract(Vector2D v1, Vector2D v2) {
        return new Vector2D(v1.x-v2.x, v1.y-v2.y);
    }

    /**
     * Multiply a vector with a scalar.
     * @param v vector to scale
     * @param scalar scalar
     * @return scaled vector
     **/
    public static Vector2D multiply(Vector2D v, double scalar) {
        return new Vector2D(v.x*scalar, v.y*scalar);
    }

    /**
     * Calculate distance between two position vectors.
     * @param p1 position vector 1
     * @param p2 position vector 2
     * @return distance between positions
     **/
    public static double distance(Vector2D p1, Vector2D p2) {
        return Math.sqrt(distanceSq(p1, p2));
    }

    /**
     * Calculate squared distance between two position vectors.
     * @param p1 position vector 1
     * @param p2 position vector 2
     * @return squared distance between positions
     **/
    public static double distanceSq(Vector2D p1, Vector2D p2) {
        return Math.pow(p1.x-p2.x, 2) + Math.pow(p1.y-p2.y, 2);
    }

    /**
     * Rotate a vector.
     * @param v vector to rotate
     * @param rotation rotation
     * @return rotated vector
     **/
    public static Vector2D rotate(Vector2D v, double rotation) {
        return Vector2D.transform(v, Vector2D.rotationMatrix(rotation));
    }
    
    /**
     * Create a rotation matrix for rotation r.
     *
     * x' = xcosr - ysinr ~ [x'] = [cosr -sinr][x]
     * y' = xsinr + ycosr   [y']   [sinr cosr ][y]
     * @param rotation rotation of rotation matrix
     * @return rotation matrix with given rotation
     **/
    public static double[][] rotationMatrix(double rotation) {
        return new double[][] {
            {Math.cos(rotation), -Math.sin(rotation)},
            {Math.sin(rotation), Math.cos(rotation)}
        };
    }

    /**
     * Transform a vector with a transformation matrix.
     * @param v vector to transform
     * @param m matrix to transform vector with
     * @return transformed vector
     **/
    public static Vector2D transform(Vector2D v, double[][] m) {
        return new Vector2D(m[0][0]*v.x + m[0][1]*v.y,
                            m[1][0]*v.x + m[1][1]*v.y);
    }
}
