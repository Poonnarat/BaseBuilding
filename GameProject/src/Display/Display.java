package Display;

import javax.swing.*;
import java.awt.*;

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
        //Add key bindings
        //First add input maps


        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));


        frame.add(canvas);
        frame.pack();
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
