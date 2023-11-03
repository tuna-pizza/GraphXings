package GraphXings.NewFiles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import GraphXings.Game.GameMove;
import GraphXings.Algorithms.Player;
import GraphXings.Data.*;

/**
 * A player that bruteforces the, hopefully, best solution
 */
public class BetterThenRandom implements Player {
    /**
     * The name of the Good palyer
     */
    private String name;

    private int sampleSize = 10;

    /**
     * Creates the good player with the assigned name.
     * 
     * @param name
     */
    public BetterThenRandom(String name) {
        this.name = name;
    }

    @Override
    public GameMove maximizeCrossings(Graph g, HashMap<Vertex, Coordinate> vertexCoordinates, List<GameMove> gameMoves,
            int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height) {
        return BruteForce(g, vertexCoordinates, gameMoves, usedCoordinates, placedVertices, width, height, true);
    }

    @Override
    public GameMove minimizeCrossings(Graph g, HashMap<Vertex, Coordinate> vertexCoordinates, List<GameMove> gameMoves,
            int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height) {
        return BruteForce(g, vertexCoordinates, gameMoves, usedCoordinates, placedVertices, width, height, false);
    }

    public GameMove BruteForce(Graph g, HashMap<Vertex, Coordinate> vertexCoordinates, List<GameMove> gameMoves,
            int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height, boolean maximize) {
        Vertex v = null;
        for (Vertex v_ : g.getVertices()) {
            if (!placedVertices.contains(v_)) {
                v = v_;
                break;
            }
        }
        // System.out.println("Placing vertex: " + v.getId());
        // Number of crossings before we placed the vertex
        int bestCrossingsAddedByVertex = maximize ? -9999999 : 9999999;
        int bestSample = 0;

        ArrayList<Integer> xPositions = new ArrayList<Integer>();
        ArrayList<Integer> yPositions = new ArrayList<Integer>();
        Random random = new Random();
        // Create sample set
        for (int sample = 0; sample < sampleSize; sample++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            if (usedCoordinates[x][y] != 0) { // The random coordinate is already taken
                sample--;
            } else {
                xPositions.add(x);
                yPositions.add(y);
            }
        }

        // Find best position where we can place vertex
        for (int sample = 0; sample < sampleSize; sample++) {
            if (usedCoordinates[xPositions.get(sample)][yPositions.get(sample)] != 0)
                continue;
            HashMap<Vertex, Coordinate> vertexCoordinates_ = vertexCoordinates;
            vertexCoordinates_.put(v, new Coordinate(xPositions.get(sample), yPositions.get(sample)));
            int crossingsAddedByVertex = computeAdditionalCrossings(g, vertexCoordinates_, v);
            if (maximize ? crossingsAddedByVertex > bestCrossingsAddedByVertex
                    : crossingsAddedByVertex < bestCrossingsAddedByVertex) {
                bestSample = sample;
                bestCrossingsAddedByVertex = crossingsAddedByVertex;
            }
        }
        // System.out.println("Placing at: " + xPositions.get(bestSample) + ", " +
        // yPositions.get(bestSample));
        return new GameMove(v, new Coordinate(xPositions.get(bestSample), yPositions.get(bestSample)));
    }

    /**
     * Computes the number of crossings. The implementation is not efficient and
     * iterates over all pairs of edges resulting in quadratic time.
     * 
     * @return The number of crossings.
     */
    private int computeAdditionalCrossings(Graph g, HashMap<Vertex, Coordinate> vertexCoordinates, Vertex addedVertex) {
        int crossingNumber = 0;
        for (Edge e1 : g.getEdges()) {
            if (e1.getS() != addedVertex && e1.getT() != addedVertex)
                continue;
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