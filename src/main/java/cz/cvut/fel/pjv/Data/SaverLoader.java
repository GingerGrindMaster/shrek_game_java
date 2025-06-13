package cz.cvut.fel.pjv.Data;

import cz.cvut.fel.pjv.creature.Creature;
import cz.cvut.fel.pjv.creature.Player;
import cz.cvut.fel.pjv.main.GamePanel;
import cz.cvut.fel.pjv.object.*;

import java.io.*;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles saving and loading game data.
 */
public class SaverLoader { //inspirated by tutorial
    GamePanel gamePanel;
    private static  final Logger logger = Logger.getLogger(Player.class.getName());


    /**
     * Constructs a new instance of the SaverLoader class.
     *
     * @param gamePanel the GamePanel instance
     */
    public SaverLoader(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }


    /**
     * Retrieves a creature item based on its name.
     *
     * @param name the name of the item
     * @return the Creature item, or null if not found
     */
    public Creature getItem(String name){
        Creature item = null;
        if(name.equals("hearth")){item = new CrystalHearth(gamePanel);}
        if(Objects.equals(name, "displayer")){item = new Displayer(gamePanel); }
        if(name.equals("key")){item = new Key(gamePanel);}
        if(name.equals("MysteryPotion")){item = new MysteryPotion(gamePanel);}
        if(name.equals("bracelet")){item = new SpeedBracelet(gamePanel); }
        if(name.equals("finalkey")){item = new FinalKey(gamePanel);}
        return item;
    }

    /**
     * Performs a save operation by serializing the game data and writing it to a file.
     */
    public void doASave(){
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File("save.txt")));
            DataStorage dataStorage = new DataStorage();
            //player
            dataStorage.full_lifes = gamePanel.player.fullLifes;
            dataStorage.life = gamePanel.player.life;
            dataStorage.damage = gamePanel.player.damage;
            dataStorage.hasDisplayer = gamePanel.player.hasDisplayer;
            dataStorage.hasFinalKey = gamePanel.player.hasFinalKey;
            dataStorage.hasKey = gamePanel.player.hasKey;
            dataStorage.playerMapX = gamePanel.player.mapX;
            dataStorage.playerMapY = gamePanel.player.mapY;
            //inventory
            for (int k =0; k < gamePanel.player.inventory.size(); k++){
                dataStorage.itemName.add(gamePanel.player.inventory.get(k).name);
            }
            //objs on map
            dataStorage.itemOnMapName = new String[gamePanel.levelCount][gamePanel.object[1].length];
            dataStorage.itemOnMapX = new int[gamePanel.levelCount][gamePanel.object[1].length];
            dataStorage.itemOnMapY = new int [gamePanel.levelCount][gamePanel.object[1].length];
            dataStorage.itemOnMapOpened = new boolean [gamePanel.levelCount][gamePanel.object[1].length];
            // Iterate over each level
            for(int levelNow = 0;  levelNow < gamePanel.levelCount; levelNow ++){
                // Iterate over each object in the level
                for(int i = 0; i < gamePanel.object[1].length; i++){
                    // Check if the object is null
                    if(gamePanel.object[levelNow][i] == null){
                        // Set the item name as "EMPTY" if the object is null
                        dataStorage.itemOnMapName[levelNow][i] = "EMPTY";
                    }
                    else{
                        // Set the item name as the object's name
                        dataStorage.itemOnMapName[levelNow][i] = gamePanel.object[levelNow][i].name;
                        // Set the item's map coordinates
                        dataStorage.itemOnMapX[levelNow][i] = gamePanel.object[levelNow][i].mapX;
                        dataStorage.itemOnMapY[levelNow][i] = gamePanel.object[levelNow][i].mapY;
                        // Set the item's opened status
                        dataStorage.itemOnMapOpened[levelNow][i] = gamePanel.object[levelNow][i].opened;
                    }
                }
            }
            objectOutputStream.writeObject(dataStorage);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "doSave method error.");
            throw new RuntimeException(e);
        }

    }

    /**
     * Loads the game state from a saved file.
     * Reads a serialized DataStorage object from the file and updates the game panel and player attributes accordingly.
     * Clears the player's inventory and populates it with items from the loaded data.
     * Updates the objects on the map based on the loaded data, including their positions, opened status, and visual representation.
     */
    public void makeALoad(){

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File("save.txt")));
            DataStorage dataStorage = (DataStorage)objectInputStream.readObject();

            // Update player's attributes based on the loaded data
            gamePanel.player.fullLifes = dataStorage.full_lifes;
            gamePanel.player.life = dataStorage.life;
            gamePanel.player.damage = dataStorage.damage;
            gamePanel.player.hasDisplayer = dataStorage.hasDisplayer;
            gamePanel.player.hasFinalKey = dataStorage.hasFinalKey;
            gamePanel.player.hasKey = dataStorage.hasKey;
            gamePanel.player.mapX = dataStorage.playerMapX;
            gamePanel.player.mapY = dataStorage.playerMapY;

            gamePanel.player.inventory.clear();

            // Populate player's inventory with items from the loaded data
            for (int i = 0; i < dataStorage.itemName.size(); i++){
                gamePanel.player.inventory.add(getItem(dataStorage.itemName.get(i)));
            }
            // Update objects on the map based on the loaded data
            for(int levelNow = 0;  levelNow < gamePanel.levelCount; levelNow ++){
                for(int i = 0; i < gamePanel.object[1].length; i++){

                   if(dataStorage.itemOnMapName[levelNow][i].equals("EMPTY")){
                       gamePanel.object[levelNow][i] = null;
                   }
                   else{
                       // Set the object based on its name retrieved from dataStorage
                       gamePanel.object[levelNow][i].equals(getItem(dataStorage.itemOnMapName[levelNow][i]));  //zda se ze funguje
                       gamePanel.object[levelNow][i].mapX = dataStorage.itemOnMapX[levelNow][i];
                       gamePanel.object[levelNow][i].mapY = dataStorage.itemOnMapY[levelNow][i];
                       gamePanel.object[levelNow][i].opened = dataStorage.itemOnMapOpened[levelNow][i];

                       // If the object is opened, update its visual representation and hitbox
                       if(gamePanel.object[levelNow][i].opened){
                           gamePanel.object[levelNow][i].down = gamePanel.object[levelNow][i].down2;
                           gamePanel.object[levelNow][i].hitBox.height = 0;
                           gamePanel.object[levelNow][i].hitBox.width = 0;
                       }

                   }

                }
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "makeALoad method error.");
            throw new RuntimeException(e);
        }
    }

}
