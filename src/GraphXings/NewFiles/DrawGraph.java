package GraphXings.NewFiles;


import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DrawGraph extends JFrame{

    GraphPanel panel;

    public DrawGraph() {
        panel = new GraphPanel();

        
       
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    
     
}
