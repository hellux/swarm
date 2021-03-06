package se.liu.ida.noahe116.tddd78.swarm.game.components;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.*;
import se.liu.ida.noahe116.tddd78.swarm.game.level.*;

/**
 * Handle collisions between entities.
 * <p>Collision areas can only be circles.</p>
 *
 * <br><strong>Black/whiteList:</strong>
 * <p>Types of entities can be either ignored (blacklist) or
 * exclusively checked for collisions (whitelist).
 * By default the blacklist is used and is empty, thus
 * all collisions will be checked. When entities are
 * added to the whitelist, the whitelist will be used instead.</p>
 **/
public class CollisionComponent extends Component {
    private final double radius;
    private int damage = 0;

    private boolean blacklist = true;
    private List<EntityType> typeBlacklist = new ArrayList<>();
    private List<EntityType> typeWhitelist = new ArrayList<>();

    /**
     * Create a collision with the collision circle radius r.
     * @param r radius of the collision circle
     **/
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
            difference.unit(),
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
     * } </pre>
     * @param cc collisioncomponent to check for collisions with
     * @param level game level of current game
     * @return whether an collision occurs or not
     **/ 
    public boolean collidesWith(CollisionComponent cc, GameLevel level) {
        //TODO check future postitons and extrapolate the collision point
        if (this.ignores(cc.entity) || cc.ignores(this.entity)) {
            return false;
        }
        
        Vector2D wrappedCenter = level.wrapAround(this.center(), cc.center());
        if (Vector2D.distanceSq(this.center(), wrappedCenter) <
            Math.pow(this.radius+cc.radius, 2)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Collide with an entity.
     * Call collideWith methods in all components.
     * @param e entity collided with
     * @param level current game level
     **/
    public void collideWith(Entity e, GameLevel level) {
        Collection<Component> components = e.getComponents();
        for (Component c : components) {
            if (c instanceof CollidingComponent) {
                CollidingComponent cc = (CollidingComponent) c;
                cc.collideWith(this.entity, level);
            }
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

    /**
     * Blacklist entities from collision checking.
     * @param entityTypes entity types to blacklist
     **/
    public void blacklist(EntityType...entityTypes) {
        this.blacklist = true;
        Collections.addAll(this.typeBlacklist, entityTypes);
    }

    /**
     * Whitelist entities for collision checking.
     * @param entityTypes entity types to whitelist
     **/
    public void whitelist(EntityType...entityTypes) {
        this.blacklist = false;
        Collections.addAll(this.typeWhitelist, entityTypes);
        }

    /**
     * Check if one of the entities ignores the other.
     * @param e entity to check if it's ignored
     * @return whether the entity is ignored
     **/
    public boolean ignores(Entity e) {
        EntityType type = e.getType();
        if (this.blacklist) {
            return this.typeBlacklist.contains(type);
        } else {
            return !this.typeWhitelist.contains(type);
        }
    }
}
