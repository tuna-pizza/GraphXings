package GraphXings.Legacy.Game.League;

import GraphXings.Legacy.Algorithms.Player;

import java.util.HashMap;

public class LeagueResult
{
    HashMap<Player,Integer> leaguePoints = new HashMap<>();
    public LeagueResult(HashMap<Player,Integer> leaguePoints)
    {
        this.leaguePoints = leaguePoints;
    }
    public String announceResults()
    {
        String result = "--------------\nLeague Results\n--------------\n";
        for (Player player : leaguePoints.keySet())
        {
            result = result + player.getName() + "\t" + leaguePoints.get(player).toString() + "\n";
        }
        return result;
    }
}
