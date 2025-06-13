package cz.cvut.fel.pjv.main;


import cz.cvut.fel.pjv.Data.SaverLoader;
import cz.cvut.fel.pjv.creature.Creature;
import cz.cvut.fel.pjv.creature.Player;
import cz.cvut.fel.pjv.surface.TileManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
This class represents the place where the game is running
*/
public class GamePanel extends JPanel implements Runnable{

    private static  final Logger logger = Logger.getLogger(GamePanel.class.getName());

    JPanel myPanel = new JPanel();
    //TILES
    final private int scalingConstant = 3;
    final private int origTileSize = 16;
    public final int actualTileSize = origTileSize * scalingConstant;
    //SCREEN LAYOUT, COLS AND ROWS
    final public int screenCols = 16;
    final public int screenRows = 14;
    public final int screenWdth = actualTileSize * screenCols;
    public final int screenHGht = actualTileSize * screenRows;
    //LEVELS
    public final int levelCount = 5;  //how many maps we can have
    public int levelNow = 0;  //map now
    //FPS SETTINGS
    final int FPS = 60;

    TileManager tileManager = new TileManager(this);
    public KeyManager keyM = new KeyManager(this);
    Thread gameThrd;
    public WalkableChecker walkableChecker = new WalkableChecker(this);
    public ObjectManager objM = new ObjectManager(this);
    public ActionManager aManager = new ActionManager(this);
    public UI ui = new UI(this);
    SaverLoader saverLoader = new SaverLoader(this);
    public Player player = new Player(this, keyM);

    //array for various game objects
    public Creature[][] object = new Creature[levelCount][20];
    //array for npcs
    public Creature[][] npcs = new Creature[levelCount][10];
    public Creature[][] monsters = new Creature[levelCount][20];
    ArrayList<Creature> listOfCreatures= new ArrayList<>();
    //------------- PHASES ------------------------
    public int phase;
    public final int startPhase = 0;
    public final int playingPhase = 1;
    public final int pausedPhase = 2;
    public final int talkingPhase = 3;
    public final int inventoryPhase = 4;
    public final int controlsPhase = 5;
    public final int gameOverPhase = 6;
    public final int winPhase = 7;

    public int counter = 0;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWdth, screenHGht));
        this.setBackground(Color.DARK_GRAY);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyM);
        this.setFocusable(true);
    }


    /**
     * Prepares the game by placing objects, NPCs, and monsters on the map and setting the initial game phase.
     */
    public void prepareGame() {
        try {
            objM.putObject();
            objM.putNPC();
            objM.puMonster();
            phase = startPhase;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Object/monster/NPC placing failed.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Resets the game state and restarts the game by placing objects, NPCs, and monsters on the map.
     */
    public void retryGame(){
        player.reset();
        try {
            objM.putObject();
            objM.putNPC();
            objM.puMonster();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Object/monster/NPC placing failed.");
            throw new RuntimeException(e);
        }

    }

    /**
     * Restarts the game by resetting the player and placing objects, NPCs, and monsters on the map.
     */
    public void restart(){
        player.reset();
        player.makeDefaultSetting();
        try {
            objM.putObject();
            objM.putNPC();
            objM.puMonster();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Object/monster/NPC placing failed.");
            throw new RuntimeException(e);
        }

    }

    /**
     * Starts the game thread to run the game loop.
     */
    public void startGameThrd(){
        gameThrd = new Thread(this);
        gameThrd.start();
    }

    /**
     * Runs the game loop by continuously updating and repainting the game panel.
     * The game loop manages the frame rate and ensures a consistent update interval.
     */
    @Override
    public void run() {
        double drawingInterval = 1000000000.0/FPS;
        double nextDrawAction = System.nanoTime() + drawingInterval;

        while(gameThrd != null){
            update();
            repaint();  //repaints updated window
            try { //FPS handling
                double remainingT = nextDrawAction - System.nanoTime();
                remainingT = remainingT/1000000;  //convert from nano s to milli s
                if (remainingT < 0){
                    remainingT = 0;
                }
                Thread.sleep((long) remainingT);   //wait till the time between end of draw and next draw passes
                nextDrawAction += drawingInterval;
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "FPS handling error.");
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Updates the game state based on the current game phase.
     */
    public void update(){
        switch(phase){
            case playingPhase:
                //player updating
                player.update();
                //npc updating
                for (int i = 0; i < npcs[1].length; i++){
                    if(npcs[levelNow][i] != null){
                        npcs[levelNow][i].update();
                    }
                }
                for (int i = 0; i < monsters[1].length; i++){
                    if(monsters[levelNow][i] != null && monsters[levelNow][i].life > 0){
                        monsters[levelNow][i].update();
                    }
                    if(monsters[levelNow][i] != null && monsters[levelNow][i].life <= 0){
                        if(monsters[levelNow][i].name == "skeleton"){
                            monsters[levelNow][i].decideIfDrop();
                            monsters[levelNow][i] = null;
                        }
                    }
                }
                break;
            case pausedPhase: /*for now nothing*/ break;
        }
    }

    /**
     * Paints the game components on the game panel.
     *
     * @param g the graphics context
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);  // = JPanel.paintComponent(g)
        Graphics2D g2D = (Graphics2D) g;  //typecast g to G2D
        if(phase == startPhase){
            ui.draw(g2D);
        }
        else {
            //draw tiles
            tileManager.draw(g2D);
            //add monsters
            for(int i = 0; i < monsters[1].length; i++){
                if(monsters[levelNow][i] != null){
                    listOfCreatures.add(monsters[levelNow][i]);
                }
            }
            //add NPCS to list
            for(int i = 0; i < npcs[1].length; i ++){
                if(npcs[levelNow][i] != null){
                    listOfCreatures.add(npcs[levelNow][i]);
                }
            }
            //add objects
            for(int i = 0; i < object[1].length; i++){
                if(object[levelNow][i] != null){
                    listOfCreatures.add(object[levelNow][i]);
                }
            }
            //add player
            listOfCreatures.add(player);

            //draw everything from the list
            for (int i = 0; i < listOfCreatures.size(); i++){
                listOfCreatures.get(i).draw(g2D);

            }
            //reset list
            listOfCreatures.clear();
            ui.draw(g2D);
        }
        g2D.dispose();  //g2D no longer needed
    }
}
