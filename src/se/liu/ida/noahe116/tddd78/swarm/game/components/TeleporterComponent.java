package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.game.entities.*;
import se.liu.ida.noahe116.tddd78.swarm.game.level.*;

/**
 * Handle player teleporter behaviour.
 **/
public class TeleporterComponent extends Component implements CollidingComponent {
    public void collideWith(Entity e, GameLevel level) {
        if (e.getType() == EntityType.PLAYER) {
            level.endLevel();
        }
    }
}
