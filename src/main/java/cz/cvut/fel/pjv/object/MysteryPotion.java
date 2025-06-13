package cz.cvut.fel.pjv.object;

import cz.cvut.fel.pjv.creature.Creature;
import cz.cvut.fel.pjv.main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MysteryPotion extends Creature {
    private static  final Logger logger = Logger.getLogger(MysteryPotion.class.getName());

    /**
     * Constructs a new instance of the MysteryPotion class.
     *
     * @param gamePanel the GamePanel object associated with the MysteryPotion.
     */
    public MysteryPotion(GamePanel gamePanel) {
        super(gamePanel);
        name = "MysteryPotion";
        pickable = true;
        try {
            down = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/things/mystery_potion.png")));
            down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/things/mystery_potion.png")));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot not find image from given path.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Uses the MysteryPotion item.
     * The MysteryPotion has a 50-50 chance of either fully healing the player or causing instant death.
     * If the random outcome is 0, the player's life is set to full.
     * If the random outcome is 1, the player's life is set to 0 (instant death).
     *
     * @param creature The creature that uses the MysteryPotion (usually the player).
     */
    @Override
    public void useIt(Creature creature){
        //Russian rullet - either full healing or instant death

        Random random = new Random();
        int r = random.nextInt(2);  //0 or 1  50-50 chance
        //full healing
        if (r == 0){
            gamePanel.player.life = gamePanel.player.fullLifes;
        }
        //instant death
        if(r == 1){
            gamePanel.player.life = 0;
        }
    }
}
