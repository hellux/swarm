package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.game.*;
import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;

public class ClagBotComponent extends LiveComponent {
    public void update() {
        Entity player = this.entity.getGameLevel().getPlayer();
    }

    public void collideWith(Entity e, Vector2D intersection) {
        if (e.getType() == EntityType.PLAYER) {
            //explode
        }
    }
}
