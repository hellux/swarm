package se.liu.ida.noahe116.tddd78.swarm.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.event.*;
import java.util.logging.*;
import java.util.function.Consumer;
import java.awt.image.BufferedImage;

import se.liu.ida.noahe116.tddd78.swarm.game.*;
import se.liu.ida.noahe116.tddd78.swarm.game.components.PlayerComponent;
import se.liu.ida.noahe116.tddd78.swarm.render.*;
import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;

@SuppressWarnings("serial")
public final class GamePanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(GamePanel.class.getName());

    private static final long NANOSECONDS_PER_SECOND = 1000000000;
    private static final long MILLISECONDS_PER_SECOND = 1000;

    /**
     * Amount of times the gameLevel will be updated (tick) per second.
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

    private Robot robot = null;
    private final GameLevel gameLevel;
    private final Scene scene;
    private final Thread thread;
    private final PlayerComponent playerComponent;

    private Vector2D center = null;
    private int cursorAreaRadius;

    private double interpolation;
    private long delay;

    private boolean showFPS = true;
    
    public GamePanel() {
        this.setBackground(Color.BLACK);
        this.gameLevel = new GameLevel();
        this.playerComponent = this.gameLevel.getPlayer().get(PlayerComponent.class);
        this.scene = new Scene(gameLevel);
        
        this.setKeyBinds();
        this.setMouseBinds();
        this.createComponentListener();
        this.hideCursor();

        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            LOGGER.log(Level.SEVERE, "failed to create robot for mouse: " + e.getMessage(), e);
        }

        this.thread = new Thread(this::gameLoop);
        this.thread.start();
    }

    /**
     * Update all variables that are dependent on the size of the component.
     **/
    private void updateDimensions() {
        int width = this.getWidth();
        int height = this.getHeight();
        int min = Math.min(width, height);
        final double SCALE_FACTOR = 1920;

        this.center = new Vector2D(width/2, height/2);
        this.cursorAreaRadius =
            (int) (CURSOR_RADIUS_RATIO*min);
        this.scene.getCamera().changeScale(min/SCALE_FACTOR);
        this.scene.getCamera().updateSize(width, height);
    }
    
    private void handleMouse() {
        /*
         * mousePos is relative to component
         **/
        Point2D mousePos = this.getMousePosition();
        if (mousePos != null) {
            Vector2D mouseVector = Vector2D.subtract(new Vector2D(mousePos), this.center);
            this.playerComponent.setThrustPower(mouseVector.magnitude()/cursorAreaRadius);
            this.playerComponent.setRotation(mouseVector.rotation());
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
            try {
                final long NANOSECONDS_PER_MILLISECOND = NANOSECONDS_PER_SECOND
                                                         / MILLISECONDS_PER_SECOND;
                Thread.sleep(sleepPeriod / NANOSECONDS_PER_MILLISECOND);
            } catch (InterruptedException e) {
                LOGGER.log(Level.WARNING, e.getMessage(), e); 
            }
        }
    }

    /**
     * Run the main loop that updates and draws the gameLevel.
     * <p> The gameLevel is updated depending on the TICKRATE constant. The drawing
     * of the gameLevel, however, is done as fast as possible (given it doesn't exceed
     * the max frame rate). Drawing the gameLevel state between updates of the gameLevel is
     * done by interpolation. The max framerate must be higher than max the gameLevel's tickrate.
     **/
    private void gameLoop() {
        long nextTick = System.nanoTime();
        long lastFrame = nextTick;
        long currentFrame = nextTick;
        long nextFrame = nextTick;

        while (true) {
            sleepUntil(nextFrame);

            currentFrame = System.nanoTime();
            this.delay = currentFrame - lastFrame;
            lastFrame = currentFrame;
            nextFrame = currentFrame + MIN_FRAME_PERIOD;

            if (currentFrame > nextTick) {
                this.gameLevel.update();
                nextTick += TICK_PERIOD;
            } 
            
            this.interpolation = (double) (System.nanoTime() + TICK_PERIOD - nextTick)
                                        / TICK_PERIOD;
            this.handleMouse();
            this.repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.scene.render((Graphics2D) g, this.interpolation);
        this.drawCursor(g);
        if (this.showFPS) {
            this.displayFPS(g);
        }
    }

    private void displayFPS(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString(Integer.toString(
            (int) (NANOSECONDS_PER_SECOND/this.delay)),
            0, g.getFont().getSize()
        );
    }

    private void drawCursor(Graphics g) {
        Point mousePos = this.getMousePosition();
        if (mousePos != null) {
            g.setColor(Color.RED);
            int diameter = this.cursorAreaRadius/15;
            g.drawOval(mousePos.x-diameter/2, mousePos.y-diameter/2, diameter, diameter); 
            }
    }

    private void setKeyBinds() {
        this.bindKeyToggle(KeyEvent.VK_SPACE, this.playerComponent::setThrust);
        this.bindKey(KeyStroke.getKeyStroke("F3"), () -> this.showFPS = !this.showFPS);
    }

    private void setMouseBinds() {
        this.bindMouseToggle(MouseEvent.BUTTON1, this.playerComponent::firePrimary);
        this.bindMouseToggle(MouseEvent.BUTTON2, this.playerComponent::fireSecondary);
    }

    private void bindMouseToggle(int button, Consumer<Boolean> bind) {
        final MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == button) bind.accept(true);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == button) bind.accept(false);
            }
        };
        this.addMouseListener(adapter);
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

        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(keyStroke, key);
        this.getActionMap().put(key, action);
    }

    private void createComponentListener() {
        final ComponentListener dimensionUpdater = new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                GamePanel.this.updateDimensions();
            }
        };

        this.addComponentListener(dimensionUpdater);
    }
    
    /**
     * Hide window manager's cursor.
     * The cursor is hidden and redrawn in the gameLevel loop to make sure
     * the gameLevel's cursor can be in sync with the gameLevel loop.
     **/
    private void hideCursor() {
        BufferedImage cursorImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(
            cursorImage, new Point(0, 0), "blank");
        this.setCursor(cursor);
    }
}

