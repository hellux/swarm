package se.liu.ida.noahe116.tddd78.swarm.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.event.*;
import java.lang.Thread;
import java.util.logging.*;
import java.util.function.Consumer;

import se.liu.ida.noahe116.tddd78.swarm.game.*;
import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;
import se.liu.ida.noahe116.tddd78.swarm.render.Scene;

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

    /**
     * Ratio of radius of the cursor's area to the min of the component's width and height.
     **/
    private static final double CURSOR_RADIUS_RATIO = 0.2;

    private Robot robot;
    private Game game;
    private Scene scene;
    private Thread thread;

    private Vector2D center;
    private int cursorAreaRadius;

    private double interpolation;
    
    public GamePanel() {
        this.setBackground(Color.BLACK);
        this.game = new Game();
        this.scene = new Scene(game);
        this.setKeyBinds();
        this.createComponentListener();

        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            LOGGER.log(Level.SEVERE, "failed to create robot for mouse: " + e.getMessage(), e);
        }

        this.thread = new Thread(this::gameLoop);
        this.thread.start();
    }

    /**
     * Update all variables that are dependent on the sive of the component.
     **/
    private void updateDimensions() {
        this.center = new Vector2D(this.getWidth()/2, this.getHeight()/2);
        this.cursorAreaRadius =
            (int) (CURSOR_RADIUS_RATIO*Math.min(this.getWidth(), this.getHeight()));
    }
    
    private void handleMouse() {
        Point2D mousePos = this.getMousePosition();
        if (mousePos != null) {
            Vector2D mouseVector = Vector2D.subtract(new Vector2D(mousePos), this.center);
            this.game.getPlayer().get(PlayerComponent.class).
                setThrustPower(mouseVector.magnitude()/cursorAreaRadius);
            this.game.getPlayer().get(PlayerComponent.class).
                setRotation(mouseVector.rotation());
            this.limitMouse(mouseVector);
        }
    }

    /**
     * Prevent the mouse from leaving a radius in the center of the screen.
     * <p>If the mouse is outside the radius it will be moved to the closest point
     * inside the radius.
     * @param mouseVector current position of the mouse relative to the center
     *                    of the panel.
     **/
    private void limitMouse(Vector2D mouseVector) {
        if (mouseVector.magnitude() > this.cursorAreaRadius) {
            Vector2D newMouse = Vector2D.add(this.center, Vector2D.add(
                Vector2D.fromLengthRotation(cursorAreaRadius,
                                            mouseVector.rotation()),
                new Vector2D(this.getLocationOnScreen())));
            this.robot.mouseMove((int) newMouse.x, (int) newMouse.y);
        }
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
                this.game.update();
                nextTick += TICK_PERIOD;
            } 
            
            this.interpolation = (double) (System.nanoTime() + TICK_PERIOD - nextTick)
                                        / TICK_PERIOD;
            this.repaint();
            this.handleMouse();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.scene.render((Graphics2D) g, this.interpolation);
    }

    private void setKeyBinds() {
        this.bindKeyToggle(KeyEvent.VK_SPACE,
            this.game.getPlayer().get(PlayerComponent.class)::setThrust);
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

    private void createComponentListener() {
        final ComponentListener dimensionUpdater = new ComponentListener() {
            public void componentResized(ComponentEvent e) {
                updateDimensions();
            }

            public void componentMoved(ComponentEvent e) {}
            public void componentShown(ComponentEvent e) {}
            public void componentHidden(ComponentEvent e) {}
        };

        this.addComponentListener(dimensionUpdater);
    }
}

