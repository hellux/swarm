package se.liu.ida.noahe116.tddd78.swarm.game;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;

public class PlayerComponent extends Component {
    private static double DRAG = 0.9;
    private static double MAX_THRUST = 0.5;
    private static double MAX_SPEED = 1;

    private double thrustPower = 0;
    private boolean thrust = false;

    public PlayerComponent() { 
        this.isActive = true;
    }

    @Override
    public void update() { 
        if (this.thrust) {
            Vector2D acceleration =
                Vector2D.fromLengthRotation(this.thrustPower*MAX_THRUST,
                    this.entity.get(PositionComponent.class).getRotation());
            this.entity.get(PositionComponent.class).accelerate(acceleration);
        }
    }

    public void setThrust(boolean t) {
        this.thrust = t;
    }

    public void setThrustPower(double tp) {
        this.thrustPower = tp;
    }

    public void setRotation(double rotation) {
        this.entity.get(PositionComponent.class).setRotation(rotation);
    }
}
