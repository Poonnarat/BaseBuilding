package frickingnoobs.noobs;

import DataTypes.Location;

/**
 * Created by Username on 19/12/2016.
 */
public class Tile extends GameObject{
    public enum TerrainType {Dirt, Grass, Stone}
    public TerrainType tileType;
    public Building buildingInTile;
    public Tile(Location pos, TerrainType type){
        this.Position = pos;
        this.tileType = type;
    }
}
