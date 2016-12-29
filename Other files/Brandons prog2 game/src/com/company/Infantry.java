package com.company;

/**
 * Created by Username on 21/07/2016.
 */
public class Infantry extends Enemy {
    public Infantry(int waveNo){
        super(waveNo);
        enemyImageLocation = "Infantry.png";
        getImage();
        editMaxHealth(2);
        value += 5;

    }
}
