package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.common.*;

public class PositionComponent extends LiveComponent {
    private static final double DRAG = 0.94;

    private final Vector2D position = new Vector2D();
    private final Vector2D velocity = new Vector2D();
    private final Vector2D acceleration = new Vector2D();

    private double rotation = 0;
    private double rotationalSpeed = 0;
    private double drag = 1;

    public PositionComponent(Vector2D pos) {
        this.position.x = pos.x;
        this.position.y = pos.y;
    }

    public PositionComponent() {
        this(new Vector2D());
    }

    public void update() {
        this.position.add(this.velocity);
        this.position.x = Math2.floorMod(this.position.x, this.entity.getGameLevel().getSize());
        this.position.y = Math2.floorMod(this.position.y, this.entity.getGameLevel().getSize());

        this.rotation = Math2.floorMod(this.rotation + this.rotationalSpeed, 2*Math.PI);

        this.velocity.add(this.acceleration);
        this.velocity.multiply(this.drag);
    }

    public Vector2D futurePos(double interpolation) {
        if (this.active) {
            Vector2D futureVel = Vector2D.multiply(
                Vector2D.add(this.velocity, this.acceleration),
                this.drag*interpolation
            );
            return Vector2D.add(this.position,
                                Vector2D.multiply(futureVel, interpolation));
        } else {
            return this.position;
        }
    }

    public double futureRot(double interpolation) {
        if (this.active) {
            return this.rotation + this.rotationalSpeed * interpolation;
        } else {
            return this.rotation;
        }
    }

    public void accelerate(Vector2D acc) {
        this.velocity.add(acc);
    }

    public void setAcceleration(Vector2D acc) {
        this.acceleration.x = acc.x;
        this.acceleration.y = acc.y;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation; 
    }

    public void setRotationalSpeed(double speed) {
        this.rotationalSpeed = speed;
    }

    public void setDrag(boolean b) {
        this.drag = b ? DRAG : 1;
    }

    public double getRotation() {
        return this.rotation;
    }

    public void setPosition(Vector2D pos) {
        this.position.x = pos.x;
        this.position.y = pos.y;
    }

    public Vector2D getPosition() {
        return this.position;
    }

    public Vector2D getVelocity() {
        return this.velocity;
    }

    /**
     * Wrap a coordinate around the game level if it's on the other side relative to p2.
     * <pre> {@code
     *
     * P   :  position of this component 
     * P'  :  position of point to eventually be wrapped around
     * )(  :  pivot = (C+S/2) % S
     *
     * Cases:
     *
     *      |---P--------)(-----------|
     *      0                         S
     *
     *       P , P' < pivot   =>   P' =  P'
     *       P < pivot < P'   =>   P' -= S  (*)
     *
     *
     *      |--------)(-----------P---|
     *      0                         S
     *
     *       pivot < P , P'   =>   P' =  P'
     *       P' < pivot < P   =>   P' += S  (*)
     *
     * } </pre>
     * @param p1 position 
     * @param p2 position to eventually wrap around
     * @return same position but eventually wrapped around.
     **/
    private double wrapAround(double p1, double p2) {
        double levelSize = this.entity.getGameLevel().getSize(); 
        double pivot = Math2.floorMod(p1 + levelSize/2, levelSize);

        if (p1 < pivot && pivot < p2) {
            return p2 - levelSize;
        } else if (p2 < pivot && pivot < p1) {
            return p2 + levelSize;
        } else {
            return p2;
        }
    }
    
    public Vector2D wrapAround(Vector2D pos) {
        return new Vector2D(
            this.wrapAround(this.position.x, pos.x),
            this.wrapAround(this.position.y, pos.y)
        );
    }
}
