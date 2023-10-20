package GraphXings.Algorithms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import GraphXings.Game.GameMove;
import GraphXings.Data.*;

/**
 * A player that bruteforces the, hopefully, best solution
 */
public class GoodPlayer implements Player {
    /**
     * The name of the Good palyer
     */
    private String name;

    /**
     * Creates the good player with the assigned name.
     * 
     * @param name
     */
    public GoodPlayer(String name) {
        this.name = name;
    }

    @Override
    public GameMove maximizeCrossings(Graph g, HashMap<Vertex, Coordinate> vertexCoordinates, List<GameMove> gameMoves,
            int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height) {
        if (gameMoves.size() > 0) {
            System.out.println("last placed vertex: " + gameMoves.get(gameMoves.size() - 1).getVertex().getId());
        }
        System.out.println(
                "Currently there are: " + computeIntermediateCrossingNumber(g, vertexCoordinates) + " crossings");
        System.out.println("maximizing");
        Vertex v = null;
        for (Vertex v_ : g.getVertices()) {
            if (!placedVertices.contains(v_)) {
                v = v_;
                break;
            }
        }
        System.out.println("Placing vertex: " + v.getId());
        int numberOfCrossings = computeIntermediateCrossingNumber(g, vertexCoordinates);
        int bestX = 0;
        int bestY = 0;
        // Find first valid field where we can place vertex, starting condition
        for (int possibleX = 0; possibleX < width; possibleX++) {
            for (int possibleY = 0; possibleY < height; possibleY++) {
                if (usedCoordinates[possibleX][possibleY] == 0) {
                    bestX = possibleX;
                    bestY = possibleY;
                    HashMap<Vertex, Coordinate> vertexCoordinates_ = vertexCoordinates;
                    vertexCoordinates_.put(v, new Coordinate(possibleX, possibleY));
                    numberOfCrossings = computeIntermediateCrossingNumber(g, vertexCoordinates_);
                }
            }
        }

        // Find best position where we can place vertex
        for (int possibleX = 0; possibleX < width; possibleX++) {
            for (int possibleY = 0; possibleY < height; possibleY++) {
                if (usedCoordinates[possibleX][possibleY] != 0)
                    continue;
                HashMap<Vertex, Coordinate> vertexCoordinates_ = vertexCoordinates;
                vertexCoordinates_.put(v, new Coordinate(possibleX, possibleY));
                int intermediateCrossingNumber = computeIntermediateCrossingNumber(g, vertexCoordinates_);
                System.out.println("If we place vertex at " + possibleX + " " + possibleY + " there would be "
                        + intermediateCrossingNumber + " crossings");
                if (intermediateCrossingNumber > numberOfCrossings) {
                    bestX = possibleX;
                    bestY = possibleY;
                    numberOfCrossings = intermediateCrossingNumber;
                }
            }
        }
        System.out.println("Placing at: " + bestX + ", " + bestY);
        return new GameMove(v, new Coordinate(bestX, bestY));
    }

    @Override
    public GameMove minimizeCrossings(Graph g, HashMap<Vertex, Coordinate> vertexCoordinates, List<GameMove> gameMoves,
            int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height) {
        if (gameMoves.size() > 0) {
            System.out.println("last placed vertex: " + gameMoves.get(gameMoves.size() - 1).getVertex().getId());
        }
        System.out.println(
                "Currently there are: " + computeIntermediateCrossingNumber(g, vertexCoordinates) + " crossings");
        System.out.println("minimizing");
        Vertex v = null;
        for (Vertex v_ : g.getVertices()) {
            if (!placedVertices.contains(v_)) {
                v = v_;
                break;
            }
        }
        System.out.println("Placing vertex: " + v.getId());
        int numberOfCrossings = computeIntermediateCrossingNumber(g, vertexCoordinates);
        int bestX = 0;
        int bestY = 0;
        // Find first valid field where we can place vertex, starting condition
        for (int possibleX = 0; possibleX < width; possibleX++) {
            for (int possibleY = 0; possibleY < height; possibleY++) {
                if (usedCoordinates[possibleX][possibleY] == 0) {
                    bestX = possibleX;
                    bestY = possibleY;
                    HashMap<Vertex, Coordinate> vertexCoordinates_ = vertexCoordinates;
                    vertexCoordinates_.put(v, new Coordinate(possibleX, possibleY));
                    numberOfCrossings = computeIntermediateCrossingNumber(g, vertexCoordinates_);
                }
            }
        }

        // Find best position where we can place vertex
        for (int possibleX = 0; possibleX < width; possibleX++) {
            for (int possibleY = 0; possibleY < height; possibleY++) {
                if (usedCoordinates[possibleX][possibleY] != 0)
                    continue;
                HashMap<Vertex, Coordinate> vertexCoordinates_ = vertexCoordinates;
                vertexCoordinates_.put(v, new Coordinate(possibleX, possibleY));
                int intermediateCrossingNumber = computeIntermediateCrossingNumber(g, vertexCoordinates_);
                System.out.println("If we place vertex at " + possibleX + " " + possibleY + " there would be "
                        + intermediateCrossingNumber + " crossings");
                if (intermediateCrossingNumber < numberOfCrossings) {
                    bestX = possibleX;
                    bestY = possibleY;
                    numberOfCrossings = intermediateCrossingNumber;
                }
            }
        }
        System.out.println("Placing at: " + bestX + ", " + bestY);
        return new GameMove(v, new Coordinate(bestX, bestY));
    }

    /**
     * Computes the number of crossings. The implementation is not efficient and
     * iterates over all pairs of edges resulting in quadratic time.
     * 
     * @return The number of crossings.
     */
    private int computeIntermediateCrossingNumber(Graph g, HashMap<Vertex, Coordinate> vertexCoordinates) {
        int crossingNumber = 0;
        for (Edge e1 : g.getEdges()) {
            for (Edge e2 : g.getEdges()) {
                if (!e1.equals(e2)) {
                    if (!e1.isAdjacent(e2)) {
                        if (vertexCoordinates.get(e1.getS()) == null)
                            continue;
                        if (vertexCoordinates.get(e1.getT()) == null)
                            continue;
                        if (vertexCoordinates.get(e2.getS()) == null)
                            continue;
                        if (vertexCoordinates.get(e2.getT()) == null)
                            continue;
                        Segment s1 = new Segment(vertexCoordinates.get(e1.getS()), vertexCoordinates.get(e1.getT()));
                        Segment s2 = new Segment(vertexCoordinates.get(e2.getS()), vertexCoordinates.get(e2.getT()));
                        if (Segment.intersect(s1, s2)) {
                            crossingNumber++;
                        }
                    }
                }
            }
        }
        return crossingNumber / 2;
    }

    @Override
    public void initializeNextRound() {

    }

    @Override
    public String getName() {
        return name;
    }
}
