package se.liu.ida.noahe116.tddd78.swarm.render;

import java.awt.image.BufferedImage;

import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;
import se.liu.ida.noahe116.tddd78.swarm.render.sprites.Sprite;

public class AnimatedRenderComponent extends RenderComponent {
    private long startTime;
    private long length;

    public AnimatedRenderComponent(Sprite sprite,
                                   Entity e,
                                   RenderPriority priority,
                                   int length) {
        super(sprite, e, priority);
        this.startTime = System.nanoTime();
        this.length = length*100000;
    }

    @Override
    public BufferedImage[] getImages() {
        BufferedImage[] images = this.sprite.getImages(this.entity);
        BufferedImage image = null;

        int index = (int) ((System.nanoTime() - this.startTime) / this.length);
        System.out.println(index + ", " + images.length);
        if (index < images.length) {
            image = images[index];
        }

        return new BufferedImage[] {image};
    }
}
