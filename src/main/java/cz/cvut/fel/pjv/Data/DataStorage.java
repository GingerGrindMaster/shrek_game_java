package cz.cvut.fel.pjv.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents the data storage for the game.
 * Stores various game-related data such as player attributes, items, and map information.
 */
public class DataStorage implements Serializable {
    int full_lifes;
    int life;
    int damage;
    public boolean hasKey;
    public boolean hasFinalKey;
    public boolean hasDisplayer;
    int playerMapX;
    int playerMapY;

    ArrayList<String> itemName = new ArrayList<>();

    String[][] itemOnMapName;
    int[][] itemOnMapX;
    int[][] itemOnMapY;
    boolean[][] itemOnMapOpened;

}
