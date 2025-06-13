package cz.cvut.fel.pjv.creature;
import cz.cvut.fel.pjv.main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Creature {
    /* parent class for all in-game creatures */
    protected GamePanel gamePanel;
    public int mapX, mapY;
    public int velocity;
    public String dir = "down";
    public String name;
    public BufferedImage up, up2, down,down2, leftside, leftside2, rightside, rightside2;
    public BufferedImage img, img2, img3, img4;
    public BufferedImage attackUp1, attackUp2, attackDown1,attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    public BufferedImage dead;
    public boolean opened;
    //LOGGER
    private static  final Logger logger = Logger.getLogger(Creature.class.getName());


// ------- animation --------------
    protected int picNum = 0;
    protected int picCount = 0;
    public int dirCalmer = 0;  //prevents from changing dirs too fast
// ------life ---------
    public int fullLifes;
    public int life;
// ------ getting dmg hold-off --------
    public boolean noDamageTime = false;
    public  int  noDmgCounter = 0;

    public int type ; // 0 = player, 1 = npc, 2 = monster

//------- hit-box area -------------
    public Rectangle hitBox = new Rectangle(6, 10, 39,32); //same as player
    public  boolean hitDone = false;
    public int originalHitPointX;
    public int originalHitPointY;
    public boolean hit = false;
    public Rectangle attackRadius = new Rectangle(0,0,48,48);

// ----- dialogues ----------
    String[] smallTalkOptions = new String[20];
    int smallTalkNumber = 0;

    public boolean isAttacking = false;
    public int damage;
    int counter = 0;
    protected boolean pickable = false;
    public  boolean inPrison = false;


    /**
     * Default constructor for the Creature class.
     *
     * @param gamePanel The GamePanel object.
     */
    public Creature(GamePanel gamePanel) {

        this.gamePanel = gamePanel;
    }

    /**
     * Placeholder method for using the creature.
     *
     * @param creature The creature to be used.
     */
    public void useIt(Creature creature){}

    public void react(){}  //simple """AI""" - monster faces player when attacked

    public void setWalkingDirection(){}  //"defined" in another class

    /**
     * Displays wise words of the creature.
     * If there are no more small talk options, it resets the small talk number.
     */
    public void tellWiseWords(){
        if (smallTalkOptions[smallTalkNumber] == null){
            smallTalkNumber = 0;
        }
        gamePanel.ui.currSmallTalk = smallTalkOptions[smallTalkNumber];
        smallTalkNumber ++;
    }

    /**
     * Placeholder method for deciding if the creature should drop something.
     */
    public void decideIfDrop(){}

    /**
     * Drops goodies at the creature's location after it is killed.
     *
     * @param droppedGoodies The creature's dropped goodies.
     */
    public void dropGoodies(Creature droppedGoodies){

        for (int i = 0;  i < gamePanel.object[1].length; i ++){
            if(gamePanel.object[gamePanel.levelNow][i] == null ){
                gamePanel.object[gamePanel.levelNow][i] = droppedGoodies;
                gamePanel.object[gamePanel.levelNow][i].mapX = mapX;
                gamePanel.object[gamePanel.levelNow][i].mapY = mapY ;
                gamePanel.object[gamePanel.levelNow][i].pickable = true;
                break;
            }
        }
    }

    /**
     * Updates the creature's state and behavior.
     * This method is called in each game update cycle.
     */
    public void update(){

        setWalkingDirection();
        hitDone = false;
        gamePanel.walkableChecker.checkTileStatus(this);
        gamePanel.walkableChecker.checkObjStatus(this, false);

        //collision between creatures - collision happens between monsters, npcs
        gamePanel.walkableChecker.checkCreatureStatus(this, gamePanel.npcs);
        gamePanel.walkableChecker.checkCreatureStatus(this, gamePanel.monsters);
        boolean playerContact = gamePanel.walkableChecker.checkPlayerStatus(this);
        if(this.type == 2 && playerContact == true){
            if(gamePanel.player.noDamageTime == false){
                //we can give dmg
                gamePanel.player.life -= 1;  //basic monster damage
                gamePanel.player.noDamageTime = true;
            }
        }
        if(this.type == 3 && playerContact == true){
            if(gamePanel.player.noDamageTime == false){
                //we can give dmg
                gamePanel.player.life -= 3;
                gamePanel.player.noDamageTime = true;
            }
        }
        if(this.name == "spikes"){
            try {
                this.down = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/trap/calm_spikes.png")));
                this.down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/trap/calm_spikes.png")));

            } catch (IOException e) {
                logger.log(Level.SEVERE, "Cannot not find image from given path.");
                throw new RuntimeException(e);
            }
        }
        if (hitDone == false){

            if(dir == "rightside") {
                mapX += velocity;
                if (mapX > gamePanel.screenWdth - gamePanel.actualTileSize) {
                    mapX -= velocity;
                }
            }
            if (dir == "leftside") {
                mapX -= velocity;
                if (mapX < 0){
                    mapX += velocity;
                }
            }
            if (dir == "up") {
                mapY -= velocity;
                    if (mapY < 0) {
                        mapY += velocity;
                    }
            }
            if (dir == "down"){
                mapY += velocity;
                    if (mapY > gamePanel.screenHGht - gamePanel.actualTileSize) {
                        mapY -= velocity;
                    }
            }
        }
        //animation speed manager
        picCount ++;
        if (picCount > 20){
            if(picNum == 0){
                picNum = 1;
            }
            else if(picNum == 1){
                picNum = 0;
            }
            picCount = 0;
        }
        if (noDamageTime == true) {
            noDmgCounter++;
            if (noDmgCounter > 30) {   //can receive dmg more frequently
                noDamageTime = false;
                noDmgCounter = 0;
            }
        }
    }

    /**
     * Draws the creature on the screen.
     *
     * @param g2d The Graphics2D object used for drawing.
     */
    public void draw(Graphics2D g2d){

        BufferedImage img = null;
        if(dir == "rightside"){
            if(picNum == 0){ img = rightside;  }
            if(picNum == 1){  img = rightside2;  }
        }
        if(dir == "leftside"){
            if(picNum == 0){ img = leftside; }
            if(picNum == 1){ img = leftside2; }
        }
        if (dir == "up"){
            if(picNum == 0){img = up;}
            if(picNum == 1){ img = up2;}
        }
        if(dir == "down"){
            if(picNum == 0){ img = down;    }
            if(picNum == 1){ img = down2;  }
        }
        if(noDamageTime){
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }
        if(this.life <= 0 && gamePanel.counter<80 &&(this.name == "bat" || this.name == "spider")){
            //dead monster image is shown for a short period of time
            counter++;
            img = dead;
            this.hitDone = false;
            this.velocity = 0;
            this.damage = 0;
            this.hitBox.x = 0;
            this.hitBox.y = 0;
            this.hitBox.height = 0;
            this.hitBox.width = 0;
            if(counter>80){
                this.dead = null;
                counter = 0;
            }
        }
        g2d.drawImage(img, mapX, mapY, gamePanel.actualTileSize, gamePanel.actualTileSize, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2d.setFont(new Font("Arial", Font.PLAIN, 29));
        g2d.setColor(Color.white);
        //g2d.drawString("Invincible: " + noDmgCounter, 8*48, 30);  display invincible time "stopwatch" on screen
        if(gamePanel.player.hasDisplayer){
            g2d.drawString("lifes: " + gamePanel.player.life, gamePanel.actualTileSize*12, 30);
        }
    }
}
