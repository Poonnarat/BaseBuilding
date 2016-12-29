package frickingnoobs.noobs;
import DataTypes.Location;
import Display.Display;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

/**
 * Created by .poon on 12/18/2016 AD.
 */
public class Game implements Runnable{// This is the main class of the game

    private Display display;
    public int width, height;
    public String title;

    private Thread thread;
    private Boolean running = false;

    private BufferStrategy bs;
    private Graphics g;

    //World info
    World world;
    int worldWidth = 50, worldHeight = 50;

    //Graphics variables
    static int TileSize = 32; // In pixels.

    //Keep track of in game objects
    ArrayList<GameObject> objectsInGame = new ArrayList<>(); //Every object that is currently in the game
    ArrayList<GameObject> objectsToRender; //Every object that will be rendered

    //Render start point
    public static Location topLeftFocus = new Location(0,0);
    public static int cameraPixelsWidth = 640;
    public static int cameraPixelsHeight = 320;
    public static int cameMoveSpeed = 10;

    public Game(String title, int width, int height){
        this.width = width;
        this.height = height;
        this.title = title;
    }
    public void init(){// initialize graphic stuff
        display = new Display(title, width, height);
        topLeftFocus = new Location(worldWidth/2,worldHeight/2);
    }

    @Override
    public void run() {

        init();

        int fps = 60;
        double timePerTick = 1000000000 / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        long timer = 0;
        int ticks = 0;

        //Initialise world
        CreateNewWorld();

        while(running){
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            timer += now - lastTime;
            lastTime = now;

            if(delta >= 1){
                tick();
                render();
                ticks++;
                delta--;
            }

            if(timer >= 1000000000){
                System.out.println("Ticks and Frames: " + ticks);
                ticks = 0;
                timer = 0;
            }
        }

        stop();

    }


    public synchronized void start(){
        if(running){
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop(){
        if(!running)
            return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void tick(){
        objectsToRender = new ArrayList<>();
        objectsInGame.forEach(GameObject::Update);
    }

    private void render(){

        bs = display.getCanvas().getBufferStrategy();
        if(bs == null){
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();
        //Clear Screen
        g.clearRect(0, 0, width, height);
        //Draw Here!
        for (GameObject go: objectsToRender) {
            g.setColor(Color.green);
            g.drawRect(Math.round(go.Position.x*TileSize - topLeftFocus.x), Math.round(go.Position.y*TileSize - topLeftFocus.y),TileSize,TileSize);
        }

        //End Drawing!
        bs.show();
        g.dispose();

    }
    void CreateNewWorld(){
        world = new World(worldWidth,worldHeight,true);
    }
}
