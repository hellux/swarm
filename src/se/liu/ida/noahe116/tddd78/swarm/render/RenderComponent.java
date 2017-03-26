package se.liu.ida.noahe116.tddd78.swarm.render;

import java.awt.image.BufferedImage;

import se.liu.ida.noahe116.tddd78.swarm.game.*;

public class RenderComponent implements Comparable<RenderComponent> {
    private final Sprite sprite;
    private final Entity entity;
    private final RenderPriority priority;

    public RenderComponent(Sprite sprite, Entity entity, RenderPriority priority) {
        this.sprite = sprite;
        this.entity = entity;
        this.priority = priority;
    }

    public BufferedImage[] getImages() {
        return this.sprite.getImages(this.entity);
    }
    
    public PositionComponent getPositionComponent() {
        return this.entity.get(PositionComponent.class);
    }

    public RenderPriority getPriority() {
        return this.priority;
    }

    public int compareTo(RenderComponent rc) {
        return this.priority.compareTo(rc.getPriority());
    }
}
