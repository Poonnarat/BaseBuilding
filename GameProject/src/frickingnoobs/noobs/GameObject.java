package frickingnoobs.noobs;

/**
 * Created by Username on 19/12/2016.
 */
public class GameObject {
    //Anything that has to keep track of its position in the game world will inherit from the gameobject class
    int x,y;
    public GameObject(){
        x = 0;
        y = 0;
    }
    public GameObject(int x, int y){
        this.x = x;
        this.y = y;
    }
}
