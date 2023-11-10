package GraphXings.Game.Match;

import GraphXings.Algorithms.Player;
import GraphXings.Game.Game;
import GraphXings.Game.GameInstance.GameInstance;
import GraphXings.Game.GameInstance.GameInstanceFactory;
import GraphXings.Game.GameResult;

/**
 * A class managing a Best of X type match of GraphXings.
 */
public class Match {
	/**
	 * The first player.
	 */
	private Player player1;
	/**
	 * The second player.
	 */
	private Player player2;
	/**
	 * The game instance factory.
	 */
	private GameInstanceFactory factory;
	/**
	 * The number of games to be played.
	 */
	private int bestOutOf;

	/**
	 * Constructs a match object.
	 * 
	 * @param player1   The first player.
	 * @param player2   The second player.
	 * @param factory   The game instance factory to be used for created the games!
	 * @param bestOutOf The number of games to be played.
	 */
	public Match(Player player1, Player player2, GameInstanceFactory factory, int bestOutOf) {
		this.player1 = player1;
		this.player2 = player2;
		this.bestOutOf = bestOutOf;
		this.factory = factory;
	}

	/**
	 * Plays the match.
	 * 
	 * @return A MatchResult object containing the results of the match.
	 */
	public MatchResult play() {
		boolean end = false;
		int gamesWon1 = 0;
		int gamesWon2 = 0;
		int gamesPlayed = 0;
		long startTime = System.nanoTime();
		// System.out.println("game numbers | create game | play | winner declaration");
		// System.out.println("game | init p1 | init p2 | play");
		System.out.println("init used to 0 | copy | player action | add");
		while (!end) {
			System.out.print(gamesPlayed + 1);
			long t1 = System.nanoTime();
			GameInstance gi = factory.getGameInstance();
			Game game = new Game(gi.getG(), gi.getWidth(), gi.getHeight(), player1, player2);
			long t2 = System.nanoTime();
			GameResult gr = game.play();
			gamesPlayed++;
			long t3 = System.nanoTime();
			if (gamesPlayed > 2 * bestOutOf) {
				end = true;
			}
			if (gr.getWinner() == null) {
				continue;
			} else {
				int gamesWonWinner;
				if (gr.getWinner().equals(player1)) {
					gamesWon1++;
					gamesWonWinner = gamesWon1;
				} else {
					gamesWon2++;
					gamesWonWinner = gamesWon2;
				}
				if (gamesWonWinner > bestOutOf / 2) {
					end = true;
				}
			}
			// System.out.print((gamesPlayed + 1) + "/" + bestOutOf);
			// System.out.print(" | " + (t2 - t1) / 1000000 + "ms");
			// System.out.print(" | " + (t3 - t2) / 1000000 + "ms");
			// System.out.println(" | " + (System.nanoTime() - t3) / 1000000 + "ms");
		}
		System.out.println("while loop: " + (System.nanoTime() - startTime) / 1000000 + "ms");
		return new MatchResult(player1, player2, gamesWon1, gamesWon2);
	}
}
