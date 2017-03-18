package se.liu.ida.noahe116.tddd78.swarm.render;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

import se.liu.ida.noahe116.tddd78.swarm.game.*;

public class Scene {
    private class RenderComponent<T extends Entity> {
        private Sprite<T> sprite;
        private T entity;

        private RenderComponent(Sprite<T> sprite, T entity) {
            this.sprite = sprite;
            this.entity = entity;
        }

        public void draw(Graphics2D g2d, double interpolation) {
            this.sprite.draw(this.entity, g2d, interpolation);
        }
    }

    private Game game;
    private List<RenderComponent<Entity>> renderComponents = new ArrayList<>();

    public Scene(Game game) {
        this.game = game;
    }

    /**
     * Render an interpolated frame of the game's current state.
     * @param interpolation ratio of time that has passed since the last tick to the
     *                      period between ticks.
     **/
    public void render(Graphics2D g2d, double interpolation) {
        System.out.println(this.game.getPlayer().get(PositionComponent.class).getPosititon());
        for (Entity e : this.game.getEntities()) {
            //add render component for visible entities
        }
        for (RenderComponent<Entity> rc : this.renderComponents) {
            rc.draw(g2d, interpolation);
        }
    }
}
