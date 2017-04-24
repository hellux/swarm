package se.liu.ida.noahe116.tddd78.swarm.game.components;

import java.util.List;
import java.util.ArrayList;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;
import se.liu.ida.noahe116.tddd78.swarm.game.level.*;

public class CollisionComponent extends Component {
    public final double radius;
    private int damage = 0;
    private boolean knockback = false;
    private List<Entity> ignore = new ArrayList<>();

    public CollisionComponent(double r) {
        this.radius = r;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Return a vector representing the intersection with another col-comp.
     * @param cc other collision component
     * @param level level where entities are
     * @return vector I of collision intersection pointing towards this,
     *         if no collision, a flipped vector between the edges will be
     *         returned.
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
    public Vector2D intersection(CollisionComponent cc, GameLevel level) {
        Vector2D wrappedCenter = level.wrapAround(this.center(), cc.center());
        Vector2D difference = Vector2D.subtract(this.center(), wrappedCenter);
        return Vector2D.multiply(
            difference.normal(),
            Math.abs(difference.magnitude() - this.radius - cc.radius)
        );
    }

    /**
     * Check if a collision occurs with another col-comp.
     * <pre> {@code
     *          , - ~ - ,
     *      , '           ' ,
     *    ,                   ,
     *   ,                     ,
     *  ,          C1           ,
     *  , - - - - - o           ,
     *  ,   r1       \ - ~ - ,  ,
     *   ,       , '  \        ; ,
     *    ,    ,       \ d    ,    ,
     *      , ,        \   , '      ,
     *       ,' - , _ , \,     r2    ,
     *       ,           o - - - - - ,
     *       ,           C2          ,
     *        ,                     ,
     *         ,                   ,
     *           ,              , '
     *             ' - , _ ,  '
     *
     *  d < r1+r2
     *  d^2 < (r1+r2)^2
     **/ 
    public boolean collidesWith(CollisionComponent cc, GameLevel level) {
        //TODO check future postitons and interpolate the collision point
        if (this.ignore.contains(cc.getEntity()) || cc.ignores(this.entity)) {
            return false;
        }
        
        Vector2D wrappedCenter = level.wrapAround(this.center(), cc.center());
        if (Vector2D.distanceSq(this.center(), wrappedCenter) <
            Math.pow(this.radius+cc.radius, 2)) {
            Vector2D difference = Vector2D.subtract(this.center(), wrappedCenter);
            return true;
        } else {
            return false;
        }
    }

    public Vector2D center() {
        return this.entity.get(PositionComponent.class).getPosition();
    }

    public double getRadius() {
        return this.radius;
    }

    public int getDamage() {
        return this.damage;
    }

    public void ignore(Entity e) {
        this.ignore.add(e);
    }

    public boolean ignores(Entity e) {
        return this.ignore.contains(e);
    }
}
