package cz.cvut.fel.pjv.object;

import cz.cvut.fel.pjv.creature.Creature;
import cz.cvut.fel.pjv.main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Displayer extends Creature {
    private static  final Logger logger = Logger.getLogger(Displayer.class.getName());

    /**
     * Constructs a new instance of the Displayer class.
     *
     * @param gamePanel the GamePanel object associated with the Displayer.
     */
    public Displayer(GamePanel gamePanel) {
        super(gamePanel);
        name = "displayer";
        pickable = true;
        try {
            down = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/things/displayer.png")));
            down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/things/displayer.png")));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot not find image from given path.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Uses the Displayer item.
     * Sets the player's "hasDisplayer" flag to true - so players lifes will be written on screen.
     *
     * @param creature the creature to use the item on (in this case, the player)
     */
    @Override
    public void useIt(Creature creature){
        gamePanel.player.hasDisplayer = true;
    }
}
