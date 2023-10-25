package GraphXings.Game;
import GraphXings.Data.Graph;
import GraphXings.Algorithms.Player;
public class Match
{
	private Player player1;
	private Player player2;
	private GameInstanceFactory factory;
	private int bestOutOf;
	public Match(Player player1, Player player2, GameInstanceFactory factory, int bestOutOf)
	{
		this.player1 = player1;
		this.player2 = player2;
		this.bestOutOf = bestOutOf;
		this.factory = factory;
	}
	public MatchResult play()
	{
		boolean end = false;
		int gamesWon1 = 0;
		int gamesWon2 = 0;
		int gamesPlayed = 0;
		while(!end)
		{
			GameInstance gi = factory.getGameInstance();
			Game game = new Game(gi.getG(),gi.getWidth(),gi.getHeight(),player1,player2);
			GameResult gr = game.play();
			gamesPlayed++;
			if (gamesPlayed > 2*bestOutOf)
			{
				end = true;
			}
			if (gr.getWinner() == null)
			{
				continue;
			}
			else
			{
				int gamesWonWinner;
				if (gr.getWinner().equals(player1))
				{
					gamesWon1++;
					gamesWonWinner = gamesWon1;
				}
				else
				{
					gamesWon2++;
					gamesWonWinner = gamesWon2;
				}
				if (gamesWonWinner > bestOutOf/2)
				{
					end = true;
				}
			}
		}
		return new MatchResult(player1,player2,gamesWon1,gamesWon2);
	}
}
