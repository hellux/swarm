package se.liu.ida.noahe116.tddd78.swarm.render;  

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.*;

import se.liu.ida.noahe116.tddd78.swarm.game.*;
import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;

public class PlayerSprite implements Sprite {
    BufferedImage base;
    public PlayerSprite() {
        try {
            this.base = ImageIO.read(new File("resources/img/ship_base.png"));
        } catch (IOException e) {
        }
    }

    public void draw(Entity entity, Graphics2D g2d, double interpolation) {
        PositionComponent posComp = entity.get(PositionComponent.class);
        this.drawImage(g2d, this.base, posComp.getPosititon(), posComp.getRotation()); 
    }

    private void drawImage(Graphics2D g2d, BufferedImage img,
                           Vector2D pos, double rotation) {
        Graphics2D imageGraphics = this.base.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate(img.getWidth()/4, img.getHeight()/4);
        at.rotate(rotation);
        imageGraphics.setTransform(at);
        imageGraphics.drawImage(this.base, img.getWidth()/3, img.getHeight()/2, null); 
        imageGraphics.dispose();
        g2d.drawImage(this.base, (int) pos.x, (int) pos.y, null);
    }
}
