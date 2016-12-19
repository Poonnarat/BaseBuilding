package frickingnoobs.noobs;

/**
 * Created by Username on 19/12/2016.
 */
public class Tile extends GameObject{
    public enum TerrainType {Dirt, Grass, Stone}
    public TerrainType tileType;
    public Building buildingInTile;
    public Tile(int x,int y, TerrainType type){
        this.x = x;
        this.y = y;
        this.tileType = type;
    }
}
