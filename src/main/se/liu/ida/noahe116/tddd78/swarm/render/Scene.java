package se.liu.ida.noahe116.tddd78.swarm.render;

import java.awt.*;
import java.util.HashMap;
import java.util.AbstractMap;

import se.liu.ida.noahe116.tddd78.swarm.game.*;

public class Scene {
    private class RenderComponent {
        private Sprite sprite;
        private Entity entity;

        private RenderComponent(Sprite sprite, Entity entity) {
            this.sprite = sprite;
            this.entity = entity;
        }

        public void draw(Graphics2D g2d, double interpolation) {
            this.sprite.draw(this.entity, g2d, interpolation);
        }
    }

    private Game game;
    private AbstractMap<Entity, RenderComponent> renderComponents = new HashMap<>();
    private Camera camera;

    public Scene(Game game) {
        this.game = game;
    }

    /**
     * Render an interpolated frame of the game's current state.
     * @param interpolation ratio of time that has passed since the last tick to the
     *                      period between ticks.
     **/
    public void render(Graphics2D g2d, double interpolation) {
        for (Entity e : this.game.getEntities()) {
            if (true) { //TODO check if entity needs to be drawn
                if (!renderComponents.containsKey(e)) {
                    Sprite sprite = new PlayerSprite(); //TODO create correct sprite
                    renderComponents.put(e, new RenderComponent(sprite, e));
                }
            } else {
                renderComponents.remove(e);
            }
        }
        for (RenderComponent rc : this.renderComponents.values()) {
            rc.draw(g2d, interpolation);
        }
    }
}
