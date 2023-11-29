package GraphXings.Legacy.Game;

import GraphXings.Algorithms.CrossingCalculator;
import GraphXings.Game.GameMove;
import GraphXings.Game.GameState;
import GraphXings.Legacy.Algorithms.Player;
import GraphXings.Data.Graph;

import java.util.*;


/**
 * A class for managing a game of GraphXings!
 */
public class Game
{
    /**
     * Decides whether or not data is copied before being passed on to players.
     */
    public static boolean safeMode = true;
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
     * The time limit for players.
     */
    private long timeLimit;

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
        this.timeLimit = Long.MAX_VALUE;
    }

    /**
     * Instantiates a game of GraphXings.
     * @param g The graph to be drawn.
     * @param width The width of the game board.
     * @param height The height of the game board.
     * @param player1 The first player. Plays as the maximizer in round one.
     * @param player2 The second player. Plays as the minimizer in round one.
     * @param timeLimit The time limit for players.
     */
    public Game(Graph g, int width, int height, Player player1, Player player2, long timeLimit)
    {
        this.g = g;
        this.width = width;
        this.height = height;
        this.player1 = player1;
        this.player2 = player2;
        this.timeLimit = timeLimit;
    }

    /**
     * Runs the full game of GraphXings.
     * @return Provides a GameResult Object containing the game's results.
     */
    public GameResult play()
    {
        Random r = new Random(System.nanoTime());
        if (r.nextBoolean())
        {
            Player swap = player1;
            player1 = player2;
            player2 = swap;
        }
        try
        {
            player1.initializeNextRound(g.copy(),width,height, Player.Role.MAX);
            player2.initializeNextRound(g.copy(),width,height, Player.Role.MIN);
            int crossingsGame1 = playRound(player1, player2);
            player1.initializeNextRound(g.copy(),width,height, Player.Role.MIN);
            player2.initializeNextRound(g.copy(),width,height, Player.Role.MAX);
            int crossingsGame2 = playRound(player2, player1);
            return new GameResult(crossingsGame1,crossingsGame2,player1,player2,false,false,false,false);
        }
        catch (InvalidMoveException ex)
        {
            System.err.println(ex.getCheater().getName() + " cheated!");
            if (ex.getCheater().equals(player1))
            {
                return new GameResult(0, 0, player1, player2,true,false,false,false);
            }
            else if (ex.getCheater().equals(player2))
            {
                return new GameResult(0,0,player1,player2,false,true,false,false);
            }
            else
            {
                return new GameResult(0,0,player1,player2,false,false,false,false);
            }
        }
        catch (TimeOutException ex)
        {
            System.err.println(ex.getTimeOutPlayer().getName() + " ran out of time!");
            if (ex.getTimeOutPlayer().equals(player1))
            {
                return new GameResult(0, 0, player1, player2,false,false,true,false);
            }
            else if (ex.getTimeOutPlayer().equals(player2))
            {
                return new GameResult(0,0,player1,player2,false,false,false,true);
            }
            else
            {
                return new GameResult(0,0,player1,player2,false,false,false,false);
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
    private int playRound(Player maximizer, Player minimizer) throws InvalidMoveException, TimeOutException
    {
        int turn = 0;
        GameState gs = new GameState(g,width,height);
        LinkedList<GameMove> gameMoves = new LinkedList<>();
        long timeMaximizer = 0;
        long timeMinimizer = 0;
        while (turn < g.getN())
        {
            GameMove newMove;
            Graph copyOfG = g;
            LinkedList<GameMove> copyOfGameMoves = gameMoves;
            if (turn%2 == 0)
            {
                long moveStartTime = System.nanoTime();
                newMove = maximizer.maximizeCrossings(copyOfG,gs.getVertexCoordinates(),copyOfGameMoves,gs.getUsedCoordinates(),gs.getPlacedVertices(),width,height);
                timeMaximizer += System.nanoTime()-moveStartTime;
                if (timeMaximizer > timeLimit)
                {
                    throw new TimeOutException(maximizer);
                }
                if (!gs.checkMoveValidity(newMove))
                {
                    throw new InvalidMoveException(maximizer);
                }
            }
           else
           {
               long moveStartTime = System.nanoTime();
               newMove = minimizer.minimizeCrossings(copyOfG,gs.getVertexCoordinates(),copyOfGameMoves,gs.getUsedCoordinates(),gs.getPlacedVertices(),width,height);
               timeMinimizer += System.nanoTime()-moveStartTime;
               if (timeMinimizer > timeLimit)
               {
                   throw new TimeOutException(minimizer);
               }
               if (!gs.checkMoveValidity(newMove))
               {
                   throw new InvalidMoveException(minimizer);
               }
           }
           gameMoves.add(newMove);
           gs.applyMove(newMove);
           turn++;
        }
        CrossingCalculator cc = new CrossingCalculator(g,gs.getVertexCoordinates());
        return  cc.computeCrossingNumber();
    }
}
