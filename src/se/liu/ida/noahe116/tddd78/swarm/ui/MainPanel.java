package se.liu.ida.noahe116.tddd78.swarm.ui;

import javax.swing.*;
import java.awt.*;
import java.util.logging.*;
import java.io.IOException;

import se.liu.ida.noahe116.tddd78.swarm.game.level.*;
import se.liu.ida.noahe116.tddd78.swarm.game.*;

/**
 * Main panel of swarm game.
 * Contains game panel and menu panel, switches with cardlayout.
 **/
@SuppressWarnings("serial")
public final class MainPanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(GamePanel.class.getName());

    private final static String GAMEPANEL = "gamePanel";
    private final static String MENUPANEL = "menuPanel";

    private GamePanel gamePanel; 
    private MenuPanel menuPanel;
    private CardLayout layout;

    private Game game;

    public MainPanel() {
        this.setBackground(Color.BLACK);

        this.layout = new CardLayout();
        this.setLayout(this.layout);

        this.gamePanel = new GamePanel();
        this.menuPanel = new MenuPanel(this);

        this.add(this.gamePanel, GAMEPANEL);
        this.add(this.menuPanel, MENUPANEL);

        this.layout.show(this, MENUPANEL);
    }

    private void mainLoop() {
        GameLevel gameLevel = this.game.getLevel();
        this.layout.show(this, GAMEPANEL);
        
        boolean quit = false;
        while (!quit) {
            LevelStatus status = this.gamePanel.runGame(gameLevel);
            if (status == LevelStatus.COMPLETED) {
                gameLevel = this.game.getNextLevel(gameLevel);
                this.saveGame();
            } else {
                quit = true;
            }
        }

        this.layout.show(this, MENUPANEL);
    }

    private void saveGame() {
        try {
            Sessions.save(this.game);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "could not save game!", e);
        }
    }


    public void startGame(Game game) {
        this.game = game;

        new Thread(this::mainLoop, "mainLoop").start();
    }
}
