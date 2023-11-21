package GraphXings.Game.League;

import GraphXings.Algorithms.NewPlayer;

import java.util.HashMap;

/**
 * A class for storing the results of a league of GraphXings.
 */
public class NewLeagueResult
{
	/**
	 * A map mapping players to the points they accumulated in matches of the league.
	 */
	HashMap<NewPlayer,Integer> leaguePoints = new HashMap<>();

	/**
	 * Constructs a new LeagueResult object,
 	 * @param leaguePoints A map mapping players to the points they accumulated in matches of the league.
	 */
	public NewLeagueResult(HashMap<NewPlayer,Integer> leaguePoints)
	{
		this.leaguePoints = leaguePoints;
	}

	/**
	 * Returns a description of the results of the league!
	 * @return A description of the results of the league.
	 */
	public String announceResults()
	{
		String result = "--------------\nLeague Results\n--------------\n";
		for (NewPlayer player : leaguePoints.keySet())
		{
			result = result + player.getName() + "\t" + leaguePoints.get(player).toString() + "\n";
		}
		return result;
	}
}
