# GraphXings

An implementation of the asymmetric game GraphXings where two players 1 and 2 compete against each other in two game rounds. In each turn, the player is to position an unplaced vertex of a known graph on the integer grid. In the first round, the goal of player 1 is to create as many crossings as possible whereas player 2 attempts to avoid crossings. In the second round the objectives are swapped. The winner is the player who created more crossings when maximizing the number of crossings.

See <https://i11www.iti.kit.edu/projects/cyclexings/index> for a human-playable instance where the graph is always a cycle.

The structure of the package GraphXings is as follows:

- Package Algorithms contains algorithmic solutions.
  - The interface Player can be implemented to obtain a playing agent. Class RandomPlayer is a sample implementation performing random moves.
  - Class CrossingCalculator contains a simple function to compute the number of crossings.
- Package Data contains required data types.
  - Classes Graph, Vertex, Edge implement a graph data structure using an adjacency list representation.
  - Class Coordinate and Segment represent Points and Segments in 2D, respectively.
  - Class Rational implements calculations with rational numbers for more precise crossing computation compared to float.
- Package Game contains the game engine.
  - Class Game implements the main functionality of GraphXings.
  - Classes GameMove and GameResult are wrappers to convey results of moves and full games, respectively.
  - Class InvalidMoveException provides an exception that can be thrown in case a player attempts to cheat.
- The main function is contained in GraphXings.java. It creates a 10-cycle and instantiates a game with two random players. Then it lets them play against each other and displays the result.

If you want to implement a new player, your starting point is the interface Player in Package Algorithms.
