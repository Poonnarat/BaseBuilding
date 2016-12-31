package Display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

import static frickingnoobs.noobs.Launcher.game;

/**
 * Created by .poon on 12/18/2016 AD.
 */
public class Display {
    //The window
    private JFrame frame;
    private String title;
    private int width, height;

    // Graphic
    private Canvas canvas;


    // Constructor
    public Display(String title, int width, int height){
        this.frame = frame;
        this.title = title;
        this.width = width;
        this.height = height;

        createDisplay();


    }
    // Methods
    private void createDisplay(){
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// close in the back ground
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);// open on the center of the screen
        frame.setVisible(true);
        JPanel gamePanel = game.gp;
        frame.add(gamePanel);
        //Add key bindings
        //First add input maps
        gamePanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_W,0,false),"PressCamUp");
        gamePanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S,0,false),"PressCamDown");
        gamePanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A,0,false),"PressCamLeft");
        gamePanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_D,0,false),"PressCamRight");
        gamePanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_W,0,true),"ReleaseCamUp");
        gamePanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S,0,true),"ReleaseCamDown");
        gamePanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A,0,true),"ReleaseCamLeft");
        gamePanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_D,0,true),"ReleaseCamRight");

        //Add action maps
        gamePanel.getActionMap().put("PressCamUp",game.camUpPress);
        gamePanel.getActionMap().put("PressCamDown",game.camDownPress);
        gamePanel.getActionMap().put("PressCamLeft",game.camLeftPress);
        gamePanel.getActionMap().put("PressCamRight",game.camRightPress);
        gamePanel.getActionMap().put("ReleaseCamUp",game.camUpRelease);
        gamePanel.getActionMap().put("ReleaseCamDown",game.camDownRelease);
        gamePanel.getActionMap().put("ReleaseCamLeft",game.camLeftRelease);
        gamePanel.getActionMap().put("ReleaseCamRight",game.camRightRelease);
        /*
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));


        frame.add(canvas);
        frame.pack();
        */
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
