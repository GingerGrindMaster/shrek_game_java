package cz.cvut.fel.pjv.creature;

import cz.cvut.fel.pjv.main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrashCan_npc extends Creature{

    private static  final Logger logger = Logger.getLogger(TrashCan_npc.class.getName());

    /**
     * Constructs a TrashCan_npc object.
     *
     * @param gamePanel The GamePanel object.
     */
    public TrashCan_npc(GamePanel gamePanel) {
        super(gamePanel);
        dir = "rightside";
        velocity = 1;
        getNPCImage();
        prepareSmallTalk();
        hitBox.x = 2;
        hitBox.y = 2;
        hitBox.width = 40;
        hitBox.height = 40;
    }

    /**
     * Loads the image of the NPC.
     * This method sets the image for different directions.
     */
    public void getNPCImage() {
        try{
            up = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NPC/trashcanU1.png")));
            up2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NPC/trashcanU2.png")));
            down = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NPC/trashcanD1.png")));
            down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NPC/trashcanD2.png")));
            leftside = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NPC/trashcanD1.png")));
            leftside2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NPC/trashcanD2.png")));
            rightside = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NPC/trashcanD1.png")));
            rightside2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NPC/trashcanD2.png")));
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
        dirCalmer++;
        if(dirCalmer == 60){
            Random random = new Random();
            int i = random.nextInt(100)+1;
            if(i <= 25){ dir = "up";}
            if(i > 25 && i<= 50){dir="down";}
            if(i >50 && i <= 75){dir="rightside";}
            if(i > 75 && i <= 100){dir="leftside";}
            dirCalmer = 0;
        }
    }

    /**
     * Prepares small talk options for the NPC.
     * The small talk options are stored in an array of strings.
     */
    public void prepareSmallTalk(){
        smallTalkOptions[0] =
        "      Někde v tomto levelu je záhadná lahvička.\n\n"+
        "              jóóóó, jogurt to mám ráád\n\n"+
        "        a steak už jsem neměl ani nepamatuju.\n\n"+
        "      věčně mi tu hrabou ty sprostý kostlivci\n"+
        "                  !#?!@!@!?#!%@@!#\n"+
        "               jóóó, jednou budu milionář.\n";
        smallTalkOptions[1] =
        "           Jóóó, když jsem byl mladej koš,\n" +
        "            do mě někdo vyhodil pár mincí\n"+
        "       a od tý doby šetřím a až budu milionář,\n"+
        "                tak si koupím dalmatýny.\n"+
        "              Pokud máš tu lahvičku u sebe,\n"+
        "                   !@?!?#!?!?#?#!??!#?#\n"+
        "             poslouchej pozorně co ti řeknu.\n"+
        "                   !@?!?#!?!?#?#!??!#?#\n"+
        "                   !@?!?#!?!?#?#!??!#?#\n";

        smallTalkOptions[2] =
        "        Jakože, ty kráso prostě sněhuláci jsou\n"+
        "                        prostě nejlepší.\n"+
        "               Ta lahvička o který ti chci říct\n"+
        "                    !!@#<?#!?@!@#@@!#\n"+
        "            je zatrolenej nahodilej lektvar.\n"+
        "                 Jóóóó, já budu milionář.\n"+
        "   Když jsem byl malej koš, někdo ho do mě vylil\n"+
        "                    a teď vypadám takto.\n"+
        "        Ale prej tě většinou buď jen zabije nebo\n"+
        "          tě komplet vyléčí, docela pohoda ne?\n";

        smallTalkOptions[3]=
        "                  !#?!@!@!?#!%@@!#\n"+
        "                  ?!!?@#!>#!?>!!@>\n"+
        "                  !#?!@!@!?#!%@@!#\n"+
        "                  ?!!?@#!>#!?>!!@>\n"+
        "                  !#?!@!@!?#!%@@!#\n"+
        "                  ?!!?@#!>#!?>!!@>\n"+
        "                  !#?!@!#!?#!%@@!#\n"+
        "                  !#?!#!@!?#!%#@!#\n"+
        "                  !#?!#!@!?#!%#@!#\n"+
        "                  !#?#@!#!?#!%@#!#\n";
    }

    /**
     * Displays wise words of the NPC.
     * If the NPC is not in prison, it invokes the parent class method to display wise words.
     */
    public void tellWiseWords(){
        super.tellWiseWords();
    }



}
