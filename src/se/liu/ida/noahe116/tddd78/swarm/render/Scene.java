package se.liu.ida.noahe116.tddd78.swarm.render;

import java.util.logging.*;
import java.awt.*;
import java.util.HashMap;
import java.util.AbstractMap;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;

import se.liu.ida.noahe116.tddd78.swarm.game.*;
import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;

/**
 * Handles visualization of the game.
 **/
public class Scene {
    private static final Logger LOGGER = Logger.getLogger(Scene.class.getName());

    private final Game game;
    private final AbstractMap<Entity, RenderComponent> renderComponents; 
    private final Camera camera;

    private Vector2D cameraInterpolation;

    public Scene(Game game) {
        this.game = game;
        this.renderComponents = new HashMap<>();
        this.camera = new Camera(game.getPlayer().get(PositionComponent.class));
    }

    /**
     * Render an interpolated frame of the game's current state.
     * @param interpolation ratio of time that has passed since the last tick to the
     *                      period between ticks.
     **/
    public void render(Graphics2D g2d, double interpolation) {
        this.updateCameraInterpolation(interpolation);
        this.addRenderComponents();
        this.drawRenderComponents(g2d, interpolation);
    }

    private void updateCameraInterpolation(double interpolation) {
        PositionComponent posComp = this.camera.getPositionComponent();
        this.cameraInterpolation = Vector2D.subtract(posComp.futurePos(interpolation),
                                                     posComp.getPosition());
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
        for (RenderComponent rc : this.renderComponents.values()) {
            BufferedImage[] images = rc.getImages();
            PositionComponent posComp = rc.getPositionComponent();
            for (BufferedImage image : images) {
                if (image != null) {
                    Scene.this.drawImage(g2d,
                                         image,
                                         posComp,
                                         interpolation);
                }
            }
        }
    }

    private Vector2D interpolate(PositionComponent posComp, double interpolation) {
        Vector2D interpolatedPos = posComp.futurePos(interpolation); 
        return Vector2D.subtract(interpolatedPos, this.cameraInterpolation);
    }

    public void drawImage(Graphics2D g2d,
                          BufferedImage img,
                          PositionComponent posComp,
                          double interpolation) {
        //TEMP indicator
        Vector2D cam = this.camera.translate(this.interpolate(new PositionComponent(), interpolation));
        this.drawImage(g2d, img, (int) Math.round(cam.x), (int) Math.round(cam.y), 0);

        Vector2D interpolatedPos = this.interpolate(posComp, interpolation);
        Vector2D translatedPos = this.camera.translate(interpolatedPos);
        this.drawImage(g2d, img,
                       (int) Math.round(translatedPos.x),
                       (int) Math.round(translatedPos.y), 
                       posComp.getRotation());   
    }

    public void drawImage(Graphics2D g2d, BufferedImage img,
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
