package se.liu.ida.noahe116.tddd78.swarm.ui;

import javax.swing.*;
import java.awt.*;
import java.util.logging.*;
import java.io.IOException;
import java.nio.file.*;

import se.liu.ida.noahe116.tddd78.swarm.game.*;

@SuppressWarnings("serial")
public final class MenuPanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(MenuPanel.class.getName());

    private static String MAINMENU = "mainMenu";
    private static String PLAYMENU = "playMenu";
    private static String HELPMENU = "helpMenu";

    private final MainPanel mainPanel;

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
        private JLabel info = new JLabel("", SwingConstants.CENTER);
        private JTextField levelField = new JTextField(1);

        private Game game = null;

        public PlayMenu() {
            JButton back = new JButton("Back");
            JButton begin = new JButton("Begin");
            JButton create = new JButton("Create game");
            JButton load = new JButton("Load game");

            back.addActionListener((e) -> MenuPanel.this.showMenu(MAINMENU));
            begin.addActionListener((e) -> this.play()); 
            create.addActionListener((e) -> this.create());
            load.addActionListener((e) -> this.load());

            this.setLayout(new GridLayout(2, 3));

            this.add(this.info);
            this.add(this.levelField);
            this.add(begin);
            this.add(back);
            this.add(create);
            this.add(load);
        }

        private void play() {
            if (this.game == null) {
                JOptionPane.showMessageDialog(this, "Load or create a game first!");
            } else {
                int level;
                try {
                    level = Integer.parseInt(this.levelField.getText());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Please enter an integer as level.");
                    return;
                }
                if (this.game.validLevel(level)) {
                    game.setCurrentLevel(level);
                    MenuPanel.this.mainPanel.startGame(this.game);
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a level between 1 and " +
                        this.game.getMaxLevel());
                }
            }
        }

        private void create() {
            String name = JOptionPane.showInputDialog(this, "Enter a name for your new game:");
            if (name != null) {
                try {
                    this.setGame(Sessions.create(name));
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this,
                        "Failed to create game, try a different name");
                }
            }
        }

        private void load() {
            String name = JOptionPane.showInputDialog(this, "Enter the name of your saved game");
            if (name != null) {
                try {
                    this.setGame(Sessions.load(name));
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Could not load session"); 
                    LOGGER.log(Level.INFO, "failed to load session: " + e.getMessage());
                }
            }
        }

        public void setGame(Game game) {
            this.game = game;
            this.info.setText("Level: " + game.getMaxLevel() + " Name: " + game.getName());
            this.levelField.setText(Integer.toString((game.getMaxLevel())));
        }
    }

    public final class CreateGameMenu extends JPanel {
        public CreateGameMenu() {

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

    public MenuPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;

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
