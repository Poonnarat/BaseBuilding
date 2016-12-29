package com.company;

import java.util.*;
/**
 * Created by Username on 21/07/2016.
 */
public class WaveSpawner {
    int x = 0;
    int spawned = 0;
    List<Enemy> enemies = new ArrayList<>();
    int waveSpeed = 1;
    public WaveSpawner(Enemy[] e, int speed){
        for(int x = 0; x < e.length; x++){
            enemies.add(e[x]);
        }
        waveSpeed = speed;
    }
    void Update(){
        if(x >= 35 - (5 * waveSpeed)) {
            x = 0;
            Game.enemiesInGame.add(enemies.get(spawned));
            spawned++;
            if(spawned == enemies.size()){
                Game.finishedWaves.add(this);
            }
        }
        x++;
    }
}
