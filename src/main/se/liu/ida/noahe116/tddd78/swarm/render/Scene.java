package se.liu.ida.noahe116.tddd78.swarm.render;

import java.awt.*;
import java.util.HashMap;
import java.util.AbstractMap;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;

import se.liu.ida.noahe116.tddd78.swarm.game.*;
import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;

public class Scene {
    private class RenderComponent {
        private Sprite sprite;
        private Entity entity;

        private RenderComponent(Sprite sprite, Entity entity) {
            this.sprite = sprite;
            this.entity = entity;
        }

        public void draw(Graphics2D g2d, double interpolation) {
            this.sprite.draw(this.entity, g2d, interpolation, Scene.this);
        }
    }

    private Game game;
    private AbstractMap<Entity, RenderComponent> renderComponents = new HashMap<>();
    private Camera camera;

    public Scene(Game game) {
        this.game = game;
        this.camera = new Camera(this.game.getPlayer().
            get(PositionComponent.class).getPosititon());
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

    public void drawImage(Graphics2D g2d, BufferedImage img,
                          Vector2D pos, double rotation) {
        Vector2D translatedPos = this.camera.translate(pos);
        int imageX = (int) (translatedPos.x - img.getWidth()/2);
        int imageY = (int) (translatedPos.y - img.getHeight()/2);
        AffineTransform oldTransform = g2d.getTransform();
        g2d.rotate(rotation, translatedPos.x, translatedPos.y);
        g2d.drawImage(img, imageX, imageY, null);
        g2d.setTransform(oldTransform);
    }

    public Camera getCamera() {
        return this.camera;
    }
}
