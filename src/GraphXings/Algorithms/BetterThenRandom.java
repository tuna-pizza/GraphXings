package GraphXings.Algorithms;

import GraphXings.Data.*;
import GraphXings.Game.GameMove;
import java.util.Map.Entry;
import java.lang.reflect.Array;
import java.util.*;

public class BetterThenRandom implements Player {

    private String name;

    public BetterThenRandom(String name) {
        this.name = name;
    }

    @Override
    public GameMove maximizeCrossings(Graph g, HashMap<Vertex, Coordinate> vertexCoordinates, List<GameMove> gameMoves,
            int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height) {
        return bruteforceMax(g, usedCoordinates, placedVertices, width, height, gameMoves, vertexCoordinates);
    }

    @Override
    public GameMove minimizeCrossings(Graph g, HashMap<Vertex, Coordinate> vertexCoordinates, List<GameMove> gameMoves,
            int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height) {
        return bruteforceMin(g, usedCoordinates, placedVertices, width, height, gameMoves, vertexCoordinates);
    }

    @Override
    public void initializeNextRound(Graph g, int width, int height, Role role) {

    }

    /**
     * Take's information of the state of the game and loops through all of the
     * possibilities
     * returns the coordinates and the current vertex with the most crossings
     * 
     * @return the best gamemove
     */
    private GameMove bruteforceMax(Graph g, int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width,
            int height, List<GameMove> gameMoves, HashMap<Vertex, Coordinate> vertexCoordinates) {

        // if first move is random, the winrate spikes from 80-90% to 90-95%
        if (gameMoves.size() == 0) {
            // System.out.println("is empty");
            return randomMove(g, usedCoordinates, placedVertices, width, height);
        }

        Coordinate c;
        Vertex v;
        v = nextVertex(g, placedVertices);
        Graph graphCopy;
        HashMap<Vertex, Coordinate> vertexCoordinatesCopy = new HashMap<>(vertexCoordinates);
        HashMap<Coordinate, Integer> scoreMap = new HashMap<>();

        // looping through the map
        for (int i = 0; i < usedCoordinates.length; i++) {

            for (int j = 0; j < usedCoordinates[i].length; j++) {
                if (usedCoordinates[i][j] == 0) {
                    c = new Coordinate(i, j);
                    graphCopy = g;

                    vertexCoordinatesCopy.put(v, c);
                    scoreMap.put(c, computeIntermediateCrossingNumber(graphCopy, vertexCoordinatesCopy));

                    break;
                }

            }

        }
        c = highestScoredCoordinate(scoreMap);
        return new GameMove(v, c);
    }

    /**
     * Take's information of the state of the game and loops through all of the
     * possibilities
     * returns the coordinates and the current vertex with the least crossings
     * 
     * @return the best gamemove
     */
    private GameMove bruteforceMin(Graph g, int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width,
            int height, List<GameMove> gameMoves, HashMap<Vertex, Coordinate> vertexCoordinates) {

        Coordinate c;
        Vertex v;
        v = nextVertex(g, placedVertices);
        Graph graphCopy;
        HashMap<Vertex, Coordinate> vertexCoordinatesCopy;
        HashMap<Coordinate, Integer> scoreMap = new HashMap<>();

        // looping through the map
        for (int i = 0; i < usedCoordinates.length; i++) {
            for (int j = 0; j < usedCoordinates[i].length; j++) {
                if (usedCoordinates[i][j] == 0) {
                    c = new Coordinate(i, j);
                    graphCopy = g;
                    vertexCoordinatesCopy = vertexCoordinates;
                    vertexCoordinatesCopy.put(v, c);
                    scoreMap.put(c, computeIntermediateCrossingNumber(graphCopy, vertexCoordinatesCopy));
                    break;
                }
            }
        }
        c = lowestScoredCoordinate(scoreMap);
        return new GameMove(v, c);
    }

    /**
     * Computes a random valid move. is a copy from the random player class
     * 
     * @param g               The graph.
     * @param usedCoordinates The used coordinates.
     * @param placedVertices  The already placed vertices.
     * @param width           The width of the game board.
     * @param height          The height of the game board.
     * @return A random valid move.
     */
    private GameMove randomMove(Graph g, int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width,
            int height) {
        Random r = new Random();

        int stillToBePlaced = g.getN() - placedVertices.size();
        int next = r.nextInt(stillToBePlaced);
        int skipped = 0;
        Vertex v = null;
        for (Vertex u : g.getVertices()) {
            if (!placedVertices.contains(u)) {
                if (skipped < next) {
                    skipped++;
                    continue;
                }
                v = u;
                break;
            }
        }
        Coordinate c = new Coordinate(0, 0);
        do {
            c = new Coordinate(r.nextInt(width), r.nextInt(height));
        } while (usedCoordinates[c.getX()][c.getY()] != 0);
        return new GameMove(v, c);
    }

    /**
     * calculates the highest value from
     * 
     * @param scoreMap
     * @return the coordinate from its value
     */
    private Coordinate highestScoredCoordinate(HashMap<Coordinate, Integer> scoreMap) {
        Entry<Coordinate, Integer> highestEntry = null;

        for (Entry<Coordinate, Integer> entry : scoreMap.entrySet()) {
            if (highestEntry == null || entry.getValue().compareTo(highestEntry.getValue()) > 0) {
                highestEntry = entry;
            }
        }

        if (highestEntry != null) {
            int highestValue = highestEntry.getValue();
            // System.out.println("Crossings: " + highestValue);

        } else {

        }

        return highestEntry.getKey();
    }

    /**
     * calculates the lowest value from
     * 
     * @param scoreMap
     * @return the coordinate from its value
     */
    private Coordinate lowestScoredCoordinate(HashMap<Coordinate, Integer> scoreMap) {

        Entry<Coordinate, Integer> minEntry = null;

        for (Entry<Coordinate, Integer> entry : scoreMap.entrySet()) {
            if (minEntry == null || entry.getValue().compareTo(minEntry.getValue()) < 0) {
                minEntry = entry;
            }
        }

        if (minEntry != null) {
            int lowestValue = minEntry.getValue();
            // System.out.println("Crossings: " + lowestValue);
        }

        return minEntry.getKey();
    }

    /**
     * Helper function to determinate the next vertex
     * 
     * @param g              is the given graph
     * @param placedVertices are the already placed vertexes
     * @return the next, not placed vertex
     */
    private Vertex nextVertex(Graph g, HashSet<Vertex> placedVertices) {

        for (Vertex u : g.getVertices()) {
            if (!placedVertices.contains(u)) {
                return u;
            }
        }
        return null;
    }

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
    public String getName() {
        return name;
    }
}
