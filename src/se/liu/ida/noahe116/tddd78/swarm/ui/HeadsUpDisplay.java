package se.liu.ida.noahe116.tddd78.swarm.ui;

import java.awt.*;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;
import se.liu.ida.noahe116.tddd78.swarm.game.level.*;
import se.liu.ida.noahe116.tddd78.swarm.game.components.*;

/**
 * Draw a heads up display over the game.
 **/
public class HeadsUpDisplay {
    private static final Color OBJ_IND_COLOR = new Color(0, 89, 170);
    private static final Color CURSOR_COLOR = Color.RED;
    private static final double diameterRatio = 1./30;
    private static final double OBJ_IND_POS = 0.3;
    private static final int SHIELD_BAR_THICKNESS = 4;

    private final GameLevel gameLevel;
    private final PlayerComponent playerComponent;

    private int width;
    private int height;
    private Vector2D center;
    private int cursorAreaRadius;

    public HeadsUpDisplay(GameLevel level, PlayerComponent pc) {
        this.gameLevel = level;
        this.playerComponent = pc;
    }

    public void updateDimensions(int w, int h, int cursorAreaRadius) {
        this.width = w;
        this.height = h;
        this.center = new Vector2D(width/2, height/2);
        this.cursorAreaRadius = cursorAreaRadius;
    }

    public void draw(Graphics g, double extrapolation, Vector2D mousePos) {
        this.drawCursor(g, mousePos);
        this.drawObjective(g, extrapolation);
        this.drawShield(g);
        this.drawLives(g);
    }

    private void drawCircle(Graphics g, Vector2D pos, Color color) {
        g.setColor(color);
        int diameter = (int) (this.cursorAreaRadius*diameterRatio);
        g.drawOval((int) pos.x-diameter/2, (int) pos.y-diameter/2, diameter, diameter); 
    }

    private void drawObjective(Graphics g, double extrapolation) {
        Vector2D dir = this.gameLevel.getObjectiveDirection(extrapolation);
        if (dir != null) {
            Vector2D pos = Vector2D.add(
                this.center,
                Vector2D.fromLengthRotation(this.cursorAreaRadius*OBJ_IND_POS, dir.rotation()) 
            );
            this.drawCircle(g, pos, OBJ_IND_COLOR);
        }
    }

    private void drawShield(Graphics g) {
        double shield = this.playerComponent.shieldStrength();
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, this.width, SHIELD_BAR_THICKNESS);
        g.setColor(Color.MAGENTA);
        g.fillRect(0, 0, (int) (this.width*shield), SHIELD_BAR_THICKNESS);
    }

    private void drawLives(Graphics g) {
        int lives = this.playerComponent.extraLives();
        g.setColor(Color.WHITE);
        g.drawString("Lives: " + lives, 0, 20);
    }

    private void drawCursor(Graphics g, Vector2D mousePos) {
        if (mousePos != null) {
            this.drawCircle(g, mousePos, CURSOR_COLOR);
        }
    }
}
