package se.liu.ida.noahe116.tddd78.swarm.ui;

import javax.swing.JFrame;

public class Swarm {
    private static final String WINDOW_TITLE = "Swarm";

    public static void main(String args[]) {
        JFrame frame = new JFrame(WINDOW_TITLE);
        
        frame.add(new MainPanel());

        frame.setVisible(true);
    }
}        
