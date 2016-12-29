package com.company;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * Created by Username on 11/07/2016.
 */
public abstract class Tower {
    String towerName;
    BufferedImage[] sprites = new BufferedImage[2];
    int range;
    int damage;
    int attackSpeed;
    int xPos,yPos;
    int price;
    boolean shooting = false;

    int shootAnimation;

    Enemy targetEnemy = null;


    int shootBuffer = 0;
    double rotation = 0;

    void Update(){

        // Handle enemy stuff
        if(!Game.enemiesInGame.contains(targetEnemy)){targetEnemy = null;}
        if(targetEnemy == null && Game.enemiesInGame.size() > 0){
            targetEnemy = FindNewEnemy();
        }
        if (targetEnemy != null) {
            double angle = Math.atan((double)(targetEnemy.y - yPos) / (double)(targetEnemy.x - xPos));
            if(targetEnemy.x - xPos < 0){
                rotation = angle + Math.toRadians(180);
            }else{
                rotation = angle;
            }

            //MUST HAPPEN AFTER THE ROTATION IS CALCULATED
            if(Math.sqrt((targetEnemy.x - xPos) * (targetEnemy.x - xPos) + (targetEnemy.y - yPos) * (targetEnemy.y - yPos)) > range){
                targetEnemy = null;
            }
            if(shootBuffer >= 60){
                Shoot();
                shootBuffer = 0;
            }
            shootBuffer+= attackSpeed;
        }
        if(shootAnimation <= 0){
            shooting = false;
        }
        shootAnimation--;
    }
    Enemy FindNewEnemy(){
        Enemy closest = null;
        double distance = 0;
        boolean first = true;
        shootBuffer = 0;
        for (Enemy e: Game.enemiesInGame) {
            double curDistance = Math.sqrt((e.x - xPos) * (e.x - xPos) + (e.y - yPos) * (e.y - yPos));
            if(first){
                closest = e;
                distance = curDistance;
                first = false;
            }
            else if(curDistance < distance){
                closest = e;
                distance = curDistance;
            }
        }
        if(distance < range) {
            return closest;
        }
        return null;
    }
    void Shoot(){
        System.out.println("Shooting");
        shootAnimation = 5;
        shooting = true;
        if(targetEnemy != null) {
            targetEnemy.CalculateHit(this);
        }
    }
}
