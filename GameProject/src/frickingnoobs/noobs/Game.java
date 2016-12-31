package frickingnoobs.noobs;
import DataTypes.Location;
import Display.Display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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

    public static GamePanel gp;

    //World info
    World world;
    int worldWidth = 200, worldHeight = 200;

    //Graphics variables
    static int TileSize = 200; // In pixels.

    //Keep track of in game objects
    ArrayList<GameObject> objectsInGame = new ArrayList<>(); //Every object that is currently in the game
    ArrayList<GameObject> objectsToRender; //Every object that will be rendered

    //Render start point
    public static Location topLeftFocus = new Location(0,0);
    public static int cameraPixelsWidth = 640;
    public static int cameraPixelsHeight = 320;
    public static int cameMoveSpeed = 5;
    private static boolean pLeft,pRight,pUp,pDown = false;

    //Actions for keybindings
    public static Action camUpPress = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            pUp = true;
        }
    };
    public static Action camDownPress = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            pDown = true;
        }
    };
    public static Action camRightPress = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            pRight = true;
        }
    };
    public static Action camLeftPress = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            pLeft = true;
        }
    };
    public static Action camUpRelease = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            pUp = false;
        }
    };
    public static Action camDownRelease = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            pDown = false;
        }
    };
    public static Action camLeftRelease = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            pLeft = false;
        }
    };
    public static Action camRightRelease = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            pRight = false;
        }
    };

    //Game methods
    public Game(String title, int width, int height){
        this.width = width;
        this.height = height;
        this.title = title;
    }
    public void init(){// initialize graphic stuff
        gp = new GamePanel();
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
        //Update Camera
        MoveCamera();
        objectsToRender = new ArrayList<>();
        objectsInGame.forEach(GameObject::Update);
    }

    private void render(){
        display.Revalidate();
        gp.repaint();

    }
    void CreateNewWorld(){
        world = new World(worldWidth,worldHeight,true);
    }
    void MoveCamera(){
        int deltaX = 0;
        int deltaY = 0;
        if(pDown){
            deltaY++;
        }
        if(pUp){
            deltaY--;
        }
        if(pLeft){
            deltaX--;
        }
        if(pRight){
            deltaX++;
        }
        topLeftFocus.x += cameMoveSpeed*deltaX;
        topLeftFocus.y += cameMoveSpeed*deltaY;
    }
    private class GamePanel extends JPanel {
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            //Draw here
            if(objectsToRender != null) {
                for (GameObject go : objectsToRender) {
                    if(go.sprite == 1){
                        g.setColor(Color.BLACK);
                    }
                    else{
                        g.setColor(Color.white);
                    }
                    g.fillRect(Math.round(go.Position.x * TileSize - topLeftFocus.x), Math.round(go.Position.y * TileSize - topLeftFocus.y), TileSize, TileSize);
                }
            }
        }
    }
}
