package GraphXings;

import java.util.HashMap;
import java.util.Map;

import GraphXings.Algorithms.RandomPlayer;
import GraphXings.Data.Edge;
import GraphXings.Data.Graph;
import GraphXings.Data.Vertex;
import GraphXings.Game.Game;
import GraphXings.Game.GameResult;
import GraphXings.NewFiles.BetterThenRandom;

public class GraphXings {
    public static void main(String[] args) {
        // Create a graph g. This time it is a 10-cycle!
        Graph g = new Graph();
        int numberOfVertices = 100;
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

        int runs = 1;
        Game game;
        GameResult res;
        String[] results = new String[runs];
        for (int i = 0; i < runs; i++) {
            System.out.print("Running game ");
            System.out.println(i);
            game = new Game(g, 1000, 1000, new BetterThenRandom("ClosestVertex"), new RandomPlayer("Random"));
            res = game.play();
            results[i] = res.winner("ClosestVertex", "Random");
        }

        Map<String, Integer> hm = new HashMap();

        for (String x : results) {

            if (!hm.containsKey(x)) {
                hm.put(x, 1);
            } else {
                hm.put(x, hm.get(x) + 1);
            }
        }
        System.out.println(hm);
    }
}
