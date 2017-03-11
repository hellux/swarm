package se.liu.ida.noahe116.tddd78.swarm.ui;

import javax.swing.JPanel;
import java.lang.Thread;

import se.liu.ida.noahe116.tddd78.swarm.game.Game;

public class GamePanel extends JPanel {
    /**
     * Amount of times the game will be updated (tick) per second.
     **/
    private static final int TICKRATE = 25;
    
    /**
     * Time between ticks in milliseconds.
     **/
    private static final double TICK_PERIOD = 1000.0 / TICKRATE;

    /**
     * Maximum frames per second that will be rendered.
     **/
    private static final int MAX_FPS = 120;

    /**
     * Minimum time between frames in milliseconds.
     **/
    private static final double MIN_FRAME_PERIOD = 1000.0 / MAX_FPS;

    private Game game;
    
    public GamePanel() {
        this.game = new Game();
    }

    /**
     * Render an interpolated frame of the game's current state.
     * @param interpolation ration of time that has been passed since last tick until next.
     **/
    private void drawGame(double interpolation) {

    }
    
    /**
     * Run the main loop that updates and draws the game.
     *
     * <p> The game is updated dependent on the TICKRATE constant. The drawing
     * of the game, however, is done as fast as possible (given it doesn't exceed
     * the max frame rate). Drawing the game state between updates of the game is
     * done by interpolation.
     *
     **/
    private void gameLoop() {
        double nextTick = System.currentTimeMillis();
        double nextFrame = nextTick;
        double sleepPeriod;
        double interpolation;

        while (true) {
            nextFrame = System.currentTimeMillis() + MIN_FRAME_PERIOD;
            if (System.currentTimeMillis() > nextTick) {
                this.game.tick();
                nextTick += TICK_PERIOD;
            } else {
                nextTick = System.currentTimeMillis();
            }
            
            interpolation = (System.currentTimeMillis() + TICK_PERIOD - nextTick )
                            / TICK_PERIOD;
            this.drawGame(interpolation);

            sleepPeriod = nextFrame - System.currentTimeMillis();
            if (sleepPeriod >= 0) {
                try {
                    Thread.sleep((long) sleepPeriod);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("sleep was interrupted: " + e.getMessage());
                }
            } 
        }
    }
}
