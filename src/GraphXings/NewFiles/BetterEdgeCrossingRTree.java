package GraphXings.NewFiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import com.github.davidmoten.rtree2.Entry;
import com.github.davidmoten.rtree2.RTree;
import com.github.davidmoten.rtree2.geometry.Geometries;
import com.github.davidmoten.rtree2.geometry.Geometry;

import GraphXings.Data.Coordinate;
import GraphXings.Data.Edge;
import GraphXings.Data.Graph;
import GraphXings.Data.Segment;
import GraphXings.Data.Vertex;

public class BetterEdgeCrossingRTree {
    Graph g;
    List<Vertex> vertices = new ArrayList<Vertex>();
    RTree<Edge,Geometry> rTree = RTree.create();

    public BetterEdgeCrossingRTree(Graph g) {
        this.g = g;
    }

    /*
     * vertex
     * gs: the GameState does need to have inserted the vertex already!!!
     */
    public void insertVertexByCoordinate(Vertex vertex, HashMap<Vertex, Coordinate> mapVertexToCoordinate) {
        Comparator<Vertex> comperator = Comparator.comparingInt(vertex_ -> mapVertexToCoordinate.get(vertex_).getX());
        int index = Collections.binarySearch(vertices, vertex, comperator);
        if (index < 0) {
            index *= -1;
            index -= 1;
        }
        vertices.add(index, vertex);
    }

    public void removeCoordinate(Vertex vertex) {
        vertices.remove(vertex);
    }

    /*
     * mapVertexToCoordinates does NOT have inserted the vertex yet! That's why it
     * is a test and not a calculation
     */
    public int testCoordinate(Vertex vertex, Coordinate coordinateToAdd,
            HashMap<Vertex, Coordinate> mapVertexToCoordinate) {
        mapVertexToCoordinate.put(vertex, coordinateToAdd);
        insertVertexByCoordinate(vertex, mapVertexToCoordinate);
        int crossingsAddedByVertex = calculateIntersections(mapVertexToCoordinate);
        mapVertexToCoordinate.remove(vertex);
        removeCoordinate(vertex);
        return crossingsAddedByVertex;
    }

    public int calculateIntersections(HashMap<Vertex, Coordinate> mapVertexToCoordinate) {       

        for (Vertex vertex : vertices) {
            // iterate over the edges adjacent to this vertex
            for (Edge edge : g.getIncidentEdges(vertex)) {
                // for the current edge get the neighbor
                Vertex neighbor = vertex == edge.getS() ? edge.getT() : edge.getS();
                // skip this neighbor if it wasn't placed yet
                if (mapVertexToCoordinate.get(neighbor) == null) {
                    continue;
                }
               rTree = rTree.add(edge ,Geometries.line(mapVertexToCoordinate.get(edge.getS()).getX(),mapVertexToCoordinate.get(edge.getS()).getY(), mapVertexToCoordinate.get(edge.getT()).getX(),mapVertexToCoordinate.get(edge.getT()).getY()));
            }

        }
        Iterable<Entry<Edge, Geometry>> search = rTree.search(Geometries.rectangle(0,0,100,100));


         HashSet<Edge> allEdges = new HashSet<>();
        
        for (Entry<Edge, Geometry> entry : search) {
            System.out.println(entry.value().getS().getId());
            if (!allEdges.contains(entry.value())) {
                    allEdges.add(entry.value());      
                }
        }

        int crossingNumber = 0;

        for (Edge e1 : allEdges) {
            for (Edge e2 : allEdges) {
                if (!e1.equals(e2)) {
                    if (!e1.isAdjacent(e2)) {
                        if (!mapVertexToCoordinate.containsKey(e1.getS()) || !mapVertexToCoordinate.containsKey(e1.getT())
                                || !mapVertexToCoordinate.containsKey(e2.getS())
                                || !mapVertexToCoordinate.containsKey(e2.getT())) {
                            continue;
                        }
                        Segment s1 = new Segment(mapVertexToCoordinate.get(e1.getS()), mapVertexToCoordinate.get(e1.getT()));
                        Segment s2 = new Segment(mapVertexToCoordinate.get(e2.getS()), mapVertexToCoordinate.get(e2.getT()));
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
