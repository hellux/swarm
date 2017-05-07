package se.liu.ida.noahe116.tddd78.swarm.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.event.*;
import java.util.Map.Entry;
import java.util.logging.*;
import java.util.function.Consumer;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import se.liu.ida.noahe116.tddd78.swarm.game.components.*;
import se.liu.ida.noahe116.tddd78.swarm.game.level.*;
import se.liu.ida.noahe116.tddd78.swarm.render.*;
import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;

/**
 * Handle inputs and graphics canvas of game.
 **/
@SuppressWarnings("serial")
public final class GamePanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(GamePanel.class.getName());

    private static final long NANOSECONDS_PER_SECOND = 1000000000;
    private static final long MILLISECONDS_PER_SECOND = 1000;
    private static final long NANOSECONDS_PER_MILLISECOND = NANOSECONDS_PER_SECOND
                                                            / MILLISECONDS_PER_SECOND;

    /**
     * Amount of times the gameLevel will be updated (tick) per second.
     **/
    private static final int NORMAL_TICKRATE = 40;
    private static final int SLOW_TICKRATE = 2;
    private int tickrate = NORMAL_TICKRATE;

    /**
     * Maximum frames per second that will be rendered.
     **/
    private static final int MAX_FPS = 120;
    private static final long MIN_FRAME_PERIOD = NANOSECONDS_PER_SECOND / MAX_FPS;

    /**
     * Ratio of radius of the cursor's area to the min of the component's width and height.
     **/
    private static final double CURSOR_RADIUS_RATIO = 0.3;
    private static final double SCALE_FACTOR = 1920;

    private Robot robot = null;
    private GameLevel gameLevel = null;
    private Scene scene = null;
    private HeadsUpDisplay hud = null;
    private PlayerComponent playerComponent = null;

    private Vector2D center = null;
    private int cursorAreaRadius;

    private double extrapolation;
    private long delay;

    private boolean quit = false;
    private boolean gameActive = false;
    private boolean showFPS = false;

    private Map<Runnable, Long> taskSchedule = new ConcurrentHashMap<>();

    public GamePanel() {
        this.setBackground(Color.BLACK);
        this.createComponentListener();
        this.hideCursor();

        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            LOGGER.log(Level.SEVERE, "failed to create robot for mouse: " + e.getMessage(), e);
        }
    }

    public LevelStatus runGame(GameLevel gameLevel) {
        this.gameLevel = gameLevel;
        this.playerComponent = this.gameLevel.getPlayer().get(PlayerComponent.class);
        this.scene = new Scene(gameLevel);
        this.hud = new HeadsUpDisplay(gameLevel, this.playerComponent);
        
        this.setKeyBinds();
        this.setMouseBinds();
        this.updateDimensions();
        this.tickrate = NORMAL_TICKRATE;
        this.taskSchedule.clear();

        this.gameActive = true;
        this.quit = false;

        return this.gameLoop();
    }

    /**
     * Update all variables that are dependent on the size of the component.
     **/
    private void updateDimensions() {
        int width = this.getWidth();
        int height = this.getHeight();
        int min = Math.min(width, height);

        // int float error is negligible
        this.center = new Vector2D(width/2, height/2);
        this.cursorAreaRadius =
            (int) (CURSOR_RADIUS_RATIO*min);

        this.scene.getCamera().changeScale(min/SCALE_FACTOR);
        this.scene.getCamera().updateSize(width, height);
        this.hud.updateDimensions(width, height, cursorAreaRadius);
    }
    
    private void handleMouse() {
        Point2D mousePos = this.getMousePosition();
        if (mousePos != null) {
            Vector2D mouseVector = Vector2D.subtract(new Vector2D(mousePos), this.center);
            Vector2D limitedMouse = this.limitMouse(mouseVector);
            this.playerComponent.setThrustPower(limitedMouse.magnitude()/cursorAreaRadius);
            this.playerComponent.setRotation(limitedMouse.rotation());
        }
    }

    /**
     * Prevent the mouse from leaving a radius in the center of the screen.
     * <p>If the mouse is outside the radius it will be moved to the closest point
     * inside the radius.
     * @param mouseVector current position of the mouse relative to the center
     *                    of the panel.
     * @return the limited position of the mouse relative to the center
     *         of the panel
     **/
    private Vector2D limitMouse(Vector2D mouseVector) {
        if (mouseVector.magnitude() > this.cursorAreaRadius) {
            Vector2D limited = Vector2D.fromLengthRotation(
                cursorAreaRadius,
                mouseVector.rotation()
            );

            Vector2D limitedTranslated = Vector2D.add(
                Vector2D.add(
                    this.center,
                    limited),
                new Vector2D(this.getLocationOnScreen())
            );

            this.robot.mouseMove((int) limitedTranslated.x,
                                 (int) limitedTranslated.y);
            return limited;
        }
        return mouseVector;
    }
    
    private void sleepUntil(long time) {
        long sleepPeriod = time - System.nanoTime();

        if (sleepPeriod >= 0) {
            try {
                Thread.sleep(sleepPeriod / NANOSECONDS_PER_MILLISECOND);
            } catch (InterruptedException e) {
                LOGGER.log(Level.WARNING, e.getMessage(), e); 
            }
        }
    }

    private void schedule(Runnable method, double seconds) {
        this.taskSchedule.put(method, System.nanoTime() + (long) (NANOSECONDS_PER_SECOND*seconds));
    }

    private void executeSchedule() {
        List<Runnable> remove = new ArrayList<>();
        Long currentTime = System.nanoTime();

        for (Entry<Runnable, Long> item : this.taskSchedule.entrySet()) {
            if (item.getValue() < currentTime) {
                Runnable method = item.getKey();
                method.run();
                remove.add(method);
            }
        }

        for (Runnable method : remove) {
            this.taskSchedule.remove(method);
        }
    }

    /**
     * Run the main loop that updates and draws the gameLevel.
     * TODO move to separate cl
     * <p> The gameLevel is updated depending on the TICKRATE constant. The drawing
     * of the gameLevel, however, is done as fast as possible (given it doesn't exceed
     * the max frame rate). Drawing the gameLevel state between updates of the gameLevel is
     * done by extrapolation. The max framerate must be higher than max the gameLevel's tickrate.
     **/
    private LevelStatus gameLoop() {
        long nextTick = System.nanoTime();
        long lastFrame = nextTick;
        long nextFrame = nextTick;

        while (!this.quit) {
            sleepUntil(nextFrame);

            this.executeSchedule();
            long currentFrame = System.nanoTime();
            this.delay = currentFrame - lastFrame;
            lastFrame = currentFrame;
            nextFrame = currentFrame + MIN_FRAME_PERIOD;

            if (currentFrame > nextTick) {
                if (this.gameLevel.update() != LevelStatus.IN_PROGRESS) {
                    this.tickrate = SLOW_TICKRATE;
                    this.schedule(this::quit, 0.5);
                }
                nextTick += this.tickPeriod();
            } 
            
            this.extrapolation = (double) (System.nanoTime() + this.tickPeriod() - nextTick)
                                        / this.tickPeriod();
            this.handleMouse();
            this.repaint();
        }
       
        this.gameActive = false;

        return this.gameLevel.getStatus();
    }

    private void quit() {
        this.quit = true;
    }

    private void activateSlowMotion() {
        double transition = 1;
        int duration = 4;
        int steps = NORMAL_TICKRATE - SLOW_TICKRATE;
        double deltaTime = transition/steps;
        for (int i = 0; i < steps; i++) {
            final int j = i;
            this.schedule(() -> this.tickrate = NORMAL_TICKRATE - j, deltaTime*j);
            this.schedule(() -> this.tickrate = NORMAL_TICKRATE - j, duration-deltaTime*j);

        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (this.scene != null) {
            Point mp = this.getMousePosition();
            Vector2D mousePos = mp == null ? null : new Vector2D(mp);
            this.scene.render((Graphics2D) g, this.extrapolation);
            this.hud.draw(g, this.extrapolation, mousePos);
            if (this.showFPS) this.displayFPS(g);
        }
    }

    private void displayFPS(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString(Integer.toString(
            (int) (NANOSECONDS_PER_SECOND/this.delay)),
            0, g.getFont().getSize()
        );
    }

    private void setKeyBinds() {
        this.bindKeyToggle(KeyEvent.VK_SPACE, this.playerComponent::setThrust);

        this.bindKey(KeyStroke.getKeyStroke("F3"), () -> this.showFPS = !this.showFPS);
        this.bindKey(KeyStroke.getKeyStroke("F4"), this.scene::toggleShowHitBoxes);
        this.bindKey(KeyStroke.getKeyStroke("ESCAPE"), this::exit);

        this.bindKey(KeyStroke.getKeyStroke("A"), () -> this.playerComponent.equipPrimary(1));
        this.bindKey(KeyStroke.getKeyStroke("O"), () -> this.playerComponent.equipPrimary(2));
        this.bindKey(KeyStroke.getKeyStroke("E"), () -> this.playerComponent.equipPrimary(3));
        this.bindKey(KeyStroke.getKeyStroke("U"), () -> this.playerComponent.equipPrimary(4));
        this.bindKey(KeyStroke.getKeyStroke("I"), this::activateSlowMotion);

    }

    private void setMouseBinds() {
        for (MouseListener ml : this.getMouseListeners()) {
            this.removeMouseListener(ml);
        }
        this.bindMouseToggle(MouseEvent.BUTTON1, this.playerComponent::firePrimary);
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
                if(GamePanel.this.gameActive) {
                    GamePanel.this.updateDimensions();
                }
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

    private long tickPeriod() {
        return NANOSECONDS_PER_SECOND / this.tickrate;
    }

    private void exit() {
        this.quit = true;
    }
}

