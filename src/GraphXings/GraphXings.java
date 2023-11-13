package GraphXings;

import GraphXings.Algorithms.NewPlayer;
import GraphXings.Algorithms.NewRandomPlayer;
import GraphXings.Data.Edge;
import GraphXings.Data.Graph;
import GraphXings.Data.Vertex;
import GraphXings.Game.GameInstance.ConstantGameInstanceFactory;
import GraphXings.Game.GameInstance.GameInstanceFactory;
import GraphXings.Game.GameInstance.RandomCycleFactory;
import GraphXings.Game.Match.NewMatch;
import GraphXings.Game.Match.NewMatchResult;
import GraphXings.NewFiles.NewMixingPlayer;
import GraphXings.NewFiles.NewMixingPlayer.Strategy;

public class GraphXings {
    public static void main(String[] args) {
        // global matchup config
        int bestOutOf = 1;
        int sampleSize = 10;
        double percentage = 0.995;
        Strategy strategy = Strategy.Annealing;
        NewPlayer player1 = new NewMixingPlayer("NewMixingPlayer", sampleSize, percentage, strategy);
        NewPlayer player2 = new NewRandomPlayer("NewRandomPlayer");

        // type of game instanciation (random cycle -> true or constant -> false)
        boolean cycleFactory = false;
        // config to generate the graph for the constant game only(!)
        int numberOfVertices = 10000;
        int width = 10000;
        int height = 10000;
        assert numberOfVertices <= (width * height) : "Graph not possible";

        GameInstanceFactory gif = null;
        if (cycleFactory) {
            gif = new RandomCycleFactory();
        } else {
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
        NewMatch match = new NewMatch(player1, player2, gif, bestOutOf);
        long startTime = System.nanoTime();
        NewMatchResult mr = match.play();
        long endTime = System.nanoTime();
        System.out.println(mr.announceResult());
        System.out.println("average game time: " + (endTime - startTime) / bestOutOf / 1000000 + "ms");
    }
}
