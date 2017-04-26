package se.liu.ida.noahe116.tddd78.swarm.game.collectibles;

import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;
import se.liu.ida.noahe116.tddd78.swarm.game.level.GameLevel;

public interface Collectible {
    void pickedUp(Entity e, GameLevel level);
}
