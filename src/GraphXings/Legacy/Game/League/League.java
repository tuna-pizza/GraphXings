package GraphXings.Legacy.Game.League;

import GraphXings.Legacy.Algorithms.Player;
import GraphXings.Game.GameInstance.GameInstanceFactory;
import GraphXings.Legacy.Game.Match.Match;
import GraphXings.Legacy.Game.Match.MatchResult;

import java.util.HashMap;
import java.util.List;

public class League
{
    private List<Player> players;
    private int bestOf;
    private long timeLimit;
    private GameInstanceFactory factory;
    private int pointsPerGameWon;
    private int pointsPerTie;

    public League(List<Player> players, int bestOf, GameInstanceFactory factory)
    {
        this.players = players;
        this.bestOf = bestOf;
        this.timeLimit = Long.MAX_VALUE;
        this.factory = factory;
        pointsPerGameWon = 3;
        pointsPerTie = 1;
    }

    public League(List<Player> players, int bestOf, long timeLimit, GameInstanceFactory factory)
    {
        this.players = players;
        this.bestOf = bestOf;
        this.timeLimit = timeLimit;
        this.factory = factory;
        pointsPerGameWon = 3;
        pointsPerTie = 1;
    }

    public LeagueResult runLeague()
    {
        HashMap<Player,Integer> leaguePoints = new HashMap<>();
        for (int i = 0; i < players.size(); i++)
        {
            leaguePoints.put(players.get(i),0);
        }
        int matchNumber = 1;
        for (int i = 0; i < players.size(); i++)
        {
            for (int j = 0; j < i; j++)
            {
                Player player1 = players.get(i);
                Player player2 = players.get(j);
                Match m = new Match(player1, player2, factory, bestOf, timeLimit);
                MatchResult mr = m.play();
                System.out.println("Match " + matchNumber++ + ": " + mr.announceResult());
                Player winner = mr.getWinner();
                if (winner != null)
                {
                    leaguePoints.put(winner,leaguePoints.get(winner)+pointsPerGameWon);
                }
                else
                {
                    leaguePoints.put(player1,leaguePoints.get(player1)+pointsPerTie);
                    leaguePoints.put(player2,leaguePoints.get(player2)+pointsPerTie);
                }
            }
        }
        return new LeagueResult(leaguePoints);
    }
}
