package se.liu.ida.noahe116.tddd78.swarm.game.collectibles;

import java.util.logging.*;

import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;
import se.liu.ida.noahe116.tddd78.swarm.game.level.GameLevel;

/**
 * Crystal collectible that is part of game objective.
 **/
public class CrystalCollectible implements Collectible {
    public void pickedUp(Entity e, GameLevel level) {
        level.addCrystal();
    }
}

