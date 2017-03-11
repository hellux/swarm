package se.liu.ida.noahe116.tddd78.swarm.game;

public class Player {
    private static double DRAG = 0.01;
    private static double THRUST = 2.0;

    private int x = 0, y = 0;
    private int dx = 0, dy = 0;
    private int rotation = 0;
    private boolean thrust = false;

    public void tick() { 
        if (this.thrust) {
            this.dx += Math.cos(rotation)*THRUST;
            this.dy += Math.sin(rotation)*THRUST;
        }

        this.dx -= this.dx*DRAG;
        this.dy -= this.dy*DRAG;

        this.x += this.dx;
        this.y += this.dy;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getRotation() {
        return this.rotation;
    }

    public void enableThrust() {
        this.thrust = true;
    }

    public void disableThrust() {
        this.thrust = false;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
