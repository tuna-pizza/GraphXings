package GraphXings.Game;

import GraphXings.Algorithms.CrossingCalculator;
import GraphXings.Algorithms.Player;
import GraphXings.Data.Coordinate;
import GraphXings.Data.Graph;
import GraphXings.Data.Vertex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * A class for managing a game of GraphXings!
 */
public class Game
{
    /**
     * The width of the game board.
     */
    private int width;
    /**
     * The height of the game board.
     */
    private int height;
    /**
     * The graph to be drawn.
     */
    private Graph g;
    /**
     * The first player.
     */
    private Player player1;
    /**
     * The second player.
     */
    private Player player2;

    /**
     * Instantiates a game of GraphXings.
     * @param g The graph to be drawn.
     * @param width The width of the game board.
     * @param height The height of the game board.
     * @param player1 The first player. Plays as the maximizer in round one.
     * @param player2 The second player. Plays as the minimizer in round one.
     */
    public Game(Graph g, int width, int height, Player player1, Player player2)
    {
        this.g = g;
        this.width = width;
        this.height = height;
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Runs the full game of GraphXings.
     * @return Provides a GameResult Object containing the game's results.
     */
    public GameResult play()
    {
        try
        {
            player1.initializeNextRound(g.copy(),width,height, Player.Role.MAX);
            player2.initializeNextRound(g.copy(),width,height, Player.Role.MIN);
            int crossingsGame1 = playRound(player1, player2);
            player1.initializeNextRound(g.copy(),width,height, Player.Role.MIN);
            player2.initializeNextRound(g.copy(),width,height, Player.Role.MAX);
            int crossingsGame2 = playRound(player2, player1);
            return new GameResult(crossingsGame1,crossingsGame2,player1,player2,false,false);
        }
        catch (InvalidMoveException ex)
        {
            if (ex.getCheater().equals(player1))
            {
                return new GameResult(0, 0, player1, player2,true,false);
            }
            else if (ex.getCheater().equals(player2))
            {
                return new GameResult(0,0,player1,player2,false,true);
            }
            else
            {
                return new GameResult(0,0,player1,player2,false,false);
            }
        }
    }

    /**
     * Plays a single round of the game.
     * @param maximizer The player with the goal to maximize the number of crossings.
     * @param minimizer The player with the goal to minimize the number of crossings
     * @return The number of crossings yielded in the final drawing.
     * @throws InvalidMoveException An exception caused by cheating.
     */
    private int playRound(Player maximizer, Player minimizer) throws InvalidMoveException
    {
        int turn = 0;
        LinkedList<GameMove> gameMoves = new LinkedList<>();
        HashMap<Vertex, Coordinate> vertexCoordinates = new HashMap<>();
        HashSet<Vertex> placedVertices = new HashSet<>();
        int[][] usedCoordinates = new int[width][height];
        for (int x = 0; x < width; x++)
        {
            for (int y=0; y < height; y++)
            {
                usedCoordinates[x][y] = 0;
            }
        }
        while (turn < g.getN())
        {
            GameMove newMove;
            Graph copyOfG = g.copy();
            LinkedList<GameMove> copyOfGameMoves = copyGameMoves(gameMoves);
            HashMap<Vertex, Coordinate> copyOfVertexCoordinates = copyVertexCoordinates(vertexCoordinates);
            HashSet<Vertex> copyOfPlacedVertices = copyPlacedVertices(placedVertices);
            int[][] copyOfUsedCoordinates = copyUsedCoordinates(usedCoordinates);
            if (turn%2 == 0)
            {
                newMove = maximizer.maximizeCrossings(copyOfG,copyOfVertexCoordinates,copyOfGameMoves,copyOfUsedCoordinates,copyOfPlacedVertices,width,height);
                if (!checkMoveValidity(newMove,placedVertices,usedCoordinates))
                {
                    throw new InvalidMoveException(maximizer);
                }
            }
           else
           {
                newMove = minimizer.minimizeCrossings(copyOfG,copyOfVertexCoordinates,copyOfGameMoves,copyOfUsedCoordinates,copyOfPlacedVertices,width,height);
                if (!checkMoveValidity(newMove,placedVertices,usedCoordinates))
                {
                    throw new InvalidMoveException(minimizer);
                }
           }
           gameMoves.add(newMove);
           usedCoordinates[newMove.getCoordinate().getX()][newMove.getCoordinate().getY()]=1;
           placedVertices.add(newMove.getVertex());
           vertexCoordinates.put(newMove.getVertex(), newMove.getCoordinate());
           turn++;
        }
        CrossingCalculator cc = new CrossingCalculator(g,vertexCoordinates);
        return  cc.computeCrossingNumber();
    }

    /**
     * Checks if a move is valid given the current state of the game.
     * @param newMove The potential move to be performed.
     * @param placedVertices The vertices that already are placed.
     * @param usedCoordinates A 0-1-map of the coordinates. 1 indicates an already used coordinate.
     * @return True if the move is valid, false if it is invalid.
     */
    private boolean checkMoveValidity(GameMove newMove, HashSet<Vertex> placedVertices, int[][] usedCoordinates)
    {
        if (newMove.getVertex() == null ||newMove.getCoordinate() == null)
        {
            return  false;
        }
        if (placedVertices.contains(newMove.getVertex()))
        {
            return false;
        }
        int x = newMove.getCoordinate().getX();
        int y = newMove.getCoordinate().getY();
        if (x >= width || y >= height)
        {
            return false;
        }
        if (usedCoordinates[x][y] != 0)
        {
            return false;
        }
        return true;
    }

    /**
     * Creates a copy of the vertex coordinates.
     * @param vertexCoordinates The original vertex coordinates.
     * @return A copy of vertexCoordinates.
     */
    private HashMap<Vertex, Coordinate> copyVertexCoordinates(HashMap<Vertex, Coordinate> vertexCoordinates)
    {
        HashMap<Vertex, Coordinate> copy = new HashMap<>();
        for (Vertex v : vertexCoordinates.keySet())
        {
            copy.put(v,vertexCoordinates.get(v));
        }
        return copy;
    }

    /**
     * Creates a copy of the list of placed vertices.
     * @param placedVertices The original list of placed vertices.
     * @return A copy of placedVertices.
     */
    private HashSet<Vertex> copyPlacedVertices(HashSet<Vertex> placedVertices)
    {
        HashSet<Vertex> copy = new HashSet<>();
        for (Vertex v : placedVertices)
        {
            copy.add(v);
        }
        return copy;
    }

    /**
     * Returns a copy of the list of prior game moves.
     * @param gameMoves The list of prior game moves.
     * @return A copy of gameMoves.
     */
    private LinkedList<GameMove> copyGameMoves(List<GameMove> gameMoves)
    {
        LinkedList<GameMove> copy = new LinkedList<>();
        for(int i=0; i < gameMoves.size(); i++)
        {
            copy.add(gameMoves.get(i));
        }
        return copy;
    }

    /**
     * Returns a copy of the map of used coordinates.
     * @param usedCoordinates The original map of used coordinates.
     * @return A copy of usedCoordinates.
     */
    private int[][] copyUsedCoordinates(int[][] usedCoordinates)
    {
        int[][] copy = new int[usedCoordinates.length][usedCoordinates[0].length];
        for (int i = 0; i < usedCoordinates.length; i++)
        {
            for (int j = 0; j < usedCoordinates[0].length; j++)
            {
                copy[i][j]=usedCoordinates[i][j];
            }
        }
        return copy;
    }
}
