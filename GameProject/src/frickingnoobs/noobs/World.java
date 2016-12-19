package frickingnoobs.noobs;

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
        worldTiles = new Tile[width][height];
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                worldTiles[x][y] = new Tile(x,y, Tile.TerrainType.Grass);
            }
        }
    }
}
