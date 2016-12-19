package frickingnoobs.noobs;

import java.util.ArrayList;

/**
 * Created by Username on 19/12/2016.
 */
public class Camera extends GameObject {

    public int unitsInScreen = 5;
    public float zoomLevel = 1;

    ArrayList<GameObject> objectsInArea = new ArrayList<>(); //Objects that can be seen by the camera (For rendering purposes)

}
