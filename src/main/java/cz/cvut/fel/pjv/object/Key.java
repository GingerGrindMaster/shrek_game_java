package cz.cvut.fel.pjv.object;

import cz.cvut.fel.pjv.creature.Creature;
import cz.cvut.fel.pjv.main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Key extends Creature {
    private static  final Logger logger = Logger.getLogger(Key.class.getName());

    /**
     * Constructs a new instance of the Key class.
     *
     * @param gamePanel the GamePanel object associated with the Key.
     */
    public Key(GamePanel gamePanel) {
        super(gamePanel);
        pickable = true;
        name = "key";
        hitBox.width = 32;
        hitBox.height = 42;
        try {
            down = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/things/key.png")));
            down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/things/key.png")));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot not find image from given path.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Uses the Key item.
     * This method is empty because the Key does not have any specific use.
     *
     * @param creature the creature to use the item on (not applicable in this case)
     */
    @Override
    public void useIt(Creature creature){
        //
    }
}
