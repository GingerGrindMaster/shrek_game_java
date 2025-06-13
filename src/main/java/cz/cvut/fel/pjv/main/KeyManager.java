package cz.cvut.fel.pjv.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * KeyManager class handles key events and manages the state of various keys for the game.
 */
public class KeyManager implements KeyListener {
    GamePanel gamePanel;
    public boolean up,  down, left, right, talk_key_Q;

    /**
     * Constructs a KeyManager object with the specified GamePanel.
     *
     * @param gamePanel the GamePanel object to associate with the KeyManager
     */
    public KeyManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }


    /**
     * Invoked when a key is typed (pressed and released).
     *
     * @param e the KeyEvent that occurred
     */
    @Override
    public void keyTyped(KeyEvent e) {}


    /**
     * Invoked when a key is pressed.
     *
     * @param e the KeyEvent that occurred
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int c = e.getKeyCode();
        if(gamePanel.phase == gamePanel.startPhase){
            if (c == KeyEvent.VK_S){
                gamePanel.ui.task += 1;
                if(gamePanel.ui.task > 2){
                    gamePanel.ui.task = 0;
                }
            }
            if(c == KeyEvent.VK_W){
                gamePanel.ui.task -= 1;
                if(gamePanel.ui.task < 0){
                    gamePanel.ui.task = 2;
                }
            }
            if(c == KeyEvent.VK_ENTER){
                if(gamePanel.ui.task == 0){
                    gamePanel.phase = gamePanel.playingPhase;
                }
                if(gamePanel.ui.task == 1){
                    gamePanel.saverLoader.makeALoad();
                    gamePanel.phase = gamePanel.playingPhase;
                }
                if(gamePanel.ui.task == 2){
                    System.exit(0);
                }
            }
        }


        if (gamePanel.phase == gamePanel.playingPhase){
            if (c == KeyEvent.VK_W) {
                up = true;
            }
            if (c == KeyEvent.VK_A) {
                left = true;
            }
            if (c == KeyEvent.VK_S) {
                down = true;
            }
            if (c == KeyEvent.VK_D) {
                right = true;
            }
            if (c == KeyEvent.VK_ESCAPE){  // pausing
                gamePanel.phase = gamePanel.pausedPhase;
            }
            if (c == KeyEvent.VK_Q){
                talk_key_Q = true;
            }
            if(c == KeyEvent.VK_SPACE){
                gamePanel.player.isAttacking = true;
            }
            if(c == KeyEvent.VK_E){
                gamePanel.phase = gamePanel.inventoryPhase;
            }
            if(c == KeyEvent.VK_O){
                gamePanel.phase = gamePanel.controlsPhase;
            }
            if(c == KeyEvent.VK_C){
                gamePanel.saverLoader.doASave();  //save current game state
                System.out.println("game saved");
                gamePanel.phase = gamePanel.talkingPhase;
                gamePanel.ui.currSmallTalk = "\n\n\n\n                          Game Saved!";
            }
        }
        else if (gamePanel.phase == gamePanel.pausedPhase){
            if (c == KeyEvent.VK_ESCAPE){  // pausing
                gamePanel.phase = gamePanel.playingPhase;
            }
        }
        else if(gamePanel.phase == gamePanel.talkingPhase){
            if(gamePanel.levelNow == 3){
                if (c == KeyEvent.VK_Q){
                    gamePanel.phase = gamePanel.winPhase;
                }
            }
            else if (c == KeyEvent.VK_Q){
                gamePanel.phase = gamePanel.playingPhase;
            }
        }
        else if(gamePanel.phase == gamePanel.inventoryPhase){
            if(c == KeyEvent.VK_E){
                gamePanel.phase = gamePanel.playingPhase;
            }
            if(c == KeyEvent.VK_W){
                if(gamePanel.ui.inPlaceRow != 0){
                    gamePanel.ui.inPlaceRow --;
                }
            }
            if(c == KeyEvent.VK_A){
                if(gamePanel.ui.inPlaceCol != 0){
                    gamePanel.ui.inPlaceCol --;
                }
            }
            if(c == KeyEvent.VK_S){
                if(gamePanel.ui.inPlaceRow != 2){
                    gamePanel.ui.inPlaceRow ++;
                }
            }
            if(c == KeyEvent.VK_D){
                if(gamePanel.ui.inPlaceCol != 2){
                    gamePanel.ui.inPlaceCol ++;
                }
            }
            if(c == KeyEvent.VK_ENTER){
                gamePanel.player.chooseItem();
            }
        }
        else if(gamePanel.phase == gamePanel.controlsPhase){
            if(c == KeyEvent.VK_O){
                gamePanel.phase = gamePanel.playingPhase;
            }
        }
        //WIN
        else if (gamePanel.phase == gamePanel.winPhase){
            if(c == KeyEvent.VK_ENTER) {
                if (gamePanel.ui.task == 0) {
                    gamePanel.restart();
                    gamePanel.phase = gamePanel.startPhase;
                }
            }
        }
        //GAME OVER
        else if(gamePanel.phase == gamePanel.gameOverPhase){
            if (c == KeyEvent.VK_S){
                gamePanel.ui.task += 1;
                if(gamePanel.ui.task > 1){
                    gamePanel.ui.task = 0;
                }
            }
            if(c == KeyEvent.VK_W){
                gamePanel.ui.task -= 1;
                if(gamePanel.ui.task < 0){
                    gamePanel.ui.task = 1;
                }
            }
            if(c == KeyEvent.VK_ENTER) {
                if (gamePanel.ui.task == 0) {
                    //retry
                    gamePanel.retryGame();
                    gamePanel.phase = gamePanel.playingPhase;
                }
                if (gamePanel.ui.task == 1) {
                    gamePanel.restart();
                    gamePanel.phase = gamePanel.startPhase;

                }
            }
        }
    }

    /**
     * Invoked when a key has been released.
     *
     * @param e the KeyEvent object representing the key release event
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int c = e.getKeyCode();
        if (c == KeyEvent.VK_W) {
            up = false;
        }
        if (c == KeyEvent.VK_A) {
            left = false;
        }
        if (c == KeyEvent.VK_S) {
            down = false;
        }
        if (c == KeyEvent.VK_D) {
            right = false;
        }


    }
}
