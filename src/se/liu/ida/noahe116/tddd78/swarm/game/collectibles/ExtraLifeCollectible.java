package se.liu.ida.noahe116.tddd78.swarm.game.collectibles;

import java.util.logging.*;

import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;
import se.liu.ida.noahe116.tddd78.swarm.game.components.*;
import se.liu.ida.noahe116.tddd78.swarm.game.level.GameLevel;

public class ExtraLifeCollectible implements Collectible {
    private static final Logger LOGGER =
        Logger.getLogger(ExtraLifeCollectible.class.getName());

    public void pickedUp(Entity e, GameLevel level) {
        HealthComponent hc = e.get(HealthComponent.class);
        if (hc == null) {
            LOGGER.log(Level.WARNING, "entity has no healthcomp: " + e);
        } else {
            hc.addExtraLives(1);
        }
    }
}
