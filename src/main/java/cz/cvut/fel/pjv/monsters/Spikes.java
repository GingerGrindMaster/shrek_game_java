package cz.cvut.fel.pjv.monsters;

import cz.cvut.fel.pjv.creature.Creature;
import cz.cvut.fel.pjv.main.ActionManager;
import cz.cvut.fel.pjv.main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Spikes extends Creature {
    private static  final Logger logger = Logger.getLogger(Spikes.class.getName());

    /**
     * Constructs a new instance of the Spikes class.
     *
     * @param gamePanel the GamePanel object associated with the Spikes.
     */
    public Spikes(GamePanel gamePanel) {
        super(gamePanel);
        type = 2;
        name = "spikes";
        damage = 2;
        velocity = 0;
        fullLifes = 1000;  // = unbreakable
        life = fullLifes;
        hitBox.x = 0;
        hitBox.y = 0;
        hitBox.width = 47;
        hitBox.height = 48;
        originalHitPointX = hitBox.x;
        originalHitPointY = hitBox.y;
        hitDone = false;
        getSpikesImage();
    }

    /**
     * Loads the images for the spikes.
     * The images are loaded from resource files.
     */
    public void getSpikesImage(){
        try {
            down = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/trap/calm_spikes.png")));
            down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/trap/calm_spikes.png")));
            up = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/trap/activated_spikes.png")));
            up2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/trap/activated_spikes.png")));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot not find image from given path.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the walking direction of the spikes.
     * The spikes randomly switch between the up and down directions.
     */
    @Override
    public void setWalkingDirection() {
        dirCalmer ++;
        if(dirCalmer == 55){
            Random random = new Random();
            int r = random.nextInt(100)+1;  //form 0 to 29 ...+ 1
            if(r <= 50){
                dir = "down";
                hitBox.width = 0;
                hitBox.height = 0;
                //debug System.out.println("no hitbox");
            }
            if(r > 50){
                dir = "up";
                hitBox.height = 48;
                hitBox.width = 40;

            }
            dirCalmer = 0;
        }

    }
}
