package se.liu.ida.noahe116.tddd78.swarm.game.weapons;

import java.util.logging.*;

import se.liu.ida.noahe116.tddd78.swarm.game.*;
import se.liu.ida.noahe116.tddd78.swarm.game.components.*;
import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;

public class ProjectileWeapon extends Weapon {
    private static final Logger LOGGER = Logger.getLogger(ProjectileWeapon.class.getName());

    private final int damage;
    private final int radius;
    private final Vector2D[] launchPoints;
    private final Vector2D[] velocities;

    public ProjectileWeapon(int cooldown,
                            int damage,
                            int radius,
                            Vector2D[] launchPoints,
                            Vector2D[] velocities) {
        super(cooldown);
        this.damage = damage;
        this.radius = radius;
        this.launchPoints = launchPoints;
        this.velocities = velocities;

        if (this.launchPoints.length != this.velocities.length) {
            LOGGER.log(Level.SEVERE, "arrays differ in size");
        }
    }

    public void fire() {
        //TODO add projectile to level, make it come from entity (now origin)
        System.out.println("Pew Pew");
        for (int p = 0; p < this.launchPoints.length; p++) {
            PositionComponent pc = new PositionComponent(this.launchPoints[p]);
            pc.accelerate(this.velocities[p]);

            CollisionComponent cc = new CollisionComponent(this.radius);
            cc.setDamage(this.damage);

            Entity proj = new Entity(EntityType.PROJECTILE);
            proj.add(pc);
            proj.add(cc);
        }
    }
}
