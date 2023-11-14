package GraphXings.NewFiles;

import GraphXings.Algorithms.Player;
import GraphXings.Game.GameMove;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import GraphXings.Data.*;

/**
 * A player performing random moves.
 */
public class MixingPlayer implements Player {
    /**
     * The name of the player.
     */
    private String name;

    /**
     * The strategies of the player
     */
    private Player strategy1;
    private Player strategy2;

    /**
     * Creates a player with the assigned name.
     * that mixes two strategies
     * 
     * @param name
     */
    public MixingPlayer(String name) {
        this.name = name;
    }

    @Override
    public GameMove maximizeCrossings(Graph g, HashMap<Vertex, Coordinate> vertexCoordinates, List<GameMove> gameMoves,
            int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height) {
        return new GameMove(null, null);
    }

    @Override
    public GameMove minimizeCrossings(Graph g, HashMap<Vertex, Coordinate> vertexCoordinates, List<GameMove> gameMoves,
            int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height) {
        return new GameMove(null, null);
    }

    @Override
    public void initializeNextRound(Graph g, int width, int height, Role role) {

    }

    @Override
    public String getName() {
        return name;
    }

    public String getStrategies() {
        return name + " mixes " + strategy1.getName() + " and " + strategy2.getName();
    }

}
