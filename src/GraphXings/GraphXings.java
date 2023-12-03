package GraphXings;

import GraphXings.Algorithms.NewPlayer;
import GraphXings.Game.GameInstance.RandomCycleFactory;
import GraphXings.Game.League.NewLeague;
import GraphXings.Game.League.NewLeagueResult;

import java.util.ArrayList;

public class GraphXings
{
    public static void main (String[] args)
    {
        ArrayList<NewPlayer> players = new ArrayList<>();
        //TODO: add players here
        RandomCycleFactory factory = new RandomCycleFactory(24091869, true);
        long timeLimit = 300000000000l;
        NewLeague l = new NewLeague(players,3,timeLimit,factory);
        NewLeagueResult lr = l.runLeague();
        System.out.println(lr.announceResults());
// import GraphXings.Algorithms.NewPlayer;
// import GraphXings.Algorithms.NewRandomPlayer;
// import GraphXings.Data.Edge;
// import GraphXings.Data.Graph;
// import GraphXings.Data.Vertex;
// import GraphXings.Game.GameInstance.ConstantGameInstanceFactory;
// import GraphXings.Game.GameInstance.GameInstanceFactory;
// import GraphXings.Game.GameInstance.RandomCycleFactory;
// import GraphXings.Game.Match.NewMatch;
// import GraphXings.Game.Match.NewMatchResult;
// import GraphXings.NewFiles.MixingPlayer;
// import GraphXings.NewFiles.MixingPlayer.Strategy;
// import GraphXings.otherCode.Gruppe8.*;

// public class GraphXings {
//     public static void main(String[] args) {

//         // global matchup config
//         int bestOutOf = 5;
//         int sampleSize = 50;
//         double percentage = 0.93;
//         Strategy strategy = Strategy.Annealing;
//         NewPlayer player1 = new MixingPlayer("MixingPlayer", sampleSize, percentage, strategy);
//         NewPlayer player2 = new EfficientWinningPlayer("Group8");

//         // type of game instanciation (random cycle -> true or constant -> false)
//         boolean cycleFactory = false;
//         // config to generate the graph from cycleFactory
//         int seed = 108910;
//         boolean includeMatchingEdges = true;
//         // config to generate the graph for the constant game only(!)
//         int numberOfVertices = 1500;
//         int width = 1000;
//         int height = 1000;
//         assert numberOfVertices <= (width * height) : "Graph not possible";

//         GameInstanceFactory gif = null;
//         if (cycleFactory) {
//             gif = new RandomCycleFactory(seed, includeMatchingEdges);
//         } else {
//             Graph g = new Graph();
//             Vertex firstVertex = new Vertex(Integer.toString(0));
//             Vertex oldVertex = firstVertex;
//             Vertex newVertex = null;
//             g.addVertex(oldVertex);
//             for (int i = 1; i < numberOfVertices; i++) {
//                 newVertex = new Vertex(Integer.toString(i));
//                 g.addVertex(newVertex);
//                 Edge edge = new Edge(oldVertex, newVertex);
//                 g.addEdge(edge);
//                 oldVertex = newVertex;
//             }
//             if (newVertex != null) {
//                 Edge edge = new Edge(newVertex, firstVertex);
//                 g.addEdge(edge);
//             }
//             gif = new ConstantGameInstanceFactory(g, width, height);
//         }

//         // play the matchup
//         NewMatch match = new NewMatch(player1, player2, gif, 1);
//         long startTime = System.nanoTime();
//         NewMatchResult mr = match.play();
//         long endTime = System.nanoTime();
//         System.out.println(mr.announceResult());
//         System.out.println("average game time: " + (endTime - startTime) / bestOutOf / 1000000 + "ms");
    }
}
