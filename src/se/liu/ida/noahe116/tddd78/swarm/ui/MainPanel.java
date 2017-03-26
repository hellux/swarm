package se.liu.ida.noahe116.tddd78.swarm.ui;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public final class MainPanel extends JPanel {
    public MainPanel() {
        this.add(new GamePanel());
    }
}
