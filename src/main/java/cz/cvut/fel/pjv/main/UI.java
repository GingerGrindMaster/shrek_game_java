package cz.cvut.fel.pjv.main;

import cz.cvut.fel.pjv.creature.Creature;
import cz.cvut.fel.pjv.object.Lifes;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The UI class represents the user interface of the game.
 */
public class UI {
    private static  final Logger logger = Logger.getLogger(ActionManager.class.getName());


    GamePanel gamePanel;
    Font mvboli;
    Graphics2D g2d;
    public String currSmallTalk = "";
    public int task = 0;
    BufferedImage fulllife, twoHearths, oneHearth, dead;
    ArrayList<String> msg = new ArrayList<>();
    ArrayList<Integer> msgCounter = new ArrayList<>();
    int inPlaceCol = 0;
    int inPlaceRow = 0;


    /**
     * Constructs a new UI object with the specified GamePanel.
     *
     * @param gamePanel the GamePanel to associate with the UI
     */
    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        mvboli = new Font("MV Boli", Font.PLAIN, 25);
        Creature Lifes = new Lifes(gamePanel);
        fulllife = Lifes.img;
        twoHearths = Lifes.img2;
        oneHearth = Lifes.img3;
        dead = Lifes.img4;
    }

    /**
     * Draws the user interface.
     *
     * @param g2d the Graphics2D object used for drawing
     */
    public void draw(Graphics2D g2d){
        this.g2d = g2d;
        g2d.setFont(mvboli);
        g2d.setColor(Color.WHITE);

        //START
        if(gamePanel.phase == gamePanel.startPhase){
            drawStartingScreen();
        }
        //PLAY
        else if(gamePanel.phase == gamePanel.playingPhase){
            displayPlayerLifes();
            displayGivenDamage();

        }
        //PAUSED
        else if(gamePanel.phase == gamePanel.pausedPhase){
            displayPlayerLifes();
            drawPause();
        }
        //DIALOGUE
        else if(gamePanel.phase == gamePanel.talkingPhase){
            displayPlayerLifes();
            drawTalkBubble();
        }
        //INVENTORY
        else if(gamePanel.phase == gamePanel.inventoryPhase){
            displayInventory();
        }
        //CONTROLS POPUP
        else if(gamePanel.phase == gamePanel.controlsPhase){
            displayControls();
        }
        //GAMEOVER
        else if(gamePanel.phase == gamePanel.gameOverPhase){  //pokud budu delat jine kdzy umre a kdyz zachrani princeznu
            drawGameOverScreen();
        }
        //WIN
        else if(gamePanel.phase == gamePanel.winPhase){  //pokud budu delat jine kdzy umre a kdyz zachrani princeznu
            drawWinScreen();
        }
        else{
            logger.log(Level.SEVERE, "Unknown game-phase. Unable to proceed.");
            System.out.println(gamePanel.phase);
        }

    }

    /**
     * Displays the controls popup.
     */
    public void displayControls(){
        Font myFont = new Font("Arial Unicode MS", Font.PLAIN, 20);
        int x = gamePanel.actualTileSize*3 + 24;
        int y = gamePanel.actualTileSize * 2;
        int w = gamePanel.actualTileSize *9;
        int h = gamePanel.actualTileSize *11;
        gamePanel.phase = gamePanel.controlsPhase;
        gamePanel.ui.currSmallTalk =
        "                       OVLÁDÁNÍ\n\n"+
        "W,A,S,D - POHYB\n"+
        "W,A,S,D + Q - POKEC S NPC\n"+
        "Q - ZAVŘENÍ DIALOG OKÉNKA\n"+
        "W,A,S,D + Q - OBJEVENÍ TAJNÉHO MÍSTA\n" +
        "                       PRO DOBYTÍ ŽIVOTŮ\n"+
        "O - MANUÁL OVLÁDÁNÍ\n"+
        "SPACE - ÚTOK\n"+
        "E - INVENTÁŘ\n"+
        "ENTER - POUŽITÍ ITEMU / ZVOLENÍ\n"+
        "ESC - PAUZA HRY\n"+
        "C - ULOŽENÍ HRY";

        makeSmallWindow(x,y,w,h);
        g2d.setFont(myFont);
        g2d.setColor(Color.WHITE);
        x += gamePanel.actualTileSize;
        y += gamePanel.actualTileSize;
        String[] lines = currSmallTalk.split("\n");

        // Get the height of one line of text
        FontMetrics fm = g2d.getFontMetrics();
        int lineHeight = fm.getHeight();
        lineHeight += 10;

        // Draw each line separately
        for (String line : lines) {
            g2d.drawString(line, x-30, y);
            y += lineHeight;
        }
    }

    /**
     * Displays the inventory.
     */
    public void displayInventory(){
        int x = gamePanel.player.mapX + gamePanel.actualTileSize - 120;
        int y = gamePanel.player.mapY - gamePanel.actualTileSize * 4;
        int w = gamePanel.actualTileSize *4;
        int h = gamePanel.actualTileSize *4;

        //inventory displayed only in our window
        if(gamePanel.player.mapX > gamePanel.actualTileSize*14){
            x = gamePanel.player.mapX - gamePanel.actualTileSize*4;
            y = gamePanel.player.mapY;
        }
        if(gamePanel.player.mapX < gamePanel.actualTileSize*2){
            x = gamePanel.player.mapX + gamePanel.actualTileSize;
            y = gamePanel.player.mapY;
        }
        if(gamePanel.player.mapY < gamePanel.actualTileSize*4){
            x = gamePanel.player.mapX + gamePanel.actualTileSize - 120;
            y = gamePanel.player.mapY + gamePanel.actualTileSize;
        }
        if(gamePanel.player.mapY < gamePanel.actualTileSize*4 && gamePanel.player.mapX < gamePanel.actualTileSize*2){
            x = gamePanel.player.mapX + gamePanel.actualTileSize;
            y = gamePanel.player.mapY;
        }
        if(gamePanel.player.mapY < gamePanel.actualTileSize*4 && gamePanel.player.mapX > gamePanel.actualTileSize*14){
            x = gamePanel.player.mapX - gamePanel.actualTileSize*4;
            y = gamePanel.player.mapY;
        }
        makeSmallWindow(x,y,w,h);

        final int inPlaceXBeginning = x +20;
        final int inPlaceYBeginning = y +20;
        int inPlaceX = inPlaceXBeginning;
        int inPlaceY = inPlaceYBeginning;
        for(int i = 0; i < gamePanel.player.inventory.size(); i++){
            g2d.drawImage(gamePanel.player.inventory.get(i).down, inPlaceX, inPlaceY, 48, 48,  null);
            inPlaceX += gamePanel.actualTileSize;
            if(i == 2 || i == 5){
                inPlaceX = inPlaceXBeginning;
                inPlaceY += gamePanel.actualTileSize;
            }
        }
        int mouseX = inPlaceXBeginning + gamePanel.actualTileSize * inPlaceCol;
        int mouseY = inPlaceYBeginning + gamePanel.actualTileSize  *inPlaceRow;
        int mouseW = gamePanel.actualTileSize;
        int mouseH = gamePanel.actualTileSize;

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.white);
        g2d.drawRoundRect(mouseX, mouseY, mouseW, mouseH, 10, 10 );
    }

    /**
     * Draws the game over screen.
     */
    public void drawGameOverScreen(){
        BufferedImage img;
        try {
            img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/things/torch.png")));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot not find image from given path.");
            throw new RuntimeException(e);
        }
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 80));
        g2d.setColor(new Color(19,19,19));
        g2d.fillRect(0, 0, gamePanel.screenWdth, gamePanel.screenHGht);
        String text = "GAME OVER!";
        int x = getX(text);  //in method to not slow down our program
        int y = 90;
        g2d.setColor(new Color(255, 255, 255));
        g2d.drawString(text, x, y);
        x = gamePanel.actualTileSize* 5 + 20;
        y = gamePanel.actualTileSize* 3;
        g2d.drawImage(dead, x, y, 48 * 5, 48 * 5, null);
        //----
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN,50f));
        g2d.setColor(new Color(255, 255, 255));
        text = "Retry?";
        x = getX(text);
        y += gamePanel.actualTileSize*7;
        g2d.drawString(text, x, y);
        if(task == 0){
            g2d.drawImage(img, x - 35, y -35, 30, 30, null);
        }
        //----
        text = "Quit?";
        x = getX(text);
        y += 55;
        g2d.drawString(text, x, y);
        if(task == 1){
            g2d.drawImage(img, x - 35, y -35, 30, 30, null);
        }
    }

    /**
     * Draws the win screen.
     */
    public void drawWinScreen(){
        BufferedImage img;

        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 50));
        g2d.setColor(new Color(19,19,19));
        g2d.fillRect(0, 0, gamePanel.screenWdth, gamePanel.screenHGht);
        String text = "CONGRATS, YOU WON!!";
        int x = getX(text);  //in method to not slow down our program
        int y = 90;
        g2d.setColor(new Color(255, 255, 255));
        g2d.drawString(text, x, y);
        x = gamePanel.actualTileSize* 5 + 20;
        y = gamePanel.actualTileSize* 3;
        try {
            img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NPC/princess.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        g2d.drawImage(img, x, y, 48 * 5, 48 * 5, null);
        try {
            img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/things/torch.png")));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot not find image from given path.");
            throw new RuntimeException(e);
        }

        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN,50f));
        g2d.setColor(new Color(255, 255, 255));
        text = "Quit?";
        x = getX(text);
        y = gamePanel.actualTileSize*9;
        g2d.drawString(text, x, y);

        if(task == 0){
            g2d.drawImage(img, x - 35, y -35, 30, 30, null);
        }
    }

    /**
     * Displays the player's lives.
     */
    public void displayPlayerLifes(){
        int x = gamePanel.screenCols* gamePanel.actualTileSize - (gamePanel.actualTileSize*2);
        int y = gamePanel.actualTileSize - 30;
        if(gamePanel.player.life > 0 && gamePanel.player.life < 4){
            g2d.drawImage(oneHearth, x, y,48*2, 48*2,  null);
        }
        if(gamePanel.player.life > 3 && gamePanel.player.life < 7){
            g2d.drawImage(twoHearths, x, y,48*2, 48*2,  null);
        }
        if(gamePanel.player.life > 6 && gamePanel.player.life < 10){
            g2d.drawImage(fulllife, x, y,48*2, 48*2,  null);
        }
    }

    /**
     * Displays the damage given to the player.
     */
    public void displayGivenDamage(){  //inspired
        int x = gamePanel.actualTileSize*9;
        int y = gamePanel.actualTileSize* 1;
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 20));
        for (int i = 0; i < msg.size(); i++){
            if(msg.get(i) != null){
                g2d.setColor(Color.white);
                g2d.drawString(msg.get(i), x, y);
                int counter = msgCounter.get(i) + 1;
                msgCounter.set(i, counter);
                y += 50;
                if(msgCounter.get(i) > 180){
                    msg.remove(i);
                    msgCounter.remove(i);
                }
            }
        }


    }

    /**
     * Adds a message to the list of messages.
     *
     * @param s the message to be added
     */
    public void addMsgToList(String s){
            msg.add(s);
            msgCounter.add(0);
    }

    /**
     * Calculates and returns the inventory index based on the current row and column.
     *
     * @return the inventory index
     */
    public int getItmInventoryIdx(){
        int idx = inPlaceCol + (inPlaceRow *3);
        return idx;
    }


//-------- STARTING MENU SCREEN ---------------

    /**
     * Draws the starting screen.
     */
    public void drawStartingScreen() {
        int x, y;
        BufferedImage background;
        try {
            background = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/things/menu_background.png")));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot not find image from given path.");
            throw new RuntimeException(e);
        }
        g2d.drawImage(background, 0,0, null);

        String startingText = "Saving Private Princes";
        x = gamePanel.actualTileSize + 60;
        y = gamePanel. actualTileSize * 2;
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 50));
        g2d.setColor(Color.white);
        g2d.drawString(startingText, x, y);

        x = gamePanel.screenWdth/2 - (gamePanel.actualTileSize*4)/2  + 10;
        y += gamePanel.actualTileSize*4 + 85;
        g2d.drawImage(gamePanel.player.down, x, y, gamePanel.actualTileSize*4, gamePanel.actualTileSize*4, null);
        drawMenu(x, y);
    }
//-------- DRAW MENU  ---------------

    /**
     * Draws a menu with options and corresponding icons at the specified position.
     *
     * @param curr_x the x-coordinate of the menu's starting position
     * @param curr_y the y-coordinate of the menu's starting position
     */
    public void drawMenu(int curr_x, int curr_y){
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 20));
        String s;
        BufferedImage img;
        try {
            img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/things/torch.png")));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot not find image from given path.");
            throw new RuntimeException(e);
        }

        s = "START NEW GAME";
        curr_x = getX(s);
        curr_y -= gamePanel.actualTileSize*3;
        g2d.drawString(s, curr_x,curr_y);

        if(task == 0){
            g2d.drawImage(img, curr_x - 35, curr_y-25, 30, 30, null);
        }

        s = "LOAD GAME";
        curr_x = getX(s);
        curr_y += gamePanel.actualTileSize;
        g2d.drawString(s, curr_x,curr_y);
        if(task == 1){
            g2d.drawImage(img, curr_x - 35, curr_y-25, 30, 30, null);
        }
        s = "QUIT";
        curr_x = getX(s);
        curr_y += gamePanel.actualTileSize;
        g2d.drawString(s, curr_x,curr_y);
        if(task == 2){
            g2d.drawImage(img, curr_x - 35, curr_y-25, 30, 30, null);
        }
    }

//-------- DRAW TALKING BUBBLE ---------------

    /**
     * Draws the talk bubble for dialogue.
     */
    public void drawTalkBubble(){
        Font myFont = new Font("Arial Unicode MS", Font.ITALIC, 20);
        int x, y, wdth, hght;
        x = gamePanel.actualTileSize*2;
        y = gamePanel.actualTileSize;
        wdth = gamePanel.screenWdth - (gamePanel.actualTileSize*6);
        hght = gamePanel.actualTileSize * 8;
        makeSmallWindow(x, y, wdth, hght);
        g2d.setFont(myFont);
        g2d.setColor(Color.WHITE);
        x += gamePanel.actualTileSize;
        y += gamePanel.actualTileSize;
        String[] lines = currSmallTalk.split("\n");

        // Get the height of one line of text
        FontMetrics fm = g2d.getFontMetrics();
        int lineHeight = fm.getHeight();
        lineHeight += 10;

        // Draw each line separately
        for (String line : lines) {
            g2d.drawString(line, x-30, y);
            y += lineHeight;
        }
    }

    /**
     * Creates a small window with the specified position, size, and appearance.
     *
     * @param x      the x-coordinate of the window's top-left corner
     * @param y      the y-coordinate of the window's top-left corner
     * @param width  the width of the window
     * @param height the height of the window
     */
    public void makeSmallWindow(int x, int y, int width, int height){
        Color color = new Color(14, 13, 13);
        Color color2 = new Color(192, 161, 12);
        g2d.setColor(color);
        int arcW = 50;
        int arcH = 50;
        g2d.setStroke(new BasicStroke(5));
        g2d.setPaint(color);
        g2d.fillRoundRect(x, y, width, height, arcW, arcH);
        g2d.setStroke(new BasicStroke(5));
        g2d.setPaint(color2);
        g2d.drawRoundRect(x + 5, y + 5, width - 10, height - 10, arcW - 10, arcH - 10);
    }
//-------- PAUSE  SCREEN ---------------

    /**
     * Draws the pause screen.
     */
    public void drawPause(){
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 80));
        String pauseText = "GAME IS PAUSED";
        int x = getX(pauseText);  //in method to not slow down our program
        int y = gamePanel.screenHGht/2;
        g2d.drawString(pauseText, x, y);

    }
    public int getX(String t){
        //display centered
        int len = (int)g2d.getFontMetrics().getStringBounds(t, g2d).getWidth();
        int x = gamePanel.screenWdth/2 - (len/2);
        return x;
    }
}
