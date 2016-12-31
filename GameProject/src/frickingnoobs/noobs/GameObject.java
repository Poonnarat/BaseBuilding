package frickingnoobs.noobs;

import DataTypes.Location;

import static frickingnoobs.noobs.Launcher.game;


/**
 * Created by Username on 19/12/2016.
 */
public class GameObject {
    //Anything that has to keep track of its position in the game world will inherit from the gameobject class
    Location Position;

    public int sprite; //Temporary variable


    GameObject() {
        Position = new Location(0, 0);
    }

    GameObject(Location pos) {
        Position = pos;
    }

    public void OnMovePosition() {
        //Override to do something when the position is changed
    }

    void Update(){//What to do on every tick
        if(Position.x*game.TileSize >= game.topLeftFocus.x-game.TileSize && Position.x*game.TileSize <= game.topLeftFocus.x + game.cameraPixelsWidth+game.TileSize && Position.y*game.TileSize >= game.topLeftFocus.y-game.TileSize  && Position.y*game.TileSize <= game.topLeftFocus.y + game.cameraPixelsHeight+game.TileSize){
            game.objectsToRender.add(this);
        }
    }
}
