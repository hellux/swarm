package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.game.collectibles.Collectible;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.EntityType;
import se.liu.ida.noahe116.tddd78.swarm.game.level.*;

/**
 * Objects that can be collected.
 **/
public class CollectibleComponent extends Component implements CollidingComponent {
    private final Collectible collectible;

    public CollectibleComponent(Collectible collectible) {
        this.collectible = collectible;
    }

    @Override
    public void collideWith(Entity e, GameLevel level) {
        if (e.getType() == EntityType.PLAYER) {
            this.collectible.pickedUp(e, level);
            this.entity.kill();
        }
    }
}
