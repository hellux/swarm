package se.liu.ida.noahe116.tddd78.swarm.render;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map.Entry;
import java.util.logging.*;
import java.util.AbstractMap;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;
import se.liu.ida.noahe116.tddd78.swarm.game.components.PositionComponent;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;
import se.liu.ida.noahe116.tddd78.swarm.game.level.*;

/**
 * Handles visualization of the game.
 **/
public class Scene {
    private static final Logger LOGGER = Logger.getLogger(Scene.class.getName());

    @SuppressWarnings({"unchecked", "serial", "rawtypes"})
    private static final AbstractMap<LevelType, Color> BG_COLORS = new EnumMap(LevelType.class) {{
        put(LevelType.HARVEST, new Color(0, 0, 20));
        put(LevelType.ELIMINATE, new Color(31, 0, 0));
        put(LevelType.LOOT, new Color(16, 0, 31));
    }};

    private final GameLevel gameLevel;
    private final AbstractMap<Entity, RenderComponent> renderComponents = new HashMap<>();
    private final Camera camera;

    public Scene(GameLevel gameLevel) {
        this.gameLevel = gameLevel;
        this.camera = new Camera(gameLevel.getPlayer().get(PositionComponent.class));
    }

    /**
     * Render an interpolated frame of the gameLevel's current state.
     * @param interpolation ratio of time that has passed since the last tick to the
     *                      period between ticks.
     **/
    public void render(Graphics2D g2d, double interpolation) {
        this.drawBackground(g2d);
        this.camera.updateInterpolation(interpolation);
        this.addRenderComponents();
        this.drawRenderComponents(g2d, interpolation);
    }

    private void drawBackground(Graphics2D g2d) {
        Rectangle clip = g2d.getClipBounds();
        g2d.setColor(BG_COLORS.get(this.gameLevel.getType()));
        g2d.fillRect(0, 0, (int) clip.getWidth(), (int) clip.getHeight());
    }

    private void addRenderComponents() {
        this.renderComponents.clear();

        //FIXME sometimes causes concurrency exception at start
        for (Entity e : this.gameLevel.getEntities()) {
            if (this.shouldDraw(e)) { 
                if (!this.renderComponents.containsKey(e)) {
                    RenderComponent rc = RcCreator.createRenderComponent(e);
                    if (rc != null) this.renderComponents.put(e, rc); 
                }
            }
        }
    }

    private boolean shouldDraw(Entity e) {
        return !e.isKilled();
    }

    private void drawRenderComponents(Graphics2D g2d, double interpolation) {
        this.renderComponents.entrySet()
                             .stream()
                             .sorted(Entry.comparingByValue())
                             .forEach(entry -> {
            RenderComponent rc = entry.getValue();
            BufferedImage[] images = rc.getImages();
            PositionComponent pc = rc.getPositionComponent();
            for (BufferedImage image : images) {
                if (image != null) {
                    this.drawImage(g2d,
                                   image,
                                   pc,
                                   interpolation);
                }
            }
        });
    }

    private void drawImage(Graphics2D g2d,
                          BufferedImage img,
                          PositionComponent pc,
                          double interpolation) {
        Vector2D translatedPos = this.camera.translate(pc.futurePos(interpolation),
                                                       this.gameLevel);
        this.drawImage(g2d, img,
                       (int) Math.round(translatedPos.x),
                       (int) Math.round(translatedPos.y), 
                       pc.futureRot(interpolation));   
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
