package frickingnoobs.noobs;

import Display.Display;

public class Launcher {

    public static Game game;
    public static void main(String[] args) {
        game = new Game("Tile Game!", 640, 360);
        game.start();
    }
}
