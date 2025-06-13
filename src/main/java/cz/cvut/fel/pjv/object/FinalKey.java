package cz.cvut.fel.pjv.object;

import cz.cvut.fel.pjv.creature.Creature;
import cz.cvut.fel.pjv.main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/*drops from killed monster in some level*/
public class FinalKey extends Creature {

    private static  final Logger logger = Logger.getLogger(FinalKey.class.getName());

    /**
     * Constructs a new instance of the FinalKey class.
     *
     * @param gamePanel the GamePanel object associated with the FinalKey.
     */
    public FinalKey(GamePanel gamePanel) {
        super(gamePanel);
        pickable = true;
        name = "finalkey";
        hitBox.width = 32;
        hitBox.height = 38;


        try {
            down = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/things/final_key.png")));
            down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/things/final_key.png")));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot not find image from given path.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Uses the FinalKey item.
     * This method is empty because the FinalKey does not have any specific use.
     *
     * @param creature the creature to use the item on (not applicable in this case)
     */
    @Override
    public void useIt(Creature creature){
        // No specific use for the FinalKey
    }



}
