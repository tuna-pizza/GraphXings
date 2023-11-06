package GraphXings.Algorithms;

import GraphXings.Data.Coordinate;
import GraphXings.Data.Edge;
import GraphXings.Data.Graph;
import GraphXings.Data.Vertex;
import GraphXings.Game.Game;
import GraphXings.Game.GameMove;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.text.html.HTMLDocument.Iterator;

import java.lang.Math;

/**
 * A player performing random moves.
 */
public class Player01 implements Player {
    /**
     * The name of the random player.
     */
    private String name;

    /**
     * Creates a random player with the assigned name.
     * 
     * @param name
     */
    public Player01(String name) {
        this.name = name;
    }

    public double myRound(double val) {
        if (val < 0) {
            return Math.ceil(val);
        }
        return Math.floor(val);
    }

    @Override
    public GameMove maximizeCrossings(Graph g, HashMap<Vertex, Coordinate> vertexCoordinates, List<GameMove> gameMoves,
            int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height) {
        // place it as far away as possible (mirrored around center point)

        // get random move to start from
        GameMove rm = randomMove(g, usedCoordinates, placedVertices, width, height);
        Vertex v = rm.getVertex();

        // get neighbors
        LinkedList<Vertex> neighbors = getNeighbors(g, v);
        Vertex next = neighbors.get(0);
        Vertex prev = neighbors.get(1);

        // check if one of the neighbors was placed already
        Vertex neighbor = null;
        boolean r = placedVertices.contains(next);
        boolean t = placedVertices.contains(prev);
        if (!r && !t) {
            return rm;
            // } else if (r && t) { // maximize both neighbors if both are set (later in
            // game)
            // // TODO: do the brute force maximizing here!!!
            // return rm;
        } else { // mirror around the center if only one is given
            neighbor = r ? next : prev;
            int x = vertexCoordinates.get(neighbor).getX();
            int y = vertexCoordinates.get(neighbor).getY();
            int ix = width - 1 - x;
            int iy = height - 1 - y;
            // mirror around the center
            if (usedCoordinates[ix][iy] == 0) {
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
                int a = ix + (int) myRound(curr * deltaX);
                int b = iy + (int) myRound(curr * deltaY);
                if (a < 0 || a >= width || b < 0 || b >= height) {
                    break;
                }
                if (usedCoordinates[a][b] == 0) {
                    return new GameMove(v, new Coordinate(a, b));
                }
                // else get further away
                curr++;
            }
        }
        // else return the random move
        return rm;

        // if (!r && !t) { // TODO: if neither neighbor is set get the neighbor that has
        // 2 fixed neighbors
        // // if possible
        // } else if (r) { // check if next neighbor (id+1) can be placed next to the
        // current vertex
        // neighbor = next;
        // } else if (t) { // check if previous neighbor (id-1) can be placed next to
        // the current vertex
        // neighbor = prev;
        // } else { // if both are set
        // return randomMove(g, usedCoordinates, placedVertices, width, height);
        // }
    }

    public LinkedList<Vertex> getNeighbors(Graph g, Vertex v) {
        Iterable<Edge> e = g.getIncidentEdges(v);
        LinkedList<Vertex> neighbors = new LinkedList<>();
        for (Edge edge : e) {
            neighbors.add(v.equals(edge.getS()) ? edge.getS() : edge.getT());
        }
        assert neighbors.size() == 2;
        return neighbors;
    }

    public GameMove neighborMove(Vertex current, Vertex neighbor, Graph g,
            HashMap<Vertex, Coordinate> vertexCoordinates,
            int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height) {
        int x = vertexCoordinates.get(current).getX();
        int y = vertexCoordinates.get(current).getY();
        int neighborhood = 3;
        for (int i = 1; i < neighborhood; i++) {
            for (int j = -i; j <= i; j++) {
                if ((x + j) >= 0 && (x + j) < width) {
                    for (int k = -i; k <= i; k++) {
                        if (Math.abs(j) == i || Math.abs(k) == i) {
                            if ((y + k) >= 0 && (y + k) < height) {
                                if (usedCoordinates[x + j][y + k] == 0) {
                                    return new GameMove(neighbor, new Coordinate(x + j, y + k));
                                }
                            }
                        }
                    }
                }
            }
        }
        return randomMove(g, usedCoordinates, placedVertices, width, height);
    }

    @Override
    public GameMove minimizeCrossings(Graph g, HashMap<Vertex, Coordinate> vertexCoordinates, List<GameMove> gameMoves,
            int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height) {

        // get the neighbor vertex
        LinkedList<Vertex> neighbors = null;
        GameMove lastMove = gameMoves.getLast();
        Vertex current = lastMove.getVertex();
        neighbors = getNeighbors(g, current);
        Vertex next = neighbors.get(0);
        Vertex prev = neighbors.get(1);
        neighbors = getNeighbors(g, next);
        Vertex nextnext = current.equals(neighbors.get(0)) ? neighbors.get(1) : neighbors.get(0);
        neighbors = getNeighbors(g, prev);
        Vertex prevprev = current.equals(neighbors.get(0)) ? neighbors.get(1) : neighbors.get(0);
        Vertex neighbor = null;

        boolean r = placedVertices.contains(next);
        boolean t = placedVertices.contains(prev);
        boolean z = placedVertices.contains(nextnext);
        boolean u = placedVertices.contains(prevprev);
        if (!r && !t) { // TODO: if neither neighbor is set get the neighbor that has 2 fixed neighbors
                        // if possible
            neighbor = z ? next : (u ? prev : next);
        } else if (!r) { // check if next neighbor (id+1) can be placed next to the current vertex
            neighbor = next;
        } else if (!t) { // check if previous neighbor (id-1) can be placed next to the current vertex
            neighbor = prev;
        } else { // if both are set
            if (!z) {
                current = next;
                neighbor = nextnext;
            } else if (!u) {
                current = prev;
                neighbor = prevprev;
            } else {
                return randomMove(g, usedCoordinates, placedVertices, width, height);
            }
        }

        return neighborMove(current, neighbor, g, vertexCoordinates, usedCoordinates, placedVertices, width, height);
    }

    @Override
    public void initializeNextRound() {

    }

    /**
     * Computes a non-random valid move.
     * 
     * @param g               The graph.
     * @param usedCoordinates The used coordinates.
     * @param placedVertices  The already placed vertices.
     * @param width           The width of the game board.
     * @param height          The height of the game board.
     * @return A random valid move.
     */
    private GameMove randomMove(Graph g, int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width,
            int height) {
        Random r = new Random();
        int stillToBePlaced = g.getN() - placedVertices.size();
        int next = r.nextInt(stillToBePlaced);
        int skipped = 0;
        Vertex v = null;
        for (Vertex u : g.getVertices()) {
            if (!placedVertices.contains(u)) {
                if (skipped < next) {
                    skipped++;
                    continue;
                }
                v = u;
                break;
            }
        }
        Coordinate c = new Coordinate(0, 0);
        do {
            c = new Coordinate(r.nextInt(width), r.nextInt(height));
        } while (usedCoordinates[c.getX()][c.getY()] != 0);
        return new GameMove(v, c);
    }

    @Override
    public String getName() {
        return name;
    }
}
