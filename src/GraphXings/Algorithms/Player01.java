package GraphXings.Algorithms;

import GraphXings.Data.Coordinate;
import GraphXings.Data.Graph;
import GraphXings.Data.Vertex;
import GraphXings.Game.Game;
import GraphXings.Game.GameMove;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

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

    @Override
    public GameMove maximizeCrossings(Graph g, HashMap<Vertex, Coordinate> vertexCoordinates, List<GameMove> gameMoves,
            int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height) {
        class Rounder {
            public double myRound(double val) {
                if (val < 0) {
                    return Math.ceil(val);
                }
                return Math.floor(val);
            }
        }
        // get random move to start from
        GameMove rm = randomMove(g, usedCoordinates, placedVertices, width, height);

        // place it as far away as possible (mirrored around center point)
        Vertex v = rm.getVertex();
        Coordinate c = new Coordinate(0, 0);

        // check if one of the neighbors was placed already
        String next = Integer.toString((Integer.parseInt(v.getId()) + 1) % g.getN() + 1);
        String prev = Integer.toString((Integer.parseInt(v.getId()) - 1) % g.getN() + 1);
        Vertex neighborNext = null;
        Vertex neighborPrev = null;
        for (Vertex u : g.getVertices()) {
            if (u.getId().equals(next)) {
                neighborNext = u;
            }
            if (u.getId().equals(prev)) {
                neighborPrev = u;
            }
        }

        if (placedVertices.contains(neighborNext)) {
            int x = vertexCoordinates.get(neighborNext).getX();
            int y = vertexCoordinates.get(neighborNext).getY();
            c = new Coordinate(width - 1 - x, height - 1 - y);
            if (usedCoordinates[width - 1 - x][height - 1 - y] == 0) {
                return new GameMove(v, c);
            }
            double deltaX = width - 1 - (2 * x);
            double deltaY = height - 1 - (2 * y);
            double length = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            deltaX /= length;
            deltaY /= length;
            int curr = 1;
            while (curr < length) {
                // check diagonal point
                int a = width - 1 - x - (int) new Rounder().myRound(curr * deltaX);
                int b = height - 1 - y - (int) new Rounder().myRound(curr * deltaY);
                if (usedCoordinates[a][b] == 0) {
                    c = new Coordinate(a, b);
                    return new GameMove(v, c);
                }

                // // check in-between-diagonal-and-horizontal/vertical points
                // for (int i = curr; i < length; i++) {
                // int a2 = width - 1 - x + (int) new Rounder().myRound((i) * deltaX);
                // int b2 = height - 1 - y + (int) new Rounder().myRound((i) * deltaY);
                // if (a2 >= 0 && a2 < width && usedCoordinates[a2][b] == 0) {
                // c = new Coordinate(a2, b);
                // return new GameMove(v, c);
                // }
                // if (b2 >= 0 && b2 < height && usedCoordinates[a][b2] == 0) {
                // c = new Coordinate(a, b2);
                // return new GameMove(v, c);
                // }
                // }

                // else get closer
                curr++;
            }
        }

        // same for previous neighbor (id-1)
        if (placedVertices.contains(neighborPrev)) {
            int x = vertexCoordinates.get(neighborPrev).getX();
            int y = vertexCoordinates.get(neighborPrev).getY();
            c = new Coordinate(width - 1 - x, height - 1 - y);
            if (usedCoordinates[width - 1 - x][height - 1 - y] == 0) {
                return new GameMove(v, c);
            }
            double deltaX = width - 1 - (2 * x);
            double deltaY = height - 1 - (2 * y);
            double length = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            deltaX /= length;
            deltaY /= length;
            int curr = 1;
            while (curr < length) {
                // check diagonal point
                int a = width - 1 - x - (int) new Rounder().myRound(curr * deltaX);
                int b = height - 1 - y - (int) new Rounder().myRound(curr * deltaY);
                if (usedCoordinates[a][b] == 0) {
                    c = new Coordinate(a, b);
                    return new GameMove(v, c);
                }

                // check in-between-diagonal-and-horizontal/vertical points
                for (int i = curr; i < length; i++) {
                    int a2 = width - 1 - x + (int) new Rounder().myRound(i * deltaX);
                    int b2 = height - 1 - y + (int) new Rounder().myRound(i * deltaY);
                    if (a2 >= 0 && a2 < width && usedCoordinates[a2][b] == 0) {
                        c = new Coordinate(a2, b);
                        return new GameMove(v, c);
                    }
                    if (b2 >= 0 && b2 < height && usedCoordinates[a][b2] == 0) {
                        c = new Coordinate(a, b2);
                        return new GameMove(v, c);
                    }
                }

                // else get closer
                curr++;
            }
        }

        // else return the random move
        return rm;

    }

    @Override
    public GameMove minimizeCrossings(Graph g, HashMap<Vertex, Coordinate> vertexCoordinates, List<GameMove> gameMoves,
            int[][] usedCoordinates, HashSet<Vertex> placedVertices, int width, int height) {
        GameMove lastMove = gameMoves.getLast();

        // get the neighbor vertex
        String next = Integer.toString((Integer.parseInt(lastMove.getVertex().getId()) + 1) % g.getN());
        String prev = Integer.toString((Integer.parseInt(lastMove.getVertex().getId()) - 1) % g.getN());
        if (next.equals("0")) {
            next = "10";
        }
        if (prev.equals("0")) {
            prev = "10";
        }
        Vertex neighborNext = null;
        Vertex neighborPrev = null;
        for (Vertex u : g.getVertices()) {
            if (u.getId().equals(next)) {
                neighborNext = u;
            }
            if (u.getId().equals(prev)) {
                neighborPrev = u;
            }
        }
        // check if next neighbor (id+1) of last placed vertex can be placed next to it
        if (!placedVertices.contains(neighborNext)) {
            Coordinate c = new Coordinate(0, 0);
            int x = vertexCoordinates.get(lastMove.getVertex()).getX();
            int y = vertexCoordinates.get(lastMove.getVertex()).getY();
            if ((x + 1) < width && usedCoordinates[x + 1][y] == 0) {
                c = new Coordinate(x + 1, y);
                return new GameMove(neighborNext, c);
            }
            if ((x - 1) >= 0 && usedCoordinates[x - 1][y] == 0) {
                c = new Coordinate(x - 1, y);
                return new GameMove(neighborNext, c);
            }
            if ((y + 1) < height && usedCoordinates[x][y + 1] == 0) {
                c = new Coordinate(x, y + 1);
                return new GameMove(neighborNext, c);
            }
            if ((y - 1) >= 0 && usedCoordinates[x][y - 1] == 0) {
                c = new Coordinate(x, y - 1);
                return new GameMove(neighborNext, c);
            }
        }

        // check same for previous neighbor (id-1)
        if (!placedVertices.contains(neighborPrev)) {
            Coordinate c = new Coordinate(0, 0);
            int x = vertexCoordinates.get(lastMove.getVertex()).getX();
            int y = vertexCoordinates.get(lastMove.getVertex()).getY();
            if ((x + 1) < width && usedCoordinates[x + 1][y] == 0) {
                c = new Coordinate(x + 1, y);
                return new GameMove(neighborPrev, c);
            }
            if ((x - 1) >= 0 && usedCoordinates[x - 1][y] == 0) {
                c = new Coordinate(x - 1, y);
                return new GameMove(neighborPrev, c);
            }
            if ((y + 1) < height && usedCoordinates[x][y + 1] == 0) {
                c = new Coordinate(x, y + 1);
                return new GameMove(neighborPrev, c);
            }
            if ((y - 1) >= 0 && usedCoordinates[x][y - 1] == 0) {
                c = new Coordinate(x, y - 1);
                return new GameMove(neighborPrev, c);
            }
        }

        // else make a random move
        return randomMove(g, usedCoordinates, placedVertices, width, height);
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
