package se.liu.ida.noahe116.tddd78.swarm.render;

import java.awt.*;

import se.liu.ida.noahe116.tddd78.swarm.game.*;

public interface Sprite<T extends Entity> {
    public void draw(T entity, Graphics2D g2d, double interpolation);
}
