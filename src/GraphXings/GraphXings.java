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
import GraphXings.NewFiles.DrawGraph;
import GraphXings.NewFiles.MixingPlayer;
import GraphXings.NewFiles.MixingPlayer.Strategy;
import GraphXings.NewFiles.otherGroups.Abgabegruppe_8_737_assignsubmission_file.Gruppe8.Gruppe8.EfficientWinningPlayer;

public class GraphXings {
    public static void main(String[] args) {

        // if(1==1) {
        // new DrawGraph();
        //
        // return;
        // }

        // global matchup config
        int bestOutOf = 20;
        int sampleSize = 50;
        double percentage = 0.93;
        Strategy strategy = Strategy.Annealing;
        NewPlayer player1 = new MixingPlayer("MixingPlayer", sampleSize, percentage, strategy);
        NewPlayer player2 = new EfficientWinningPlayer("Grp8");

        // type of game instanciation (random cycle -> true or constant -> false)
        boolean cycleFactory = true;
        // config to generate the graph from cycleFactory
        int seed = 108910;
        boolean includeMatchingEdges = true;
        // config to generate the graph for the constant game only(!)
        int numberOfVertices = 1000;
        int width = 1000;
        int height = 1000;
        assert numberOfVertices <= (width * height) : "Graph not possible";

        GameInstanceFactory gif = null;
        if (cycleFactory) {
            gif = new RandomCycleFactory(seed, includeMatchingEdges);
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
