package frickingnoobs.noobs;

import DataTypes.Location;

import static frickingnoobs.noobs.Launcher.game;

/**
 * Created by Username on 19/12/2016.
 */
public class World {

    public Tile[][] worldTiles;
    public int width,height;

    public World(int sizeX, int sizeY, boolean generateNew){
        width = sizeX;
        height = sizeY;
        if(generateNew){
           GenerateMap();
        }
    }
    void GenerateMap(){
        System.out.println("Generating Map");
        System.out.println(width + ":" + height);
        worldTiles = new Tile[width][height];
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                Tile.TerrainType type = null;
                if((x+y)%2 == 1){
                    type = Tile.TerrainType.Grass;
                }
                else{
                    type = Tile.TerrainType.Dirt;
                }
                Tile tile = new Tile(new Location(x,y), type);
                worldTiles[x][y] = tile;
                game.objectsInGame.add(tile);
            }
        }
    }
}
