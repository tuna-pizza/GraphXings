package GraphXings.NewFiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import GraphXings.Data.Coordinate;
import GraphXings.Data.Edge;
import GraphXings.Data.Segment;
import GraphXings.Data.Vertex;

public class BetterEdgeCrossing {
    List<Vertex> vertecies = new ArrayList<Vertex>();
    TreeSet<Edge> binarySearchTree;

    public BetterEdgeCrossing() {
    }

    public void insertCoordinate(Vertex vertex, HashMap<Vertex, Coordinate> vertexCoordinatePair) {
        Comparator<Vertex> comperator = Comparator.comparingInt(vertex_ -> vertexCoordinatePair.get(vertex_).getX());
        int index = Collections.binarySearch(vertecies, vertex, comperator);
        if (index < 0) {
            index *= -1;
            index -= 1;
        }
        vertecies.add(index, vertex);
    }

    public void removeCoordinate(Vertex vertex) {
        vertecies.remove(vertex);
    }

    public int calculateIntersections(Iterable<Edge> edges, HashMap<Vertex, Coordinate> vertexCoordinatePair) {
        int crossings = 0;
        HashMap<Vertex, List<Edge>> vertexToEdge = new HashMap<>();
        for (Edge edge : edges) {
            Vertex starting = edge.getS();
            Vertex ending = edge.getT();
            vertexToEdge.computeIfAbsent(starting, k -> new ArrayList<Edge>()).add(edge);
            vertexToEdge.computeIfAbsent(ending, k -> new ArrayList<Edge>()).add(edge);
        }

        Comparator<Edge> comparator = (Edge e1, Edge e2) -> {
            Coordinate starting1 = vertexCoordinatePair.get(e1.getS());
            Coordinate ending1 = vertexCoordinatePair.get(e1.getT());
            int max1 = Math.max(starting1.getY(), ending1.getY());
            Coordinate starting2 = vertexCoordinatePair.get(e2.getS());
            Coordinate ending2 = vertexCoordinatePair.get(e2.getT());
            int max2 = Math.max(starting2.getY(), ending2.getY());
            return Integer.compare(max1, max2);
        };
        binarySearchTree = new TreeSet<Edge>(comparator);
        for (Vertex vertex : vertecies) {
            List<Edge> edgesOfVertex = vertexToEdge.get(vertex);
            for (Edge edgeOfVertex : edgesOfVertex) {
                Vertex starting = edgeOfVertex.getS();
                Vertex ending = edgeOfVertex.getT();
                Vertex vertexToCompareWith = vertex == starting ? ending : starting;
                if (vertexCoordinatePair.get(vertexToCompareWith) == null)
                    continue;
                if (vertexCoordinatePair.get(vertex).getX() < vertexCoordinatePair.get(vertexToCompareWith).getX()) {
                    binarySearchTree.add(edgeOfVertex);
                    Segment thisSegment = new Segment(vertexCoordinatePair.get(edgeOfVertex.getS()),
                            vertexCoordinatePair.get(edgeOfVertex.getT()));
                    Edge predEdge = binarySearchTree.higher(edgeOfVertex);
                    if (predEdge != null) {
                        Segment predSegment = new Segment(vertexCoordinatePair.get(predEdge.getS()),
                                vertexCoordinatePair.get(predEdge.getT()));
                        if (Segment.intersect(thisSegment, predSegment)) {
                            crossings++;
                            continue;
                        }
                    }
                    Edge succEdge = binarySearchTree.lower(edgeOfVertex);
                    if (succEdge != null) {
                        Segment succSegment = new Segment(vertexCoordinatePair.get(succEdge.getS()),
                                vertexCoordinatePair.get(succEdge.getT()));
                        if (Segment.intersect(thisSegment, succSegment)) {
                            crossings++;
                            continue;
                        }
                    }
                } else {
                    Segment thisSegment = new Segment(vertexCoordinatePair.get(edgeOfVertex.getS()),
                            vertexCoordinatePair.get(edgeOfVertex.getT()));
                    Edge predEdge = binarySearchTree.higher(edgeOfVertex);
                    if (predEdge != null) {
                        Segment predSegment = new Segment(vertexCoordinatePair.get(predEdge.getS()),
                                vertexCoordinatePair.get(predEdge.getT()));
                        if (Segment.intersect(thisSegment, predSegment)) {
                            crossings++;
                            continue;
                        }
                    }
                    Edge succEdge = binarySearchTree.lower(edgeOfVertex);
                    if (succEdge != null) {
                        Segment succSegment = new Segment(vertexCoordinatePair.get(succEdge.getS()),
                                vertexCoordinatePair.get(succEdge.getT()));
                        if (Segment.intersect(thisSegment, succSegment)) {
                            crossings++;
                            continue;
                        }
                    }

                    Vertex startingDelete = edgeOfVertex.getS();
                    Vertex endingDelete = edgeOfVertex.getT();
                    if (startingDelete != vertex) {
                        if (vertexCoordinatePair.get(startingDelete).getX() < vertexCoordinatePair.get(vertex)
                                .getX()) {
                            binarySearchTree.remove(edgeOfVertex);
                            break;
                        }
                    }
                    if (endingDelete != vertex) {
                        if (vertexCoordinatePair.get(endingDelete).getX() < vertexCoordinatePair.get(vertex)
                                .getX()) {
                            binarySearchTree.remove(edgeOfVertex);
                            break;
                        }
                    }
                }
            }
        }
        return crossings;
    }
}
