package se.liu.ida.noahe116.tddd78.swarm.ui;

import javax.swing.*;
import java.util.logging.*;
import java.io.IOException;

public class Swarm {
    private static final String WINDOW_TITLE = "Swarm";

    public static void main(String args[]) throws IOException {
        Logger.getLogger("").addHandler(new FileHandler("log/log_swarm.txt"));

        JFrame frame = new JFrame(WINDOW_TITLE);
        
        frame.add(new MainPanel());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}        
