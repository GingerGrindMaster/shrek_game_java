package cz.cvut.fel.pjv.creature;

import cz.cvut.fel.pjv.main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Wizard_npc extends Creature{

    private static  final Logger logger = Logger.getLogger(Wizard_npc.class.getName());


    /**
     * Constructs a Wizard_npc object.
     *
     * @param gamePanel The GamePanel object.
     */
    public Wizard_npc(GamePanel gamePanel) {
        super(gamePanel);
        dir = "rightside";
        velocity = 1;
        getNPCImage();
        prepareSmallTalk();
    }

    /**
     * Loads the image of the NPC.
     * This method sets the image for different directions.
     */
    public void getNPCImage() {
        try{
            rightside = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NPC/wizard_R.png")));
            rightside2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NPC/wizard_R2.png")));
            leftside = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NPC/wizard_L.png")));
            leftside2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NPC/wizard_L2.png")));
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
        dirCalmer ++;
        if(dirCalmer == 60){
            Random random = new Random();
            int r = random.nextInt(100)+1;
            if(r <= 50){
                dir = "leftside";
            }
            if(r > 50){
                dir = "rightside";
            }
            dirCalmer = 0;
        }
    }

    /**
     * Prepares small talk options for the NPC.
     * The small talk options are stored in an array of strings.
     */
    public void prepareSmallTalk(){
        smallTalkOptions[0] =
                "                 Dostaň se skrz dungeon živý,\n" +
                "nepodlehni jeho nástrahám, nepodceň ho, je silný,\n" +
                "               osvoboď krásku v něm vězněnou,\n" +
                "           požádej ji o ruku a udělej ji svou ženou.\n" +
                "                 Dungeon život dává i bere,\n" +
                "                   střez se proto nepřítele.\n" +
                "                    Hledej předměty a klíče,\n" +
                "doufám, že si hru užiješ, neboť jak řekl Nietzsche:\n" +
                "      „V každém pravém muži je skryto dítě.\"";
        smallTalkOptions[1] =
                "                        Zdravím hrdino,\n"+
                "             chceš říct jedno zásadní moudro?\n" +
                "                      Já věděl, že chceš!\n" +
                "                      Nuže, slyš radu mou!\n" +
                "           Hmmmmmmm, jak to vlastně bylo?\n"+
                "                       Ááá, už to mám.\n\n"+
                "              Nikdy ve snu na záchod nechoď,\n"
                + "                            je to chyták!\n";
        smallTalkOptions[2] = "               Pro postup do dalšího levelu\n"+
                              "                    potřebuješ získat klíč.\n" +
                              "                  Bez něj daleko nedojdeš!";

        smallTalkOptions[3]=
                "                  Poslechni si tenhle vtip,\n"+
                "            každý se potřeuje občas uklidnit.\n\n"+
                "             Programátor povídá druhému:\n"+
                "  \"Doktor mi říkal, že mám oslabený organismus,\n"+
                "              tak mně předepsal beta karoten.\"\n" +
                "                          \"A pomáhá to?\"\n" +
                "            \"Já si ho zatím nevzal, počkám si,\n"+
                "                      až bude plná verze.\"";


    }

    /**
     * Displays wise words of the NPC.
     * If the NPC is not in prison, it invokes the parent class method to display wise words.
     */
    public void tellWiseWords(){  //tells player important info
        super.tellWiseWords();
    }

}
