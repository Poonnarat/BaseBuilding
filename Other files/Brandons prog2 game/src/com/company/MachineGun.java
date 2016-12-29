package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Username on 20/07/2016.
 */
public class MachineGun extends Tower {
    public MachineGun(int x, int y){
        towerName = "MachineGun";
        damage = 1;
        attackSpeed = 5;
        range = 150;
        price = 200;
        xPos = x;
        yPos = y;
        try{
            BufferedImage images = ImageIO.read(new File("MachineGun.png"));
            sprites[0] = images.getSubimage(0,0,32,32);
            sprites[1] = images.getSubimage(32,0,32,32);
        }catch(IOException e){
            System.out.println("MachineGun.png not found");
        }
    }
}
