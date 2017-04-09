package se.liu.ida.noahe116.tddd78.swarm.render;

import java.util.AbstractMap;
import java.util.EnumMap;

import se.liu.ida.noahe116.tddd78.swarm.game.*;
import se.liu.ida.noahe116.tddd78.swarm.render.sprites.PlayerSprite;
import se.liu.ida.noahe116.tddd78.swarm.render.sprites.Sprite;

public final class RcCreator {
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

    public static RenderComponent createRenderComponent(Entity e) {
        return new RenderComponent(SPRITES.get(e.getType()),
                                   e,
                                   RENDER_PRIORITIES.get(e.getType()));
    }
}
