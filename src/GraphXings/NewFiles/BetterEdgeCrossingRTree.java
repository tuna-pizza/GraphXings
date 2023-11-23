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
import com.github.davidmoten.rtree2.geometry.Rectangle;

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
    public void insertAllCoodinates(HashSet<Vertex> placedVertecies) {
        vertices = new ArrayList<Vertex>();
        placedVertecies.forEach(vertex -> {
            vertices.add(vertex);
        });;

    }

    public void insertVertex(Vertex v) {
        vertices.add(v);
    }

    public int testCoordinate(Vertex vertex, Coordinate coordinateToAdd,
        HashMap<Vertex, Coordinate> mapVertexToCoordinate) {
        mapVertexToCoordinate.put(vertex, coordinateToAdd);
        vertices.add(vertex);
        int crossingsAddedByVertex = calculateIntersections(mapVertexToCoordinate,vertex);
        mapVertexToCoordinate.remove(vertex);
        removeCoordinate(vertex);
        return crossingsAddedByVertex;
    }

    public void removeCoordinate(Vertex vertex) {
        vertices.remove(vertex);
    }

    public void createImg(){
       rTree.visualize(1000, 1000).save("./images/Graph_" + ".png" );
    }

    public int calculateIntersections(HashMap<Vertex, Coordinate> mapVertexToCoordinate, Vertex v) {     
        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;

        // iterate over the edges adjacent to this vertex
            for (Edge edge : g.getIncidentEdges(v)) {
        // for the current edge get the neighbor
            Vertex neighbor = v == edge.getS() ? edge.getT() : edge.getS();
            // skip this neighbor if it wasn't placed yet
            if (mapVertexToCoordinate.get(neighbor) == null) {
                continue;
            }
            x1 = Integer.min(mapVertexToCoordinate.get(edge.getS()).getX(), mapVertexToCoordinate.get(edge.getT()).getX());
            y1 = Integer.min(mapVertexToCoordinate.get(edge.getS()).getY(), mapVertexToCoordinate.get(edge.getT()).getY());
            x2 = Integer.max(mapVertexToCoordinate.get(edge.getS()).getX(), mapVertexToCoordinate.get(edge.getT()).getX());
            y2 = Integer.max(mapVertexToCoordinate.get(edge.getS()).getY(), mapVertexToCoordinate.get(edge.getT()).getY());
            rTree = rTree.add(edge ,Geometries.line(mapVertexToCoordinate.get(edge.getS()).getX(),mapVertexToCoordinate.get(edge.getS()).getY(), mapVertexToCoordinate.get(edge.getT()).getX(),mapVertexToCoordinate.get(edge.getT()).getY()));
        }
    
        Rectangle rectangle = Geometries.rectangle(x1,y1,x2,y2);
   
        Iterable<Entry<Edge, Geometry>> search = rTree.search(rectangle);

        HashSet<Vertex> searchedVertecies = new HashSet<>();
        
        for (Entry<Edge, Geometry> entry : search) {
         searchedVertecies.add(entry.value().getS());
         searchedVertecies.add(entry.value().getT());      
        }

        int crossings = 0;

        // create a TreeSet where you automatically sort the added edges by the y-value
        Comparator<Edge> comparator = (Edge e1, Edge e2) -> {
            Coordinate starting1 = mapVertexToCoordinate.get(e1.getS());
            Coordinate ending1 = mapVertexToCoordinate.get(e1.getT());
            int max1 = Math.max(starting1.getY(), ending1.getY());
            Coordinate starting2 = mapVertexToCoordinate.get(e2.getS());
            Coordinate ending2 = mapVertexToCoordinate.get(e2.getT());
            int max2 = Math.max(starting2.getY(), ending2.getY());
            return Integer.compare(max1, max2);
        };
        TreeSet<Edge> binarySearchTree = new TreeSet<Edge>(comparator);



        for (Vertex vertex : searchedVertecies) {
            // iterate over the edges adjacent to this vertex
            for (Edge edge : g.getIncidentEdges(vertex)) {
                // for the current edge get the neighbor
                Vertex neighbor = vertex == edge.getS() ? edge.getT() : edge.getS();
                // skip this neighbor if it wasn't placed yet
                if (mapVertexToCoordinate.get(neighbor) == null) {
                    continue;
                }

                // create a segment from the current edge for the intersection check
                Segment thisSegment = new Segment(mapVertexToCoordinate.get(edge.getS()),
                        mapVertexToCoordinate.get(edge.getT()));

                // if the vertex is positioned left (x-axis) of its neighbor
                // compute whether there is a crossing
                if (mapVertexToCoordinate.get(vertex).getX() < mapVertexToCoordinate.get(neighbor).getX()) {
                    // (!) add the edge to the search tree if we sweep over the line from now on
                    binarySearchTree.add(edge);

                    // get the next higher edge (y-axis) after the current edge
                    Edge higherEdge = binarySearchTree.higher(edge);
                    // if that edge is valid (there is a higher edge one than the current edge)
                    if (higherEdge != null) {
                        // create another segment from this predicted edge
                        Segment higherSegment = new Segment(mapVertexToCoordinate.get(higherEdge.getS()),
                                mapVertexToCoordinate.get(higherEdge.getT()));
                        // if the segments intersect, we increment the crossing number and check for the
                        // next neighbor
                        if (Segment.intersect(thisSegment, higherSegment)) {
                            crossings++;
                            continue;
                        }
                    }
                    // analogous as for .higher() above, get the next lower edge (y-axis) before the
                    // current edge and check if there is a crossing
                    Edge lowerEdge = binarySearchTree.higher(edge);
                    if (lowerEdge != null) {
                        Segment lowerSegment = new Segment(mapVertexToCoordinate.get(lowerEdge.getS()),
                                mapVertexToCoordinate.get(lowerEdge.getT()));
                        if (Segment.intersect(thisSegment, lowerSegment)) {
                            crossings++;
                            continue;
                        }
                    }
                }
                // analogous to the other case, if the vertex is positioned right (x-axis) of
                // its neighbor
                else {
                    Edge higherEdge = binarySearchTree.higher(edge);
                    if (higherEdge != null) {
                        Segment higherSegment = new Segment(mapVertexToCoordinate.get(higherEdge.getS()),
                                mapVertexToCoordinate.get(higherEdge.getT()));
                        if (Segment.intersect(thisSegment, higherSegment)) {
                            crossings++;
                            continue;
                        }
                    }
                    Edge lowerEdge = binarySearchTree.lower(edge);
                    if (lowerEdge != null) {
                        Segment lowerSegment = new Segment(mapVertexToCoordinate.get(lowerEdge.getS()),
                                mapVertexToCoordinate.get(lowerEdge.getT()));
                        if (Segment.intersect(thisSegment, lowerSegment)) {
                            crossings++;
                            continue;
                        }
                    }

                    // (!) delete the neighbor from the search tree if we are finished sweeping over
                    // the edge
                    binarySearchTree.remove(edge);
                }
            }
        }
        // return the number of crossings with the given vertex in the current gamestate
        return crossings;
    }

}
