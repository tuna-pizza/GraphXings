package GraphXings;

import GraphXings.Algorithms.RandomPlayer;
import GraphXings.Algorithms.Player01;
import GraphXings.Data.Edge;
import GraphXings.Data.Graph;
import GraphXings.Data.Vertex;
import GraphXings.Game.Game;
import GraphXings.Game.GameResult;

import java.util.HashMap;
import java.util.Map;

public class GraphXings {
    public static void main(String[] args) {
        // Create a graph g. This time it is a 10-cycle!
        // Graph g = new Graph();
        // Vertex v1 = new Vertex("1");
        // Vertex v2 = new Vertex("2");
        // Vertex v3 = new Vertex("3");
        // Vertex v4 = new Vertex("4");
        // Vertex v5 = new Vertex("5");
        // Vertex v6 = new Vertex("6");
        // Vertex v7 = new Vertex("7");
        // Vertex v8 = new Vertex("8");
        // Vertex v9 = new Vertex("9");
        // Vertex v10 = new Vertex("10");
        // g.addVertex(v1);
        // g.addVertex(v2);
        // g.addVertex(v3);
        // g.addVertex(v4);
        // g.addVertex(v5);
        // g.addVertex(v6);
        // g.addVertex(v7);
        // g.addVertex(v8);
        // g.addVertex(v9);
        // g.addVertex(v10);
        // Edge e1 = new Edge(v1, v2);
        // Edge e2 = new Edge(v2, v3);
        // Edge e3 = new Edge(v3, v4);
        // Edge e4 = new Edge(v4, v5);
        // Edge e5 = new Edge(v5, v6);
        // Edge e6 = new Edge(v6, v7);
        // Edge e7 = new Edge(v7, v8);
        // Edge e8 = new Edge(v8, v9);
        // Edge e9 = new Edge(v9, v10);
        // Edge e10 = new Edge(v10, v1);
        // g.addEdge(e1);
        // g.addEdge(e2);
        // g.addEdge(e3);
        // g.addEdge(e4);
        // g.addEdge(e5);
        // g.addEdge(e6);
        // g.addEdge(e7);
        // g.addEdge(e8);
        // g.addEdge(e9);
        // g.addEdge(e10);

        // Run the game with two players.
        // // Game game = new Game(g, 5, 4, new RandomPlayer("Player 1"), new
        // // RandomPlayer("Player 2"));
        // Game game = new Game(g, 5, 4, new RandomPlayer("Player 1"), new
        // Player01("Player 2"));
        // GameResult res = game.play();
        // // Display the result!
        // System.out.println(res.announceResult());

        // parameters
        int runs = 100;
        int numberOfVertices = 10;
        int width = 1000;
        int height = 1000;

        // build game
        Game game;
        GameResult res;
        int[] results = new int[runs];

        // build graph
        Graph g = new Graph();
        Vertex firstVertex = new Vertex(Integer.toString(0));
        Vertex oldVertex = firstVertex;
        Vertex newVertex = null;
        g.addVertex(oldVertex);
        for (int i = 1; i < numberOfVertices; i++) {
            newVertex = new Vertex(Integer.toString(i));
            g.addVertex(newVertex);
            Edge edge = new Edge(oldVertex, newVertex);
            g.addEdge(edge);
            oldVertex = newVertex;
        }
        if (newVertex != null) {
            Edge edge = new Edge(newVertex, firstVertex);
            g.addEdge(edge);
        }

        long startT = System.currentTimeMillis();
        for (int i = 0; i < runs; i++) {
            game = new Game(g, width, height, new RandomPlayer("Player 1"), new Player01("Player 2"));
            res = game.play();
            results[i] = res.winner();
        }
        long endT = System.currentTimeMillis();
        System.out.println("Each Game takes " + (endT - startT) / runs + " ms");

        Map<Integer, Integer> hm = new HashMap();

        for (Integer x : results) {

            if (!hm.containsKey(x)) {
                hm.put(x, 1);
            } else {
                hm.put(x, hm.get(x) + 1);
            }
        }
        System.out.println(hm);
    }
}
