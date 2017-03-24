package se.liu.ida.noahe116.tddd78.swarm.ui;

import javax.swing.*;
import java.awt.*;
import java.util.logging.*;
import java.io.IOException;

public final class Swarm {
    private static final String WINDOW_TITLE = "Swarm";

    private Swarm() {}

    public static void main(String[] args) throws IOException {
        Logger.getLogger("").addHandler(new FileHandler("log/log_swarm.txt"));

        JFrame frame = new JFrame(WINDOW_TITLE);
        
        frame.setLayout(new BorderLayout());
        frame.add(new GamePanel(), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}        
