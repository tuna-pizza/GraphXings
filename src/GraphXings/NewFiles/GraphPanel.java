package GraphXings.NewFiles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import GraphXings.Data.Coordinate;
import GraphXings.Data.Edge;
import GraphXings.Data.Graph;
import GraphXings.Data.Vertex;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GraphPanel extends JPanel {

    private List<Coordinate> coordinates;
    private List<GuiCoordinate> guiCoordinates;
    private double zoomFactor;
    private Point lastMousePosition;
    private int panelHeight = 700;
    private int panelWidth = 700;
    /**
     * 
     * TODO: isReady fixen
     */ 
    private boolean isReady = false;
    private List<Edge> edges; 
    private HashMap<Vertex, Coordinate> vertexCoordinateMap;
    private Graph graph;
    private HashSet<Vertex> placedVertecies;
    private boolean showEdges = true;


    public GraphPanel() {
        this.coordinates = new ArrayList<>();
        this.guiCoordinates = new ArrayList<>();
        this.edges = new ArrayList<Edge>();
        this.vertexCoordinateMap = new HashMap<>();
        this.zoomFactor = 1.0;

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if(isReady == true) {
                        if (lastMousePosition != null) {
                            double deltaX = e.getX() - lastMousePosition.getX();
                            double deltaY = e.getY() - lastMousePosition.getY();
                            try {
                                 translateView(deltaX, deltaY);
                            } catch (ConcurrentModificationException e2) {
                            }
                           
                            repaint();
                        }
                        lastMousePosition = e.getPoint();
                        updateOriginalCoordinates();
                    }
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                     if(isReady == true) {
                         lastMousePosition = e.getPoint();
                     }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if(isReady == true) {
                    lastMousePosition = null;
                    }
                }
            });

            
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(isReady == true) {
                // Get the center of the panel
                Point panelCenter = new Point(panelWidth / 2, panelHeight / 2);

                // Calculate the translation to keep the center fixed
                int deltaX = (int) ((panelCenter.getX() - panelWidth / 2));
                int deltaY = (int) ((panelCenter.getY() - panelHeight / 2));

                // Translate the view based on the calculated deltas
                translateView(deltaX, deltaY);

                // Adjust zoom factor based on mouse wheel movement
                int notches = e.getWheelRotation();
                double oldZoomFactor = zoomFactor;
              // Adjust zoom factor based on mouse wheel movement

                zoomFactor -= 0.06 * notches;
                repaint();

                // Calculate the translation to keep the center fixed after zooming
                int newDeltaX = (int) ((panelCenter.getX() - getWidth() / 2) * (1 - zoomFactor / oldZoomFactor));
                int newDeltaY = (int) ((panelCenter.getY() - getHeight() / 2) * (1 - zoomFactor / oldZoomFactor));

                // Translate the view based on the new calculated deltas
                translateView(newDeltaX, newDeltaY);
                setEdges(graph, placedVertecies, vertexCoordinateMap);
                updateOriginalCoordinates(); // Add this line
                }
                
            }
        });
            
        
    }

    public void showEdges(boolean showEdges) {
        this.showEdges = showEdges;
    }

    private void translateView(double deltaX, double deltaY) {
        try {
            for (GuiCoordinate guiCoordinate : guiCoordinates) {
                guiCoordinate.translate(deltaX, deltaY);
            }
            
            if(showEdges == true) {
                // Update the coordinates of the vertices based on the translation
                for (Vertex vertex : placedVertecies) {

                    vertexCoordinateMap.get(vertex).translate((int)deltaX, (int)deltaY);
                    
                }
            }
          
            
          
    
            updateOriginalCoordinates();
        } catch (ConcurrentModificationException | NullPointerException e ) {
            
        }
    }

    private void updateOriginalCoordinates() {
        coordinates.clear();
        try {
            for (GuiCoordinate guiCoordinate : guiCoordinates) {
            int x = guiCoordinate.getX();
            int y = guiCoordinate.getY();

            // Apply zoom factor and translation
            int scaledX = (int) (x / zoomFactor);
            int scaledY = (int) (y / zoomFactor);

            coordinates.add(new Coordinate(scaledX, scaledY));
        }
        } catch (ConcurrentModificationException e) {
            // TODO: handle exception
        }
        
    }

    public void changeReadyState(boolean readyState) {
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

    public void setEdges(List<Edge> edges, Graph g) {
        this.edges = edges;
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

    public void setEdges(Graph graph, HashSet<Vertex> placedVertices, HashMap<Vertex, Coordinate> vertexCoordinateMap) {
        this.edges.clear();
        this.vertexCoordinateMap = vertexCoordinateMap;
        this.graph = graph;
        this.placedVertecies = placedVertices;
    
        // Clear the edges list before updating it
        
        try {
            for (Vertex v : placedVertices) {
                if (graph.getIncidentEdges(v) != null) {
                    for (Edge edge : graph.getIncidentEdges(v)) {
                        edges.add(edge);
                    }
                }
            }
        } catch (NullPointerException e) {

        }
    
        repaint();  // Make sure to repaint after updating the edges
    }

    private int getHorizontalScrollPosition() {
        JViewport viewport = getScrollPaneViewport();
        return (viewport != null) ? viewport.getViewPosition().x : 0;
    }
    
    private int getVerticalScrollPosition() {
        JViewport viewport = getScrollPaneViewport();
        return (viewport != null) ? viewport.getViewPosition().y : 0;
    }
    
    private JViewport getScrollPaneViewport() {
        Container parent = getParent();
        while (parent != null && !(parent instanceof JScrollPane)) {
            parent = parent.getParent();
        }
        return (parent instanceof JScrollPane) ? ((JScrollPane) parent).getViewport() : null;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    
        Graphics2D g2d = (Graphics2D) g;
    
        // Apply zoom factor to the graphics context
        g2d.scale(zoomFactor, zoomFactor);

        int scaledPointSize = (int) (10 / zoomFactor);
        if(showEdges == true) {
            System.out.println("Shows Edges");
             if (edges != null) {
                g2d.setColor(Color.BLACK);
                for (Edge edge : edges) {
                    int xStart = vertexCoordinateMap.get(edge.getS()).getX();
                    int yStart = vertexCoordinateMap.get(edge.getS()).getY();
                    int xEnd = vertexCoordinateMap.get(edge.getT()).getX();
                    int yEnd = vertexCoordinateMap.get(edge.getT()).getY();

                    // Adjust the coordinates based on the inverse of the zoom factor
                    xStart = (int) (xStart / zoomFactor);
                    yStart = (int) (yStart / zoomFactor);
                    xEnd = (int) (xEnd / zoomFactor);
                    yEnd = (int) (yEnd / zoomFactor);

                    // Adjust the coordinates based on the translation
                    xStart -= getHorizontalScrollPosition();
                    yStart -= getVerticalScrollPosition();
                    xEnd -= getHorizontalScrollPosition();
                    yEnd -= getVerticalScrollPosition();

                    g2d.setStroke(new BasicStroke((int)(1/zoomFactor)));
                
                    g2d.drawLine((int)(xStart * zoomFactor) , (int)(yStart * zoomFactor),(int) (xEnd* zoomFactor),(int) (yEnd* zoomFactor));
            }

        }
        }
       
        // Draw your points on the panel
        for (int i = 0; i < guiCoordinates.size(); i++) {
            try {
                GuiCoordinate guiCoordinate = guiCoordinates.get(i);
                int x = guiCoordinate.getX();
                int y = guiCoordinate.getY();

                // Alternate colors based on the index
                if (i % 2 == 0) {
                    g2d.setColor(Color.BLUE);
                } else {
                    g2d.setColor(Color.RED);
                }
    
                g2d.fillOval(x - scaledPointSize / 2, y - scaledPointSize / 2, scaledPointSize, scaledPointSize);
    
            } catch (IndexOutOfBoundsException | NullPointerException e) {
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

    public void resetZoom() {
        zoomFactor = 1.0;
        lastMousePosition = null;
        repaint();
    }
    


}

