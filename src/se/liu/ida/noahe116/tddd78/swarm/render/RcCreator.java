package se.liu.ida.noahe116.tddd78.swarm.render;

import java.util.AbstractMap;
import java.util.EnumMap;

import se.liu.ida.noahe116.tddd78.swarm.game.*;

public final class RcCreator {
    private RcCreator() {}

    private static final AbstractMap<EntityType, Sprite> sprites =
        new EnumMap(EntityType.class) {{
            put(EntityType.PLAYER, new PlayerSprite());
            put(EntityType.ASTEROID, new Sprite("junk_1.png"));
    }};

    private static final AbstractMap<EntityType, RenderPriority> renderPriorities =
        new EnumMap(EntityType.class) {{
            put(EntityType.PLAYER, RenderPriority.PLAYER);
            put(EntityType.ASTEROID, RenderPriority.STATIC);
    }};

    public static RenderComponent createRenderComponent(Entity e) {
        return new RenderComponent(sprites.get(e.getType()),
                                   e,
                                   renderPriorities.get(e.getType()));
    }
}
