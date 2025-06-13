package cz.cvut.fel.pjv.Data;

import cz.cvut.fel.pjv.creature.Creature;
import cz.cvut.fel.pjv.main.GamePanel;
import cz.cvut.fel.pjv.object.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class SaverLoaderTest {
    private GamePanel gamePanel;
    private SaverLoader saverLoader;

    @BeforeEach
    void setUp() {
        gamePanel = new GamePanel();
        saverLoader = new SaverLoader(gamePanel);
    }

    @Test
    void getItem_ValidName_ReturnsCorrectItem() {
        Creature item = saverLoader.getItem("hearth");
        assertNotNull(item);
        assertTrue(item instanceof CrystalHearth);

        item = saverLoader.getItem("displayer");
        assertNotNull(item);
        assertTrue(item instanceof Displayer);

        item = saverLoader.getItem("key");
        assertNotNull(item);
        assertTrue(item instanceof Key);

        item = saverLoader.getItem("MysteryPotion");
        assertNotNull(item);
        assertTrue(item instanceof MysteryPotion);

        item = saverLoader.getItem("bracelet");
        assertNotNull(item);
        assertTrue(item instanceof SpeedBracelet);

        item = saverLoader.getItem("finalkey");
        assertNotNull(item);
        assertTrue(item instanceof FinalKey);
    }

    @Test
    void makeALoad_ValidSaveFile_LoadsGameData() {
        try {
            // Perform a save to create a sample save file
            saverLoader.doASave();

            // Modify the game data to ensure it gets updated by makeALoad()
            gamePanel.player.fullLifes = 10;
            gamePanel.player.life = 5;
            gamePanel.player.damage = 50;
            gamePanel.player.hasDisplayer = true;
            gamePanel.player.hasFinalKey = true;
            gamePanel.player.hasKey = true;
            gamePanel.player.mapX = 1;
            gamePanel.player.mapY = 2;
            gamePanel.player.inventory.clear();
            gamePanel.object[0][0] = new Key(gamePanel); // Add an object to be replaced

            // Perform a load to update the game data
            saverLoader.makeALoad();

            // Verify that the game data has been correctly loaded
            assertEquals(9, gamePanel.player.fullLifes);
            assertEquals(1, gamePanel.player.damage);
            assertFalse(gamePanel.player.hasDisplayer);
            assertFalse(gamePanel.player.hasFinalKey);
            assertFalse(gamePanel.player.hasKey);
            assertEquals(0, gamePanel.player.inventory.size());
            assertNull(gamePanel.object[0][0]); // The object should have been replaced with null
        } finally {
            // Clean up: delete the save file after the test
            File saveFile = new File("save.txt");
            saveFile.delete();
        }
    }

}