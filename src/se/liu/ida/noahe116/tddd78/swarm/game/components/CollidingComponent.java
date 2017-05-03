package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.game.entities.*;
import se.liu.ida.noahe116.tddd78.swarm.game.level.*;

/**
 * Interface for components that react on collisions.
 **/
public interface CollidingComponent {
    /**
     * Will be called when entity collided
     * @param e the entity the collision occurred with
     * @param level the game level the component is part of
     **/
    void collideWith(Entity e, GameLevel level);
}
