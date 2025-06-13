package cz.cvut.fel.pjv.surface;

import cz.cvut.fel.pjv.main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * The TileManager class manages tiles and their rendering on the game surface.
 */
public class TileManager {
    private static  final Logger logger = Logger.getLogger(TileManager.class.getName());
    GamePanel gamePanel;
    public Tile[] tile_types;
    public int[][][] levelTileNumbers;

    /**
     * Constructs a new TileManager object with the specified GamePanel.
     *
     * @param gamePanel the GamePanel instance
     */
    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        tile_types = new Tile[10];
        levelTileNumbers = new int[gamePanel.levelCount][gamePanel.screenCols][gamePanel.screenRows];
        getImg();

        readMap("/maps/level1.txt", 0);
        readMap("/maps/level2.txt", 1);
        readMap("/maps/level3.txt", 2);
        readMap("/maps/level4.txt", 3);
        readMap("/maps/test.txt", 4);

    }

    /**
     * Loads the tile images.
     */
    public void getImg(){
        try {
            tile_types[0] = new Tile();
            tile_types[0].img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/path.png")));
            tile_types[0].hit = false;

            tile_types[1] = new Tile();
            tile_types[1].img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/walls.png")));
            tile_types[1].hit = true;

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot not find level text file from given path.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Draws the tiles on the game surface.
     *
     * @param g2d the Graphics2D object
     */
    public void draw(Graphics2D g2d){
        int row =0, col = 0, x = 0, y = 0;
        while (col < gamePanel.screenCols && row < gamePanel.screenRows){
            int tileNumber = levelTileNumbers[gamePanel.levelNow][col][row];  //stores map data within
            g2d.drawImage(tile_types[tileNumber].img, x, y, gamePanel.actualTileSize, gamePanel.actualTileSize, null);
            col += 1;
            x += gamePanel.actualTileSize;
            //full line check
            if (col == gamePanel.screenCols){
                y += gamePanel.actualTileSize;
                col = 0;
                row += 1;
                x = 0;
            }
        }
    }

    /**
     * Reads the map data from a text file.
     *
     * @param path the path to the text file
     * @param map  the map index
     */
    public void readMap(String path, int map){
            InputStream inputStream = getClass().getResourceAsStream(path);
            assert inputStream != null;  //mby delete
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));  //reads text from a character-input stream
            int row = 0, col = 0;
            while(col < gamePanel.screenCols && row < gamePanel.screenRows){
                String line;
                try {
                    line = bufferedReader.readLine();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "readLine failed.");
                    throw new RuntimeException(e);
                }

                while (col < gamePanel.screenCols){
                        String[] nums = line.split(" ");
                        int num = Integer.parseInt(nums[col]);
                        levelTileNumbers[map][col][row] = num;
                        col += 1;
                }
                if (col == gamePanel.screenCols){
                    col = 0;
                    row += 1;
                }
            }
        try {
            bufferedReader.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Unable to close BufferedReader.");
            //throw new RuntimeException(e);  uncomment if wanted
        }


    }
}
