package cz.cvut.fel.pjv.monsters;

import cz.cvut.fel.pjv.creature.Creature;
import cz.cvut.fel.pjv.creature.Princess_npc;
import cz.cvut.fel.pjv.main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Saw extends Creature {
    private static  final Logger logger = Logger.getLogger(Saw.class.getName());

    /**
     * Constructs a new instance of the Saw class.
     *
     * @param gamePanel the GamePanel object associated with the Saw.
     * @throws IOException if an error occurs while loading the images.
     */
    public Saw(GamePanel gamePanel) throws IOException {
        super(gamePanel);
        type = 3;
        name = "saw";

        velocity = 4;
        fullLifes = 1000;  // = unbreakable
        life = fullLifes;
        hitBox.x = 0;
        hitBox.y = 6;
        hitBox.width = 32;
        hitBox.height = 41;
        originalHitPointX = hitBox.x;
        originalHitPointY = hitBox.y;
        damage = 3;
        getSawImage();
    }

    /**
     * Loads the images for the saw.
     * The images are loaded from resource files.
     */
    public void getSawImage()  {
        try {
            rightside = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/trap/saw1.png")));
            rightside2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/trap/saw2.png")));
            leftside = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/trap/saw3.png")));
            leftside2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/trap/saw4.png")));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot not find image from given path.");
            throw new RuntimeException(e);
        }
    }


    /**
     * Sets the walking direction of the saw.
     * The saw always moves to the right side.
     */
    @Override
    public void setWalkingDirection() {
        dir = "rightside";
    }
}
