package se.liu.ida.noahe116.tddd78.swarm.render;

import java.awt.image.BufferedImage;

import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;
import se.liu.ida.noahe116.tddd78.swarm.render.sprites.Sprite;

/**
 * RenderComponent with alternating images.
 * Currently unused because my animation sprites look terrible.
 **/
public class AnimatedRenderComponent extends RenderComponent {
    private static int NANOSECONDS_PER_MILLISECOND = 1000000;
    private long startTime;
    private long length;

    /**
     * @param sprite sprite of rc
     * @param e entity of rc
     * @param priority render priority of rc
     * @param length time animation will play in milliseconds
     **/
    public AnimatedRenderComponent(Sprite sprite,
                                   Entity e,
                                   RenderPriority priority,
                                   int length) {
        super(sprite, e, priority);
        this.startTime = System.nanoTime();
        this.length = length*NANOSECONDS_PER_MILLISECOND;
    }

    @Override
    public BufferedImage[] getImages() {
        BufferedImage[] images = this.sprite.getImages(this.entity);
        BufferedImage image = null;

        int index = (int) ((System.nanoTime() - this.startTime) / this.length);
        if (index < images.length) {
            image = images[index];
        }

        return new BufferedImage[] {image};
    }
}
