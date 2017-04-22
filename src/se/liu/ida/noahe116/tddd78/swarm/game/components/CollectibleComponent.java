package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;
import se.liu.ida.noahe116.tddd78.swarm.game.*;

public class CollectibleComponent extends Component {
    Collectible collectible;

    public CollectibleComponent(Collectible collectible) {
        this.collectible = collectible;
    }

    public void collideWith(Entity e, Vector2D intersection) {
        if (e.getType() == EntityType.PLAYER) {
            this.collectible.pickedUp(e);
            this.entity.kill();
        }
    }
}
