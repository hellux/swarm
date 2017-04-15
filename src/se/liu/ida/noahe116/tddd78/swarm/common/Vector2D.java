package se.liu.ida.noahe116.tddd78.swarm.common;

import java.awt.geom.Point2D;

public class Vector2D {
    public double x, y;

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

    public void add(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public void subtract(double x, double y) {
        this.x -= x;
        this.y -= y;
    }
    
    public void multiply(double multiplier) {
        this.x *= multiplier;
        this.y *= multiplier;
    }

    public double magnitude() {
        return Math.sqrt(this.magnitudeSq());
    }

    public double magnitudeSq() {
        return this.x*this.x + this.y*this.y;
    }

    public double rotation() {
        return Math.atan2(this.y, this.x);
    }

    public Vector2D normal() {
        return Vector2D.fromLengthRotation(1, this.rotation());
    }

    public Vector2D copy() {
        return new Vector2D(this.x, this.y);
    }

    public void add(Vector2D v) {
        this.add(v.x, v.y);
    }

    public void subtract(Vector2D v) {
        this.subtract(v.x, v.y);
    }

    @Override
    public String toString() {
        return String.format("(%f, %f)", this.x, this.y);
    }

    public static Vector2D fromLengthRotation(double length, double rotation) {
        return new Vector2D(Math.cos(rotation)*length, Math.sin(rotation)*length);
    }
    
    public static Vector2D add(Vector2D v1, Vector2D v2) {
        return new Vector2D(v1.x+v2.x, v1.y+v2.y);
    }

    public static Vector2D subtract(Vector2D v1, Vector2D v2) {
        return new Vector2D(v1.x-v2.x, v1.y-v2.y);
    }

    public static Vector2D multiply(Vector2D v, double multiplier) {
        return new Vector2D(v.x*multiplier, v.y*multiplier);
    }

    public static double distance(Vector2D v1, Vector2D v2) {
        return Math.sqrt(distanceSq(v1, v2));
    }

    public static double distanceSq(Vector2D v1, Vector2D v2) {
        return Math.pow(v1.x-v2.x, 2) + Math.pow(v1.y-v2.y, 2);
    }

    public static Vector2D rotate(Vector2D v, double rotation) {
        return Vector2D.transform(v, Vector2D.rotationMatrix(rotation));
    }
    
    /**
     * Create a rotation matrix for rotation r.
     *
     * x' = xcosr - ysinr ~ [x'] = [cosr -sinr][x]
     * y' = xsinr + ycosr   [y']   [sinr cosr ][y]
     **/
    public static double[][] rotationMatrix(double rotation) {
        return new double[][] {
            {Math.cos(rotation), -Math.sin(rotation)},
            {Math.sin(rotation), Math.cos(rotation)}
        };
    }

    public static Vector2D transform(Vector2D v, double[][] m) {
        return new Vector2D(m[0][0]*v.x + m[0][1]*v.y,
                            m[1][0]*v.x + m[1][1]*v.y);
    }
}
