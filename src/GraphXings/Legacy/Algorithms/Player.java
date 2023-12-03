package GraphXings.Legacy.Algorithms;

import GraphXings.Data.*;
import GraphXings.Game.GameMove;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * A player of the GraphXings game.
 */
public interface Player
{
    /**
     * Tells the player to perform the next move where the objective is to create crossings! In a valid move, a vertex of g that is not belonging to placedVertices is positioned onto a coordinate that is
     * not marked with a 1 in usedCoordinates. In addition, only the x-coordinates 0 to width-1 and the y-coordinates 0 to height-1 are allowed.
     * @param g The graph to be drawn.
     * @param vertexCoordinates The coordinates of the already placed vertices. Implemented as a HashMap mapping the Vertex object to its Coordinate.
     * @param gameMoves A sorted list of the already performed game moves.
     * @param usedCoordinates A 0-1 map of the coordinates of the allowed grid. Coordinates that have been used contain a 1 in the map, otherwise 0.
     * @param placedVertices A set of the already placed vertices.
     * @param width The width of the game board.
     * @param height The height of the game board.
     * @return The move to be performed.
     */
    public GameMove maximizeCrossings(Graph g, HashMap<Vertex,Coordinate> vertexCoordinates, List<GameMove> gameMoves, int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height);
    /**
     * Tells the player to perform the next move where the objective is to avoid crossings! In a valid move, a vertex of g that is not belonging to placedVertices is positioned onto a coordinate that is
     * not marked with a 1 in usedCoordinates. In addition, only the x-coordinates 0 to width-1 and the y-coordinates 0 to height-1 are allowed.
     * @param g The graph to be drawn.
     * @param vertexCoordinates The coordinates of the already placed vertices. Implemented as a HashMap mapping the Vertex object to its Coordinate.
     * @param gameMoves A sorted list of the already performed game moves.
     * @param usedCoordinates A 0-1 map of the coordinates of the allowed grid. Coordinates that have been used contain a 1 in the map, otherwise 0.
     * @param placedVertices A set of the already placed vertices.
     * @param width The width of the game board.
     * @param height The height of the game board.
     * @return The move to be performed.
     */
    public GameMove minimizeCrossings(Graph g, HashMap<Vertex,Coordinate> vertexCoordinates, List<GameMove> gameMoves, int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height);

    /**
     * Tells the player to get ready for the next round.
     * @param g The graph to be drawn in the next round.
     * @param width The width of the game board.
     * @param height The height of the game board.
     * @param role The role in the next round, either MAX or MIN.
     */
    public void initializeNextRound(Graph g, int width, int height, Role role);
    /**
     * Gets the name of the player.
     * @return The player's name.
     */
    public String getName();

    /**
     * An enum describing the role of the player in the next round.
     */
    public enum Role {MAX,MIN};
}
