package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.EntityType;
import se.liu.ida.noahe116.tddd78.swarm.game.level.*;

/**
 * Controller component for the clag bot enemy AI.
 **/
public class ClagBotComponent extends LiveComponent implements CollidingComponent {
    private static final int DETECT_DISTANCE = 2000;

    @Override
    public void update(GameLevel level) {
        Entity player = level.getPlayer();
        Vector2D pos = this.entity.get(PositionComponent.class).getPosition();
        Vector2D playerPos = level.wrapAround(
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
        }
    }

    @Override
    public void collideWith(Entity e, GameLevel level) {
        if (e.getType() == EntityType.PLAYER) {
            this.entity.kill();
            //explode
        }
    }
}
