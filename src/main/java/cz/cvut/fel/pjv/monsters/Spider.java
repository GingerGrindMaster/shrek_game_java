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

public class Spider extends Creature {

    private static  final Logger logger = Logger.getLogger(Spider.class.getName());

    /**
     * Constructs a new instance of the Spider class.
     *
     * @param gamePanel the GamePanel object associated with the Spider.
     */
    public Spider(GamePanel gamePanel) {
        super(gamePanel);
        type = 2;
        name = "spider";
        damage = 2;
        velocity = 2;
        fullLifes = 3;
        life = fullLifes;
        hitBox.x = 4;
        hitBox.y = 10;
        hitBox.width = 32;
        hitBox.height = 32;
        originalHitPointX = hitBox.x;
        originalHitPointY = hitBox.y;
        getSpiderImage();
    }


    /**
     * Loads the images for the spider.
     * The images are loaded from resource files.
     */
    public void getSpiderImage(){
        try {
            up = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemies/spiderD1.png")));
            up2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemies/spiderD2.png")));
            down = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemies/spiderU2.png")));
            down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemies/spiderU1.png")));
            dead = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemies/spider_dead.png")));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot not find image from given path.");
            throw new RuntimeException(e);
        }


    }

    /**
     * Reacts to the player's direction.
     * The spider adjusts its direction based on the player's direction.
     */
    @Override
    public void react() {
        dirCalmer = 0;
        if(gamePanel.player.dir == "up"){dir = "down";}
        if(gamePanel.player.dir == "down"){dir = "up";}

    }

    /**
     * Sets the walking direction of the spider.
     * The spider randomly changes its direction (up or down).
     */
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


}
