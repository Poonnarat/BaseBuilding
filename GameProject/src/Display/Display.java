package Display;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static frickingnoobs.noobs.Game.cameraPixelsHeight;
import static frickingnoobs.noobs.Game.cameraPixelsWidth;
import static frickingnoobs.noobs.Launcher.game;

/**
 * Created by .poon on 12/18/2016 AD.
 */
public class Display implements ActionListener{
    //The window
    private JFrame frame;
    private String title;
    private int width, height;

    //Button things
    JPanel buttonPanel;
    JButton build;
    //Image location for buttons
    String buildButtonLocation = "res/Icons/button.png";





    // Constructor
    public Display(String title, int width, int height){
        this.frame = frame;
        this.title = title;
        this.width = width;
        this.height = height;
        createDisplay();


    }
    // Methods
    private void createDisplay() {
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// close in the back ground
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);// open on the center of the screen
        frame.setVisible(true);

        //Add Buttons
        buttonPanel = new JPanel();

        build = new JButton("Turn on build menu",RetrieveImage(buildButtonLocation));
        buttonPanel.add(build);
        build.setPreferredSize(new Dimension(100,40));
        build.addActionListener(this);
        frame.add(buttonPanel,BorderLayout.SOUTH);
        buttonPanel.setLocation(10,height);
        buttonPanel.setVisible(true);
        buttonPanel.setFocusable(false);

        //This has to be the last thing to be added since i dont know how to set different panel focuses
        JPanel gamePanel = game.gp;
        frame.add(gamePanel,BorderLayout.CENTER);
        gamePanel.setFocusable(true);
        gamePanel.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                width = (int) frame.getSize().getWidth();
                height = (int) frame.getSize().getHeight();
                cameraPixelsWidth = width;
                cameraPixelsHeight = height;

            }

            @Override
            public void componentMoved(ComponentEvent e) {
                System.out.println("moved");
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
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "PressCamUp");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "PressCamDown");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "PressCamLeft");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "PressCamRight");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "ReleaseCamUp");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "ReleaseCamDown");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "ReleaseCamLeft");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "ReleaseCamRight");

        //Add action maps
        ActionMap actionMap = gamePanel.getActionMap();
        actionMap.put("PressCamUp", game.camUpPress);
        actionMap.put("PressCamDown", game.camDownPress);
        actionMap.put("PressCamLeft", game.camLeftPress);
        actionMap.put("PressCamRight", game.camRightPress);
        actionMap.put("ReleaseCamUp", game.camUpRelease);
        actionMap.put("ReleaseCamDown", game.camDownRelease);
        actionMap.put("ReleaseCamLeft", game.camLeftRelease);
        actionMap.put("ReleaseCamRight", game.camRightRelease);


    }
    public void Revalidate(){
        frame.revalidate();
    }
    ImageIcon RetrieveImage(String location){
        try{
            BufferedImage image = ImageIO.read(new File(location));
            ImageIcon icon = new ImageIcon(image);
            return icon;
        }
        catch(IOException e){
            System.out.println("Could not find " + location);
            e.printStackTrace();
            return null;
        }
    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource().equals(build)){
            //Do what the build button does
        }
    }
}
