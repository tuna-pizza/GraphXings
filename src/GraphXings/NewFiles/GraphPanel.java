package GraphXings.NewFiles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import GraphXings.Data.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class GraphPanel extends JPanel {

    private List<Coordinate> coordinates;
    private List<GuiCoordinate> guiCoordinates;
    private double zoomFactor = 1.0;
    private Point lastMousePosition;
    private int panelHeight = 700;
    private int panelWidth = 700;
    private  boolean isReady = false;


    public GraphPanel() {
        this.coordinates = new ArrayList<>();
        this.guiCoordinates = new ArrayList<>();

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if(isReady) {
                        if (lastMousePosition != null) {
                            double deltaX = e.getX() - lastMousePosition.getX();
                            double deltaY = e.getY() - lastMousePosition.getY();
                            translateView(deltaX, deltaY);
                            repaint();
                        }
                        lastMousePosition = e.getPoint();
                    }
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                     if(isReady) {
                         lastMousePosition = e.getPoint();
                     }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if(isReady) {
                    lastMousePosition = null;
                    }
                }
            });

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(isReady) {
                // Get the center of the panel
                Point panelCenter = new Point(panelWidth / 2, panelHeight / 2);

                // Calculate the translation to keep the center fixed
                int deltaX = (int) ((panelCenter.getX() - getWidth() / 2));
                int deltaY = (int) ((panelCenter.getY() - getHeight() / 2));

                // Translate the view based on the calculated deltas
                translateView(deltaX, deltaY);

                // Adjust zoom factor based on mouse wheel movement
                int notches = e.getWheelRotation();
                double oldZoomFactor = zoomFactor;
                zoomFactor -= 0.1 * notches;

                // Calculate the translation to keep the center fixed after zooming
                int newDeltaX = (int) ((panelCenter.getX() - getWidth() / 2) * (1 - zoomFactor / oldZoomFactor));
                int newDeltaY = (int) ((panelCenter.getY() - getHeight() / 2) * (1 - zoomFactor / oldZoomFactor));

                // Translate the view based on the new calculated deltas
                translateView(newDeltaX, newDeltaY);
                repaint();
                }
                
            }
        });
            
        
    }

    private void translateView(double deltaX, double deltaY) {
        try {
            for (GuiCoordinate guiCoordinate : guiCoordinates) {
            guiCoordinate.translate(deltaX, deltaY);
        }
        updateOriginalCoordinates();
        } catch (Exception e) {
            // TODO: handle exception
        }
       
    }

    private void updateOriginalCoordinates() {
        coordinates.clear();
        for (GuiCoordinate guiCoordinate : guiCoordinates) {
            int x = guiCoordinate.getX();
            int y = guiCoordinate.getY();

            // Apply zoom factor and translation
            int scaledX = (int) (x / zoomFactor);
            int scaledY = (int) (y / zoomFactor);

            coordinates.add(new Coordinate(scaledX, scaledY));
        }
    }

    public void changeReadyState(Boolean readyState) {
        this.isReady = readyState;
    }

    public void clearPanel() {
        coordinates.clear();
        guiCoordinates.clear();
        repaint();
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
        updateGuiCoordinates(); // Update guiCoordinates when coordinates change
        repaint();
    }

    private void updateGuiCoordinates() {
        try {
             // Update guiCoordinates based on the current zoom and translation
            guiCoordinates.clear();
            for (Coordinate coordinate : coordinates) {
            int x = coordinate.getX();
            int y = coordinate.getY();

            // Apply zoom factor and translation
            int scaledX = (int) (x * zoomFactor);
            int scaledY = (int) (y * zoomFactor);

            guiCoordinates.add(new GuiCoordinate(scaledX, scaledY));
        }
        } catch (Exception e) {
            // TODO: handle exception
        }
       
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Apply zoom factor to the graphics context
        g2d.scale(zoomFactor, zoomFactor);

        // Draw your points on the panel
        for (int i = 0; i < guiCoordinates.size(); i++) {
          try {
            try {
            GuiCoordinate guiCoordinate = guiCoordinates.get(i);
            int x = guiCoordinate.getX();
            int y = guiCoordinate.getY();
                             // Adjust the size of the points
            int scaledPointSize = (int) (10 / zoomFactor);

            // Alternate colors based on the index
            if (i % 2 == 0) {
                g2d.setColor(Color.BLUE);
            } else {
                g2d.setColor(Color.RED);
            }

            g2d.fillOval(x - scaledPointSize / 2, y - scaledPointSize / 2, scaledPointSize, scaledPointSize);

            } catch (Exception e) {
                // TODO: handle exception
            }
          } catch (IndexOutOfBoundsException e) {
            // TODO: handle exception
          } 
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(panelWidth, panelHeight);
    }

    private static class GuiCoordinate {
        private int x;
        private int y;

        public GuiCoordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void translate(double deltaX, double deltaY) {
            x += deltaX;
            y += deltaY;
        }
    }
}