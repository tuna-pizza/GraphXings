package GraphXings.NewFiles;

import java.util.LinkedList;
import java.util.Random;

import GraphXings.Game.GameMove;
import GraphXings.Game.GameState;
import GraphXings.Algorithms.NewPlayer;
import GraphXings.Data.*;

/**
 * A player performing random moves.
 */
public class NewMirroringPlayer implements NewPlayer {
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
     * Creates a random player with the assigned name.
     * 
     * @param name
     */
    public NewMirroringPlayer(String name) {
        this.name = name;
        this.r = new Random(name.hashCode());
    }

    public double myRound(double val) {
        if (val < 0) {
            return Math.ceil(val);
        }
        return Math.floor(val);
    }

    @Override
    public GameMove maximizeCrossings(GameMove lastMove) {
        // First: Apply the last move by the opponent.
        if (lastMove != null) {
            gs.applyMove(lastMove);
        }
        // Second: Compute the new move.
        GameMove newMove = getMaximizingMove(lastMove);
        // Third: Apply the new move to the local GameState.
        gs.applyMove(newMove);
        // Finally: Return the new move.
        return newMove;
    }

    @Override
    public GameMove minimizeCrossings(GameMove lastMove) {
        // First: Apply the last move by the opponent.
        if (lastMove != null) {
            gs.applyMove(lastMove);
        }
        // Second: Compute the new move.
        GameMove newMove = getMinimizingMove(lastMove);
        // Third: Apply the new move to the local GameState.
        gs.applyMove(newMove);
        // Finally: Return the new move.
        return newMove;
    }

    public GameMove getMaximizingMove(GameMove lastMove) {
        // place it as far away as possible (mirrored around center point)

        // get random move to start from
        GameMove rm = randomMove();
        Vertex v = rm.getVertex();

        // get neighbors
        LinkedList<Vertex> neighbors = getNeighbors(g, v);
        Vertex next = neighbors.get(0);
        Vertex prev = neighbors.get(1);

        // check if one of the neighbors was placed already
        Vertex neighbor = null;
        boolean r = gs.getPlacedVertices().contains(next);
        boolean t = gs.getPlacedVertices().contains(prev);
        if (!r && !t) {
            return rm;
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
                int a = ix + (int) myRound(curr * deltaX);
                int b = iy + (int) myRound(curr * deltaY);
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
        // return randomMove(g, gs.getUsedCoordinates(), gs.getPlacedVertices(), width,
        // height);
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

    public GameMove neighborMove(Vertex current, Vertex neighbor) {
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
        return randomMove();
    }

    public GameMove getMinimizingMove(GameMove lastMove) {
        // get the neighbor vertex
        LinkedList<Vertex> neighbors = null;
        Vertex current = lastMove.getVertex();
        neighbors = getNeighbors(g, current);
        Vertex next = neighbors.get(0);
        Vertex prev = neighbors.get(1);
        neighbors = getNeighbors(g, next);
        Vertex nextnext = current.equals(neighbors.get(0)) ? neighbors.get(1) : neighbors.get(0);
        neighbors = getNeighbors(g, prev);
        Vertex prevprev = current.equals(neighbors.get(0)) ? neighbors.get(1) : neighbors.get(0);
        Vertex neighbor = null;

        boolean r = gs.getPlacedVertices().contains(next);
        boolean t = gs.getPlacedVertices().contains(prev);
        boolean z = gs.getPlacedVertices().contains(nextnext);
        boolean u = gs.getPlacedVertices().contains(prevprev);
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
                return randomMove();
            }
        }

        return neighborMove(current, neighbor);
    }

    @Override
    public void initializeNextRound(Graph g, int width, int height, Role role) {
        this.g = g;
        this.width = width;
        this.height = height;
        this.gs = new GameState(width, height);
    }

    /**
     * Computes a random valid move.
     * 
     * @return A random valid move.
     */
    private GameMove randomMove() {
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

    @Override
    public String getName() {
        return name;
    }
}