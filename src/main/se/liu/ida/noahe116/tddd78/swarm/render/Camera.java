package se.liu.ida.noahe116.tddd78.swarm.render;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;

public class Camera {
    private static double SCALE = 10;
    private Vector2D position;
    private Vector2D size = new Vector2D();

    public Camera(Vector2D position) {
        this.position = position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public void updateSize(int componentWidth, int componentHeight) {
        this.size.x = componentWidth/SCALE;
        this.size.y = componentHeight/SCALE;
    }

    public Vector2D getPosititon() {
        return this.position;
    }
    
    public Vector2D getSize() {
        return this.size;
    }

    public double getScale() {
        return SCALE;
    }

    /**
     * Translate game coordinates to component coordinates.
     * @param gameCoordinate a position in the game's coordinate system. 
     * @return same position in the component's coordinate system.
     **/
    public Vector2D translate(Vector2D gameCoordinate) {
        Vector2D componentOrigo = Vector2D.subtract(this.position,
                                                    Vector2D.multiply(this.size, 0.5));
        Vector2D translatedCoordinate = Vector2D.multiply(
            Vector2D.subtract(gameCoordinate, componentOrigo),
            SCALE);
        return translatedCoordinate;
    }
}
