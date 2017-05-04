package se.liu.ida.noahe116.tddd78.swarm.render;

import se.liu.ida.noahe116.tddd78.swarm.common.*;
import se.liu.ida.noahe116.tddd78.swarm.game.components.PositionComponent;
import se.liu.ida.noahe116.tddd78.swarm.game.level.*;

/**
 * Camera of the scene.
 * Can be attached to an entity by using it's position component.
 * Interpolation should be updated once per frame.
 **/
public class Camera {
    /**
     * Center of camera's view.
     **/
    private PositionComponent posComp;

    /**
     * Vector representing size of camera view
     **/
    private Vector2D size = new Vector2D();

    /**
     * Scale of game coordinates relative to game coordinates.
     * scale * size = component size
     **/
    private double scale;
    private Vector2D interpolatedPos = null;

    public Camera(PositionComponent posComp) {
        this.posComp = posComp;
    }

    public void setPositionComponent(PositionComponent posComp) {
        this.posComp = posComp;
    }

    public void changeScale(double scale) {
        this.size.x *= scale / this.scale;
        this.size.y *= scale / this.scale;
        this.scale = scale;
    }

    public void updateSize(int componentWidth, int componentHeight) {
        this.size.x = componentWidth/this.scale;
        this.size.y = componentHeight/this.scale;
    }

    /**
     * Update the interpolated position of the camera.
     * Should only be done once every frame, more is unnecessary.
     * @param interpolation ratio of passed time since last tick
     *                      until the next.
     **/
    public void updateInterpolation(double interpolation) {
        this.interpolatedPos = this.posComp.futurePos(interpolation);
    }

    public PositionComponent getPositionComponent() {
        return this.posComp;
    }

    public double getScale() {
        return this.scale;
    }

    /**
     * Translate game coordinates to component coordinates.
     * <pre> {@code
     * 
     *  Oc    x         S.x
     *   +---->-----------------------------.
     *   |         Og   x                   |
     * y |          +-->                    |
     *   V          |                       | 
     *   |        y V     . C               | S.y
     *   |               .                  |
     *   |                P                 |
     *   |                                  |
     *   |__________________________________|
     *
     *   Oc: component's origin
     *   Og: game's origin
     *   C: camera position
     *   P: game coordinates
     *   S: this.size (camera view)
     *   S * scale = component size (in pixels)
     *
     *   Position of game coordinates on component: 
     *   P' = scale * (P - (C-S/2))
     *
     *   Example 1: camera position C
     *   C' = scale * (C - (C-S/2)) = scale * S/2 
     *      = component size / 2 (center of component)
     *
     *   Example 2: component's origin Oc = C-S/2
     *   Oc' = scale * ((C-S/2) - (C-S/2)) = scale * (0, 0) 
     *       = (0, 0) (origin, top left corner)
     *
     *   } </pre>
     * @param gameCoordinate a position in the game's coordinate system. 
     * @param level current game level
     * @return same position in the component's coordinate system.
     **/
    public Vector2D translate(Vector2D gameCoordinate, GameLevel level) {
        Vector2D wrappedPos = level.wrapAround(this.posComp.getPosition(), gameCoordinate);
        Vector2D componentOrigin = Vector2D.subtract(this.interpolatedPos,
                                                     Vector2D.multiply(this.size, 0.5));
        Vector2D translatedCoordinate = Vector2D.multiply(
            Vector2D.subtract(wrappedPos, componentOrigin),
            this.scale);
        return translatedCoordinate;
    }
}
