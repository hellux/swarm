package se.liu.ida.noahe116.tddd78.swarm.game;

import java.util.logging.*;

import se.liu.ida.noahe116.tddd78.swarm.game.*;
import se.liu.ida.noahe116.tddd78.swarm.game.weapons.*;
import se.liu.ida.noahe116.tddd78.swarm.game.components.*;

public class WeaponCollectible implements Collectible {
    private static final Logger LOGGER =
        Logger.getLogger(WeaponCollectible.class.getName());

    private WeaponType type;
    private int ammo;
    
    public WeaponCollectible(WeaponType type, int ammo) {
        this.type = type;
        this.ammo = ammo;
    }

    public void pickedUp(Entity e) {
        WeaponHandlerComponent wh = e.get(WeaponHandlerComponent.class);
        if (wh == null) {
            LOGGER.log(Level.WARNING, "entity has no WHC: " + e);
        } else {
            wh.addAmmo(this.type, this.ammo);
        }
    }
}
