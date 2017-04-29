package se.liu.ida.noahe116.tddd78.swarm.ui;

import javax.swing.*;
import java.awt.*;
import se.liu.ida.noahe116.tddd78.swarm.game.level.*;

@SuppressWarnings("serial")
public final class MainPanel extends JPanel {
    private final static String GAMEPANEL = "gamePanel";
    private final static String MENUPANEL = "menuPanel";

    private GamePanel gamePanel; 
    private MenuPanel menuPanel;
    private CardLayout layout;

    public MainPanel() {
        this.layout = new CardLayout();
        this.setLayout(this.layout);

        this.gamePanel = new GamePanel();
        this.menuPanel = new MenuPanel();

        this.add(this.gamePanel, GAMEPANEL);
        this.add(this.menuPanel, MENUPANEL);

        this.layout.show(this, MENUPANEL);
    }
}
