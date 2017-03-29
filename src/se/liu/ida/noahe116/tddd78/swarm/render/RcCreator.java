package se.liu.ida.noahe116.tddd78.swarm.render;

import java.util.AbstractMap;
import java.util.EnumMap;

import se.liu.ida.noahe116.tddd78.swarm.game.*;

public final class RcCreator {
    private final AbstractMap<EntityType, Sprite> sprites;
    private final AbstractMap<EntityType, RenderPriority> renderPriorities;

    public RcCreator() {
        this.sprites = this.createSprites();
        this.renderPriorities = this.createRenderPriorities();
    }

    private AbstractMap<EntityType, Sprite> createSprites() {
        @SuppressWarnings({"rawtypes", "unchecked", "serial"})
        AbstractMap<EntityType, Sprite> sprites =
            new EnumMap(EntityType.class) {{
                put(EntityType.PLAYER, new PlayerSprite());
        }};
        return sprites;
    }

    private AbstractMap<EntityType, RenderPriority> createRenderPriorities() {
        @SuppressWarnings({"rawtypes", "unchecked", "serial"})
        AbstractMap<EntityType, RenderPriority> renderPriorities =
            new EnumMap(EntityType.class) {{
                put(EntityType.PLAYER, RenderPriority.PLAYER);
        }};
        return renderPriorities;
    }

    public RenderComponent createRenderComponent(Entity e) {
        return new RenderComponent(this.sprites.get(e.getType()),
                                   e,
                                   this.renderPriorities.get(e.getType()));
    }

}
