package se.liu.ida.noahe116.tddd78.swarm.game.collectibles;

import java.util.logging.*;

import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;
import se.liu.ida.noahe116.tddd78.swarm.game.components.*;
import se.liu.ida.noahe116.tddd78.swarm.game.level.GameLevel;

public class CrystalCollectible implements Collectible {
    private static final Logger LOGGER =
        Logger.getLogger(ShieldCollectible.class.getName());

    public void pickedUp(Entity e, GameLevel level) {
        level.addCrystal();
    }
}

