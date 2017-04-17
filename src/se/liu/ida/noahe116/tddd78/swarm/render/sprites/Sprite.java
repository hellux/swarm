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
    
    private static final String IMAGE_DIR = "resources/img/";

    static {
        
    }

    protected final AbstractMap<String, BufferedImage> images = new HashMap<>();
    
    public Sprite(String...fileNames) {
        for (String fileName : fileNames) {
            BufferedImage image = this.readImage(fileName);
            this.images.put(fileName, image); 
        }
    }

    private BufferedImage readImage(String fileName) {
            BufferedImage image = this.readImageFromResource(fileName);
            if (image == null) image = this.readImageFromFile(fileName);
            if (image == null) LOGGER.log(Level.WARNING, "failed to read image " + fileName);
            return image;
    }

    private BufferedImage readImageFromResource(String fileName) {
        BufferedImage image = null;
        InputStream is = Thread.currentThread().getContextClassLoader()
            .getResourceAsStream(fileName);

	if (is != null) try (ImageInputStream iis = ImageIO.createImageInputStream(is)) {
	    image = ImageIO.read(iis);
        }
	catch (IOException e) {
	    LOGGER.log(Level.FINE, "failed to read " + fileName + " from resource", e);
        }

        return image;
    }

    private BufferedImage readImageFromFile(String fileName) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(IMAGE_DIR + fileName));
        } catch (IOException e){
            LOGGER.log(Level.FINE, "failed to read file " + fileName, e);
        }

        return image;
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
