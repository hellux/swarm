package se.liu.ida.noahe116.tddd78.swarm.game.collectibles;

import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;
import se.liu.ida.noahe116.tddd78.swarm.game.level.GameLevel;

/**
 * An item that can be collected.
 * The name might be slightly confusing..
 **/
public interface Collectible {
    /**
     * Will be called when the item is picked up by an entity.
     * @param e the entity that picked up the item
     * @param level the current game level
     **/
    void pickedUp(Entity e, GameLevel level);
}
