package GraphXings.NewFiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import GraphXings.Algorithms.NewPlayer;
import GraphXings.Game.GameMove;
import GraphXings.Game.GameState;
import GraphXings.Data.*;

/**
 * A player performing random moves.
 */
public class NewMixingPlayer implements NewPlayer {
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
     * The strategy used
     */
    private Strategy strategy;

    /**
     * Creates a player with the assigned name.
     * that mixes two strategies
     * 
     * @param name
     */
    public NewMixingPlayer(String name, int sampleSize, double percentage, Strategy strategy) {
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
        GameMove newMove;
        newMove = getMove(lastMove, maximize);
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
            // System.out.println(progress);
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
                    if (gs.getPlacedVertices().size() < percentage) {
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

    public GameMove getMinimizingMove() {
        Vertex vertexToPlace;
        GameMove newMove = null;
        if (lastOwnMove != null) {
            ArrayList<Vertex> unplacedNeighbors = getUnplacedNeighbors(lastOwnMove.getVertex());

            if (unplacedNeighbors.size() != 0) {
                vertexToPlace = unplacedNeighbors.getLast();

                int lastX = lastOwnMove.getCoordinate().getX();
                int lastY = lastOwnMove.getCoordinate().getY();
                if (lastX < lastY) {// Bottom left area
                    if (height - lastY < lastX) {// Bottom row
                        while (true) {
                            if (gs.getUsedCoordinates()[lastX - 1][lastY] == 0) {
                                newMove = new GameMove(vertexToPlace, new Coordinate(lastX - 1, lastY));
                                break;
                            } else if (gs.getUsedCoordinates()[lastX][lastY - 1] == 0) {
                                newMove = new GameMove(vertexToPlace, new Coordinate(lastX, lastY - 1));
                                break;
                            } else {
                                lastX -= 1;
                                lastY -= 1;
                                if (lastX < 0 || lastY < 0)
                                    break;
                            }
                        }
                    } else {// left column
                        while (true) {
                            if (gs.getUsedCoordinates()[lastX][lastY - 1] == 0) {
                                newMove = new GameMove(vertexToPlace, new Coordinate(lastX, lastY - 1));
                                break;
                            } else if (gs.getUsedCoordinates()[lastX + 1][lastY] == 0) {
                                newMove = new GameMove(vertexToPlace, new Coordinate(lastX + 1, lastY));
                                break;
                            } else {
                                lastX += 1;
                                lastY -= 1;
                                if (lastX > width || lastY < 0)
                                    break;
                            }
                        }
                    }
                } else {// Top right area
                    if (lastY < width - lastX) {// Top row
                        while (true) {
                            if (gs.getUsedCoordinates()[lastX + 1][lastY] == 0) {
                                newMove = new GameMove(vertexToPlace, new Coordinate(lastX + 1, lastY));
                                break;
                            } else if (gs.getUsedCoordinates()[lastX][lastY + 1] == 0) {
                                newMove = new GameMove(vertexToPlace, new Coordinate(lastX, lastY + 1));
                                break;
                            } else {
                                lastX += 1;
                                lastY += 1;
                                if (lastX > width || lastY > height)
                                    break;
                            }
                        }
                    } else {// Right column
                        while (true) {
                            if (gs.getUsedCoordinates()[lastX][lastY + 1] == 0) {
                                newMove = new GameMove(vertexToPlace, new Coordinate(lastX, lastY + 1));
                                break;
                            } else if (gs.getUsedCoordinates()[lastX - 1][lastY] == 0) {
                                newMove = new GameMove(vertexToPlace, new Coordinate(lastX - 1, lastY));
                                break;
                            } else {
                                lastX -= 1;
                                lastY += 1;
                                if (lastX < 0 || lastY > height)
                                    break;
                            }
                        }
                    }
                }
            }
        }
        if (newMove == null) {// We either have no real last move or no neighbor for our lastmove
            int largestGap = getLargestGap();
            vertexToPlace = mapIdToVertex.get(Long.toString(largestGap));
            int circumference = height * 2 + width * 2;
            int fieldID = largestGap % circumference; // Basically map a vertex to a distinct field vertex one
                                                      // placed on 0, 0. Vertex two placed on 0, 1. ....
            if (fieldID < width) { // Place on top
                while (true) {
                    if (gs.getUsedCoordinates()[fieldID][0] == 0) {
                        newMove = new GameMove(vertexToPlace, new Coordinate(fieldID, 0));
                        break;
                    } else {
                        fieldID += 1;
                        if (fieldID > width)
                            break;
                    }
                }
            } else if (fieldID < width + height) { // Place on right column
                fieldID = fieldID - width;
                while (true) {
                    if (gs.getUsedCoordinates()[width][fieldID] == 0) {
                        newMove = new GameMove(vertexToPlace, new Coordinate(width, fieldID));
                        break;
                    } else {
                        fieldID += 1;
                        if (fieldID > height)
                            break;
                    }
                }
            } else if (fieldID < width * 2 + height) {// Place on bottom row
                fieldID = fieldID - width - height;
                while (true) {
                    if (gs.getUsedCoordinates()[fieldID][height] == 0) {
                        newMove = new GameMove(vertexToPlace, new Coordinate(fieldID, height));
                        break;
                    } else {
                        fieldID -= 1;
                        if (fieldID < 0)
                            break;
                    }
                }
            } else {// Place on left column
                fieldID = fieldID - width * 2 - height;
                while (true) {
                    if (gs.getUsedCoordinates()[0][fieldID] == 0) {
                        newMove = new GameMove(vertexToPlace, new Coordinate(0, fieldID));
                        break;
                    } else {
                        fieldID -= 1;
                        if (fieldID < 0)
                            break;
                    }
                }
            }
        }
        // TODO: why does it throw here anyway?
        if (newMove != null && gs.checkMoveValidity(newMove)) {
            lastOwnMove = newMove;
            return newMove;
        }
        // TODO: like this or set lastOwnMove to null (as was before)?
        // lastOwnMove = getBruteForceMove(false); // Found no easy move, do some random
        // stuff and try again
        // return lastOwnMove;
        lastOwnMove = null;
        return getBruteForceMove(false); // Found no easy move, do some random stuff and try again
    }

    public GameMove getBruteForceMove(boolean maximize) {
        // get the first vertex that is not yet placed in the
        Vertex v = null;
        for (Vertex v_ : g.getVertices()) {
            if (!gs.getPlacedVertices().contains(v_)) {
                v = v_;
                break;
            }
        }

        // Number of crossings before we placed the vertex
        int bestCrossingsAddedByVertex = maximize ? -9999999 : 9999999;
        int bestSample = 0;

        ArrayList<Integer> xPositions = new ArrayList<Integer>();
        ArrayList<Integer> yPositions = new ArrayList<Integer>();
        Random random = new Random();
        // Create sample set of possible placing positions of the current vertex v
        for (int sample = 0; sample < sampleSize; sample++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
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

        GameMove rm = getRandomMove();
        Vertex v = rm.getVertex();
        // TODO: rewrite it with unplaced neighbors!!
        ArrayList<Vertex> neighbors = getUnplacedNeighbors(v);
        // TODO: get a good vertex
        // get a good vertex, either one with 2 or 3 fixed neighbors or one that might
        // enable such a 2, 3 fixed one for oneself not the enemy don't enable the enemy
        // by fixing a second or third neighbor
        // Vertex v = getGoodVertex();
        // TODO: or: choose the best neighbor from the last move with the isSet variable
        // get neighbors of last move, get the best of those
        // ArrayList<Vertex> neighbors = getNeighbors(lastMove.getVertex());
        // List<Boolean> isSet = neighbors.stream().map(neighbor ->
        // gs.getPlacedVertices().contains(neighbor))
        // .collect(Collectors.toList());
        Vertex next = neighbors.get(0);
        Vertex prev = neighbors.get(1);

        // check if one of the neighbors was placed already
        Vertex neighbor = null;
        boolean r = gs.getPlacedVertices().contains(next);
        boolean t = gs.getPlacedVertices().contains(prev);
        if (!r && !t) {
            return getRandomMove();
            // } else if (r && t) { // maximize both neighbors if both are set (later in
            // game)
            // // TODO: do the brute force maximizing here!!!
            // return rm;
        } else { // mirror around the center if only one is given
            neighbor = r ? next : prev;
            int x = gs.getVertexCoordinates().get(neighbor).getX();
            int y = gs.getVertexCoordinates().get(neighbor).getY();
            int ix = width - 1 - x;
            int iy = height - 1 - y;
            // mirror around the center
            if (gs.getUsedCoordinates()[ix][iy] == 0) {
                return new GameMove(v, new Coordinate(ix, iy));
            }
            // if taken, take one further away
            double deltaX = ix - x;
            double deltaY = iy - y;
            double length = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            deltaX /= length;
            deltaY /= length;
            int curr = 1;
            while (true) {
                // check diagonal point away from original (making it longer)
                int a = ix + roundToClosestInteger(curr * deltaX);
                int b = iy + roundToClosestInteger(curr * deltaY);
                if (a < 0 || a >= width || b < 0 || b >= height) {
                    break;
                }
                if (gs.getUsedCoordinates()[a][b] == 0) {
                    return new GameMove(v, new Coordinate(a, b));
                }
                // else get further away
                curr++;
            }
        }
        // else return the random move
        return rm;
    }

    public GameMove getNeighborMove(Vertex current, Vertex neighbor) {
        int x = gs.getVertexCoordinates().get(current).getX();
        int y = gs.getVertexCoordinates().get(current).getY();
        int neighborhood = 3;
        for (int i = 1; i < neighborhood; i++) {
            for (int j = -i; j <= i; j++) {
                if ((x + j) >= 0 && (x + j) < width) {
                    for (int k = -i; k <= i; k++) {
                        if (Math.abs(j) == i || Math.abs(k) == i) {
                            if ((y + k) >= 0 && (y + k) < height) {
                                if (gs.getUsedCoordinates()[x + j][y + k] == 0) {
                                    return new GameMove(neighbor, new Coordinate(x + j, y + k));
                                }
                            }
                        }
                    }
                }
            }
        }
        return getRandomMove();
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
        Iterable<Edge> e = g.getIncidentEdges(v);
        ArrayList<Vertex> neighbors = new ArrayList<>();
        for (Edge edge : e) {
            Vertex vertexToAdd = v.equals(edge.getS()) ? edge.getS() : edge.getT();
            if (gs.getPlacedVertices().contains(vertexToAdd))
                continue;
            neighbors.add(vertexToAdd);
        }
        return neighbors;
    }

    public int getLargestGap() {
        // Convert the IDs to a list of integers and sort it
        if (gs.getPlacedVertices().isEmpty()) {
            return g.getN() / 2;
        }

        List<Integer> ids = gs.getPlacedVertices().stream().map(vertex -> Integer.parseInt(vertex.getId())).sorted()
                .collect(Collectors.toList());

        // Initialize variables to track the largest gap and its midpoint
        int largestGap = 0;
        int largestGapMidpoint = 0;

        // Check the gap between each pair of consecutive IDs
        for (int i = 0; i < ids.size() - 1; i++) {
            int gap = ids.get(i + 1) - ids.get(i);
            if (gap > largestGap) {
                largestGap = gap;
                largestGapMidpoint = ids.get(i) + gap / 2;
            }
        }

        // Check the gap between the last ID and the total number of vertices
        int finalGap = g.getN() - ids.get(ids.size() - 1);
        if (finalGap > largestGap) {
            largestGapMidpoint = ids.get(ids.size() - 1) + finalGap / 2;
        }

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
