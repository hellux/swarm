package se.liu.ida.noahe116.tddd78.swarm.render;

import java.util.AbstractMap;
import java.util.EnumMap;
import java.util.logging.*;

import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.EntityType;
import se.liu.ida.noahe116.tddd78.swarm.render.sprites.PlayerSprite;
import se.liu.ida.noahe116.tddd78.swarm.render.sprites.Sprite;

/**
 * Create RenderComponents for specific EntityType.
 * Sprite classes for each EntityType are stored in SPRITES.
 * Render priorities for each EntityType are stored in RENDER_PRIORITIES.
 **/
public final class RcCreator {
    private static final Logger LOGGER = Logger.getLogger(RcCreator.class.getName());

    @SuppressWarnings({"unchecked", "serial", "rawtypes"})
    private static final AbstractMap<EntityType, Sprite> SPRITES =
        new EnumMap(EntityType.class) {{
            put(EntityType.PLAYER, new PlayerSprite());
            put(EntityType.ASTEROID, new Sprite("asteroid_1.png"));
            put(EntityType.PROJECTILE_DEFAULT, new Sprite("projectile_default.png"));
            put(EntityType.PROJECTILE_RED_LASER, new Sprite("projectile_red_laser.png"));
            put(EntityType.PROJECTILE_SPREAD, new Sprite("projectile_spread.png"));
            put(EntityType.PROJECTILE_QUAD, new Sprite("projectile_quad.png"));
            put(EntityType.COLLECTIBLE_RED_LASER, new Sprite("collectible_red_laser.png"));
            put(EntityType.COLLECTIBLE_SPREAD, new Sprite("collectible_spread.png"));
            put(EntityType.COLLECTIBLE_QUAD, new Sprite("collectible_quad.png"));
            put(EntityType.COLLECTIBLE_SHIELD, new Sprite("collectible_shield.png"));
            put(EntityType.ENEMY_CLAG_BOT, new Sprite("enemy_clag_bot.png"));
    }};

    @SuppressWarnings({"unchecked", "serial", "rawtypes"})
    private static final AbstractMap<EntityType, RenderPriority> RENDER_PRIORITIES =
        new EnumMap(EntityType.class) {{
            put(EntityType.PLAYER, RenderPriority.PLAYER);
            put(EntityType.ASTEROID, RenderPriority.STATIC);
            put(EntityType.PROJECTILE_DEFAULT, RenderPriority.DYNAMIC);
            put(EntityType.PROJECTILE_SPREAD, RenderPriority.DYNAMIC);
            put(EntityType.PROJECTILE_RED_LASER, RenderPriority.DYNAMIC);
            put(EntityType.PROJECTILE_QUAD, RenderPriority.DYNAMIC);
            put(EntityType.COLLECTIBLE_RED_LASER, RenderPriority.STATIC);
            put(EntityType.COLLECTIBLE_SPREAD, RenderPriority.STATIC);
            put(EntityType.COLLECTIBLE_QUAD, RenderPriority.STATIC);
            put(EntityType.COLLECTIBLE_SHIELD, RenderPriority.STATIC);
            put(EntityType.ENEMY_CLAG_BOT, RenderPriority.DYNAMIC);
    }};

    private RcCreator() {}

    /**
     * Create a RenderComponent for a specific entity.
     **/
    public static RenderComponent createRenderComponent(Entity e) {
        EntityType type = e.getType();
        if (!SPRITES.containsKey(type) || !RENDER_PRIORITIES.containsKey(type)){
            LOGGER.log(Level.WARNING, "RenderComponent for entity of type "
                                    + type + " could not be created");
            return null;
        }
        return new RenderComponent(SPRITES.get(type),
                                   e,
                                   RENDER_PRIORITIES.get(type));
    }
}
