package GraphXings.Algorithms;

import GraphXings.Data.Coordinate;
import GraphXings.Data.Graph;
import GraphXings.Data.Vertex;
import GraphXings.Game.GameMove;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * A player performing random moves.
 */
public class ExperimentalPlayer implements Player
{
    /**
     * The name of the random player.
     */
    private String name;
    private Scanner scanner;

    /**
     * Creates a random player with the assigned name.
     * @param name
     */
    public ExperimentalPlayer(String name)
    {
        this.name = name;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public GameMove maximizeCrossings(Graph g, HashMap<Vertex, Coordinate> vertexCoordinates, List<GameMove> gameMoves, int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height)
    {
        return humanMove(g, usedCoordinates, placedVertices, width, height);
        //return experimentalMove(g,usedCoordinates,placedVertices,width,height);
    }

    @Override
    public GameMove minimizeCrossings(Graph g, HashMap<Vertex, Coordinate> vertexCoordinates, List<GameMove> gameMoves, int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height)
    {
        return humanMove(g, usedCoordinates, placedVertices, width, height);
        //return experimentalMove(g,usedCoordinates,placedVertices,width,height);
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
    private GameMove experimentalMove(Graph g, int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height)
    {
        Random r = new Random();
        int stillToBePlaced = g.getN()- placedVertices.size();
        //System.out.println("Still to be placed: "+stillToBePlaced);
        int next = r.nextInt(stillToBePlaced);
        //System.out.println("Next: "+next + "\n");
        int skipped = 0;
        Vertex v=null;
        for (Vertex u : g.getVertices())
        {
            if (!placedVertices.contains(u))
            {
                if (skipped < next)
                {
                    skipped++;
                    continue;
                }
                v=u;
                break;
            }
        }
        Coordinate c = new Coordinate(0,0);
        do
        {
            c = new Coordinate(r.nextInt(width),r.nextInt(height));
        }
        while (usedCoordinates[c.getX()][c.getY()]!=0);
        return new GameMove(v,c);
    }
    private GameMove humanMove(Graph g, int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height)
    {
        // get the input from the user, it should be two numbers separated by a space
        // the first number is the vertex number, the second number is the coordinate
        System.out.println("Please enter the vertex number and the coordinate separated by a comma");
        String input = scanner.nextLine();
        String[] inputArray = input.split(",");
        String vertexNumber = inputArray[0];
        Coordinate c = new Coordinate(Integer.parseInt(inputArray[1]),Integer.parseInt(inputArray[2]));
        //System.out.println("Vertex: "+vertexNumber);
        //System.out.println("Coordinate: "+c.getX()+","+c.getY());
        
        while(true){
            for (Vertex u : g.getVertices())
            {
                //System.out.println("Vertex: "+u.getId());
                if (u.getId().equals(vertexNumber))
                {
                    //System.out.println("Vertex found");
                    if (placedVertices.contains(u))
                    {
                        System.out.println("This vertex has already been placed");
                    }
                    else if (usedCoordinates[c.getX()][c.getY()]!=0)
                    {
                        System.out.println("This coordinate has already been used");
                    }
                    else
                    {
                        System.out.println("Valid move");
                        return new GameMove(u,c);
                    }
                }
            }
            
        }
    }


    

    @Override
    public String getName()
    {
        return name;
    }
}
