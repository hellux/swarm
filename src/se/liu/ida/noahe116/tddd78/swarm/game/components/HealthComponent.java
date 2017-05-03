package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;
import se.liu.ida.noahe116.tddd78.swarm.game.level.*;

/**
 * Handles health, shield and lives of entities.
 **/
public class HealthComponent extends Component implements CollidingComponent {
    public static final double START_SHIELD_RATIO = 0.3;

    private final int maxHealth;
    private final int maxShieldStrength;

    private int extraLives;
    private int healthPoints;
    private int shieldStrength;

    public HealthComponent(int hp, int shield) {
        if (hp < 1) throw new IllegalArgumentException("hp < 1: " + hp);
        if (shield < 0) throw new IllegalArgumentException("shield < 0: " + shield);

        this.extraLives = 0;
        this.maxHealth = hp;
        this.maxShieldStrength = shield;

        this.setStartHealth();
    }

    private void setStartHealth() {
        this.healthPoints = this.maxHealth;
        this.shieldStrength = (int) (this.maxShieldStrength*START_SHIELD_RATIO);
    }

    public HealthComponent(int hp) {
        this(hp, 0);
    }

    private void hurt(int damage) {
        if (this.shieldStrength > 0) {
            this.shieldStrength = Math.max(this.shieldStrength - damage, 0);
        } else {
            this.healthPoints = Math.max(this.healthPoints - damage, 0);
            if (this.healthPoints == 0) {
                this.entity.get(PositionComponent.class).stop();
                this.entity.kill();
            }
        }
    }

    public void respawn() {
        this.extraLives--;
        this.setStartHealth();
        this.entity.resurrect();
    }

    @Override
    public void collideWith(Entity e, GameLevel level) {
        CollisionComponent cc = e.get(CollisionComponent.class);
        if (cc != null) {
            this.hurt(cc.getDamage());
        }
    }

    public void addShield(int shield) {
        this.shieldStrength = Math.min(this.shieldStrength + shield,
                                       this.maxShieldStrength);
    }

    public void addExtraLives(int lives) {
        if (lives < 0) throw new IllegalArgumentException("lives must be positive");

        this.extraLives += lives;
    }

    public boolean hasExtraLives() {
        return 0 < this.extraLives;
    }

    public int getExtraLives() {
        return this.extraLives;
    }

    public int getShieldStrength() {
        return this.shieldStrength;
    }

    public int getMaxShieldStrength() {
        return this.maxShieldStrength;
    }
}
