package se.liu.ida.noahe116.tddd78.swarm.ui;

import javax.swing.*;
import java.awt.*;
import java.util.logging.*;
import java.io.IOException;
import java.nio.file.*;

@SuppressWarnings("serial")
public final class MenuPanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(MenuPanel.class.getName());

    private static String MAINMENU = "mainMenu";
    private static String PLAYMENU = "playMenu";
    private static String HELPMENU = "helpMenu";

    private final MainMenu mainMenu;
    private final PlayMenu playMenu;
    private final HelpMenu helpMenu;
    private final CardLayout layout;

    public final class MainMenu extends JPanel {
        public MainMenu() {
            this.setLayout(new GridLayout(4, 1));

            JLabel title = new JLabel("SWARM", SwingConstants.CENTER);
            JButton play = new JButton("Play");
            JButton help = new JButton("Help");
            JButton quit = new JButton("Quit");
           
            play.addActionListener((e) -> MenuPanel.this.showMenu(PLAYMENU));
            help.addActionListener((e) -> MenuPanel.this.showMenu(HELPMENU));
            quit.addActionListener((e) -> System.exit(0));

            this.add(title);
            this.add(play);
            this.add(help);
            this.add(quit);
        }
    }

    public final class PlayMenu extends JPanel {
        public PlayMenu() {
            
        }
    }

    public final class HelpMenu extends JPanel {
        private final String HELP_FILE = "help.txt";

        public HelpMenu() {
            this.setBackground(Color.WHITE);
            this.setLayout(new BorderLayout());

            JTextArea textArea = new JTextArea(this.readTextFile());
            JButton back = new JButton("Back to menu");
            back.addActionListener((e) -> MenuPanel.this.showMenu(MAINMENU));

            this.add(textArea, BorderLayout.CENTER);
            this.add(back, BorderLayout.SOUTH);
        }

        public String readTextFile() {
            String text;
            try {
                text = new String(Files.readAllBytes(Paths.get(HELP_FILE)));
            } catch (IOException e) {
                // FIXME why doesn't NoSuchFileException get caught here?
                LOGGER.log(Level.WARNING, "can't read help file", e);
                text = "Help file not found, sorry!";
            } 
            return text;
        }
    }

    public MenuPanel() {
        this.layout = new CardLayout();
        this.setLayout(this.layout);

        this.mainMenu = new MainMenu();
        this.playMenu = new PlayMenu();
        this.helpMenu = new HelpMenu();

        this.add(mainMenu, MAINMENU);
        this.add(playMenu, PLAYMENU);
        this.add(helpMenu, HELPMENU);

        this.showMenu(MAINMENU);
    }

    private void showMenu(String menu) {
        this.layout.show(this, menu);
    }
}
