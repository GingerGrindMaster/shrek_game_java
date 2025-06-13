package cz.cvut.fel.pjv.main;

import cz.cvut.fel.pjv.creature.Creature;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/* the perception of the surroundings is inspired by the tutorial - hitboxes and so*/
/**
 * The WalkableChecker class checks the status of tiles, objects, creatures, and player in the game world.
 */
public class WalkableChecker {
    public int changesprite = 1;
    private static  final Logger logger = Logger.getLogger(Creature.class.getName());

    GamePanel gamePanel;

    /**
     * Constructs a WalkableChecker object with the specified GamePanel.
     *
     * @param gamePanel the GamePanel instance
     */
    public WalkableChecker(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * Checks the status of the tile that the creature is walking on.
     *
     * @param creature the creature to check
     */
    public void checkTileStatus(Creature creature){

        int creatureLeftMapX = creature.mapX + creature.hitBox.x;
        int creatureRightMapX = creature.mapX + creature.hitBox.x + creature.hitBox.width;
        int creatureTopMapY = creature.mapY + creature.hitBox.y;
        int creatureBotMapY = creature.mapY + creature.hitBox.y + creature.hitBox.height;

        int leftCol = creatureLeftMapX/gamePanel.actualTileSize;
        int rightCol = creatureRightMapX/ gamePanel.actualTileSize;
        int topRow = creatureTopMapY / gamePanel.actualTileSize;
        int botRow =  creatureBotMapY/ gamePanel.actualTileSize;

        int checkTile1, checkTile2;
        if (creature.dir == "up"){
            topRow = (creatureTopMapY - creature.velocity)/gamePanel.actualTileSize;
            checkTile1 = gamePanel.tileManager.levelTileNumbers[gamePanel.levelNow][leftCol][topRow];
            checkTile2 = gamePanel.tileManager.levelTileNumbers[gamePanel.levelNow][rightCol][topRow];
            if(gamePanel.tileManager.tile_types[checkTile1].hit || gamePanel.tileManager.tile_types[checkTile2].hit){  // if == true
                creature.hitDone = true;
            }
        }
        if (creature.dir == "down"){
            botRow = (creatureBotMapY + creature.velocity)/ gamePanel.actualTileSize;
            checkTile1 = gamePanel.tileManager.levelTileNumbers[gamePanel.levelNow][leftCol][botRow];
            checkTile2 = gamePanel.tileManager.levelTileNumbers[gamePanel.levelNow][rightCol][botRow];
            if(gamePanel.tileManager.tile_types[checkTile1].hit || gamePanel.tileManager.tile_types[checkTile2].hit) {  // if == true
                creature.hitDone = true;
            }
        }
        if (creature.dir == "leftside"){
            leftCol = (creatureLeftMapX - creature.velocity)/ gamePanel.actualTileSize;
            checkTile1 = gamePanel.tileManager.levelTileNumbers[gamePanel.levelNow][leftCol][topRow];
            checkTile2 = gamePanel.tileManager.levelTileNumbers[gamePanel.levelNow][leftCol][botRow];
            if(gamePanel.tileManager.tile_types[checkTile1].hit || gamePanel.tileManager.tile_types[checkTile2].hit) {  // if == true
                creature.hitDone = true;
                if(creature.name == "saw"){
                    creature.velocity *= -1;
                    creature.dir = "rightside";
                    }
            }
        }
        if (creature.dir == "rightside"){
            rightCol = (creatureRightMapX + creature.velocity)/ gamePanel.actualTileSize;
            checkTile1 = gamePanel.tileManager.levelTileNumbers[gamePanel.levelNow][rightCol][topRow];
            checkTile2 = gamePanel.tileManager.levelTileNumbers[gamePanel.levelNow][rightCol][botRow];
            if(gamePanel.tileManager.tile_types[checkTile1].hit || gamePanel.tileManager.tile_types[checkTile2].hit) {  // if == true
                creature.hitDone = true;
                if(creature.name == "saw"){
                    creature.velocity *= -1;
                    creature.dir = "leftside";}
            }
        }
    }
    /**
     * Checks the status of the objects in the game world.
     *
     * @param creature the creature to check
     * @param player   a boolean indicating if the creature is the player
     * @return the index of the hit object
     */
    public int checkObjStatus (Creature creature, boolean player){
        int idx = 911;  //could be anything high
        int i = 0;
        while (i < gamePanel.object[1].length){
            if(gamePanel.object[gamePanel.levelNow][i] != null){
                //hitbox's position
                creature.hitBox.x = creature.mapX + creature.hitBox.x;
                creature.hitBox.y = creature.mapY + creature.hitBox.y;

                gamePanel.object[gamePanel.levelNow][i].hitBox.x = gamePanel.object[gamePanel.levelNow][i].mapX + gamePanel.object[gamePanel.levelNow][i].hitBox.x;
                gamePanel.object[gamePanel.levelNow][i].hitBox.y = gamePanel.object[gamePanel.levelNow][i].mapY + gamePanel.object[gamePanel.levelNow][i].hitBox.y;

                if (creature.dir == "up"){
                    creature.hitBox.y -= creature.velocity;
                }
                if (creature.dir == "down"){
                    creature.hitBox.y += creature.velocity;
                }
                if (creature.dir == "leftside"){
                    creature.hitBox.x -= creature.velocity;
                }
                if (creature.dir == "rightside"){
                    creature.hitBox.x += creature.velocity;
                }
                if(creature.hitBox.intersects(gamePanel.object[gamePanel.levelNow][i].hitBox)){
                    if(gamePanel.object[gamePanel.levelNow][i].hit == true){
                        creature.hitDone = true;
                        if(creature.name == "player" && gamePanel.player.hasKey && gamePanel.object[gamePanel.levelNow][i].name == "gate"){
                            gamePanel.object[gamePanel.levelNow][i].down = gamePanel.object[gamePanel.levelNow][i].down2;
                            gamePanel.object[gamePanel.levelNow][i].opened = true;
                            gamePanel.player.hasKey = false;
                            gamePanel.object[gamePanel.levelNow][i].hitBox.width = 0;
                            gamePanel.object[gamePanel.levelNow][i].hitBox.height = 0;
                            //delete key after usage
                            for(int k = 0; k < gamePanel.player.inventory.size(); k ++){
                                if(gamePanel.player.inventory.get(k).name == "key"){
                                    gamePanel.player.inventory.remove(gamePanel.player.inventory.get(k));
                                }
                            }
                        }
                    }
                    if (player == true){
                        idx = i;
                    }
                }
                //reset
                creature.hitBox.x = creature.originalHitPointX;
                creature.hitBox.y = creature.originalHitPointY;
                gamePanel.object[gamePanel.levelNow][i].hitBox.x = gamePanel.object[gamePanel.levelNow][i].originalHitPointX;
                gamePanel.object[gamePanel.levelNow][i].hitBox.y = gamePanel.object[gamePanel.levelNow][i].originalHitPointY;
            }
            i += 1;
        }
        return idx;
    }


    /**
     * Checks the status of the other creatures in the game world.
     *
     * @param creature    the creature to check
     * @param markedCrture the array of marked creatures
     * @return the index of the hit creature
     */
    /* player -> monster, other creature hit,collision */
    public int checkCreatureStatus(Creature creature, Creature[][] markedCrture){
        int idx = 911;  //could be anything high
        int i = 0;  //loop var
        while (i < markedCrture[1].length){
            if(markedCrture[gamePanel.levelNow][i] != null){
                //hitbox's position
                creature.hitBox.x = creature.mapX + creature.hitBox.x;
                creature.hitBox.y = creature.mapY + creature.hitBox.y;

                markedCrture[gamePanel.levelNow][i].hitBox.x = markedCrture[gamePanel.levelNow][i].mapX + markedCrture[gamePanel.levelNow][i].hitBox.x;
                markedCrture[gamePanel.levelNow][i].hitBox.y = markedCrture[gamePanel.levelNow][i].mapY + markedCrture[gamePanel.levelNow][i].hitBox.y;

                if (creature.dir == "up"){
                    creature.hitBox.y -= creature.velocity;
                }
                if (creature.dir == "down"){
                    creature.hitBox.y += creature.velocity;
                }
                if (creature.dir == "leftside"){
                    creature.hitBox.x -= creature.velocity;
                }
                if (creature.dir == "rightside"){
                    creature.hitBox.x += creature.velocity;
                }
                if(creature.hitBox.intersects(markedCrture[gamePanel.levelNow][i].hitBox)){
                    if(markedCrture[gamePanel.levelNow][i] != creature){  //this avoids entity to include itself as  target of collision
                        creature.hitDone = true;
                        idx = i;
                        if(creature.name == "player" && gamePanel.player.hasFinalKey && markedCrture[gamePanel.levelNow][i].name == "princess"){
                            markedCrture[gamePanel.levelNow][i].inPrison = false;
                            try {
                                markedCrture[gamePanel.levelNow][i].down = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NPC/princess.png")));
                                markedCrture[gamePanel.levelNow][i].down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NPC/princess.png")));
                                markedCrture[gamePanel.levelNow][i].up = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NPC/princessU.png")));
                                markedCrture[gamePanel.levelNow][i].up2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NPC/princessU.png")));
                            } catch (IOException e) {
                                logger.log(Level.SEVERE, "Cannot not find image from given path.");
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                //reset
                creature.hitBox.x = creature.originalHitPointX;
                creature.hitBox.y = creature.originalHitPointY;
                markedCrture[gamePanel.levelNow][i].hitBox.x = markedCrture[gamePanel.levelNow][i].originalHitPointX;
                markedCrture[gamePanel.levelNow][i].hitBox.y = markedCrture[gamePanel.levelNow][i].originalHitPointY;
            }
            i += 1;
        }
        return idx;
    }
    /**
     * Checks the status of the player in relation to a specific object or creature.
     *
     * @param creature the creature to check
     * @return a boolean indicating if the player made contact with the creature
     */
    /*check if certain object hit a player, creature in arg is saw, or sth for ex.....sth->player collision */
    public boolean checkPlayerStatus(Creature creature){
            boolean playerContact = false;
            creature.hitBox.x = creature.mapX + creature.hitBox.x;
            creature.hitBox.y = creature.mapY + creature.hitBox.y;

            gamePanel.player.hitBox.x = gamePanel.player.mapX + gamePanel.player.hitBox.x;
            gamePanel.player.hitBox.y = gamePanel.player.mapY + gamePanel.player.hitBox.y;

            if (creature.dir == "up"){
                creature.hitBox.y -= creature.velocity;
            }
            if (creature.dir == "down"){
                creature.hitBox.y += creature.velocity;
            }
            if (creature.dir == "leftside"){
                creature.hitBox.x -= creature.velocity;
            }
            if (creature.dir == "rightside"){
                creature.hitBox.x += creature.velocity;
            }
            if(creature.hitBox.intersects(gamePanel.player.hitBox)){
                creature.hitDone = true;
                playerContact = true;
                if(creature.name == "saw"){changesprite = 0;}

            }
            if(creature.name == "saw") {
                if (changesprite == 0) {
                    try {
                        creature.rightside = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/trap/bloddysaw1.png")));
                        creature.rightside2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/trap/bloddysaw2.png")));
                        creature.leftside = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/trap/bloddysaw3.png")));
                        creature.leftside2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/trap/bloddysaw4.png")));
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, "Cannot not find image from given path.");
                        throw new RuntimeException(e);
                    }
                }
            }
            //reset
            creature.hitBox.x = creature.originalHitPointX;
            creature.hitBox.y = creature.originalHitPointY;
            gamePanel.player.hitBox.x = gamePanel.player.originalHitPointX;
            gamePanel.player.hitBox.y = gamePanel.player.originalHitPointY;
            return playerContact;
    }
}

