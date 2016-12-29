package com.company;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import static javax.swing.SwingUtilities.convertPoint;

public class Game implements ActionListener,SwingConstants,MouseMotionListener {

    JButton summonWave;
    JButton pistolTower;
    JButton machineTower;

    static int Lives = 30;
    static int Money = 150;
    static void AddMoney(int add){
        Money+= add;
        UpdateMoney();
    }

    static int wave = 0;
    int waveBufferTime = 300;
    int currentWaveTime = 0;
    static int mapWidth;
    static int mapHeight;
    static int tileSize = 32;
    List<Tile.Type[]> mapRows;
    static Tile[][] gameMap;
    static Tile[] waypoints;
    static Tile entrance,exit;
    Tower selectedTower;
    Image selectedTowerImage;
    List<Tower> towersBuilt;
    static List<Enemy> enemiesInGame;
    static List<Enemy> destroyQ;
    static List<WaveSpawner> spawningWaves;
    static List<WaveSpawner> finishedWaves;
    List<Enemy[]> Waves;

    Point mousePoint = new Point(0,0);
    Point mouseInScreen;

    boolean isBuilding = false;
    static boolean hasLives = true;

    JPanel textPanel;
    //All text labels in 1 place to keep it nice and neat (unlike the rest of the code)
    static JLabel moneyLbl;
    static JLabel livesLbl;
    static JLabel wavesLbl;
    static JLabel waveTimer;

    static boolean gameWon = false;
    static boolean gameLost = false;

    JFrame frame;

    public static void main(String[] args) {
	// write your code here
        Game game = new Game();
        game.Initialise();
    }
    public void mouseMoved(MouseEvent e){
        mousePoint = new Point(e.getX(),e.getY());
    }
    public void mouseDragged(MouseEvent e){

    }
    void Initialise(){
        frame = new JFrame();
        textPanel = new JPanel();
        moneyLbl = new JLabel("Money: " + Money);
        livesLbl = new JLabel("Lives: " + Lives);
        wavesLbl = new JLabel("Wave " + (wave + 1));
        waveTimer = new JLabel("Next wave in 15");


        //Add labels to text panel(Panel at the bottom)
        textPanel.add(livesLbl);
        textPanel.add(moneyLbl);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.PAGE_AXIS));

        //Initialise lists to keep track of objects in game
        towersBuilt = new ArrayList<>();
        enemiesInGame = new ArrayList<>();
        destroyQ = new ArrayList<>();
        Waves = new ArrayList<>();
        mapRows = new ArrayList<>();
        spawningWaves = new ArrayList<>();
        finishedWaves = new ArrayList<>();

        summonWave = new JButton("Next Wave");

        //Create buttons for towers
        pistolTower = new JButton("Pistol Tower: 70");
        machineTower = new JButton("Machine Gun: 200");

        machineTower.addActionListener(this);
        pistolTower.addActionListener(this);
        summonWave.addActionListener(this);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add buttons to the button panel(Panel on the right
        buttonPanel.add(wavesLbl);
        buttonPanel.add(waveTimer);
        buttonPanel.add(summonWave);
        buttonPanel.add(pistolTower);
        buttonPanel.add(machineTower);

        ReadMap();
        GenerateMap();
        ReadWaves();

        GamePanel gamePanel = new GamePanel();
        gamePanel.addMouseMotionListener(this);
        gamePanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(isBuilding){
                    if(SwingUtilities.isLeftMouseButton(e) && CheckMouseInWorldBounds(mouseInScreen)) {
                        Tile clickedTile = GetTileAt(mouseInScreen);
                        if (CanBuild(clickedTile)) {
                            PlaceTower(clickedTile);
                        }
                    }else if(SwingUtilities.isRightMouseButton(e)){
                        selectedTower = null;
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        frame.add(textPanel,BorderLayout.SOUTH);
        frame.add(gamePanel,BorderLayout.CENTER);
        frame.add(buttonPanel,BorderLayout.EAST);
        frame.setSize(800,500);
        frame.setVisible(true);

        AddWaypoints();
        for (Tile x : waypoints) {
            System.out.println(x.x + "," + x.y);
        }



        //This is the game logic loop
        //Logic order should be Update the enemies position, Update the turrets rotation, firing times,
        //Update the bullets currently in game then repaint.
        while(hasLives){

            UpdateWaveTimer(waveBufferTime - currentWaveTime);
            if(currentWaveTime >= waveBufferTime) {
                if(!IsLastWave()) {
                    Enemy[] nextWave = Waves.get(wave);
                    currentWaveTime = 0;
                    waveBufferTime = 1000 + nextWave.length * 25;
                    spawningWaves.add(new WaveSpawner(nextWave,wave/2));
                    wave++;
                    UpdateWaves();
                }
                else{
                    if(enemiesInGame.size() == 0){
                        hasLives = false;
                        gameWon = true;
                    }
                }
            }
            spawningWaves.forEach(WaveSpawner::Update);

            for (WaveSpawner w: finishedWaves) {
                spawningWaves.remove(w);
            }

            towersBuilt.forEach(Tower::Update);

            enemiesInGame.forEach(Enemy::Move);

            destroyQ.forEach(Enemy::DestroyNow);

            mouseInScreen = convertPoint(frame, mousePoint, gamePanel);


            if(isBuilding && selectedTower!= null && CheckMouseInWorldBounds(mouseInScreen.x,mouseInScreen.y)){
                Tile tileAtMouse = GetTileAt(mouseInScreen.x,mouseInScreen.y);
                selectedTower.xPos = tileAtMouse.GetWorldPos().x;
                selectedTower.yPos = tileAtMouse.GetWorldPos().y;

            }

            gamePanel.repaint();
            currentWaveTime++;
            try{
                Thread.sleep(25);
                destroyQ = new ArrayList<>();
                finishedWaves = new ArrayList<>();
            }catch(Exception ex){

            }
        }

    }
    boolean CheckMouseInWorldBounds(int x, int y){
        return x < mapWidth * tileSize && x >= 0 && y < mapHeight * tileSize && y >= 0;
    }
    boolean CheckMouseInWorldBounds(Point pos){
        return pos.x < mapWidth * tileSize && pos.x >= 0 && pos.y < mapHeight * tileSize && pos.y >= 0;
    }
    void GenerateMap(){
        //Generate the tile grid
        gameMap = new Tile[mapWidth][mapHeight];
        Tile.Type[][] tileArray = mapRows.toArray(new Tile.Type[mapRows.size()][]);
        for(int x = 0; x < mapWidth; x++){
            for(int y = 0; y < mapHeight; y++){
                Tile.Type pathType = tileArray[y][x];
                Tile currentTile = new Tile(pathType, x,y);
                if(y == 0 && currentTile.tileType.equals(Tile.Type.path)){entrance = currentTile;}
                if(y == mapHeight-1 && currentTile.tileType.equals(Tile.Type.path)){exit = currentTile;}
                gameMap[x][y] = currentTile;
            }
        }
        System.out.println("Entrance at " + entrance.x + "," + entrance.y + " Exit at " + exit.x + "," + exit.y);
    }
    boolean CanBuild(Tile tile){
        return tile.tileType.equals(Tile.Type.empty) && tile.tower == null && Money > selectedTower.price;
    }
    void ReadWaves(){
        File waveText = new File("Waves.txt");
        try{
            Scanner Input = new Scanner(waveText);
            System.out.println("Reading Waves.txt");
            int line = 1;
            while(Input.hasNextLine()){
                String data = Input.nextLine();
                String[] types = data.split(",");
                List<Enemy> thisWave = new ArrayList<>();
                for (String s:types) {
                    String[] x = s.split("-");
                    for(int i = 0; i < Integer.parseInt(x[1]); i ++){
                        if(x[0].equals("Footman")){
                            thisWave.add(new FootMan(line));
                        }
                        else if(x[0].equals("Infantry")){
                            thisWave.add(new Infantry(line));
                        }
                    }
                }
                Waves.add(thisWave.toArray(new Enemy[thisWave.size()]));
                line++;
            }
            Input.close();
        }catch(FileNotFoundException f){
            System.out.println("Error reading Waves.txt");
            f.printStackTrace();}
    }
    void AddWaypoints(){
        Tile currentTile = entrance;
        List<Tile> listOfWaypoints = new ArrayList<>();
        listOfWaypoints.add(entrance);
        String direction = "SOUTH";
        while(true){
            if(currentTile == exit){
                listOfWaypoints.add(exit);
                break;
            }
            if(direction.equals("SOUTH")){
                if(!gameMap[currentTile.x][currentTile.y+1].tileType.equals(Tile.Type.path)) {
                    listOfWaypoints.add(currentTile);
                    direction = findNewDirection(currentTile,direction);
                    System.out.println("Adding waypoint at " + currentTile.x + "," + currentTile.y);
                    continue;
                }
                else{currentTile = gameMap[currentTile.x][currentTile.y+1];}
            }
            else if(direction.equals("EAST")){
                if(!gameMap[currentTile.x+1][currentTile.y].tileType.equals(Tile.Type.path)){
                    listOfWaypoints.add(currentTile);
                    direction = findNewDirection(currentTile,direction);
                    System.out.println("Adding waypoint at " + currentTile.x + "," + currentTile.y);
                    continue;
                }
                else{currentTile = gameMap[currentTile.x+1][currentTile.y];}
            }
            else if(direction.equals("NORTH")){
                if(!gameMap[currentTile.x][currentTile.y-1].tileType.equals(Tile.Type.path)){
                    listOfWaypoints.add(currentTile);
                    direction = findNewDirection(currentTile,direction);
                    System.out.println("Adding waypoint at " + currentTile.x + "," + currentTile.y);
                    continue;
                }
                else{currentTile = gameMap[currentTile.x][currentTile.y-1];}
            }
            else{
                if(!gameMap[currentTile.x-1][currentTile.y].tileType.equals(Tile.Type.path)){
                    listOfWaypoints.add(currentTile);
                    System.out.println("Adding waypoint at " + currentTile.x + "," + currentTile.y);
                    direction = findNewDirection(currentTile,direction);
                    continue;
                }
                else{currentTile = gameMap[currentTile.x-1][currentTile.y];}
            }
        }
        waypoints = listOfWaypoints.toArray(new Tile[listOfWaypoints.size()]);
    }
    String findNewDirection(Tile referencePoint,String lastDir){
        String newDir = "";
        //If tile to the right is a path change direction to EAST
        if(gameMap[referencePoint.x + 1][referencePoint.y].tileType.equals(Tile.Type.path) && !lastDir.equals("WEST")){
            newDir = "EAST";
        }
        //If tile to the left is a path change direction to WEST
        else if (gameMap[referencePoint.x - 1][referencePoint.y].tileType.equals(Tile.Type.path) && !lastDir.equals("EAST")){
            newDir = "WEST";
        }
        // If tile to the south is a path change direction to SOUTH
        else if (gameMap[referencePoint.x][referencePoint.y + 1].tileType.equals(Tile.Type.path) && !lastDir.equals("NORTH")){
            newDir = "SOUTH";
        }
        //Tile to the north must be a path
        else{
            newDir = "NORTH";
        }
        return newDir;
    }
    void PlaceTower(Tile tileToPlace){
        System.out.println(selectedTower.towerName + " built");
        towersBuilt.add(selectedTower);
        tileToPlace.SetTower(selectedTower);
        AddMoney(-selectedTower.price);
        selectedTower = null;
    }
    static void UpdateMoney(){
        moneyLbl.setText("Money: " + Money);
    }
    static void UpdateLives(){
        livesLbl.setText("Lives: " + Lives);
        if(Lives <= 0){
            hasLives = false;
            gameLost = true;
        }
    }
    static void UpdateWaves(){
        wavesLbl.setText("Wave " + (wave + 1));
    }
    static void UpdateWaveTimer(int time){
        waveTimer.setText("Next wave in " + time/40);
    }
    boolean IsLastWave(){
        return wave >= Waves.size();
    }
    void ReadMap(){
        File maptxt = new File("map.txt");
        int lineLength = 0;
        boolean firstLine = true;
        try{
            Scanner input = new Scanner(maptxt);
            System.out.println("Reading mapRows.txt");
            while(input.hasNextLine()) {
                int pCount = 0;
                String line = input.nextLine();
                if(firstLine){
                    lineLength = line.length();
                    mapWidth = lineLength;
                }
                Tile.Type[] row = new Tile.Type[line.length()];
                for (int x = 0; x < line.length(); x++) {
                    if(line.length() != lineLength){
                        throw new TextException();
                    }
                    char c = line.charAt(x);
                    if(c == 'W' || c== 'w'){
                        row[x] = Tile.Type.wall;
                    }else if(c == 'p' || c == 'P'){
                        row[x] = Tile.Type.path;
                        if(firstLine){
                            pCount++;
                        }
                    }else{
                        row[x] = Tile.Type.empty;
                    }
                    if(firstLine && pCount > 1){
                        throw new TextException();
                    }
                }
                firstLine = false;
                mapRows.add(row);
            }
        }catch(IOException ex){
            System.out.println("map.txt not found");
        }catch(TextException txt){
            System.out.println("map.txt is not written in correct format");
        }
        mapHeight = mapRows.size();
    }

    static Tile GetTileAt(int x, int y){
        return gameMap[x/tileSize][y/tileSize];
    }
    static Tile GetTileAt(Point position){
        return gameMap[position.x/tileSize][position.y/tileSize];
    }

    //Where to draw all game objects

    private class GamePanel extends JPanel{
        public void paintComponent(Graphics g){
            Graphics2D g2d = (Graphics2D)g;
            //Draw mapRows
            for(int x = 0 ; x < mapWidth; x++){
                for(int y = 0; y < mapHeight; y++) {
                    Tile curTile = gameMap[x][y];

                    if (curTile.tileType.equals(Tile.Type.path)) {
                        g.setColor(Color.BLACK);
                    }else if(curTile.tileType.equals(Tile.Type.wall)){
                        g.setColor(Color.lightGray);
                    }
                    else {
                        g.setColor(Color.GREEN);
                    }
                    //Draw tiles
                    g.fillRect(curTile.x * tileSize, curTile.y * tileSize, tileSize, tileSize);
                    g.setColor(Color.black);
                    g.drawRect(curTile.x * tileSize, curTile.y * tileSize, tileSize, tileSize);
                    //Draw placed towers
                    if(curTile.HasTower()){

                        BufferedImage towerImage;

                        if(curTile.tower.shooting){
                            towerImage = curTile.tower.sprites[1];
                        }else{
                            towerImage = curTile.tower.sprites[0];
                        }

                        AffineTransform trans = new AffineTransform();
                        trans.setTransform(new AffineTransform());
                        trans.translate(curTile.GetWorldPos().x,curTile.GetWorldPos().y);
                        trans.rotate(curTile.tower.rotation+Math.toRadians(85),towerImage.getWidth()/2,towerImage.getHeight()/2);
                        g2d.drawImage(towerImage,trans,this);

                    }
                }
            }
            if(selectedTower != null){
                g.drawImage(selectedTowerImage,selectedTower.xPos,selectedTower.yPos,this);
            }
            //Draw Enemies
            for (Enemy e: enemiesInGame) {
                g.drawImage(e.enemyImage,e.x,e.y,this);
                int w = 20;
                int h = 5;
                g.setColor(Color.white);
                g.drawRect(e.x+6,e.y+20,w,h);
                g.setColor(Color.green);
                g.fillRect(e.x+6,e.y+21,(w) + ((w/e.health) * (e.curHealth - e.health)),h-1);
        }
            //Draw miscellaneous
            if(gameLost){
                g.setColor(Color.white);
                g.fillRect(this.getWidth()/2,this.getHeight()/2,400,50);
                g.setFont(new Font("Arial",1,20));
                g.setColor(Color.BLACK);
                g.drawString("Oh no! You lost",this.getWidth()/2,this.getHeight()/2+30);
            }
            else if(gameWon){
                g.setColor(Color.white);
                g.fillRect(this.getWidth()/2,this.getHeight()/2,400,50);
                g.setFont(new Font("Arial",1,20));
                g.setColor(Color.BLACK);
                g.drawString("Congratulations! You survived",this.getWidth()/2,this.getHeight()/2+30);
            }
        }
    }

    public void actionPerformed(ActionEvent event){
        if(event.getSource().equals(pistolTower)){
            selectedTower = new Pistol(0,0);
            try{
                selectedTowerImage = selectedTower.sprites[0];
                isBuilding = true;
            }catch (Exception e){
                System.out.println("Could not find tower image");
            }
        }
        if(event.getSource().equals(machineTower)){
            selectedTower = new MachineGun(0,0);
            selectedTowerImage = selectedTower.sprites[0];
            isBuilding = true;
        }
        if(event.getSource().equals(summonWave)){
            if(!IsLastWave()) {
                AddMoney((waveBufferTime - currentWaveTime) / 20);
                currentWaveTime = waveBufferTime;
            }
        }
    }
    class TextException extends  Exception{

    }
}
