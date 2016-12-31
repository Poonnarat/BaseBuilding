package Display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;

import static frickingnoobs.noobs.Game.cameMoveSpeed;
import static frickingnoobs.noobs.Game.cameraPixelsHeight;
import static frickingnoobs.noobs.Game.cameraPixelsWidth;
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
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);// open on the center of the screen
        frame.setVisible(true);
        JPanel gamePanel = game.gp;
        frame.add(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                cameraPixelsWidth = (int)gamePanel.getSize().getWidth();
                cameraPixelsHeight = (int)gamePanel.getSize().getHeight();

            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
        //Add key bindings
        //First add input maps
        InputMap inputMap = gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W,0,false),"PressCamUp");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S,0,false),"PressCamDown");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A,0,false),"PressCamLeft");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D,0,false),"PressCamRight");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W,0,true),"ReleaseCamUp");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S,0,true),"ReleaseCamDown");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A,0,true),"ReleaseCamLeft");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D,0,true),"ReleaseCamRight");

        //Add action maps
        ActionMap actionMap = gamePanel.getActionMap();
        actionMap.put("PressCamUp",game.camUpPress);
        actionMap.put("PressCamDown",game.camDownPress);
        actionMap.put("PressCamLeft",game.camLeftPress);
        actionMap.put("PressCamRight",game.camRightPress);
        actionMap.put("ReleaseCamUp",game.camUpRelease);
        actionMap.put("ReleaseCamDown",game.camDownRelease);
        actionMap.put("ReleaseCamLeft",game.camLeftRelease);
        actionMap.put("ReleaseCamRight",game.camRightRelease);
    }

    public Canvas getCanvas() {
        return canvas;
    }
    public void Revalidate(){
        frame.revalidate();
    }
}
