package se.liu.ida.noahe116.tddd78.swarm.render;

import java.awt.Graphics2D;
import java.util.logging.*;
import java.util.AbstractMap;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.util.Map;

import se.liu.ida.noahe116.tddd78.swarm.game.*;
import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;
import se.liu.ida.noahe116.tddd78.swarm.game.components.PositionComponent;

/**
 * Handles visualization of the game.
 **/
public class Scene {
    private static final Logger LOGGER = Logger.getLogger(Scene.class.getName());

    private final Game game;
    private final AbstractMap<Entity, RenderComponent> renderComponents = new HashMap<>();
    private final Camera camera;

    public Scene(Game game) {
        this.game = game;
        this.camera = new Camera(game.getPlayer().get(PositionComponent.class));
    }

    /**
     * Render an interpolated frame of the game's current state.
     * @param interpolation ratio of time that has passed since the last tick to the
     *                      period between ticks.
     **/
    public void render(Graphics2D g2d, double interpolation) {
        this.camera.updateInterpolation(interpolation);
        this.addRenderComponents();
        this.drawRenderComponents(g2d, interpolation);
    }

    private void addRenderComponents() {
        for (Entity e : this.game.getEntities()) {
            if (true) { //TODO determine if entity needs to be drawn
                if (!renderComponents.containsKey(e)) {
                    RenderComponent rc = RcCreator.createRenderComponent(e);
                    renderComponents.put(e, rc); 
                }
            } else {
                renderComponents.remove(e);
            }
        }
    }

    private void drawRenderComponents(Graphics2D g2d, double interpolation) {
        this.renderComponents.entrySet()
                             .stream()
                             .sorted(Map.Entry.comparingByValue())
                             .forEach(entry -> {
            RenderComponent rc = entry.getValue();
            BufferedImage[] images = rc.getImages();
            PositionComponent posComp = rc.getPositionComponent();
            for (BufferedImage image : images) {
                if (image != null) {
                    this.drawImage(g2d,
                                   image,
                                   posComp,
                                   interpolation);
                }
            }
        });
    }

    private void drawRenderComponent(Graphics2D g2d, double interpolation, RenderComponent rc) {

    }

    
    private void drawImage(Graphics2D g2d,
                          BufferedImage img,
                          PositionComponent posComp,
                          double interpolation) {
        Vector2D translatedPos = this.camera.translate(posComp.futurePos(interpolation));
        this.drawImage(g2d, img,
                       (int) Math.round(translatedPos.x),
                       (int) Math.round(translatedPos.y), 
                       posComp.getRotation());   
    }

    private void drawImage(Graphics2D g2d, BufferedImage img,
                          int centerX, int centerY, double rotation) {
        int leftX = centerX - img.getWidth()/2;
        int topY = centerY - img.getHeight()/2;

        AffineTransform oldTransform = g2d.getTransform();

        g2d.translate(centerX, centerY);
        g2d.scale(this.camera.getScale(), this.camera.getScale());
        g2d.rotate(rotation);
        g2d.translate(-centerX, -centerY);

        g2d.drawImage(img, leftX, topY, null);

        g2d.setTransform(oldTransform);
    }

    public Camera getCamera() {
        return this.camera;
    }
}
