package GraphXings.Algorithms;

import GraphXings.Data.Coordinate;
import GraphXings.Data.Graph;
import GraphXings.Data.Vertex;
import GraphXings.Game.GameMove;
import GraphXings.Algorithms.CrossingCalculator;


import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/**
 * A player performing random moves.
 */
public class MinMaxPlayer implements Player
{
    /**
     * The name of the random player.
     */
    private String name;

    /**
     * Creates a random player with the assigned name.
     * @param name
     */
    public MinMaxPlayer(String name)
    {
        this.name = name;
    }

    @Override
    public GameMove maximizeCrossings(Graph g, HashMap<Vertex, Coordinate> vertexCoordinates, List<GameMove> gameMoves, int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height)
    {
        return maxMove(g, vertexCoordinates, gameMoves, usedCoordinates, placedVertices, width, height);
    }

    @Override
    public GameMove minimizeCrossings(Graph g, HashMap<Vertex, Coordinate> vertexCoordinates, List<GameMove> gameMoves, int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height)
    {
        return minMove(g, vertexCoordinates, gameMoves, usedCoordinates, placedVertices, width, height);
    }

    @Override
    public void initializeNextRound()
    {

    }

    /**
     * Computes a random valid move.
     * @param g The graph.
     * @param usedCoordinates The used coordinates.
     * @param placedVertices The already placed vertices.
     * @param width The width of the game board.
     * @param height The height of the game board.
     * @return A random valid move.
     */
    private GameMove maxMove(Graph g, HashMap<Vertex, Coordinate> vertexCoordinates, List<GameMove> gameMoves, int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height)
    {
        return bestMove(g, vertexCoordinates, gameMoves, usedCoordinates, placedVertices, width, height, true);
    }
    private GameMove minMove(Graph g, HashMap<Vertex, Coordinate> vertexCoordinates, List<GameMove> gameMoves, int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height)
    {
        return bestMove(g, vertexCoordinates, gameMoves, usedCoordinates, placedVertices, width, height, false);
    }

    private GameMove bestMove(Graph g, HashMap<Vertex, Coordinate> vertexCoordinates, List<GameMove> gameMoves, int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height, boolean maximizingPlayer){
        int bestValue = Integer.MAX_VALUE;
        if (maximizingPlayer){
            bestValue = Integer.MIN_VALUE;
        }
        
        
        GameMove bestMove = null;

        List<GameMove> possibleGameMoves = getPossibleGameMoves(g, usedCoordinates, placedVertices, width, height);

        for (GameMove m : possibleGameMoves){
            HashMap<Vertex, Coordinate> childVertexCoordinates = copyVertexCoordinates(vertexCoordinates);
            childVertexCoordinates.put(m.getVertex(), m.getCoordinate());
            int value = minimax(g, usedCoordinates, placedVertices, width, height, 3, childVertexCoordinates, maximizingPlayer, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (value > bestValue){
                bestValue = value;
                bestMove = m;
            }
        }
        return bestMove;
    }

    private int minimax(Graph g, int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height, int depth, HashMap<Vertex, Coordinate> vertexCoordinates, boolean maximizingPlayer, int alpha, int beta)
    {
        if (depth == 0)
        {
            CrossingCalculator cc = new CrossingCalculator(g, vertexCoordinates);
            return cc.computePartialCrossingNumber();
        }
        if (maximizingPlayer){
            int maxEval = Integer.MIN_VALUE;
            List<GameMove> possibleGameMoves = getPossibleGameMoves(g, usedCoordinates, placedVertices, width, height);
            for (GameMove m : possibleGameMoves){
                HashMap<Vertex, Coordinate> childVertexCoordinates = copyVertexCoordinates(vertexCoordinates);
                childVertexCoordinates.put(m.getVertex(), m.getCoordinate());
                int eval = minimax(g, usedCoordinates, placedVertices, width, height, depth - 1, childVertexCoordinates, false, alpha, beta);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha){
                    break;
                }
            }
            return maxEval;
        }
        else{
            int minEval = Integer.MAX_VALUE;
            List<GameMove> possibleGameMoves = getPossibleGameMoves(g, usedCoordinates, placedVertices, width, height);
            for (GameMove m : possibleGameMoves){
                HashMap<Vertex, Coordinate> childVertexCoordinates = copyVertexCoordinates(vertexCoordinates);
                childVertexCoordinates.put(m.getVertex(), m.getCoordinate());
                int eval = minimax(g, usedCoordinates, placedVertices, width, height, depth - 1, childVertexCoordinates, true, alpha, beta);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha){
                    break;
                }
            }
            return minEval;
        }

    }

    private List<GameMove> getPossibleGameMoves(Graph g, int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height)
    {
        List<GameMove> possibleGameMoves = new LinkedList<>();

        for (int rowIdx = 0; rowIdx < height; rowIdx++)
        {
            for (int colIdx = 0; colIdx < width; colIdx++)
            {
                if (usedCoordinates[colIdx][rowIdx] == 0)
                {
                    Coordinate c = new Coordinate(colIdx, rowIdx);
                    for (Vertex v : g.getVertices())
                    {
                        possibleGameMoves.add(new GameMove(v,c));

                    }
                }
            }
        }
        return possibleGameMoves;
    }

    @Override
    public String getName()
    {
        return name;
    }

    private HashMap<Vertex, Coordinate> copyVertexCoordinates(HashMap<Vertex, Coordinate> vertexCoordinates)
    {
        HashMap<Vertex, Coordinate> copy = new HashMap<>();
        for (Vertex v : vertexCoordinates.keySet())
        {
            copy.put(v,vertexCoordinates.get(v));
        }
        return copy;
    }
}
