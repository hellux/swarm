package se.liu.ida.noahe116.tddd78.swarm.game;

import se.liu.ida.noahe116.tddd78.swarm.game.*;
import se.liu.ida.noahe116.tddd78.swarm.game.weapons.*;

public class WeaponCollectible implements Collectible {
    private WeaponType type;
    
    public WeaponCollectible(WeaponType type) {
        this.type = type;
    }

    public void pickedUp(Entity e) {
    }
}
