package cz.cvut.fel.pjv.object;

import cz.cvut.fel.pjv.creature.Creature;
import cz.cvut.fel.pjv.main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpeedBracelet extends Creature {
    private static  final Logger logger = Logger.getLogger(SpeedBracelet.class.getName());

    /**
     * Constructs a SpeedBracelet object.
     *
     * @param gamePanel The GamePanel instance.
     */
    public SpeedBracelet(GamePanel gamePanel) {
        super(gamePanel);
        name = "bracelet";
        pickable = true;
        try {
            down = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/things/bracelet_of_speed.png")));
            down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/things/bracelet_of_speed.png")));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot not find image from given path.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Uses the SpeedBracelet item.
     * Sets the player's velocity to 4, increasing their speed.
     * Updates the game panel phase and displays a message to the player.
     *
     * @param creature The creature that uses the SpeedBracelet (usually the player).
     */
    @Override
    public void useIt(Creature creature){
        gamePanel.player.velocity = 4;  ///orig player speed je 3
        gamePanel.phase = gamePanel.talkingPhase;
        gamePanel.ui.currSmallTalk =
                "              Nasadil sis prastarý nárdelník.\n"+
                "              Zprvu jsi ale váhal, jestli můžeš\n"+
                "            věřit šperkům z chodeb v hloubce.\n\n" +
                "              Najednou se začínáš cítit divně.\n " +
                "                  Jakoby ti zesílily nohy.\n" +
                "                     Zkus se rozběhnout. \n" +
                "              Řekl bych, že ti to půjde rychleji.\n";
    }


}
