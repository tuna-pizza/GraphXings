package GraphXings.NewFiles;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

import GraphXings.Game.GameMove;
import GraphXings.Game.GameState;
import GraphXings.Algorithms.NewPlayer;
import GraphXings.Data.*;

/**
 * A player that bruteforces the, hopefully, best solution
 */
public class NewBruteForcePlayer implements NewPlayer {
    /**
     * The name of the Good palyer
     */
    private String name;
    /**
     * The graph to be drawn.
     */
    private Graph g;
    /**
     * The current state of the game;
     */
    private GameState gs;
    /**
     * The width of the game board.
     */
    private int width;
    /**
     * The height of the game board.
     */
    private int height;
    /**
     * The Edge Crossing algorithm to be applied
     */
    private BetterEdgeCrossing betterEdgeCrossing;
    /**
     * The sample size of the brute force method
     */
    private int sampleSize;

    /**
     * Creates the good player with the assigned name.
     * 
     * @param name
     */
    public NewBruteForcePlayer(String name) {
        this.name = name;
        this.sampleSize = 10;
    }

    @Override
    public GameMove maximizeCrossings(GameMove lastMove) {
        // First: Apply the last move by the opponent.
        if (lastMove != null) {
            gs.applyMove(lastMove);
        }
        // Second: Compute the new move.
        GameMove newMove = BruteForce(true);
        // Third: Apply the new move to the local GameState.
        gs.applyMove(newMove);
        // Finally: Return the new move.
        return newMove;
    }

    @Override
    public GameMove minimizeCrossings(GameMove lastMove) {
        // First: Apply the last move by the opponent.
        if (lastMove != null) {
            gs.applyMove(lastMove);
        }
        // Second: Compute the new move.
        GameMove newMove = BruteForce(false);
        // Third: Apply the new move to the local GameState.
        gs.applyMove(newMove);
        // Finally: Return the new move.
        return newMove;
    }

    public GameMove BruteForce(boolean maximize) {
        // get the first vertex that is not yet placed in the game
        Vertex v = null;
        for (Vertex v_ : g.getVertices()) {
            if (!gs.getPlacedVertices().contains(v_)) {
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
            if (gs.getUsedCoordinates()[x][y] != 0) { // The random coordinate is already taken
                sample--;
            } else {
                xPositions.add(x);
                yPositions.add(y);
            }
        }

        // Find best position (maximizing crossings) we can place vertex v at
        for (int sample = 0; sample < sampleSize; sample++) {
            if (gs.getUsedCoordinates()[xPositions.get(sample)][yPositions.get(sample)] != 0)
                continue;
            HashMap<Vertex, Coordinate> vertexCoordinates_ = gs.getVertexCoordinates();
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
        HashMap<Vertex, Coordinate> vertexCoordinates_ = gs.getVertexCoordinates();
        vertexCoordinates_.put(v, coordinateToAdd);
        betterEdgeCrossing.insertCoordinate(v, vertexCoordinates_);

        return new GameMove(v, coordinateToAdd);
    }

    @Override
    public void initializeNextRound(Graph g, int width, int height, Role role) {
        // Store graph, width, height and create a new GameState.
        this.g = g;
        this.width = width;
        this.height = height;
        this.gs = new GameState(width, height);
        this.betterEdgeCrossing = new BetterEdgeCrossing();
    }

    @Override
    public String getName() {
        return name;
    }
}