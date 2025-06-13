package cz.cvut.fel.pjv.monsters;

import cz.cvut.fel.pjv.creature.Creature;
import cz.cvut.fel.pjv.main.GamePanel;
import cz.cvut.fel.pjv.object.FinalKey;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Skeleton extends Creature {
    private static  final Logger logger = Logger.getLogger(Skeleton.class.getName());

    /**
     * Constructs a new instance of the Skeleton class.
     *
     * @param gamePanel the GamePanel object associated with the Skeleton.
     */
    public Skeleton(GamePanel gamePanel) {
        super(gamePanel);
        type = 2;
        name = "skeleton";
        damage = 1;
        velocity = 1;
        fullLifes = 4;
        life = fullLifes;
        hitBox.x = 4;
        hitBox.y = 10;
        hitBox.width = 32;
        hitBox.height = 32;
        originalHitPointX = hitBox.x;
        originalHitPointY = hitBox.y;

        getSkeletImage();

    }
    /**
     * Determines whether to drop any items when the skeleton is defeated.
     * The skeleton drops a FinalKey item only in the last level (levelNow == 3).
     */
    @Override
    public void decideIfDrop(){
        int i = new Random().nextInt(50)+1;
        System.out.println(i+" "+gamePanel.levelNow );
        if(i > 10  && gamePanel.levelNow == 3) {  //only in the last level
            dropGoodies(new FinalKey(gamePanel));
        }
    }

    /**
     * Loads the images for the skeleton.
     * The images are loaded from resource files.
     */
    public void getSkeletImage(){
        try {
            up = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemies/skeletonU1.png")));
            up2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemies/skeletonU2.png")));
            down = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemies/skeletonR2.png")));
            down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemies/skeletonR1.png")));
            leftside = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemies/skeletonL1.png")));
            leftside2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemies/skeletonL2.png")));
            rightside = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemies/skeletonR1.png")));
            rightside2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemies/skeletonR2.png")));
            dead = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemies/skeleton_dead.png")));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot not find image from given path.");
            throw new RuntimeException(e);
        }

    }

    /**
     * Sets the walking direction of the skeleton.
     * The skeleton randomly changes its direction (up, down, rightside, leftside).
     */
    @Override
    public void setWalkingDirection() {
        dirCalmer++;
        if(dirCalmer == 60){
            Random random = new Random();
            int i = random.nextInt(100)+1;
            if(i <= 25){ dir = "up";}
            if(i > 25 && i<= 50){dir="down";}
            if(i >50 && i <= 75){dir="rightside";}
            if(i > 75 && i <= 100){dir="leftside";}
            dirCalmer = 0;
        }
    }

    /**
     * Reacts to the player's direction.
     * The skeleton adjusts its direction based on the player's direction.
     */
    @Override
    public void react() {
        dirCalmer = 0;
        velocity = 2;
        if(gamePanel.player.dir == "up"){dir = "down";}
        if(gamePanel.player.dir == "down"){dir = "up";}
        if(gamePanel.player.dir == "leftside"){dir = "rightside";}
        if(gamePanel.player.dir == "rightside"){dir = "leftside";}

    }
}
