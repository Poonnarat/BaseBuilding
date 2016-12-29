package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Username on 11/07/2016.
 */
public class Pistol extends Tower {

    public Pistol(int xPos, int yPos){
        towerName = "Pistol Tower";
        price = 70;
        damage = 1;
        range = 150;
        attackSpeed = 2;
        this.xPos = xPos;
        this.yPos = yPos;
        try {
            BufferedImage images = ImageIO.read(new File("PistolSheet.png"));
            sprites[0] = images.getSubimage(0,0,32,32);
            sprites[1] = images.getSubimage(32,0,32,32);
        }catch(IOException e) {
            System.out.println("Could not find Pistol.png");
        }
    }
}
