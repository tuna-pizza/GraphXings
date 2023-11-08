package GraphXings.Game;

import GraphXings.Data.Coordinate;
import GraphXings.Data.Graph;
import GraphXings.Data.Vertex;

import java.util.HashMap;
import java.util.HashSet;

/**
 * A class describing the current state of the game.
 */
public class GameState
{
	/**
	 * A HashMap mapping vertices to their coordinates if already placed.
	 */
	private HashMap<Vertex, Coordinate> vertexCoordinates;
	/**
	 * A collection of vertices that have already been placed.
	 */
	private HashSet<Vertex> placedVertices;
	/**
	 * The width of the drawing.
	 */
	private int width;
	/**
	 * The height of the drawing.
	 */
	private int height;
	/**
	 * A 0-1 map describing which coordinates have already been used.
	 */
	private int[][] usedCoordinates;

	/**
	 * Creates a new GameState object describing the initial empty game board.
	 * @param width The width of the game board.
	 * @param height The height of the game board.
	 */
	public GameState(int width, int height)
	{
		vertexCoordinates = new HashMap<>();
		placedVertices = new HashSet<>();
		usedCoordinates = new int[width][height];
		for (int x = 0; x < width; x++)
		{
			for (int y=0; y < height; y++)
			{
				usedCoordinates[x][y] = 0;
			}
		}
		this.width = width;
		this.height = height;
	}

	/**
	 * Checks if a move is valid given the current state of the game.
	 * @param newMove The potential move to be performed.
	 * @return True if the move is valid, false if it is invalid.
	 */
	public boolean checkMoveValidity(GameMove newMove)
	{
		if (newMove.getVertex() == null ||newMove.getCoordinate() == null)
		{
			return  false;
		}
		if (placedVertices.contains(newMove.getVertex()))
		{
			return false;
		}
		int x = newMove.getCoordinate().getX();
		int y = newMove.getCoordinate().getY();
		if (x >= width || y >= height)
		{
			return false;
		}
		if (usedCoordinates[x][y] != 0)
		{
			return false;
		}
		return true;
	}

	/**
	 * Applies changes to the game state according to the new move.
	 * @param newMove The new move to be performed.
	 */
	public void applyMove(GameMove newMove)
	{
		usedCoordinates[newMove.getCoordinate().getX()][newMove.getCoordinate().getY()]=1;
        placedVertices.add(newMove.getVertex());
        vertexCoordinates.put(newMove.getVertex(), newMove.getCoordinate());
	}

	/**
	 * Gets the set of placed vertices.
	 * @return The set of placed vertices.
	 */
	public HashSet<Vertex> getPlacedVertices()
	{
		return placedVertices;
	}

	/**
	 * Gets the coordinates assigned to vertices.
	 * @return A HashMap describing the coordinate of vertices.
	 */
	public HashMap<Vertex, Coordinate> getVertexCoordinates()
	{
		return vertexCoordinates;
	}

	/**
	 * Gets a 0-1 map of the already used vertices.
	 * @return
	 */
	public int[][] getUsedCoordinates()
	{
		return usedCoordinates;
	}
}
