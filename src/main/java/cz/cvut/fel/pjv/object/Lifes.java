package cz.cvut.fel.pjv.object;

import cz.cvut.fel.pjv.creature.Creature;
import cz.cvut.fel.pjv.main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Lifes extends Creature {
    private static  final Logger logger = Logger.getLogger(Lifes.class.getName());

    /**
     * Constructs a new instance of the Lifes class.
     *
     * @param gamePanel the GamePanel object associated with the Lifes.
     */
    public Lifes(GamePanel gamePanel){
        super(gamePanel);
        name = "life";
        try {
            img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/lifes/full_lifes.png")));
            img2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/lifes/lifes-1.png")));
            img3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/lifes/lifes-2.png")));
            img4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/lifes/nolifes.png")));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot not find image from given path.");
            throw new RuntimeException(e);
        }


    }
}
