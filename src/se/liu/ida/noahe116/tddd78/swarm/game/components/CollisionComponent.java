package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;

public class CollisionComponent extends Component {
    private static double KNOCKBACK_MULTIPLIER = 3;

    public final double radius;
    private int damage = 5;
    private boolean knockback = false;

    public CollisionComponent(double r) {
        this.radius = r;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setKnockback(boolean b) {
        this.knockback = b;
    }

    /**
     * Return a vector representing the intersection with another col-comp.
     * @param cc other collision component
     * @return vector I of collision intersection pointing towards this,
     *         if no collision; return null.
     * <p>Diagram</p>
     * <pre> {@code
     *          , - ~ - ,
     *      , '           ' ,
     *    ,                   ,
     *   ,                     ,
     *  ,     this.center()     ,
     *  ,           o           ,
     *  ,            ⵤ - ~ - ,  ,
     *   ,       , '  \        ; ,
     *    ,    ,       \ I    ,    ,
     *      , ,         \  , '      ,
     *       ,' - , _ ,  ,           ,
     *       ,           o           ,
     *       ,      cc.center()      ,
     *        ,                     ,
     *         ,                   ,
     *           ,              , '
     *             ' - , _ ,  '
     *
     * D = (this.center() - cc.center())
     *     ^       _
     * I = D * abs(D-this.radius-cc.radius)
     * } </pre>
     **/
    public Vector2D intersection(CollisionComponent cc) {
        if (Vector2D.distanceSq(this.center(), cc.center()) <
            Math.pow(this.radius+cc.radius, 2)) {
            Vector2D difference = Vector2D.subtract(this.center(), cc.center());
            return Vector2D.multiply(
                difference.normal(),
                Math.abs(difference.magnitude() - this.radius - cc.radius)
            );
        }
        return null;
    }

    public void collideWith(CollisionComponent cc, Vector2D intersection) {
        HealthComponent hc = this.entity.get(HealthComponent.class);
        if (hc != null) hc.hurt(cc.getDamage());
        if (cc.hasKnockback()) {
            PositionComponent pc = this.entity.get(PositionComponent.class);
            if (pc != null) {
                pc.accelerate(Vector2D.multiply(
                    Vector2D.multiply(intersection, 1.0/Math.sqrt(this.radius)),
                    KNOCKBACK_MULTIPLIER
                ));
            }
        }
    }

    public Vector2D center() {
        return this.entity.get(PositionComponent.class).getPosition();
    }

    public int getDamage() {
        return this.damage;
    }

    public boolean hasKnockback() {
        return this.knockback;
    }
}
