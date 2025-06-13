package cz.cvut.fel.pjv.main;

import cz.cvut.fel.pjv.creature.Princess_npc;
import cz.cvut.fel.pjv.creature.TrashCan_npc;
import cz.cvut.fel.pjv.creature.Wizard_npc;
import cz.cvut.fel.pjv.monsters.*;
import cz.cvut.fel.pjv.object.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjectManager {
    GamePanel gamePanel;
    private static  final Logger logger = Logger.getLogger(ActionManager.class.getName());

    /**
     * Constructs an ObjectManager with the specified GamePanel.
     *
     * @param gamePanel the GamePanel to associate with the ObjectManager
     */
    public ObjectManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

    }

    /**
     * Reads object data from a file and puts the objects on the game panel.
     *
     * @throws IOException if an I/O error occurs while reading the file
     */
    public void putObject() throws IOException {
        String path = "monster_object_npc_files/objects";
        File f = new File(path);
        try {
            Scanner scanner = new Scanner(f).useLocale(Locale.US);
            String name;
            int x, y, lvl;
            int obj_count = 2;
            while (scanner.hasNextLine()) {
                name = scanner.next();
                lvl = scanner.nextInt();
                x = scanner.nextInt();
                y = scanner.nextInt();

                switch (name) {
                    case "NEXT":
                        obj_count = 0;
                        break;

                    case "CRYSTALHEARTH":
                        gamePanel.object[lvl][obj_count] = new CrystalHearth(gamePanel);
                        gamePanel.object[lvl][obj_count].mapX = x * gamePanel.actualTileSize;
                        gamePanel.object[lvl][obj_count].mapY = y * gamePanel.actualTileSize;
                        obj_count++;
                        break;
                    case "GATE":
                        gamePanel.object[lvl][obj_count] = new Gate(gamePanel);
                        gamePanel.object[lvl][obj_count].mapX = x * gamePanel.actualTileSize;
                        gamePanel.object[lvl][obj_count].mapY = y * gamePanel.actualTileSize;

                        if(obj_count == 1){  //backdoors always on indx 1
                            gamePanel.object[lvl][obj_count].opened = true;
                            gamePanel.object[lvl][obj_count].hitBox.width = 0;
                            gamePanel.object[lvl][obj_count].hitBox.height = 0;
                            try {
                                gamePanel.object[lvl][obj_count].down = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/things/opengate.png")));
                            }
                            catch (IOException e) {
                                logger.log(Level.SEVERE, "Cannot not find image from given path.");
                                throw new RuntimeException(e);
                            }
                        }
                        obj_count++;
                        break;

                    case "KEY":
                        gamePanel.object[lvl][obj_count] = new Key(gamePanel);
                        gamePanel.object[lvl][obj_count].mapX = x * gamePanel.actualTileSize;
                        gamePanel.object[lvl][obj_count].mapY = y * gamePanel.actualTileSize;
                        obj_count++;
                        break;
                    case "SPEEDBRACELET":
                        gamePanel.object[lvl][obj_count] = new SpeedBracelet(gamePanel);
                        gamePanel.object[lvl][obj_count].mapX = x * gamePanel.actualTileSize;
                        gamePanel.object[lvl][obj_count].mapY = y * gamePanel.actualTileSize;
                        obj_count++;
                        break;
                    case "DISPLAYER":
                        gamePanel.object[lvl][obj_count] = new Displayer(gamePanel);
                        gamePanel.object[lvl][obj_count].mapX = x * gamePanel.actualTileSize;
                        gamePanel.object[lvl][obj_count].mapY = y * gamePanel.actualTileSize;
                        obj_count++;
                        break;
                    case "MYSTERYPOTION":
                        gamePanel.object[lvl][obj_count] = new MysteryPotion(gamePanel);
                        gamePanel.object[lvl][obj_count].mapX = x * gamePanel.actualTileSize;
                        gamePanel.object[lvl][obj_count].mapY = y * gamePanel.actualTileSize;
                        obj_count++;
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Scanner error - file was not found.");
            throw new RuntimeException(e);
        }
        //TESTING LEVEL
//        levelNum =4;
//        gamePanel.object[levelNum][0] = new CrystalHearth(gamePanel);
//        gamePanel.object[levelNum][0].mapX = 8 * gamePanel.actualTileSize;
//        gamePanel.object[levelNum][0].mapY = 12 * gamePanel.actualTileSize;
//
//        gamePanel.object[levelNum][1] = new CrystalHearth(gamePanel);
//        gamePanel.object[levelNum][1].mapX = 3 * gamePanel.actualTileSize;
//        gamePanel.object[levelNum][1].mapY = 12 * gamePanel.actualTileSize;
//
//        gamePanel.monsters[levelNum][2] = new Skeleton(gamePanel);
//        gamePanel.monsters[levelNum][2].mapX = 3 * gamePanel.actualTileSize;
//        gamePanel.monsters[levelNum][2].mapY = 12 * gamePanel.actualTileSize;
//        gamePanel.monsters[levelNum][3] = new Skeleton(gamePanel);
//        gamePanel.monsters[levelNum][3].mapX = 4 * gamePanel.actualTileSize;
//        gamePanel.monsters[levelNum][3].mapY = 12 * gamePanel.actualTileSize;


    }

    /**
     * Reads NPC data from a file and puts the NPCs on the game panel.
     */
    public void putNPC() {
        String path = "monster_object_npc_files/npcs";
        File f = new File(path);
        try {
            Scanner scanner = new Scanner(f).useLocale(Locale.US);
            String name;
            int x, y, lvl;
            int npc_count = 0;
            while (scanner.hasNextLine()) {
                name = scanner.next();
                lvl = scanner.nextInt();
                x = scanner.nextInt();
                y = scanner.nextInt();

                switch (name) {
                    case "NEXT":
                        npc_count = 0;
                        break;
                    case "WIZARD":
                        gamePanel.npcs[lvl][npc_count] = new Wizard_npc(gamePanel);
                        gamePanel.npcs[lvl][npc_count].mapX = x * gamePanel.actualTileSize;
                        gamePanel.npcs[lvl][npc_count].mapY = y * gamePanel.actualTileSize;
                        npc_count++;
                        break;

                    case "TRASHCAN":
                        gamePanel.npcs[lvl][npc_count] = new TrashCan_npc(gamePanel);
                        gamePanel.npcs[lvl][npc_count].mapX = x * gamePanel.actualTileSize;
                        gamePanel.npcs[lvl][npc_count].mapY = y * gamePanel.actualTileSize;
                        npc_count++;
                        break;
                    case "PRINCESS":
                        gamePanel.npcs[lvl][npc_count] = new Princess_npc(gamePanel);
                        gamePanel.npcs[lvl][npc_count].mapX = x * gamePanel.actualTileSize;
                        gamePanel.npcs[lvl][npc_count].mapY = y * gamePanel.actualTileSize;
                        npc_count++;
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Scanner error - file was not found.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads monster data from a file and puts the monsters on the game panel.
     *
     * @throws IOException if an I/O error occurs while reading the file
     */
    public void puMonster() throws IOException {
        String path = "monster_object_npc_files/monsters";
        File f = new File(path);
        try {
            Scanner scanner = new Scanner(f).useLocale(Locale.US);
            String name;
            int x, y, lvl;
            int monster_count = 0;
            while (scanner.hasNextLine()) {
                name = scanner.next();
                lvl = scanner.nextInt();
                x = scanner.nextInt();
                y = scanner.nextInt();

                //System.out.println(name+" "+ lvl +" "+ x+ " "+ y);
                switch (name) {
                    case "NEXT": monster_count = 0; break;
                    case "SAW":
                        gamePanel.monsters[lvl][monster_count] = new Saw(gamePanel);
                        gamePanel.monsters[lvl][monster_count].mapX = x * gamePanel.actualTileSize;
                        gamePanel.monsters[lvl][monster_count].mapY = y * gamePanel.actualTileSize;
                        monster_count++;
                        break;
                    case "SKELETON":
                        gamePanel.monsters[lvl][monster_count] = new Skeleton(gamePanel);
                        gamePanel.monsters[lvl][monster_count].mapX = x * gamePanel.actualTileSize;
                        gamePanel.monsters[lvl][monster_count].mapY = y * gamePanel.actualTileSize;
                        monster_count++;
                        break;
                    case "SPIKES":
                        gamePanel.monsters[lvl][monster_count] = new Spikes(gamePanel);
                        gamePanel.monsters[lvl][monster_count].mapX = x * gamePanel.actualTileSize;
                        gamePanel.monsters[lvl][monster_count].mapY = y * gamePanel.actualTileSize;
                        monster_count++;
                        break;
                    case "BAT":
                        gamePanel.monsters[lvl][monster_count] = new Bat(gamePanel);
                        gamePanel.monsters[lvl][monster_count].mapX = x * gamePanel.actualTileSize;
                        gamePanel.monsters[lvl][monster_count].mapY = y * gamePanel.actualTileSize;
                        monster_count++;
                        break;
                    case "SPIDER":
                        gamePanel.monsters[lvl][monster_count] = new Spider(gamePanel);
                        gamePanel.monsters[lvl][monster_count].mapX = x * gamePanel.actualTileSize;
                        gamePanel.monsters[lvl][monster_count].mapY = y * gamePanel.actualTileSize;
                        monster_count++;
                        break;

                    default:
                        logger.log(Level.SEVERE, "Unknown value!");
                        throw new IllegalStateException("Unexpected value: " + name);
                }
            }
        } catch (IOException | IllegalStateException e) {
            logger.log(Level.SEVERE, "putmonster() error!");
            throw new RuntimeException(e);
        }
    }
}
