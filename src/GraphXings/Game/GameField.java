package GraphXings.Game;

import javax.swing.*;

import GraphXings.Data.Coordinate;
import GraphXings.Data.Vertex;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameField extends JPanel {

    private int width;
    private int height;
    private List<Point> circleLocations;
    private List<Point[]> lineLocations;

    // Constructor
    public GameField(int width, int height) {
        this.width = width*100;
        this.height = height*100;
        this.circleLocations = new ArrayList<>();
        this.lineLocations = new ArrayList<>();

        JFrame frame = new JFrame("Game Field");
        setPreferredSize(new Dimension(this.width, this.height));
        frame.add(this);

        JLabel label = new JLabel("Crossings: " + -1);
        frame.add(label, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();

        // this.addMouseListener(new java.awt.event.MouseAdapter() {
        //     public void mouseClicked(java.awt.event.MouseEvent evt) {
        //         int x = evt.getX();
        //         int y = evt.getY();
        //         //System.out.println("Mouse clicked at: " + x + ", " + y);
        //         //gameField.drawMove(new GameMove(null, new Coordinate(x, y)));
        //     }
        // });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Point p : circleLocations) {
            int index = circleLocations.indexOf(p);
            //System.out.println("Index: " + index);
            if (index % 2 == 0) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.BLUE);
            }
            g.fillOval(p.x - 15, p.y - 15, 30, 30); // Draws a circle with a diameter of 30 at the specified point.
        }
        for (Point[] p : lineLocations) {
            g.setColor(Color.BLACK);
            g.drawLine(p[0].x, p[0].y, p[1].x, p[1].y); // Draws a line between the two specified points.
        }
    }

    // public void drawMove(GameMove move) {
    //     int x = move.getCoordinate().getX()*100;
    //     int y = move.getCoordinate().getY()*100;


    //     circleLocations.add(new Point(x, y));
    //     if (circleLocations.size() > 1) {
    //         lineLocations.add(new Point[]{circleLocations.get(circleLocations.size() - 1), circleLocations.get(circleLocations.size() - 2)}); // Adds a line between the last two circles.
    //     }
    //     repaint();
    // }

    public void setCrossingNumber(int crossings) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        JLabel label = (JLabel) frame.getContentPane().getComponent(1);
        label.setText("Crossings: " + crossings);
        repaint();
    }

    public void reset() {
        circleLocations.clear();
        lineLocations.clear();
        repaint();
    }

    public void drawField(HashMap<Vertex, Coordinate> vertexCoordinates) {
        for (Vertex v : vertexCoordinates.keySet()) {
            int x = vertexCoordinates.get(v).getX()*100;
            int y = vertexCoordinates.get(v).getY()*100;
            Boolean alreadyExists = false;
            for (Point p : circleLocations) {
                if (p.x == x && p.y == y) {
                    alreadyExists = true;
                }
            }
            if (!alreadyExists){
            circleLocations.add(new Point(x, y));
            }
        }
        // if (circleLocations.size() > 1) {
        //     lineLocations.add(new Point[]{circleLocations.get(circleLocations.size() - 1), circleLocations.get(circleLocations.size() - 2)}); // Adds a line between the last two circles.
        // }

        for (Vertex v : vertexCoordinates.keySet()) {
            for (Vertex w : vertexCoordinates.keySet()) {
                if (v.equals(w)) {
                    continue;
                }
                if ((Integer.parseInt(v.getId()) - 1) % 10 == Integer.parseInt(w.getId()) || (Integer.parseInt(v.getId()) + 1) % 10 == Integer.parseInt(w.getId())) {
                    
                int x1 = vertexCoordinates.get(v).getX()*100;
                int y1 = vertexCoordinates.get(v).getY()*100;
                int x2 = vertexCoordinates.get(w).getX()*100;
                int y2 = vertexCoordinates.get(w).getY()*100;
                lineLocations.add(new Point[]{new Point(x1, y1), new Point(x2, y2)});
                }
            }
        }
        repaint();
    }

}