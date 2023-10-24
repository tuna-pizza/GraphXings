package GraphXings;

import GraphXings.Algorithms.BetterThenRandom;
import GraphXings.Algorithms.RandomPlayer;
import GraphXings.Data.Edge;
import GraphXings.Data.Graph;
import GraphXings.Data.Vertex;
import GraphXings.Game.Game;
import GraphXings.Game.GameResult;

public class GraphXings
{
    public static void main (String[] args)
    {
        // Create a graph g. This time it is a 10-cycle!
        Graph g = new Graph();
        Vertex v1 = new Vertex("1");
        Vertex v2 = new Vertex("2");
        Vertex v3 = new Vertex("3");
        Vertex v4 = new Vertex("4");
        Vertex v5 = new Vertex("5");
        Vertex v6 = new Vertex("6");
        Vertex v7 = new Vertex("7");
        Vertex v8 = new Vertex("8");
        Vertex v9 = new Vertex("9");
        Vertex v10 = new Vertex("10");
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        g.addVertex(v5);
        g.addVertex(v6);
        g.addVertex(v7);
        g.addVertex(v8);
        g.addVertex(v9);
        g.addVertex(v10);
        Edge e1 = new Edge(v1, v2);
        Edge e2 = new Edge(v2, v3);
        Edge e3 = new Edge(v3, v4);
        Edge e4 = new Edge(v4, v5);
        Edge e5 = new Edge(v5, v6);
        Edge e6 = new Edge(v6, v7);
        Edge e7 = new Edge(v7, v8);
        Edge e8 = new Edge(v8, v9);
        Edge e9 = new Edge(v9, v10);
        Edge e10 = new Edge(v10, v1);
        g.addEdge(e1);
        g.addEdge(e2);
        g.addEdge(e3);
        g.addEdge(e4);
        g.addEdge(e5);
        g.addEdge(e6);
        g.addEdge(e7);
        g.addEdge(e8);
        g.addEdge(e9);
        g.addEdge(e10);
        // Run the game with two players.
        Game game = new Game(g,5,4,new BetterThenRandom("Better Player"), new RandomPlayer("Random"));
        GameResult res = game.play();
        // Display the result!
        System.out.println(res.announceResult());
    }
}
