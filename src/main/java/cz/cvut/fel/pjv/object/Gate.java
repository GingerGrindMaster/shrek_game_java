package cz.cvut.fel.pjv.object;

import cz.cvut.fel.pjv.creature.Creature;
import cz.cvut.fel.pjv.main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Gate extends Creature {
    private static  final Logger logger = Logger.getLogger(Gate.class.getName());

    /**
     * Constructs a new instance of the Gate class.
     *
     * @param gamePanel the GamePanel object associated with the Gate.
     */
    public Gate(GamePanel gamePanel)  {
        super(gamePanel);
        name = "gate";
        opened = false;
        pickable = false;
        try {
            down = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/things/closedgate.png")));
            down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/things/opengate.png")));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot not find image from given path.");
            throw new RuntimeException(e);
        }

        hit = true;

    }
}
