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
public class BruteForcePlayer implements Player {
    /**
     * The name of the Good palyer
     */
    private String name;
    private BetterEdgeCrossing betterEdgeCrossing = new BetterEdgeCrossing();

    private int sampleSize = 10;

    /**
     * Creates the good player with the assigned name.
     * 
     * @param name
     */
    public BruteForcePlayer(String name) {
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
        // get the first vertex that is not yet placed in the game
        Vertex v = null;
        for (Vertex v_ : g.getVertices()) {
            if (!placedVertices.contains(v_)) {
                v = v_;
                break;
            }
        }

        // Number of crossings before we placed the vertex
        int bestCrossingsAddedByVertex = maximize ? -9999999 : 9999999;
        int bestSample = 0;

        ArrayList<Integer> xPositions = new ArrayList<Integer>();
        ArrayList<Integer> yPositions = new ArrayList<Integer>();
        Random random = new Random();
        // Create sample set of possible placing positions of the current vertex v
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

        // Find best position (maximizing crossings) we can place vertex v at
        for (int sample = 0; sample < sampleSize; sample++) {
            if (usedCoordinates[xPositions.get(sample)][yPositions.get(sample)] != 0)
                continue;
            HashMap<Vertex, Coordinate> vertexCoordinates_ = vertexCoordinates;
            Coordinate coordinateToAdd = new Coordinate(xPositions.get(sample), yPositions.get(sample));
            vertexCoordinates_.put(v, coordinateToAdd);
            betterEdgeCrossing.insertCoordinate(v, vertexCoordinates_);
            int crossingsAddedByVertex = betterEdgeCrossing.calculateIntersections(g.getEdges(), vertexCoordinates_);
            betterEdgeCrossing.removeCoordinate(v);
            if (maximize ? crossingsAddedByVertex > bestCrossingsAddedByVertex
                    : crossingsAddedByVertex < bestCrossingsAddedByVertex) {
                bestSample = sample;
                bestCrossingsAddedByVertex = crossingsAddedByVertex;
            }
        }

        Coordinate coordinateToAdd = new Coordinate(xPositions.get(bestSample), yPositions.get(bestSample));
        HashMap<Vertex, Coordinate> vertexCoordinates_ = vertexCoordinates;
        vertexCoordinates_.put(v, coordinateToAdd);
        betterEdgeCrossing.insertCoordinate(v, vertexCoordinates_);

        return new GameMove(v, coordinateToAdd);
    }

    @Override
    public void initializeNextRound(Graph g, int width, int height, Role role) {
        betterEdgeCrossing = new BetterEdgeCrossing();
    }

    @Override
    public String getName() {
        return name;
    }
}