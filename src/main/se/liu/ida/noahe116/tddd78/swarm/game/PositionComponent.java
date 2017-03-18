package se.liu.ida.noahe116.tddd78.swarm.game;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;

public class PositionComponent extends Component {
    private Vector2D position = new Vector2D();
    private Vector2D velocity = new Vector2D();
    private double rotation = 0;

    public PositionComponent() {
        this.isActive = true;
    }

    public void update() {
        this.position = this.position.add(this.velocity);
    }

    public void accelerate(Vector2D acceleration) {
        this.velocity = this.velocity.add(acceleration);
    }

    public void setRotation(double rotation) {
        this.rotation = rotation; 
    }

    public double getRotation() {
        return this.rotation;
    }

    public Vector2D getPosititon() {
        return this.position;
    }

    public Vector2D getVelocity() {
        return this.velocity;
    }
}
