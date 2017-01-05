package frickingnoobs.noobs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Username on 31/12/2016.
 */
public class CustButton extends JButton {

    //xPos and yPos being position in the main component
    //xPercent and yPercent being percentage of component size to take
    public CustButton(JComponent parent, int xPos, int yPos, int xPercent, int yPercent){
        super();
        enableInputMethods(true);
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

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
    }

    @Override
    public void paintComponent(Graphics g){

    }
}
