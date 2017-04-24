package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.game.entities.*;
import se.liu.ida.noahe116.tddd78.swarm.game.level.*;

public interface CollidingComponent {
    void collideWith(Entity e, GameLevel level);
}
