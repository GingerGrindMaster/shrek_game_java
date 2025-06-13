package cz.cvut.fel.pjv.main;

import javax.swing.JFrame;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/*the structure - the main things that the game should have to even function are inspired from tutorial - RiySnow*/

/**
 * The main class that initializes and starts the game.
 */
public class Main {

    /**
     * The entry point of the game.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        //WINDOW SETTING
        JFrame wndw = new JFrame();  //new window
        wndw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wndw.setTitle("Dungeon Adventures");
        wndw.setResizable(false);
        GamePanel gamePanel = new GamePanel();
        wndw.add(gamePanel);
        wndw.pack();
        wndw.setLocationRelativeTo(null);  //pops up centered
        wndw.setVisible(true);

        if(args.length>0 && Objects.equals(args[0], "no-logs")){
            Logger.getLogger("").setLevel(Level.OFF);
        }
        gamePanel.prepareGame();  //set up all necessities
        gamePanel.startGameThrd();  //start game thread
    }
}