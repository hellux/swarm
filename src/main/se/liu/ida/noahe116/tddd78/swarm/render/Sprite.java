package se.liu.ida.noahe116.tddd78.swarm.render;

import java.awt.Graphics2D;
import se.liu.ida.noahe116.tddd78.swarm.game.Entity;

public interface Sprite {
    public void draw(Entity entity, Graphics2D g2d, double interpolation, Scene scene);
}
