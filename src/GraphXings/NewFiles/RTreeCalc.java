package GraphXings.NewFiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.github.davidmoten.rtree2.Entry;
import com.github.davidmoten.rtree2.RTree;
import com.github.davidmoten.rtree2.geometry.Geometries;
import com.github.davidmoten.rtree2.geometry.Geometry;

import GraphXings.Data.Coordinate;
import GraphXings.Data.Edge;
import GraphXings.Data.Segment;
import GraphXings.Data.Vertex;

public class RTreeCalc<T>  {
    
    public RTreeCalc () {
    }

    public int getCrossings(HashMap<Vertex, Coordinate> vertexCoordinatePair, Iterable<Edge> edges, Vertex v1, Coordinate coords) {
        RTree<Edge,Geometry> rTree = RTree.create();
        System.out.println(coords.getX() + "/" + coords.getY());

        int crossingNumber = 0;
        int startX = 0;
        int startY = 0;
        int endX = 0;
        int endY = 0;

        HashMap<Vertex, List<Edge>> vertexToEdge = new HashMap<>();
        for (Edge edge : edges) {
            Vertex starting = edge.getS();
            Vertex ending = edge.getT();
            vertexToEdge.computeIfAbsent(starting, k -> new ArrayList<Edge>()).add(edge);
            vertexToEdge.computeIfAbsent(ending, k -> new ArrayList<Edge>()).add(edge);            
        }

        for (Map.Entry<Vertex, List<Edge>> entry : vertexToEdge.entrySet()) {
                List<Edge> edges2 = entry.getValue();
                for (Edge edge : edges2) {
                    if(v1.getId() == edge.getS().getId()) {
                        startX = vertexCoordinatePair.get(edge.getS()).getX();
                        startY = vertexCoordinatePair.get(edge.getS()).getY();
                        endX = coords.getX();
                        endX = coords.getY();

                       if(startX > endX) {
                             rTree = rTree.add(edge, Geometries.line(vertexCoordinatePair.get(edge.getS()).getX(),vertexCoordinatePair.get(edge.getS()).getY(),endX,endY));
                        } else {
                            rTree = rTree.add(edge, Geometries.line(endX,endY,vertexCoordinatePair.get(edge.getS()).getX(), vertexCoordinatePair.get(edge.getS()).getY()));
                        }
                    } else if( v1.getId() == edge.getT().getId()) {
                        startX = vertexCoordinatePair.get(edge.getT()).getX();
                        startY = vertexCoordinatePair.get(edge.getT()).getY();
                        endX = coords.getX();
                        endX = coords.getY();

                        if(startX > endX) {
                             rTree = rTree.add(edge, Geometries.line(vertexCoordinatePair.get(edge.getT()).getX(),vertexCoordinatePair.get(edge.getT()).getY(),endX,endY));
                        } else {
                            rTree = rTree.add(edge, Geometries.line(endX,endY,vertexCoordinatePair.get(edge.getT()).getX(), vertexCoordinatePair.get(edge.getT()).getY()));
                        }
                        
                    }
                }
        }

          System.out.println(startX + " " + startY + " " + endX + " " + endY);
        Iterable<Entry<Edge, Geometry>> results = rTree.search(Geometries.rectangle(startX,startY,endX,endY));
        
      

        for (Entry<Edge, Geometry> entry : results) {
            System.out.println("Edge: " + entry.value().getS().getId() + "/" + entry.value().getT().getId());
        }

        HashSet<Edge> allEdges = new HashSet<>();
        
        for (Entry<Edge, Geometry> entry : results) {
            System.out.println(entry.value().getS().getId());
            if (!allEdges.contains(entry.value())) {
                    allEdges.add(entry.value());      
                }
        }


        for (Edge e1 : allEdges) {
            for (Edge e2 : allEdges) {
                if (!e1.equals(e2)) {
                    if (!e1.isAdjacent(e2)) {
                        if (!vertexCoordinatePair.containsKey(e1.getS()) || !vertexCoordinatePair.containsKey(e1.getT())
                                || !vertexCoordinatePair.containsKey(e2.getS())
                                || !vertexCoordinatePair.containsKey(e2.getT())) {
                            continue;
                        }
                        Segment s1 = new Segment(vertexCoordinatePair.get(e1.getS()), vertexCoordinatePair.get(e1.getT()));
                        Segment s2 = new Segment(vertexCoordinatePair.get(e2.getS()), vertexCoordinatePair.get(e2.getT()));
                        if (Segment.intersect(s1, s2)) {
                            crossingNumber++;
                        }
                    }
                }
            }
        }
        return crossingNumber / 2;
    }

}

