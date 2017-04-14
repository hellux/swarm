package se.liu.ida.noahe116.tddd78.swarm.render;

import java.util.AbstractMap;
import java.util.EnumMap;
import java.util.logging.*;

import se.liu.ida.noahe116.tddd78.swarm.game.*;
import se.liu.ida.noahe116.tddd78.swarm.render.sprites.PlayerSprite;
import se.liu.ida.noahe116.tddd78.swarm.render.sprites.Sprite;

/**
 * Create RenderComponents for specific EntityType.
 * Sprite classes for each EntityType are stored in SPRITES.
 * Render priorities for each EntityType are stored in RENDER_PRIORITIES.
 **/
public final class RcCreator {
    private static final Logger LOGGER = Logger.getLogger(RcCreator.class.getName());

    @SuppressWarnings({"rawtypes", "unchecked", "serial"})
    private static final AbstractMap<EntityType, Sprite> SPRITES =
        new EnumMap(EntityType.class) {{
            put(EntityType.PLAYER, new PlayerSprite());
            put(EntityType.ASTEROID, new Sprite("asteroid_.png"));
    }};

    @SuppressWarnings({"rawtypes", "unchecked", "serial"})
    private static final AbstractMap<EntityType, RenderPriority> RENDER_PRIORITIES =
        new EnumMap(EntityType.class) {{
            put(EntityType.PLAYER, RenderPriority.PLAYER);
            put(EntityType.ASTEROID, RenderPriority.STATIC);
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
        }
        return new RenderComponent(SPRITES.get(type),
                                   e,
                                   RENDER_PRIORITIES.get(type));
    }
}
