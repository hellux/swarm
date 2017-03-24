package se.liu.ida.noahe116.tddd78.swarm.render;  

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;

import se.liu.ida.noahe116.tddd78.swarm.game.*;

public class PlayerSprite implements Sprite {
    private BufferedImage base;

    public PlayerSprite() {
        try {
            this.base = ImageIO.read(new File("resources/img/ship_base.png"));
        } catch (IOException e) {

        }
    }

    public void draw(Entity entity, Graphics2D g2d, double interpolation, Scene scene) {
        scene.drawImage(g2d, this.base, entity.get(PositionComponent.class), interpolation);
    }
}
