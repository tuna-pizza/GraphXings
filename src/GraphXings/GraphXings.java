package GraphXings;

import GraphXings.Algorithms.Player;
import GraphXings.Algorithms.RandomPlayer;
import GraphXings.Data.Edge;
import GraphXings.Data.Graph;
import GraphXings.Data.Vertex;
import GraphXings.Game.GameInstance.ConstantGameInstanceFactory;
import GraphXings.Game.GameInstance.GameInstanceFactory;
import GraphXings.Game.GameInstance.RandomCycleFactory;
import GraphXings.Game.Match.Match;
import GraphXings.Game.Match.MatchResult;
import GraphXings.NewFiles.BetterThanRandomPlayer;

public class GraphXings {
    public static void main(String[] args) {
        // number of games
        int bestOutOf = 100;

        // matchup
        Player player1 = new BetterThanRandomPlayer("BetterThanRandom");
        Player player2 = new RandomPlayer("Random");

        // type of game (random cycle -> true or constant -> false)
        boolean cycleFactory = false;

        GameInstanceFactory gif = null;
        if (cycleFactory) {
            gif = new RandomCycleFactory();
        } else {
            // generate the graph for the constant game
            int numberOfVertices = 100;
            int width = 1000;
            int height = 1000;

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
            gif = new ConstantGameInstanceFactory(g, width, height);
        }

        // play the matchup
        Match match = new Match(player1, player2, gif, bestOutOf);
        long startTime = System.nanoTime();
        MatchResult mr = match.play();
        long endTime = System.nanoTime();
        System.out.println(mr.announceResult());
        System.out.println("average game time: " + (endTime - startTime) / 1000000 / bestOutOf + "ms");
    }
}
