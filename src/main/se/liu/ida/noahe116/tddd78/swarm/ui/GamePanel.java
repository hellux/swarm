package se.liu.ida.noahe116.tddd78.swarm.ui;

import javax.swing.*;
import java.awt.event.*;
import java.lang.Thread;
import java.util.logging.*;
import java.util.function.Consumer;

import se.liu.ida.noahe116.tddd78.swarm.game.Game;

public class GamePanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(GamePanel.class.getName());

    private static final long NANOSECONDS_PER_SECOND = 1000000000;
    private static final long MILLISECONDS_PER_SECOND = 1000;

    /**
     * Amount of times the game will be updated (tick) per second.
     **/
    private static final int TICKRATE = 25;
    
    /**
     * Time between ticks in milliseconds.
     **/
    private static final long TICK_PERIOD = NANOSECONDS_PER_SECOND/ TICKRATE;

    /**
     * Maximum frames per second that will be rendered.
     **/
    private static final int MAX_FPS = 120;

    /**
     * Minimum time between frames in milliseconds.
     **/
    private static final long MIN_FRAME_PERIOD = NANOSECONDS_PER_SECOND / MAX_FPS;

    private Game game;
    private Thread thread;
    
    public GamePanel() {
        this.game = new Game();
        this.setKeyBinds();

        this.thread = new Thread(this::gameLoop);
        this.thread.start();
    }

    /**
     * Render an interpolated frame of the game's current state.
     * @param interpolation ration of time that has been passed since last tick until next.
     **/
    private void drawGame(double interpolation) {
        System.out.println("x: " + this.game.getPlayer().getX() + ", int: " + interpolation); 
    }
    
    private void sleepUntil(long time) {
        long sleepPeriod = time - System.nanoTime();

        if (sleepPeriod >= 0) {
            final long NANOSECONDS_PER_MILLISECOND = NANOSECONDS_PER_SECOND
                                                   / MILLISECONDS_PER_SECOND;
            try {
                Thread.sleep(sleepPeriod / NANOSECONDS_PER_MILLISECOND);
            } catch (InterruptedException e) {
                LOGGER.log(Level.WARNING, e.toString(), e); 
            }
        }
    }

    /**
     * Run the main loop that updates and draws the game.
     * <p> The game is updated depending on the TICKRATE constant. The drawing
     * of the game, however, is done as fast as possible (given it doesn't exceed
     * the max frame rate). Drawing the game state between updates of the game is
     * done by interpolation. The framerate must be higher than the game's tickrate.
     **/
    private void gameLoop() {
        long nextTick = System.nanoTime();
        long nextFrame = nextTick;

        double interpolation;
        
        while (true) {
            sleepUntil(nextFrame);
            nextFrame = System.nanoTime() + MIN_FRAME_PERIOD;

            if (System.nanoTime() > nextTick) {
                this.game.tick();
                nextTick += TICK_PERIOD;
            } 
            
            interpolation = (double) (System.nanoTime() + TICK_PERIOD - nextTick )
                            / TICK_PERIOD;
            this.drawGame(interpolation);
        }
    }

    private void setKeyBinds() {
        this.bindKeyToggle(KeyEvent.VK_SPACE, this.game.getPlayer()::setThrust);
    }
    
    private void bindKeyToggle(int keyCode, Consumer<Boolean> bind) {
        this.bindKey(KeyStroke.getKeyStroke(keyCode, 0, false), () -> bind.accept(true));
        this.bindKey(KeyStroke.getKeyStroke(keyCode, 0, true), () -> bind.accept(false));
    }

    private void bindKey(KeyStroke keyStroke, Runnable bind) {
        final Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bind.run();
            }
        };

        int key = keyStroke.hashCode() ^ bind.hashCode();

        this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, key);
        this.getActionMap().put(key, action);
    }
}
