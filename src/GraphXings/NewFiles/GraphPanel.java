package GraphXings.NewFiles;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class GraphPanel extends JPanel{

    int img_width = 700;
    int img_height = 700;

    int window_width = 500;
    int window_heigth = 500;

    GraphPanel() {
        this.setPreferredSize(new Dimension(500,500));
        this.setBorder(new EmptyBorder(150,150,150,150));
        new BorderLayout(50,50); 
    }   

    public void paint(Graphics g) {
        BufferedImage bi = new BufferedImage(500,500, BufferedImage.TYPE_INT_ARGB); 
        Graphics2D g2D = bi.createGraphics();
        Graphics2D g2D_ui = (Graphics2D) g;

        g2D_ui.setStroke(new BasicStroke(3));
        g2D_ui.setPaint(Color.BLUE);
        g2D_ui.drawLine((img_width-window_width)/2, (img_height-window_heigth)/2, (img_width-window_width)/2 + 500, (img_height-window_heigth)/2+500);
        g2D_ui.drawOval(0, 0, 5, 5);
        g2D_ui.drawOval(500, 500, 5, 5);
        
        g2D.setStroke(new BasicStroke(3));
        g2D.setPaint(Color.BLUE);
        g2D.drawLine(0, 0, 500, 500);
        g2D.drawOval(0, 0, 5, 5);
        g2D.drawOval(500, 500, 5, 5);


        
        
        g2D.dispose();
        try{ImageIO.write(bi,"png",new File("test.png"));}catch (Exception e) {}

    }


}
