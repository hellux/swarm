package se.liu.ida.noahe116.tddd78.swarm.render.sprites;

import javax.imageio.*;
import javax.imageio.stream.*;
import java.io.*;
import java.awt.image.BufferedImage;
import java.util.logging.*;
import java.util.AbstractMap;
import java.util.HashMap;

import se.liu.ida.noahe116.tddd78.swarm.game.Entity;

public class Sprite {
    private static final Logger LOGGER = Logger.getLogger(Sprite.class.getName());
    
    private static final String imageDir = "resources/img/";

    protected final AbstractMap<String, BufferedImage> images = new HashMap<>();
    
    public Sprite(String...fileNames) {
        for (String fileName : fileNames) {
            String imagePath = imageDir + fileName;
            BufferedImage image = null;
            File imageFile = new File(imagePath);

            try {
                ImageInputStream iis = ImageIO.createImageInputStream(imageFile);
                if (iis != null) {
                    image = ImageIO.read(iis);
                } else {
                    LOGGER.log(Level.WARNING, "failed to create IIS for: " + imagePath); 
                }
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "failed to read image: " + imagePath, e);
            }

            this.images.put(fileName, image); 
        }
    }

    protected BufferedImage[] getImageArray(String...imageNames) {
        BufferedImage[] imageArray = new BufferedImage[imageNames.length];
        for (int i = 0; i < imageNames.length; i++) {
            imageArray[i] = this.images.get(imageNames[i]);
            if (imageNames[i] == null) {
                continue;
            }
            if (imageArray[i] == null) {
                LOGGER.log(Level.WARNING, "image is not loaded: " + imageNames[i]);
            }
        }
        return imageArray;
    }

    public BufferedImage[] getImages(Entity entity) {
        return this.images.values().toArray(new BufferedImage[0]);
    }
}
