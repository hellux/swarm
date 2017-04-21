package se.liu.ida.noahe116.tddd78.swarm.render;

import se.liu.ida.noahe116.tddd78.swarm.common.*;
import se.liu.ida.noahe116.tddd78.swarm.game.components.PositionComponent;

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

    public void updateInterpolation(double interpolation) {
        this.interpolatedPos = this.posComp.futurePos(interpolation);
    }

    public PositionComponent getPositionComponent() {
        return this.posComp;
    }
    
    public Vector2D getSize() {
        return this.size;
    }

    public double getScale() {
        return this.scale;
    }
    
    /**
     + Wrap a coordinate around the game level if it's on the other side.
     * <pre> {@code
     *
     * Legend:
     *  ~: camera view
     *  -: outside camera view
     *  |: world border
     *  ): camera max
     *  (: camera min
     *
     * Cases for points (P):
     *
     *  trivial (no wrap needed):
     *
     *      |---(~~~~~~C~~~~~~)--------|
     *      0                          S
     *
     *      C_min < P < C_max   =>   P = P
     *  
     *  non-trivial:
     *      |~~~C~~~~~~)-----------(~~~|
     *      0                          S
     *  
     *      C, P < C_max < C_min   =>   P = P
     *      C < C_max < C_min < P   =>   P -= S (*)
     *
     *      |~~~)-----------(~~~~~~C~~~|
     *      0                          S
     *
     *      C_max < C_min < P, C   =>   P = P
     *      P < C_max < C_min < C   =>   P += S (*)
     *
     *  other cases:
     *      unimportant as they are outside of camera's view
     *
     * } </pre>
     * @param gameCoordinate a position in the game's coordinate system. 
     * @return same position but eventually wrapped around.
     **/
    private double wrapAround(double gameCoordinate, boolean axis) {
        double levelSize = this.posComp.getEntity().getGameLevel().getSize(); 
        double cameraPos = axis ? this.posComp.getPosition().x
                                : this.posComp.getPosition().y;
        double length = (axis ? this.size.x : this.size.y) / 2;

        double cameraMin = Math2.floorMod(cameraPos - length - 200, levelSize);
        double cameraMax = Math2.floorMod(cameraPos + length + 200, levelSize);

        if (cameraMax < cameraMin) {
            if (cameraPos < cameraMax && cameraMax < gameCoordinate) {
                return gameCoordinate - levelSize;
            }
            if (gameCoordinate < cameraMax && cameraMax < cameraPos) {
                return gameCoordinate + levelSize;
            }
        }

        return gameCoordinate;
    }

    public Vector2D wrapAround(Vector2D gameCoordinate) {
        return new Vector2D(
            this.wrapAround(gameCoordinate.x, true),
            this.wrapAround(gameCoordinate.y, false)
        );
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
     * @return same position in the component's coordinate system.
     **/
    public Vector2D translate(Vector2D gameCoordinate) {
        Vector2D componentOrigin = Vector2D.subtract(this.interpolatedPos,
                                                     Vector2D.multiply(this.size, 0.5));
        Vector2D translatedCoordinate = Vector2D.multiply(
            Vector2D.subtract(gameCoordinate, componentOrigin),
            this.scale);
        return translatedCoordinate;
    }
}
