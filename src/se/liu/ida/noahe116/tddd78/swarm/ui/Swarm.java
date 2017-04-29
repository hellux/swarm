package se.liu.ida.noahe116.tddd78.swarm.ui;

import javax.swing.*;
import java.awt.*;
import java.util.logging.*;
import java.io.IOException;

public final class Swarm {
    private static final Logger LOGGER = Logger.getLogger(Swarm.class.getName());

    private static final String WINDOW_TITLE = "Swarm";
    private static final String LOG_FILENAME = "log_swarm.txt";
    private static final Dimension DEFAULT_DIM = new Dimension(1280, 720);

    private Swarm() {}

    public static void main(String[] args) {
        try {
            Logger.getLogger("").addHandler(new FileHandler(LOG_FILENAME));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "can't open log file, logs will not be saved!", e);
        }

        JFrame frame = new JFrame(WINDOW_TITLE);
        
        frame.setLayout(new BorderLayout());
        frame.add(new MainPanel(), BorderLayout.CENTER);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(DEFAULT_DIM);

        frame.setVisible(true);
    }
}        
