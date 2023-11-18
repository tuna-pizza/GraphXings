package GraphXings.NewFiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.Iterator;

import GraphXings.Algorithms.NewPlayer;
import GraphXings.Game.GameMove;
import GraphXings.Game.GameState;
import GraphXings.Data.*;

/**
 * A player performing random moves.
 */
public class MixingPlayer implements NewPlayer {
    /**
     * The name of the Good palyer
     */
    private String name;
    /**
     * A random number generator.
     */
    private Random r;
    /**
     * The graph to be drawn.
     */
    private Graph g;
    /**
     * The current state of the game;
     */
    private GameState gs;
    /**
     * The width of the game board.
     */
    private int width;
    /**
     * The height of the game board.
     */
    private int height;
    /**
     * The Edge Crossing algorithm to be applied
     */
    private BetterEdgeCrossing betterEdgeCrossing;
    /**
     * The sample size of the brute force method
     */
    private int sampleSize;
    /**
     * The percentage value with which to choose brute force over the mirror tactic
     */
    // TODO: the larger the field and number of vertices, the higher the percentage
    // for mirroring game (e.g. annealing)
    // TODO: make it so that this is calculated: percentage =
    // duration(BruteForceMove) / duration(MirroringMove)
    // TODO: alternatively: change the sample size of the brute force player part
    private double percentage;
    /**
     * The last move made by this player
     */
    private GameMove lastOwnMove = null;
    /**
     * The id of a vertex mapped to its vertex object
     */
    private HashMap<String, Vertex> mapIdToVertex = new HashMap<String, Vertex>();
    /**
     * The strategy used (see Strategy enum)
     */
    private Strategy strategy;

    public MixingPlayer(String name) {
        this.name = name;
        this.sampleSize = 10;
        this.percentage = 0.995;
        this.strategy = Strategy.Annealing;
    }

    /**
     * Creates a player with the assigned name.
     * that mixes two strategies
     * 
     * @param name
     */
    public MixingPlayer(String name, int sampleSize, double percentage, Strategy strategy) {
        this.name = name;
        this.sampleSize = sampleSize;
        this.percentage = percentage;
        this.strategy = strategy;
        this.r = new Random(name.hashCode());
    }

    @Override
    public GameMove maximizeCrossings(GameMove lastMove) {
        return makeMove(lastMove, true);
    }

    @Override
    public GameMove minimizeCrossings(GameMove lastMove) {
        return makeMove(lastMove, false);
    }

    public GameMove makeMove(GameMove lastMove, boolean maximize) {
        // First: Apply the last move by the opponent to the local GameState (and the
        // Crossing Calculator)
        if (lastMove != null) {
            gs.applyMove(lastMove);
            betterEdgeCrossing.insertVertexByCoordinate(lastMove.getVertex(), gs.getVertexCoordinates());
        }
        // Second: Compute the new move.
        GameMove newMove = getMove(lastMove, maximize);
        // Third: Apply the new move to the local GameState (and the Crossing
        // Calculator)
        gs.applyMove(newMove);
        betterEdgeCrossing.insertVertexByCoordinate(newMove.getVertex(), gs.getVertexCoordinates());
        // Finally: Return the new move.
        return newMove;
    }

    public GameMove getMove(GameMove lastMove, boolean maximize) {
        if (maximize) {
            // double progress = (double) gs.getPlacedVertices().size() / g.getN();
            // System.out.print(progress);
            switch (strategy) {
                case BruteForce:
                    return getBruteForceMove(maximize);
                case Mirroring:
                    return getMirroringMove(lastMove);
                case Percentage:
                    if (r.nextDouble() < percentage) {
                        return getBruteForceMove(maximize);
                    } else {
                        return getMirroringMove(lastMove);
                    }
                case Annealing:
                    if ((double) gs.getPlacedVertices().size() / g.getN() < percentage) {
                        return getMirroringMove(lastMove);
                    } else {
                        return getBruteForceMove(maximize);
                    }
                case AnnealingReverse:
                    if ((double) gs.getPlacedVertices().size() / g.getN() < percentage) {
                        return getBruteForceMove(maximize);
                    } else {
                        return getMirroringMove(lastMove);
                    }
                default:
                    return getRandomMove();
            }
        } else {
            return getMinimizingMove();
        }
    }

    public Boolean enemyCounterMinimzer() {
        int numberOfMovesToCheck = 10;
        ArrayList<Vertex> vertexFiFo = new ArrayList<>();
        for (Vertex vertex : gs.getPlacedVertices()) {
            if (vertexFiFo.size() >= numberOfMovesToCheck) {
                vertexFiFo.remove(0);
            }
            vertexFiFo.add(vertex);
        }
        int neighborsStolen = 0;
        for (Vertex vertex : vertexFiFo) {
            Iterator<Edge> edges = g.getIncidentEdges(vertex).iterator();
            while (edges.hasNext()) {
                Edge edge = edges.next();
                Vertex vertexToCheck = edge.getS() != vertex ? edge.getS() : edge.getT();
                if (vertexFiFo.contains(vertexToCheck)) {
                    neighborsStolen++;
                }
            }
        }
        if (neighborsStolen > (numberOfMovesToCheck / 1.5)) {
            return true;
        }
        return false;
    }

    public GameMove getMinimizingMove() {
        Vertex vertexToPlace;
        GameMove newMove = null;
        // If the enemy tries to counter our method by always placing our neighbours we
        // return to random playing

        if (enemyCounterMinimzer()) {
            lastOwnMove = null;
            return getBruteForceMove(false);
        }

        if (lastOwnMove != null) {
            // System.out.println("lastOwnMove != null");
            ArrayList<Vertex> unplacedNeighbors = getUnplacedNeighbors(lastOwnMove.getVertex());
            if (unplacedNeighbors.size() != 0) {
                vertexToPlace = unplacedNeighbors.get(unplacedNeighbors.size() - 1);
                int lastX = lastOwnMove.getCoordinate().getX();
                int lastY = lastOwnMove.getCoordinate().getY();
                // System.out.println(lastX + " " + lastY);
                if (lastX < lastY) {// Bottom left area
                    // System.out.print("lastX < lastY");
                    if (height - lastY < lastX) {// Bottom row
                        // System.out.print("height - lastY < lastX");
                        while (true) {
                            if (lastX > 0 && gs.getUsedCoordinates()[lastX - 1][lastY] == 0) {
                                newMove = new GameMove(vertexToPlace, new Coordinate(lastX - 1, lastY));
                                break;
                            } else if (lastY > 0 && gs.getUsedCoordinates()[lastX][lastY - 1] == 0) {
                                newMove = new GameMove(vertexToPlace, new Coordinate(lastX, lastY - 1));
                                break;
                            } else {
                                lastX--;
                                lastY--;
                                if (lastX < 0 || lastY < 0)
                                    break;
                            }
                        }
                    } else {// left column
                        // System.out.print("else");
                        while (true) {
                            if (lastX < width - 1 && gs.getUsedCoordinates()[lastX + 1][lastY] == 0) {
                                newMove = new GameMove(vertexToPlace, new Coordinate(lastX + 1, lastY));
                                break;
                            } else if (lastY > 0 && gs.getUsedCoordinates()[lastX][lastY - 1] == 0) {
                                newMove = new GameMove(vertexToPlace, new Coordinate(lastX, lastY - 1));
                                break;
                            } else {
                                lastX++;
                                lastY--;
                                if (lastX > width - 1 || lastY < 0)
                                    break;
                            }
                        }
                    }
                } else {// Top right area
                    // System.out.print("else");
                    if (lastY < width - lastX) {// Top row
                        // System.out.print("lastY < width - lastX");
                        while (true) {
                            // System.out.print(lastX + " " + lastY);
                            if (lastX < width - 1 && gs.getUsedCoordinates()[lastX + 1][lastY] == 0) {
                                // System.out.println("if");
                                newMove = new GameMove(vertexToPlace, new Coordinate(lastX + 1, lastY));
                                break;
                            } else if (lastY < height - 1 && gs.getUsedCoordinates()[lastX][lastY + 1] == 0) {
                                // System.out.println("elseif");
                                newMove = new GameMove(vertexToPlace, new Coordinate(lastX, lastY + 1));
                                break;
                            } else {
                                // System.out.println("else");
                                lastX++;
                                lastY++;
                                if (lastX > width - 1 || lastY > height - 1)
                                    break;
                            }
                        }
                    } else {// Right column
                        // System.out.print("else");
                        while (true) {
                            if (lastX > 0 && gs.getUsedCoordinates()[lastX - 1][lastY] == 0) {
                                newMove = new GameMove(vertexToPlace, new Coordinate(lastX - 1, lastY));
                                break;
                            } else if (lastY < height - 1 && gs.getUsedCoordinates()[lastX][lastY + 1] == 0) {
                                newMove = new GameMove(vertexToPlace, new Coordinate(lastX, lastY + 1));
                                break;
                            } else {
                                lastX--;
                                lastY++;
                                if (lastX < 0 || lastY > height - 1)
                                    break;
                            }
                        }
                    }
                }
            }
        }
        // We either have no real last move or no neighbor for our lastmove
        if (newMove == null) {
            // System.out.println("newMove == null");
            int midpointID = getLargestGapMidpointID();
            vertexToPlace = mapIdToVertex.get(Integer.toString(midpointID));
            int circumference = height * 2 + width * 2 - 4;
            int fieldID = midpointID % circumference; // Basically map a vertex to a distinct field vertex one
                                                      // placed on 0, 0. Vertex two placed on 0, 1. ....
            if (midpointID == 100) {
                // System.out.println("test");
            }
            int x = 0;
            int y = 0;
            while (true) {
                // Place on top row (ID 0-9)
                // Place on right column (ID 10-18)
                // Place on bottom row (ID 19-27)
                // Place on left column (ID 28-35)
                if (fieldID < width) {
                    x = fieldID;
                    y = 0;
                } else if (fieldID < width + height - 1) {
                    x = width - 1;
                    y = fieldID - width + 1;
                } else if (fieldID < width * 2 + height - 2) {
                    x = width - 1 - (fieldID - width - height + 2);
                    y = height - 1;
                } else if (fieldID < circumference) {
                    x = 0;
                    y = height - 1 - (fieldID - 2 * width - height + 3);
                } else {
                    break;
                }

                // check if it is an unplaced position
                if (gs.getUsedCoordinates()[x][y] == 0) {
                    newMove = new GameMove(vertexToPlace, new Coordinate(x, y));
                    break;
                }
                fieldID++;
            }
            // if (newMove != null && !gs.checkMoveValidity(newMove)) {
            // System.out.println("bad");
            // }
        }
        if (newMove != null) {
            // System.out.println("newMove != null");
            // System.out.println(newMove.getCoordinate().getX() + " " +
            // newMove.getCoordinate().getY());
            lastOwnMove = newMove;
            return newMove;
        }
        // System.out.println("getBruteForce");

        // Found no easy move, do some random stuff and try again
        lastOwnMove = null;
        return getBruteForceMove(false); // Found no easy move, do some random stuff and try again
    }

    public GameMove getBruteForceMove(boolean maximize) {
        // get the first vertex that is not yet placed
        Vertex v = null;
        for (Vertex v_ : g.getVertices()) {
            if (!gs.getPlacedVertices().contains(v_)) {
                v = v_;
                break;
            }
        }

        // Number of crossings before we place the vertex
        int bestCrossingsAddedByVertex = maximize ? -9999999 : 9999999;
        int bestSample = 0;

        ArrayList<Integer> xPositions = new ArrayList<Integer>();
        ArrayList<Integer> yPositions = new ArrayList<Integer>();
        // Create sample set of possible placing positions of the current vertex v
        for (int sample = 0; sample < sampleSize; sample++) {
            int x = r.nextInt(width);
            int y = r.nextInt(height);
            if (gs.getUsedCoordinates()[x][y] != 0) { // The random coordinate is already taken
                sample--;
            } else {
                xPositions.add(x);
                yPositions.add(y);
            }
        }

        // Find best position (maximizing crossings) we can place vertex v at
        for (int sample = 0; sample < sampleSize; sample++) {
            if (gs.getUsedCoordinates()[xPositions.get(sample)][yPositions.get(sample)] != 0)
                continue;
            Coordinate coordinateToAdd = new Coordinate(xPositions.get(sample), yPositions.get(sample));
            int crossingsAddedByVertex = betterEdgeCrossing.testCoordinate(v, coordinateToAdd,
                    gs.getVertexCoordinates());
            if (maximize ? crossingsAddedByVertex > bestCrossingsAddedByVertex
                    : crossingsAddedByVertex < bestCrossingsAddedByVertex) {
                bestSample = sample;
                bestCrossingsAddedByVertex = crossingsAddedByVertex;
            }
        }

        Coordinate coordinateToAdd = new Coordinate(xPositions.get(bestSample), yPositions.get(bestSample));
        HashMap<Vertex, Coordinate> mapVertexToCoordinate = gs.getVertexCoordinates();
        mapVertexToCoordinate.put(v, coordinateToAdd);
        betterEdgeCrossing.insertVertexByCoordinate(v, mapVertexToCoordinate);

        return new GameMove(v, coordinateToAdd);
    }

    public GameMove getMirroringMove(GameMove lastMove) {
        // place it mirrored around center point
        // on the circle/ellipsis that is halfway between center and border
        // (1/4 and 3/4 height or width)

        // if it's the first move, take the first vertex and place it on the circle
        // right side, it's as good as any
        if (lastMove == null) {
            return new GameMove(g.getVertices().iterator().next(), new Coordinate(3 * width / 4, 0));
        }

        // TODO: alternatively: get a good vertex (disregard last move)
        // get a good vertex, either one with 2 or 3 fixed neighbors or one that might
        // enable such a 2, 3 fixed one for oneself not the enemy don't enable the enemy
        // by fixing a second or third neighbor
        // Vertex v = getGoodVertex();
        // TODO: optimize this
        // choose the best unplaced neighbor from the lastMove's vertex
        ArrayList<Vertex> neighbors = getUnplacedNeighbors(lastMove.getVertex());

        // return another random move if all neighbors are placed already
        if (neighbors.size() == 0) {
            return getRandomMove();
        }
        // get the one with the most placed neighbors
        // it will draw more fixed edges and we should optimize this
        Vertex v = null;
        int maxNeighborsPlaced = -1;
        for (Vertex v_ : neighbors) {
            int n_ = (int) StreamSupport.stream(g.getIncidentEdges(v_).spliterator(), false).count()
                    - getUnplacedNeighbors(v_).size();
            if (n_ > maxNeighborsPlaced) {
                v = v_;
                maxNeighborsPlaced = n_;
            }
        }

        // mirror around the center onto the circle of highest probabilty
        // get the unit vector from the lastMove vertex towards the center
        int x = gs.getVertexCoordinates().get(lastMove.getVertex()).getX();
        int y = gs.getVertexCoordinates().get(lastMove.getVertex()).getY();
        double deltaX = width / 2.0 - x;
        double deltaY = height / 2.0 - y;
        double length = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        deltaX /= length;
        deltaY /= length;
        // get the position on the highest probability circle
        x = roundToClosestInteger(width / 2.0 + deltaX * width / 4.0);
        y = roundToClosestInteger(height / 2.0 + deltaY * height / 4.0);
        return new GameMove(v, getNearestFreeCoordinate(x, y));
    }

    public Coordinate getNearestFreeCoordinate(int x, int y) {
        // if position is not taken yet, return it
        if (gs.getUsedCoordinates()[x][y] == 0) {
            return new Coordinate(x, y);
        }
        // else get a neighboring position
        int i = 1;
        while (true) {
            for (int j = -i; j <= i; j++) {
                if ((x + j) >= 0 && (x + j) < width) {
                    for (int k = -i; k <= i; k++) {
                        if (Math.abs(j) == i || Math.abs(k) == i) {
                            if ((y + k) >= 0 && (y + k) < height) {
                                if (gs.getUsedCoordinates()[x + j][y + k] == 0) {
                                    return new Coordinate(x + j, y + k);
                                }
                            }
                        }
                    }
                }
            }
            i++;
        }
    }

    /**
     * Computes a random valid move.
     * 
     * @return A random valid move.
     */
    private GameMove getRandomMove() {
        int stillToBePlaced = g.getN() - gs.getPlacedVertices().size();
        int next = r.nextInt(stillToBePlaced);
        int skipped = 0;
        Vertex v = null;
        for (Vertex u : g.getVertices()) {
            if (!gs.getPlacedVertices().contains(u)) {
                if (skipped < next) {
                    skipped++;
                    continue;
                }
                v = u;
                break;
            }
        }
        Coordinate c;
        do {
            c = new Coordinate(r.nextInt(width), r.nextInt(height));
        } while (gs.getUsedCoordinates()[c.getX()][c.getY()] != 0);
        return new GameMove(v, c);
    }

    public ArrayList<Vertex> getUnplacedNeighbors(Vertex v) {
        ArrayList<Vertex> neighbors = new ArrayList<>();
        // TODO: why does this happen??
        if (g.getIncidentEdges(v) == null) {
            return neighbors;
        }
        for (Edge edge : g.getIncidentEdges(v)) {
            Vertex vertexToAdd = v.equals(edge.getS()) ? edge.getT() : edge.getS();
            if (!gs.getPlacedVertices().contains(vertexToAdd)) {
                neighbors.add(vertexToAdd);
            }
        }
        return neighbors;
    }

    public int getLargestGapMidpointID() {
        if (gs.getPlacedVertices().isEmpty()) {
            return g.getN() / 2;
        }

        // Convert the IDs to a list of integers and sort it
        List<Integer> ids = gs.getPlacedVertices().stream().map(vertex -> Integer.parseInt(vertex.getId())).sorted()
                .collect(Collectors.toList());

        // Initialize variables to track the largest gap and its midpoint
        int largestGap = 0;
        int largestGapMidpoint = 0;

        // Check the gap between each pair of consecutive IDs
        for (int i = 0; i < ids.size() - 1; i++) {
            int gap = ids.get(i + 1) - ids.get(i);
            if (gap > largestGap) {
                // System.out.println("gap " + gap + " with " + (i + 1) + " and id " + ids.get(i
                // + 1) + " and " + i
                // + " and id " + ids.get(i));
                largestGap = gap;
                largestGapMidpoint = ids.get(i) + gap / 2;
            }
        }

        // Check the gap between the last and first ID
        int finalGap = g.getN() + 1 - ids.get(ids.size() - 1);
        if (finalGap > largestGap) {
            // System.out.println("second");
            // System.out.println("gap " + finalGap + " with id 1 and " + (ids.size() - 1)
            // + " and id " + ids.get(ids.size() - 1));
            // System.out.println(ids);
            // System.out.println(ids.size());
            largestGapMidpoint = (ids.get(ids.size() - 1) + finalGap / 2) % g.getN();
        }
        // System.out.println(largestGapMidpoint);

        return largestGapMidpoint;
    }

    public int roundToClosestInteger(double val) {
        if (val < 0) {
            return (int) Math.ceil(val);
        }
        return (int) Math.floor(val);
    }

    @Override
    public void initializeNextRound(Graph g, int width, int height, Role role) {
        this.g = g;
        this.width = width;
        this.height = height;
        this.gs = new GameState(width, height);

        this.betterEdgeCrossing = new BetterEdgeCrossing(g);

        for (Vertex vertex : g.getVertices()) {
            this.mapIdToVertex.put(vertex.getId(), vertex);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * An enum describing the strategy used
     * 
     * BruteForce: always choose the BruteForceMove
     * Mirroring: always choose the MirroringMove
     * Percentage: given a percentage choose the getBruteForceMove randomly
     * Annealing: given a percentage choose the first turns to be getMirroringMove
     * (e.g. span lines across center first, then getBruteForceMove choice)
     * AnnealingReverse: given a percentage choose the first turns to be
     * getBruteForceMove
     */
    public enum Strategy {
        BruteForce, Mirroring, Percentage, Annealing, AnnealingReverse
    };

}
