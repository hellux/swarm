package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.game.*;
import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;

public class ClagBotComponent extends LiveComponent {
    public static final int DETECT_DISTANCE = 2000;

    @Override
    public void update() {
        Entity player = this.entity.getGameLevel().getPlayer();
        Vector2D pos = this.entity.get(PositionComponent.class).getPosition();
        Vector2D playerPos = player.getGameLevel().wrapAround(
            pos,
            player.get(PositionComponent.class).getPosition()
        );

        //TODO implement PD regulator for rotational speed
        // instead of lock-on (in separate class)
        Vector2D difference = Vector2D.subtract(playerPos, pos);
        
        if (difference.magnitude() < DETECT_DISTANCE) {
            this.entity.get(PositionComponent.class).setRotation(difference.rotation());
            this.entity.get(PositionComponent.class).setVelocity(
                Vector2D.fromLengthRotation(10, difference.rotation()));
            //this.entity.get(ThrustComponent.class).setThrust(true);
        } else {
            //this.entity.get(ThrustComponent.class).setThrust(false);
        }

    }

    @Override
    public void collideWith(Entity e, Vector2D intersection) {
        if (e.getType() == EntityType.PLAYER) {
            this.entity.kill();
            //explode
        }
    }
}
