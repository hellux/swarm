package se.liu.ida.noahe116.tddd78.swarm.render.sprites;

import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.AbstractMap;

import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;
import se.liu.ida.noahe116.tddd78.swarm.game.weapons.*;
import se.liu.ida.noahe116.tddd78.swarm.game.components.*;

/**
 * Handle the sprites of the player entity.
 **/
public class PlayerSprite extends Sprite {
    private static final String BASE = "ship_base.png";
    private static final String DEFAULT = "ship_default.png";
    private static final String RED_LASER = "ship_red_laser.png";
    private static final String MISSILE = "ship_missile.png";
    private static final String QUAD = "ship_quad.png";
    private static final String SPREAD = "ship_spread.png";
    private static final String THRUST = "ship_thrust.png";
    
    @SuppressWarnings({ "unchecked", "serial", "rawtypes"})
    // anonymous class is static and will only be initialized once (at startup)
    // this is the least tedious way I can think of to map enums to stuff
    private static final AbstractMap<WeaponType, String> WEAPONS = new EnumMap(WeaponType.class) {{
        put(WeaponType.DEFAULT, DEFAULT);
        put(WeaponType.RED_LASER, RED_LASER);
        put(WeaponType.SPREAD, SPREAD);
        put(WeaponType.QUAD, QUAD);
    }};

    public PlayerSprite() {
        super(BASE, THRUST, DEFAULT, RED_LASER, MISSILE, QUAD, SPREAD);
    }
    
    @Override
    public BufferedImage[] getImages(Entity entity) {
        String thrust = entity.get(PlayerComponent.class).hasThrust() ? THRUST : null;
        String weapon = WEAPONS.get(entity.get(WeaponHandlerComponent.class)
            .getEquippedType(0));
        return this.getImageArray(thrust, BASE, weapon);
    }
}
