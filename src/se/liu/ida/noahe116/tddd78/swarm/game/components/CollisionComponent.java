package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;

public class CollisionComponent extends Component {
    private CollisionCircle[] collisionCircles;

    public class CollisionCircle {
        Vector2D position;
        public int radius;

        public CollisionCircle(int x, int y, int r) {
            this.position.x = x;
            this.position.y = y;
            this.radius = r;
        }

        public Vector2D center() {
            return Vector2D.add(
                CollisionComponent.this.entity.get(PositionComponent.class).getPosition(),
                this.position
            );
        }
    }

    public CollisionComponent(CollisionCircle...circles) {
        this.collisionCircles = circles;
    }

    public boolean collidesWith(CollisionComponent cc) {
        for (CollisionCircle c1 : this.collisionCircles) {
            Vector2D c1Center = c1.center();
            for (CollisionCircle c2 : cc.getCollisionCircles()) {
                if (Vector2D.distanceSq(c1Center, c2.center()) <
                    Math.pow(c1.radius+c2.radius, 2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public CollisionCircle[] getCollisionCircles() {
        return this.collisionCircles;
    }
}
