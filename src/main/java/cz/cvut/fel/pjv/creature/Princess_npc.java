package cz.cvut.fel.pjv.creature;

import cz.cvut.fel.pjv.main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Princess_npc extends Creature{

    private static  final Logger logger = Logger.getLogger(Princess_npc.class.getName());

    /**
     * Constructs a Princess_npc object.
     *
     * @param gamePanel The GamePanel object.
     */
    public Princess_npc(GamePanel gamePanel) {
        super(gamePanel);
        inPrison = true;
        dir = "down";
        name = "princess";
        velocity = 0;
        getNPCImage();
        prepareSmallTalk();

    }
    /**
     * Loads the image of the NPC.
     * This method sets the image for different directions.
     */
    public void getNPCImage() {
        try{
            down = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NPC/locked_princess2.png")));
            down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NPC/locked_princess2.png")));
            up = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NPC/locked_princess2.png")));
            up2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NPC/locked_princess2.png")));

        } catch(IOException e){
            logger.log(Level.SEVERE, "Cannot not find image from given path.");
            e.printStackTrace();
        }
    }

    /**
     * Sets the walking direction of the NPC.
     * The direction is set based on the player's direction if the NPC is not in prison.
     */
    public void setWalkingDirection(){
        if(inPrison = false){
            dir = gamePanel.player.dir;
        }
    }

    /**
     * Prepares small talk options for the NPC.
     * The small talk options are stored in an array of strings.
     */
    public void prepareSmallTalk() {
        smallTalkOptions[0] =
        "Och, jak dlouho už tu čekám na svého zachránce.\n"+
        " Kostlivci mě tu bez jídla vězní už dlouhé měsíce.\n"+
        "                 Asi si říkáš jak je možné,\n" +
        "              že pořád vypadám tak dobře.\n"+
        "    Ach chlapče, Vietnam mě naučil všechno ... \n"+
        "            Chybí mi ta vůně napalmu po ránu.\n"+
        "  Myslela jsem si, že mě vysvobodí nudný princ,\n"+
        "   ale v takového krasavce, navíc s mužnou hrudí,\n"+
        "    jsem ani nedoufala. Vezmeš si mě za ženu?\n";
    }

    /**
     * Displays wise words of the NPC.
     * If the NPC is not in prison, it invokes the parent class method to display wise words.
     */
    public void tellWiseWords(){  //tells player important info
        if(inPrison == false){
            super.tellWiseWords();
        }
    }
}
