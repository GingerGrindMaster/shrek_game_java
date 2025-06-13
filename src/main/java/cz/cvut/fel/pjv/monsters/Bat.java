package cz.cvut.fel.pjv.monsters;

import cz.cvut.fel.pjv.creature.Creature;

import cz.cvut.fel.pjv.main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bat extends Creature {
    private static  final Logger logger = Logger.getLogger(Bat.class.getName());

    /**
     * Constructs a new instance of the Bat class.
     *
     * @param gamePanel the GamePanel object associated with the Bat.
     */
    public Bat(GamePanel gamePanel) {
        super(gamePanel);
        type = 2;
        name = "bat";
        damage = 2;
        velocity = 2;
        fullLifes = 2;  // = unbreakable
        life = fullLifes;
        hitBox.x = 4;
        hitBox.y = 10;
        hitBox.width = 32;
        hitBox.height = 32;
        originalHitPointX = hitBox.x;
        originalHitPointY = hitBox.y;
        getBatImage();

    }
    /**
     * Loads the images for the bat.
     * The images are loaded from resource files.
     */
    public  void getBatImage(){
        try {
            down = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemies/batD1.png")));
            down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemies/batD2.png")));
            up = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemies/batD2.png")));
            up2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemies/batD1.png")));
            dead = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemies/dead_bat.png")));

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot not find image from given path.");
            throw new RuntimeException(e);
        }

    }
    /**
     * Sets the walking direction of the bat.
     * The direction is randomly set based on a calculated value.
     */
    @Override
    public void setWalkingDirection() {
        dirCalmer++;
        if(dirCalmer == 90){
            Random random = new Random();
            int i = random.nextInt(50)+1;
            if(i <= 25){ dir = "up";}
            if(i > 25 && i<= 50){dir="down";}

            dirCalmer = 0;
        }
    }

    /**
     * Reacts to the player's direction.
     * The bat changes its direction based on the player's direction.
     */
    @Override
    public void react() {
        dirCalmer = 0;
        if(gamePanel.player.dir == "up"){dir = "down";}
        if(gamePanel.player.dir == "down"){dir = "up";}


    }
}
