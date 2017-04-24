package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.*;
import se.liu.ida.noahe116.tddd78.swarm.game.level.*;

public class KnockbackComponent extends Component implements CollidingComponent {
    private static final double KNOCKBACK_MULTIPLIER = 20;

    @Override
    public void collideWith(Entity e, GameLevel level) {
        CollisionComponent cc1 = this.entity.get(CollisionComponent.class);
        CollisionComponent cc2 = e.get(CollisionComponent.class);

        e.get(PositionComponent.class).setVelocity(
            Vector2D.fromLengthRotation(
                Math.sqrt(cc1.getRadius()/cc2.getRadius())*KNOCKBACK_MULTIPLIER,
                          cc2.intersection(cc1, level).rotation()
            )
        );
    }
}
