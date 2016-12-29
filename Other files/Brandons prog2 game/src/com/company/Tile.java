package com.company;

import java.awt.*;

/**
 * Created by Username on 10/07/2016.
 */
public class Tile {
    public enum Type {path, wall, empty}
    public Type tileType;
    Tower tower = null;

    public int x,y;

    public Tile(Type type, int xPos, int yPos){
        tileType = type;
        x = xPos;
        y = yPos;
    }
    public Point GetWorldPos(){
        return new Point(x * Game.tileSize, y * Game.tileSize);
    }
    public void SetTower(Tower tower){
        this.tower = tower;
    }
    public boolean HasTower(){
        return tower != null;
    }
    public Point GetCenter(){
        return new Point(x*Game.tileSize + Game.tileSize/2, y * Game.tileSize + Game.tileSize/2);
    }
}