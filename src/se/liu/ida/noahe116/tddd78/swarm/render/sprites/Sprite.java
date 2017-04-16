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
            InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(fileName);
            if (is == null) {
                try {
                    is = new FileInputStream(imageDir + fileName);
                    LOGGER.log(Level.INFO, fileName + " found in resource folder."); 
                } catch (FileNotFoundException e) {
                    LOGGER.log(Level.WARNING, "failed to read file: " + fileName, e);
                }
            } else {
                LOGGER.log(Level.INFO, fileName + " found in JAR file.");
            }
            
            BufferedImage image = null;

            try {
                ImageInputStream iis = ImageIO.createImageInputStream(is);
                if (iis != null) {
                    image = ImageIO.read(iis);
                } else {
                    LOGGER.log(Level.WARNING, "failed to create IIS for: " + fileName); 
                }
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "failed to read image: " + fileName, e);
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
