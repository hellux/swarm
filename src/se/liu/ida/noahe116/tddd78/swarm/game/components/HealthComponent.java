package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;
import se.liu.ida.noahe116.tddd78.swarm.game.level.*;

public class HealthComponent extends Component implements CollidingComponent {
    private int healthPoints;
    private int shieldStrength;

    public HealthComponent(int hp, int shield) {
        if (hp < 1) throw new IllegalArgumentException("hp < 1: " + hp);
        if (shield < 0) throw new IllegalArgumentException("shield < 0: " + shield);

        this.healthPoints = hp;
        this.shieldStrength = shield;
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

    @Override
    public void collideWith(Entity e, GameLevel level) {
        CollisionComponent cc = e.get(CollisionComponent.class);
        if (cc != null) {
            this.hurt(cc.getDamage());
        }
    }
}
