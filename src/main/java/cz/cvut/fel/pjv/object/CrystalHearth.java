package cz.cvut.fel.pjv.object;

import cz.cvut.fel.pjv.creature.Creature;
import cz.cvut.fel.pjv.main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CrystalHearth extends Creature {
    private static  final Logger logger = Logger.getLogger(CrystalHearth.class.getName());

    /**
     * Constructs a new instance of the CrystalHearth class.
     *
     * @param gamePanel the GamePanel object associated with the CrystalHearth.
     */
    public CrystalHearth(GamePanel gamePanel) {
        super(gamePanel);
        name = "hearth";
        pickable = true;
        try {
            down = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/things/hearth.png")));
            down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/things/hearth.png")));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot not find image from given path.");
            throw new RuntimeException(e);
        }
    }


    /**
     * Uses the CrystalHearth item.
     * Increases the player's life by 3.
     * If the player's life exceeds the maximum number of lifes, it is capped at the maximum.
     *
     * @param creature the creature to use the item on (in this case, the player)
     */
    @Override
    public void useIt(Creature creature){
        gamePanel.player.life += 3;
        if(gamePanel.player.life > gamePanel.player.fullLifes){  //unable to have more than max lifes
            gamePanel.player.life = gamePanel.player.fullLifes;
        }
    }
}
