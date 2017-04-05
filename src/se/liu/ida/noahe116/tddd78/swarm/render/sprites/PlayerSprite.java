package se.liu.ida.noahe116.tddd78.swarm.render.sprites;

import java.awt.image.BufferedImage;

import se.liu.ida.noahe116.tddd78.swarm.game.*;
import se.liu.ida.noahe116.tddd78.swarm.game.components.PlayerComponent;

public class PlayerSprite extends Sprite {
    private static final String BASE = "ship_base.png";
    private static final String DEFAULT = "ship_default.png";
    private static final String RED_LASER = "ship_red_laser.png";
    private static final String MISSILE = "ship_missile.png";
    private static final String QUAD_LASER = "ship_quad.png";
    private static final String SPREAD_GUN = "ship_spread.png";
    private static final String THRUST = "ship_thrust.png";

    public PlayerSprite() {
        super(BASE, THRUST, DEFAULT, RED_LASER, MISSILE, QUAD_LASER, SPREAD_GUN);
    }
    
    @Override
    public BufferedImage[] getImages(Entity entity) {
        String thrust = entity.get(PlayerComponent.class).isThrust() ? THRUST : null;
        return this.getImageArray(thrust, BASE, DEFAULT);
    }
}
