package com.company;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.*;
import java.util.*;
/**
 * Created by Username on 11/07/2016.
 */
abstract class Enemy{

    int x,y;
    private int wx,wy;
    private Tile nextWaypoint;
    int speed = 1;
    int health = 1;
    int curHealth;
    int value = 5;
    String enemyImageLocation = "enemy.png";
    BufferedImage enemyImage;
    void editMaxHealth(int add) {
        health += add;
        curHealth = health;
    }

    Enemy(int waveNo){
        nextWaypoint = Game.entrance;
        GetNextWaypointPosition();
        x = Game.entrance.GetWorldPos().x;
        y = Game.entrance.GetWorldPos().y;
        getImage();
        health += waveNo-1;
        value += waveNo;
        speed += waveNo/5;
        curHealth = health;
    }
    void getImage(){
        try{
            enemyImage = ImageIO.read(new File(enemyImageLocation));
        }catch(IOException e){
            System.out.println("Could not find " + enemyImageLocation);
        }
    }
    void Move(){
        //If the next waypoint is not in the column
        if(x - wx != 0){
            if(x - wx < 0){
                x += speed;
            }
            else{
                x -= speed;
            }
        }
        else if(y - wy != 0){
            if(y - wy < 0){
                y += speed;
            }
            else{
                y -= speed;
            }
        }
        else{//Must have reached our next waypoint
            if(nextWaypoint == Game.exit){
                Game.Lives --;
                Game.UpdateLives();
                Destroy();
            }
            else{
                int index = Arrays.asList(Game.waypoints).indexOf(nextWaypoint);
                nextWaypoint = Game.waypoints[index+1];
                GetNextWaypointPosition();
            }
        }
    }
    void CalculateHit(Tower tower){
        curHealth -= tower.damage;
        if(curHealth <= 0){
            GiveMoney();
            DestroyNow();
        }
    }
    void DestroyNow(){
        if(Game.enemiesInGame.contains(this)) {
            Game.enemiesInGame.remove(this);
        }
    }
    void Destroy(){
        Game.destroyQ.add(this);
    }
    private void GiveMoney(){
        Game.AddMoney(value);
    }
    private void GetNextWaypointPosition(){
        wx = nextWaypoint.x * Game.tileSize;
        wy = nextWaypoint.y * Game.tileSize;
    }
}
