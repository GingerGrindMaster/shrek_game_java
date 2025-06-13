package cz.cvut.fel.pjv.creature;

import cz.cvut.fel.pjv.main.GamePanel;
import cz.cvut.fel.pjv.main.KeyManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Player extends Creature{
    KeyManager keyM;
    public ArrayList<Creature> inventory = new ArrayList<>();
    int invMaxSize = 9;
    public boolean hasKey = false;
    public boolean hasFinalKey = false;
    public boolean hasDisplayer = false;
    private static  final Logger logger = Logger.getLogger(Player.class.getName());

    public Player(GamePanel gamePanel, KeyManager keyM) {
        super(gamePanel);
        this.keyM = keyM;
        name = "player";
        damage = 1;
        hitBox = new Rectangle();   //creates his new hitBoxArea
        hitBox.x = 4;
        hitBox.y = 5;
        hitBox.height = 35;
        hitBox.width = 35;
        originalHitPointX = hitBox.x;  //needed same as hitPoint
        originalHitPointY = hitBox.y;

        attackRadius.width =36;
        attackRadius.height =36;

        makeDefaultSetting();
        getPlImage();
        getPlAttackImage();
    }

    public void reset(){
        /*Resets the player to the default settings.
          The player's position, life, and inventory are reset.*/

        switch (gamePanel.levelNow){  //different spawn positions in different levels
            case 0:
                mapX=6 * gamePanel.actualTileSize;
                mapY=12 * gamePanel.actualTileSize; break;
            case 1:
                mapX=6 * gamePanel.actualTileSize;
                mapY=12 * gamePanel.actualTileSize; break;
            case 2:
                mapX=12 * gamePanel.actualTileSize;
                mapY=12 * gamePanel.actualTileSize; break;
            case 3:
                mapX=7 * gamePanel.actualTileSize;
                mapY=12 * gamePanel.actualTileSize; break;
        }
        dir = "up";
        life = fullLifes;
        noDamageTime  = false;
        hasKey = false;
        hasFinalKey = false;
        hasDisplayer = false;
        inventory.clear();
        velocity = 2;
    }

    public void getPlImage() {
        /*Loads the player's image from the specified path.*/
        try{
            up = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/16x16/shreckback.png")));
            down = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/16x16/shrekbasic.png")));
            leftside = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/16x16/shrekbasic.png")));
            rightside = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/16x16/shrekbasic.png")));

        } catch(IOException e){
            logger.log(Level.SEVERE, "Cannot not find image from given path.");
            e.printStackTrace();
        }
    }
    public void getPlAttackImage() {
        /*
        Loads the player's attack image from the specified path.
        */
        try{
            attackUp1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/16x16/sh_attack_U1.png")));
            attackUp2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/16x16/sh_attack_U2.png")));
            attackDown1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/16x16/sh_attack_D1.png")));
            attackDown2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/16x16/sh_attack_D2.png")));
            attackLeft1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/16x16/sh_attack_L1.png")));
            attackLeft2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/16x16/sh_attack_L2.png")));
            attackRight1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/16x16/sh_attack_R1.png")));
            attackRight2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/16x16/sh_attack_R2.png")));

        } catch(IOException e){
            logger.log(Level.SEVERE, "Cannot not find image from given path.");
            e.printStackTrace();
        }
    }



    public void makeDefaultSetting(){
        //setting player's default starting position, speed, dir, etc.
        switch (gamePanel.levelNow){  //different spawn positions
            case 0:
                mapX=6 * gamePanel.actualTileSize;
                mapY=12 * gamePanel.actualTileSize; break;
            case 1:
                mapX=6 * gamePanel.actualTileSize;
                mapY=12 * gamePanel.actualTileSize; break;
            case 2:
                mapX=12 * gamePanel.actualTileSize;
                mapY=12 * gamePanel.actualTileSize; break;
            case 3:
                mapX=7 * gamePanel.actualTileSize;
                mapY=12 * gamePanel.actualTileSize; break;
            case 4:
                mapX=6 * gamePanel.actualTileSize;
                mapY=12 * gamePanel.actualTileSize; break;

        }
        velocity = 3;
        dir = "up";
        fullLifes = 9;  //one hearth = 3 lifes
        life = fullLifes;  //TODO orig tu ma byt = fullLifes
    }

    public void update(){
        /*
        Updates the player's state.
        Handles player movement, attacks, and collisions with objects and monsters.
        */

        if(isAttacking){
            doAttack();
        }
        else if(keyM.up || keyM.down || keyM.right || keyM.left) {

            if (keyM.up) {  // if k.up == true ...
                dir = "up";
            }
            if (keyM.down) {
                dir = "down";
            }
            if (keyM.left) {
                dir = "leftside";
            }
            if (keyM.right) {
                dir = "rightside";
            }

            //CHECKING HITS
            hitDone = false;
            gamePanel.walkableChecker.checkTileStatus(this);  //non-walkable tiles check

            int idx = gamePanel.walkableChecker.checkObjStatus(this, true);  //
            takeItem(idx);

            int npcPosition = gamePanel.walkableChecker.checkCreatureStatus(this, gamePanel.npcs);
            approachNPC(npcPosition);


            gamePanel.aManager.checkAction();
            gamePanel.keyM.talk_key_Q = false;

            int monsterIdx = gamePanel.walkableChecker.checkCreatureStatus(this, gamePanel.monsters); //player -> monster collision
            touchedMonster(monsterIdx);
            //if no hit, we can move
            if (hitDone == false) {
                if (dir == "up") {
                    if (keyM.up) {
                        mapY -= velocity;
                        if (mapY < 0) {
                            mapY += velocity;
                        }
                    }
                }
                if (dir == "down") {
                    if (keyM.down) {
                        mapY += velocity;
                        if (mapY > gamePanel.screenHGht - gamePanel.actualTileSize) {
                            mapY -= velocity;
                        }
                    }
                }
                if (dir == "leftside") {
                    if (keyM.left) {
                        mapX -= velocity;
                        if (mapX < 0) {
                            mapX += velocity;
                        }
                    }
                }
                if (dir == "rightside") {
                    if (keyM.right) {
                        mapX += velocity;
                        if (mapX > gamePanel.screenWdth - gamePanel.actualTileSize) {
                            mapX -= velocity;
                        }
                    }
                }
            }
        }
        if (noDamageTime == true) {
            noDmgCounter++;
            if (noDmgCounter > 60) {
                noDamageTime = false;
                noDmgCounter = 0;
            }
        }
        if(life > fullLifes){
            life = fullLifes;
        }

        if(life <= 0){
            gamePanel.phase = gamePanel.gameOverPhase;
            gamePanel.ui.task = -1;
        }
    }
    /*
    Chooses and uses an item from the player's inventory.
    */
    public void chooseItem(){

        int itmIdx = gamePanel.ui.getItmInventoryIdx();
        if(itmIdx < inventory.size()){
            Creature chosenItm = inventory.get(itmIdx);
            chosenItm.useIt(this);
            inventory.remove(itmIdx);
            logger.log(Level.INFO, "Item used and removed from inventory.");

        }
    }
    /*
    Performs the attack action for the player.
    Manages the attack animation and deals damage to monsters.
    */
    public void doAttack(){

        picCount ++;
        int monsterIdx;
        if (picCount <= 5){
                picNum = 1;
        }

        if(picCount > 5 && picCount <= 25){
            picNum = 2;
            int currX = mapX;
            int currY = mapY;
            int hitboxWidth = hitBox.width;
            int hitboxHeight = hitBox.height;
            if(dir == "up"){mapY -= attackRadius.height;}
            if(dir == "down"){mapY += attackRadius.height;}
            if(dir == "leftside"){mapX -= attackRadius.width;}
            if(dir == "downside"){mapX += attackRadius.width;}

            hitBox.width =attackRadius.width;
            hitBox.height=attackRadius.height;
            monsterIdx = gamePanel.walkableChecker.checkCreatureStatus(this, gamePanel.monsters);
            hurtMonster(monsterIdx);

            mapX = currX;
            mapY = currY;
            hitBox.width = hitboxWidth;
            hitBox.height = hitboxHeight;
        }
        if(picCount > 25){
            picNum = 1;
            picCount = 0;
            isAttacking = false;
        }

    }
    /**
     Draws the player on the screen using the specified Graphics2D object.
     @param g2D the Graphics2D object to draw on
     */
    @Override
    public void draw (Graphics2D g2D){

        BufferedImage img = null;
        if (dir == "up"){
            if(!isAttacking){ img = up;}
            if(isAttacking){
                //little attack animation
                if(picNum == 1){img = attackUp1;}
                if(picNum == 2){img = attackUp2;}
            }
        }
        if (dir =="down"){
            if(!isAttacking){ img = down;}
            if(isAttacking){

                if(picNum == 1){img = attackDown1;}
                if(picNum == 2){img = attackDown2;}
            }
        }
        if (dir =="rightside"){
            if(!isAttacking){ img = rightside;}
            if(isAttacking){

                if(picNum == 1){img = attackRight1;}
                if(picNum == 2){img = attackRight2;}
            }
        }
        if (dir =="leftside"){
            if(!isAttacking){ img = leftside;}
            if(isAttacking){
                if(picNum == 1){img = attackLeft1;}
                if(picNum == 2){img = attackLeft2;}
            }
        }
        g2D.drawImage(img, mapX, mapY, gamePanel.actualTileSize, gamePanel.actualTileSize, null);
    }


    /**
     Deals damage to a monster if the player hits it.
     @param idx the index of the monster in the gamePanel.monsters array
     */
    public void hurtMonster(int idx){
        if (idx != 911) {  //if != 911 - we have a hit
            if(!gamePanel.monsters[gamePanel.levelNow][idx].noDamageTime) {
                gamePanel.monsters[gamePanel.levelNow][idx].life -= damage;
                logger.log(Level.INFO, "Player hit enemy.");

                gamePanel.ui.addMsgToList("- "+damage+" HP!");
                if(gamePanel.monsters[gamePanel.levelNow][idx].life <= 0 ){
                    if(gamePanel.monsters[gamePanel.levelNow][idx].name == "bat") {
                        gamePanel.ui.addMsgToList("Bat chopped to pieces!");
                        logger.log(Level.INFO, "Bat killed.");

                    }
                    if(gamePanel.monsters[gamePanel.levelNow][idx].name == "skeleton") {
                        gamePanel.ui.addMsgToList("Skeleton chopped to pieces!");
                        logger.log(Level.INFO, "Skeleton killed.");
                    }
                    if(gamePanel.monsters[gamePanel.levelNow][idx].name == "spider") {
                        gamePanel.ui.addMsgToList("Spider chopped to pieces!");
                        logger.log(Level.INFO, "Spider killed.");
                    }
                }
                gamePanel.monsters[gamePanel.levelNow][idx].noDamageTime = true;
                gamePanel.monsters[gamePanel.levelNow][idx].react();
            }
        }
    }


    /**
     Takes an item from the gamePanel.object array and adds it to the player's inventory.
     @param idx the index of the item in the gamePanel.object array
     */
    public void takeItem(int idx){

        String s = null;
        if (idx != 911){
            if(gamePanel.object[gamePanel.levelNow][idx].pickable && inventory.size() < invMaxSize){
                inventory.add(gamePanel.object[gamePanel.levelNow][idx]);
                if(gamePanel.object[gamePanel.levelNow][idx].name == "key"){hasKey = true;}
                if(gamePanel.object[gamePanel.levelNow][idx].name == "finalkey"){hasFinalKey = true;}
                //debug System.out.println("carrying "+inventory.size()+"things");
                if(gamePanel.object[gamePanel.levelNow][idx].name == "MysteryPotion" && gamePanel.levelNow == 2){  //hint only with first encounter
                    gamePanel.phase = gamePanel.talkingPhase;
                    gamePanel.ui.currSmallTalk =
                        "              Našel jsi zajímavý lektvar.\n"+
                        "              Jediné, co tě trochu znejisťuje,\n"+
                        "    je jeho podivná barva a nepřehlédnutelný fakt,\n" +
                        "           že má na sobě namalovaný otazník.\n " +
                        "         Taky se ti v hlavě začíná rozeznívat\n" +
                        "                          zpěvný hlas: \n" +
                        "      vypij mě, vypij mě, vypij mě, VYPIJ MĚ!!\n"+
                        "        Třeba v tomhle levelu je někde někdo,\n" +
                        "                         kdo ti poradí ...\n";
                }
                gamePanel.object[gamePanel.levelNow][idx] = null;
            }
            if(pickable && inventory.size() == invMaxSize){
                s = "Inventory full!";
                logger.log(Level.INFO, "Full inventory. Cannot carry any more items.");
            }
            gamePanel.ui.addMsgToList(s);
        }
    }

    /**
     Interacts with an NPC character if the player touches it and presses the talk key.
     @param npc_position the index of the NPC in the gamePanel.npcs array
     */
    public void approachNPC(int npc_position){

        if (npc_position != 911){  //if not we hit object
            if(gamePanel.keyM.talk_key_Q){
                gamePanel.phase = gamePanel.talkingPhase;
                gamePanel.npcs[gamePanel.levelNow][0].tellWiseWords();
            }
        }
    }

    /* Handles the player getting damaged when touching a monster.  */
    public void touchedMonster(int idx){

        if (idx != 911){
            if(noDamageTime == false){
                life -= gamePanel.monsters[gamePanel.levelNow][idx].damage;
                logger.log(Level.INFO, "Damage recieved from "+ gamePanel.monsters[gamePanel.levelNow][idx].name);
                noDamageTime = true;
            }
        }
    }
}
