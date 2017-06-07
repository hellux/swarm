package se.liu.ida.noahe116.tddd78.swarm.render;

import java.awt.image.BufferedImage;

import se.liu.ida.noahe116.tddd78.swarm.game.components.PositionComponent;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;
import se.liu.ida.noahe116.tddd78.swarm.render.sprites.Sprite;

/**
 * Object used by scene to keep track of entities to draw and their sprites.
 **/
public class RenderComponent implements Comparable<RenderComponent> {
    protected final Sprite sprite;
    protected final Entity entity;
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

    @Override
    public int compareTo(RenderComponent rc) {
        return this.priority.compareTo(rc.priority);
    }

    /**
     * RenderComponents are considered equal if they have the same render priority.
     * @param o object to compare
     * @return if objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (this.getClass() != o.getClass()) return false;

        RenderComponent rc = (RenderComponent) o;
        return this.priority == rc.priority;
    }
}
