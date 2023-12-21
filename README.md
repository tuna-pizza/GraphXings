# GraphXings

An implementation of the asymmetric game GraphXings where two players 1 and 2 compete against each other in two game rounds. In each turn, the player is to position an unplaced vertex of a known graph on the integer grid. In the first round, the goal of player 1 is to create as many crossings as possible whereas player 2 attempts to avoid crossings. In the second round the objectives are swapped. The winner is the player who created more crossings when maximizing the number of crossings.

See https://i11www.iti.kit.edu/projects/cyclexings/index for a human-playable instance where the graph is always a cycle.

The structure of the package GraphXings is as follows:
- Package Algorithms contains algorithmic solutions.
  * The interface NewPlayer can be implemented to obtain a playing agent. Class NewRandomPlayer is a sample implementation performing random moves.
  * Class CrossingCalculator contains a simple function to compute the number of crossings.
  * Class IntegerMaths contains arithmetic functions for integers that are only defined for floats in java.Math.
- Package Data contains required data types.
  * Classes Graph, Vertex, Edge implement a graph data structure using an adjacency list representation.
  * Class Coordinate and Segment represent Points and Segments in 2D, respectively.
  * Class Rational implements calculations with rational numbers for more precise crossing computation compared to floats.
- Package Game contains the game engine.
  * Class Game implements the main functionality of GraphXings.
  * Classes GameMove and NewGameResult are wrappers to convey results of moves and full games, respectively.
  * Class GameState models the current state of a GraphXings game. The validity of a GameMove can be checked using the current GameState and the GameState can be updated according to a new GameMove.
  * Classes NewInvalidMoveException and NewTimeOutException provide exceptions that can be thrown in case a player attempts to cheat or runs out of time.
  * Subpackage GameInstance contains:
    - Class GameInstance that describes the setting of a game consisting of a graph to be drawn and a specification of the width and height of the drawing area.
    - Interface GameInstanceFactory provides a factory that generates GameInstances which can be queried by several consecutive games.
    - ConstantGameInstanceFactory provides a factory always providing the same GameInstance.
    - RandomCycleFactory provides a factory that creates a cycle possibly extended by a random matching of the cycle's vertices.
    - PlanarExampleInstanceFactory produces either of two small planar example graphs.
    - PlantriGameInstanceFactory can read a random graph from a file created by software plantri and construct a corresponding GameInstance.
    - PlanarGameInstanceFactory computes GameInstance based on either a random triangulation, a random cubic planar graph or a random subgraph of a triangulation.
    - MultiGameInstanceFactory allows to randomly select between several GameInstanceFactories to produce the next GameInstance.
  * Subpackage Match contains:
    - Class NewMatch allows to run a best-of-k games match between two players. Requires a GameInstanceFactory for generating GameInstances to be used in the k games.
    - Class NewMatchResult is a wrapper to convey the results of a full match.
  * Subpackage League contains:
    - Class NewLeague allows to run a league involving a set of players. Each pair of players will be matched once against each other in a best-of-k NewMatch. Requires a GameInstanceFactory for generating GameInstances.
    - Class NewLeagueResult is a wrapper to convey the results of a full league.
- Package Legacy provides legacy support for the old Player interface. Decrepated.
- The main function is contained in GraphXings.java. It creates a list of players that may be filled by the user and then runs a league between those players using RandomCycleFactory and a timeout of 5mins per Game.

If you want to implement a new player, your starting point is the interface NewPlayer in Package Algorithms.
