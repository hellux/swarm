package se.liu.ida.noahe116.tddd78.swarm.render.sprites;

import javax.imageio.*;
import javax.imageio.stream.*;
import java.io.*;
import java.awt.image.BufferedImage;
import java.util.logging.*;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;

/**
 * Load images from disk and store them.
 **/
public class Sprite {
    private static final Logger LOGGER = Logger.getLogger(Sprite.class.getName());
    
    private static final String IMAGE_DIR = "resources/img/";

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

    /**
     * Retrieve all images of the sprite in alphabetical order.
     * @param entity entity that sprite is drawn on.
     * @return array of the sprite's buffered images
     **/
    public BufferedImage[] getImages(Entity entity) {
        List<String> fileNames = new ArrayList<>(this.images.keySet());
        Collections.sort(fileNames);
        BufferedImage[] images = new BufferedImage[this.images.size()];
        for (int i = 0; i < this.images.size(); i++) {
            images[i] = this.images.get(fileNames.get(i));
        }
        return images;
    }
}
