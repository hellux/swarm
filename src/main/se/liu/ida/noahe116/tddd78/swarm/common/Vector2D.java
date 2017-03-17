package se.liu.ida.noahe116.tddd78.swarm.common;

import java.awt.geom.Point2D;

public class Vector2D {
    public final double x, y;

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

    public Vector2D subtract(double x, double y) {
        return new Vector2D(this.x-x, this.y-y);
    }
    
    public Vector2D add(double x, double y) {
        return new Vector2D(this.x+x, this.y+y);
    }

    public Vector2D multiply(double multiplier) {
        return new Vector2D(this.x*multiplier, this.y*multiplier);
    }

    public double magnitude() {
        return Math.sqrt(this.magnitudeSq());
    }

    public double magnitudeSq() {
        return this.x*this.x + this.y*this.y;
    }

    public double distance(double x, double y) {
        return Math.sqrt(this.distanceSq(x, y));
    }

    public double distanceSq(double x, double y) {
        return Math.pow((y-this.y), 2) + Math.pow(x-this.x, 2);
    }

    public double rotation() {
        return Math.atan2(this.y, this.x);
    }

    public Vector2D copy() {
        return new Vector2D(this.x, this.y);
    }

    public Vector2D add(Vector2D v) {
        return this.add(v.x, v.y);
    }

    public Vector2D subtract(Vector2D v) {
        return this.subtract(v.x, v.y);
    }

    public double distance(Vector2D v) {
        return this.distance(v.x, v.y);
    }

    public double distanceSq(Vector2D v) {
        return this.distanceSq(v.x, v.y);
    }

    public Vector2D add(Point2D p) {
        return this.add(p.getX(), p.getY());
    }

    public Vector2D subtract(Point2D p) {
        return this.subtract(p.getX(), p.getY());
    }

    public double distance(Point2D p) {
        return this.distance(p.getX(), p.getY());
    }

    public double distanceSq(Point2D p) {
        return this.distanceSq(p.getX(), p.getY());
    }

    public static Vector2D fromLengthRotation(double length, double rotation) {
        return new Vector2D(Math.cos(rotation)*length, Math.sin(rotation)*length);
    }

    public String toString() {
        return String.format("(%f, %f)", this.x, this.y);
    }
}
