package se.liu.ida.noahe116.tddd78.swarm.ui;

import javax.swing.*;
import java.awt.*;
import java.util.logging.*;
import java.io.IOException;

import se.liu.ida.noahe116.tddd78.swarm.game.level.*;
import se.liu.ida.noahe116.tddd78.swarm.game.*;

@SuppressWarnings("serial")
public final class MainPanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(GamePanel.class.getName());

    private final static String GAMEPANEL = "gamePanel";
    private final static String MENUPANEL = "menuPanel";

    private GamePanel gamePanel; 
    private MenuPanel menuPanel;
    private CardLayout layout;

    private Game game;
    private boolean quit;

    public MainPanel() {
        this.setBackground(Color.BLACK);

        this.layout = new CardLayout();
        this.setLayout(this.layout);

        this.gamePanel = new GamePanel(this);
        this.menuPanel = new MenuPanel(this);

        this.add(this.gamePanel, GAMEPANEL);
        this.add(this.menuPanel, MENUPANEL);

        this.layout.show(this, MENUPANEL);
    }

    private void mainLoop() {
        GameLevel gameLevel = this.game.getLevel();
        Thread gameThread = this.startLevel(gameLevel);

        while (!this.quit) {
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "failed to create thread for game loop");
            }
            if (!this.quit) {
                gameLevel = this.game.getNextLevel(gameLevel);
                try {
                    Sessions.save(this.game);
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "could not save game!", e);
                }
                gameThread = this.startLevel(gameLevel);
            }
        }

        System.exit(0);
    }

    private Thread startLevel(GameLevel gameLevel) {
        this.layout.show(this, GAMEPANEL);
        return this.gamePanel.startGame(gameLevel);
    }

    public void startGame(Game game) {
        this.game = game;
        Thread thread = new Thread(this::mainLoop, "mainLoop");
        thread.start();
    }

    public void quit() {
        this.quit = true;
    }
}
