package cz.cvut.fel.pjv.main;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Manages player actions and interactions with the game environment.
 * Handles movement between levels, collision detection, and triggering actions based on player position and direction.
 */
public class ActionManager {
    GamePanel gamePanel;
    ActionRectnagle actR[][][];

    int prevActionX;
    int prevActionY;
    boolean canTouchAction = true;

    private static  final Logger logger = Logger.getLogger(ActionManager.class.getName());

    /**
     * Constructs an ActionManager object with the specified GamePanel.
     * Initializes the action rectangles for each level, column, and row in the game panel.
     *
     * @param gamePanel the GamePanel instance
     */
    public ActionManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        actR = new ActionRectnagle[gamePanel.levelCount][gamePanel.screenCols][gamePanel.screenRows];
        int level = 0;
        int col, row;
        col = row = 0;
        while(level < gamePanel.levelCount && col < gamePanel.screenCols && row < gamePanel.screenRows){
            actR[level][col][row] = new ActionRectnagle();
            actR[level][col][row].x=25;
            actR[level][col][row].y= 4;
            actR[level][col][row].width= 4;
            actR[level][col][row].height= 4;
            actR[level][col][row].actRectXorig = actR[level][col][row].x;
            actR[level][col][row].actRectYorig = actR[level][col][row].y;
            col ++;
            if(col == gamePanel.screenCols){
                col = 0;
                row ++;

                if(row == gamePanel.screenRows){
                    row = 0;
                    level ++;
                }
            }
        }


    }
    /**
     * Checks the player's action and triggers corresponding actions or movements.
     * Determines if the player can perform an action based on their distance from the previous action.
     */
    public void checkAction(){
        //check if more than 1 tile away  - if not, no more action allowed
        int distanceX = Math.abs(gamePanel.player.mapX-prevActionX);
        int distanceY = Math.abs(gamePanel.player.mapY-prevActionY);
        int dist = Math.max(distanceX, distanceY);
        if(dist > gamePanel.actualTileSize){
            canTouchAction = true;
        }
        if(canTouchAction){
            //if(hit(6, 10, "up")){invisTrapAttack(6, 11, gamePanel.talkingPhase); }
            if(hit(0,6, 9, "up")){
                heal(6,9, gamePanel.talkingPhase);  //secret heal pot
            }

            //LEVEL TO LEVEL MOVEMENT
            else if(hit(0,6, 0, "any")){
                goThroughTo(1,6, 12);
                logger.log(Level.INFO, "Passed through to different level"); //from level 0 -> 1
            }
            else if(hit(1,6, 13, "down")){
                goThroughTo(0,6, 1);
                logger.log(Level.INFO, "Passed through to different level"); //move back 1->0
            }

            else if(hit(1,9, 5, "any")){
                goThroughTo(2,12, 12);
                logger.log(Level.INFO, "Passed through to different level"); //move 1->2
            }

            else if(hit(2,11, 12, "leftside")){
                goThroughTo(1,8, 5);
                logger.log(Level.INFO, "Passed through to different level"); //move 2->1
            }

            else if(hit(2,3, 12, "any")){
                goThroughTo(3,6, 11);
                logger.log(Level.INFO, "Passed through to different level"); //move 2->3
            }

            //else if(hit(3,6, 12, "any")){
            // teleportTo(2,2, 12);
            // } //move 3->2
        }

    }
    /**
     * Moves the player to a different level, column, and row in the game panel.
     * Updates the player's position, previous action coordinates, and disables immediate return to the old level.
     *
     * @param level the level to move to
     * @param col   the column to move to
     * @param row   the row to move to
     */
    public void goThroughTo(int level, int col, int row){
        gamePanel.levelNow = level;
        gamePanel.player.mapX = gamePanel.actualTileSize*col;
        gamePanel.player.mapY = gamePanel.actualTileSize*row;
        prevActionX = gamePanel.player.mapX;
        prevActionY =   gamePanel.player.mapY;
        canTouchAction = false;  //stop from immediate return back to old level

    }

    /**
     * Checks if the player's hit box intersects with the specified action rectangle on the current level.
     * The hit is considered valid if the player's direction matches the required direction or if the required direction is "any".
     * Updates the previous action coordinates if a valid hit is detected.
     *
     * @param level  the level of the action rectangle
     * @param col    the column of the action rectangle
     * @param row    the row of the action rectangle
     * @param reqDir the required direction of the player
     * @return true if the hit is valid, false otherwise
     */
    public boolean hit(int level, int col, int row, String reqDir){
        boolean hit = false;

        if(level == gamePanel.levelNow){
            gamePanel.player.hitBox.x = gamePanel.player.mapX + gamePanel.player.hitBox.x;
            gamePanel.player.hitBox.y = gamePanel.player.mapY + gamePanel.player.hitBox.y;
            actR[level][col][row].x = col * gamePanel.actualTileSize + actR[level][col][row].x;
            actR[level][col][row].y = row * gamePanel.actualTileSize + actR[level][col][row].y;
            if(gamePanel.player.hitBox.intersects(actR[level][col][row]) &&  actR[level][col][row].actionDone == false){
                if(Objects.equals(gamePanel.player.dir, reqDir) || Objects.equals(reqDir, "any")){
                    hit = true;
                    prevActionX = gamePanel.player.mapX;  //to check distance between player and prev action
                    prevActionY = gamePanel.player.mapY;
                }
            }
            //reset
            gamePanel.player.hitBox.x = gamePanel.player.originalHitPointX;
            gamePanel.player.hitBox.y = gamePanel.player.originalHitPointY;
            actR[level][col][row].x = actR[level][col][row].actRectXorig;
            actR[level][col][row].y = actR[level][col][row].actRectYorig;
        }

        return hit;
    }

    /**
     * Triggers an invisible trap attack at the specified column and row coordinates on the current level.
     * Updates the game phase, small talk message, player life, and marks the action as done.
     * Disables immediate return to the previous action.
     *
     * @param col        the column of the trap
     * @param row        the row of the trap
     * @param gamePhase  the game phase to set
     */
    public void invisTrapAttack(int col, int row, int gamePhase ){  //can be used and works, i have not used it though
        gamePanel.phase = gamePhase;
        gamePanel.ui.currSmallTalk = "invis trap!";
        gamePanel.player.life -= 3;
        actR[gamePanel.levelNow][col][row].actionDone = true;
        canTouchAction = false;

    }

    /**
     * Heals the player at the specified column and row coordinates on the current level if the talk key Q is pressed.
     * Updates the game phase, small talk message, player life, and logs a message indicating the heal pot was found.
     *
     * @param col       the column of the heal pot
     * @param row       the row of the heal pot
     * @param gamePhase the game phase to set
     */
    public void heal(int col, int row, int gamePhase ){
        if(gamePanel.keyM.talk_key_Q){
            gamePanel.phase = gamePhase;
            gamePanel.ui.currSmallTalk = "U found secret heal-pot.\n U r fully healed";
            gamePanel.player.life = gamePanel.player.fullLifes;
            logger.log(Level.INFO, "Secret heal pot found - player healed completely.");
        }
    }
}
